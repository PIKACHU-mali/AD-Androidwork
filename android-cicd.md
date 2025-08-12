# Android CI/CD（仅 Android 应用）

本文件说明本仓库 Android 应用（模块 `:app`）的持续集成与持续交付设计与推荐实践，参考了你给出的“Android CI/CD 基本流程”截图，并在关键环节给出改进建议与可直接落地的 GitHub Actions 配置示例。

---

## 一、与你截图一致的基础流程（Basic Flow）

1. Code commit → 触发 CI（Gradle 构建）
2. Check code quality → Lint、静态扫描（SAST）、依赖安全扫描（SCA）
3. Run tests → 单元测试、（可选）仪器测试/集成测试/UI 测试
4. Package app → 生成 APK 或 AAB
5. Publish → Play Console 或 Firebase App Distribution（内测分发）
6. Monitor → 崩溃与性能监控（Crashlytics、Play Console Vitals）

该流程是 Android 常见最佳实践，与“Web 前端 CI/CD”的差异在于：最终不是“部署到服务器”，而是“发布到分发渠道/商店”，监控发生在用户设备侧。

---

## 二、对流程的补充与改进建议

- 触发与分支策略：
  - PR/feature 分支：执行 Lint + 单测 + Debug 构建，产出 APK 供快速验证。
  - tag（如 vX.Y.Z）：执行 Release 构建 + 签名 + 分发到内测/商店。
- 缓存：启用 Gradle 缓存（~/.gradle），显著缩短构建时间。
- SAST/SCA：
  - SAST：GitHub CodeQL（Java/Kotlin）或 Detekt/Ktlint 组合。
  - SCA：Dependabot 或 OWASP Dependency-Check（Gradle 插件）。
- 测试层次：
  - 单元测试（./gradlew test）必须在 PR 上门禁。
  - 仪器测试（connectedAndroidTest）可在合入前夜间跑，或使用 Firebase Test Lab。
- 签名与密钥：
  - Keystore 以 Base64 形式存入 GitHub Secrets，Runner 解码到临时文件。
  - Play Console/Firebase 使用服务账号凭据（最小权限原则）。
- 产物与溯源：
  - AAB/APK 上传为 Actions Artifacts，文件名带 Git SHA/版本号。
  - 版本号由 Git tag 注入（例如 `versionNameSuffix` 或 `versionCode` 自动化）。
- 发布与回滚：
  - Play Console 使用 track（internal/alpha/beta/production）分阶段灰度；
  - Crashlytics 上传 Proguard mapping.txt，便于崩溃反混淆；
  - 出现问题时快速停更/回滚到上一版本。
- 冒烟检查：
  - 轻量：校验产物存在且 `aapt dump badging` 可解析；
  - 进阶：AVD Headless 启动并 `adb install` + 冷启动校验；或 FTL 运行一条 Sanity 测试。

---

## 三、GitHub Actions 工作流（可直接落地，不改源码）

> 将以下示例保存为 `.github/workflows/android-ci.yml`。仅使用本仓库已有的 Gradle 脚本；签名/分发步骤通过 Secrets 注入，避免改动代码。

```yaml
name: Android CI/CD

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
  # 打 tag 走 Release 流程（例如 v1.2.3）
  push:
    tags: [ 'v*' ]
  workflow_dispatch: {}

jobs:
  build:
    runs-on: ubuntu-latest
    timeout-minutes: 45

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up Temurin JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'

      - name: Cache Gradle
        uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties', 'gradle/libs.versions.toml') }}
          restore-keys: |
            ${{ runner.os }}-gradle-

      # 质量门禁
      - name: Lint
        run: ./gradlew lint --stacktrace

      - name: Unit tests
        run: ./gradlew test --stacktrace

      # 调试构建（供 PR 验证）
      - name: Assemble Debug APK
        if: ${{ github.event_name != 'push' || !startsWith(github.ref, 'refs/tags/') }}
        run: ./gradlew assembleDebug --stacktrace

      - name: Upload Debug APK
        if: ${{ github.event_name != 'push' || !startsWith(github.ref, 'refs/tags/') }}
        uses: actions/upload-artifact@v4
        with:
          name: app-debug-apk
          path: app/build/outputs/apk/debug/*.apk

      # Release 构建（当推送 tag 时）
      - name: Prepare signing keystore
        if: startsWith(github.ref, 'refs/tags/')
        run: |
          echo "$SIGNING_KEYSTORE_BASE64" | base64 -d > release.keystore
        env:
          SIGNING_KEYSTORE_BASE64: ${{ secrets.SIGNING_KEYSTORE_BASE64 }}

      - name: Build Release AAB (signed)
        if: startsWith(github.ref, 'refs/tags/')
        env:
          SIGNING_KEY_ALIAS: ${{ secrets.SIGNING_KEY_ALIAS }}
          SIGNING_KEY_PASSWORD: ${{ secrets.SIGNING_KEY_PASSWORD }}
          SIGNING_STORE_PASSWORD: ${{ secrets.SIGNING_STORE_PASSWORD }}
        run: |
          ./gradlew bundleRelease \
            -Pandroid.injected.signing.store.file=$GITHUB_WORKSPACE/release.keystore \
            -Pandroid.injected.signing.store.password=$SIGNING_STORE_PASSWORD \
            -Pandroid.injected.signing.key.alias=$SIGNING_KEY_ALIAS \
            -Pandroid.injected.signing.key.password=$SIGNING_KEY_PASSWORD

      - name: Upload Release bundle
        if: startsWith(github.ref, 'refs/tags/')
        uses: actions/upload-artifact@v4
        with:
          name: app-release-aab
          path: app/build/outputs/bundle/release/*.aab

      # 可选：Firebase App Distribution（内测）
      - name: Distribute to Firebase App Distribution
        if: startsWith(github.ref, 'refs/tags/')
        uses: wzieba/Firebase-Distribution-Github-Action@v1
        with:
          appId: ${{ secrets.FIREBASE_APP_ID }}
          token: ${{ secrets.FIREBASE_TOKEN }}
          groups: testers
          file: app/build/outputs/bundle/release/*.aab

      # 轻量冒烟检查：验证制品存在 + 后端健康接口（若有）
      - name: Smoke check
        run: |
          ls -l app/build/outputs/** || exit 1
          if [ -n "${BACKEND_HEALTH_URL}" ]; then curl -sf ${BACKEND_HEALTH_URL} | head -c 200 ; fi
        env:
          BACKEND_HEALTH_URL: ${{ secrets.BACKEND_HEALTH_URL }}
```

> 如需在 CI 中跑仪器测试（UI/集成），可以新建作业在 macOS/Ubuntu 上启动 Headless AVD，或接入 Firebase Test Lab（更稳定）。

---

## 四、与流程图的对照关系

- 1 触发：push/PR/tag → GitHub Actions
- 2 Checkout：`actions/checkout`
- 3 Set up：JDK + Gradle 缓存
- 4 Lint & Tests：`./gradlew lint`、`./gradlew test`（失败即终止）
- 4.5（可选）Security：CodeQL/Dependency-Check（SAST/SCA）
- 5 Build/Package：`assembleDebug` / `bundleRelease`
- 6 Publish：Firebase App Distribution / Play Console（需服务账号）
- 7 Monitor：Crashlytics、Play Console Vitals（上传 mapping.txt）
- 8/9 Smoke：制品校验、可选 AVD/Firebase Test Lab 冒烟

---

## 五、需要配置的 Secrets（示例）

- `SIGNING_KEYSTORE_BASE64`：Base64 编码的 keystore 内容
- `SIGNING_KEY_ALIAS`、`SIGNING_KEY_PASSWORD`、`SIGNING_STORE_PASSWORD`
- `FIREBASE_APP_ID`、`FIREBASE_TOKEN`（若用 FAD）
- `BACKEND_HEALTH_URL`（若做 API 健康检查）
- （可选）`PLAY_SERVICE_ACCOUNT_JSON`：Play Console 上传所需的 JSON 内容

---

## 六、落地与汇报要点

- 不需要修改现有业务代码即可落地：仅新增工作流文件与仓库 Secrets。
- PR 上有可下载 Debug APK 与测试报告；打 tag 自动产出可发布的 AAB。
- 监控侧通过 Crashlytics/Play Vitals 持续关注崩溃率、ANR 与性能指标。

以上为本 Android 应用的 CI/CD 方案，已与截图流程一致并补充了安全、签名、分发与冒烟检查的实操细节。
