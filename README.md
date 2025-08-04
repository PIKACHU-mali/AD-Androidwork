# NUS Mental Health Journaling App

一个专为心理健康追踪设计的Android应用程序，帮助用户记录和监控日常情绪状态及生活方式习惯。
requirements.txt记录了这个项目的开发任务以及分工.
由Mali负责Mobile前端开发. 他的github repo的branch链接: https://github.com/PIKACHU-mali/AD-Androidwork/tree/MAli.
GitHub Account: e1509820@u.nus.edu
GitHub ID: PIKACHU-mali

## 📱 应用概述

NUS Mental Health Journaling App 是一款基于现代Android开发技术栈构建的心理健康管理应用。该应用提供了直观的用户界面，让用户能够轻松记录每日的情绪变化和生活习惯，从而更好地了解和管理自己的心理健康状态。

## ✨ 主要功能

### 🎭 情绪追踪
- **一日三次记录**：支持早晨、下午、晚上三个时段的情绪记录
- **五级情绪评分**：
  - 非常好 (Very Good)
  - 好 (Good) 
  - 一般 (Neutral)
  - 不好 (Bad)
  - 非常不好 (Very Bad)
- **日历视图**：通过日历界面查看历史情绪记录
- **备注功能**：为每次情绪记录添加详细说明

### 🏃‍♀️ 生活方式追踪
- **睡眠时长**：记录每日睡眠小时数
- **饮水量**：追踪每日饮水杯数
- **加班情况**：记录是否有加班工作
- **运动时间**：记录每日运动分钟数
- **压力水平**：使用0-10级量表评估压力程度
- **社交时间**：记录每日社交互动小时数
- **每日备注**：添加当日生活状况的详细说明

## 🏗️ 技术架构

### 架构模式
应用采用 **MVVM (Model-View-ViewModel)** 架构模式：

- **Model层**：数据模型类，定义情绪和生活方式记录的数据结构
- **ViewModel层**：管理UI状态和业务逻辑，处理数据操作
- **View层**：使用Jetpack Compose构建的现代化UI界面

### 核心技术栈
- **开发语言**：Kotlin
- **UI框架**：Jetpack Compose
- **导航组件**：Navigation Compose
- **状态管理**：ViewModel + Compose State
- **图标库**：Material Icons Extended

## 📁 项目结构

```
NUS/
├── app/                           # 应用模块
│   ├── build.gradle.kts          # 应用级构建配置
│   ├── proguard-rules.pro        # ProGuard混淆规则
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/nus/
│       │   │   ├── MainActivity.kt                 # 主Activity
│       │   │   ├── model/                         # 数据模型
│       │   │   │   ├── MoodEntry.kt              # 情绪记录数据类
│       │   │   │   └── LifestyleEntry.kt         # 生活方式记录数据类
│       │   │   ├── viewmodel/                    # ViewModel层
│       │   │   │   ├── MoodViewModel.kt          # 情绪管理ViewModel
│       │   │   │   └── LifestyleViewModel.kt     # 生活方式管理ViewModel
│       │   │   └── ui/                          # UI界面
│       │   │       ├── navigation/              # 导航组件
│       │   │       │   └── AppNavigation.kt     # 应用导航配置
│       │   │       ├── screens/                 # 屏幕界面
│       │   │       │   ├── MoodScreen.kt        # 情绪追踪界面
│       │   │       │   └── LifestyleScreen.kt   # 生活方式追踪界面
│       │   │       └── theme/                   # 主题配置
│       │   │           ├── Color.kt             # 颜色定义
│       │   │           ├── Theme.kt             # 主题配置
│       │   │           └── Type.kt              # 字体配置
│       │   └── res/                             # 资源文件
│       ├── androidTest/                         # Android测试
│       └── test/                                # 单元测试
├── gradle/                        # Gradle配置
│   ├── libs.versions.toml        # 版本目录
│   └── wrapper/                  # Gradle Wrapper
├── build.gradle.kts              # 项目级构建配置
├── settings.gradle.kts           # 项目设置
├── gradle.properties             # Gradle属性
└── README.md                     # 项目说明文档
```

### 核心组件说明

#### 数据模型 (Model)
- **MoodEntry**: 情绪记录实体，包含情绪类型、时间段、日期等信息
- **LifestyleEntry**: 生活方式记录实体，包含睡眠、运动、压力等指标

#### 视图模型 (ViewModel)
- **MoodViewModel**: 管理情绪数据的状态和业务逻辑
- **LifestyleViewModel**: 管理生活方式数据的状态和业务逻辑

#### 用户界面 (UI)
- **AppNavigation**: 应用导航结构和底部导航栏
- **MoodScreen**: 情绪追踪界面，支持日期选择和情绪记录
- **LifestyleScreen**: 生活方式追踪界面，包含多种生活指标输入

## 🔧 技术规格

### 开发环境要求
- **Android Studio**: Arctic Fox 或更高版本
- **Kotlin**: 2.0.21
- **Android Gradle Plugin**: 8.10.1
- **Compose BOM**: 2024.09.00

### 目标平台
- **最低SDK版本**: Android 7.0 (API 24)
- **目标SDK版本**: Android 14 (API 35)
- **编译SDK版本**: Android 14 (API 35)

### 主要依赖库
```kotlin
// 核心库
implementation("androidx.core:core-ktx:1.10.1")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
implementation("androidx.activity:activity-compose:1.8.0")

// Compose UI
implementation(platform("androidx.compose:compose-bom:2024.09.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")

// 导航
implementation("androidx.navigation:navigation-compose:2.7.7")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

// 图标
implementation("androidx.compose.material:material-icons-extended:1.6.3")
```

## 🚀 快速开始

### 环境准备
1. 安装 Android Studio Arctic Fox 或更高版本
2. 确保已安装 Android SDK API 24-35
3. 配置 Kotlin 开发环境

### 项目构建
1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd NUS
   ```

2. **打开项目**
   - 使用 Android Studio 打开项目根目录
   - 等待 Gradle 同步完成

3. **运行应用**
   - 连接Android设备或启动模拟器
   - 点击 "Run" 按钮或使用快捷键 `Shift + F10`

### 构建命令
```bash
# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease

# 运行测试
./gradlew test

# 运行Android测试
./gradlew connectedAndroidTest
```

## 📱 使用指南

### 应用导航
应用采用底部导航栏设计，包含两个主要标签页：

1. **情绪追踪** (🙂 图标)
   - 选择日期
   - 记录早晨、下午、晚上的情绪状态
   - 添加情绪备注

2. **生活方式追踪** (📅 图标)
   - 选择日期
   - 填写各项生活指标
   - 保存当日记录

### 数据管理
- **本地存储**：所有数据暂时存储在应用内存中
- **数据持久化**：当前版本重启应用后数据会丢失
- **未来规划**：将集成数据库存储功能

### 界面特性
- **Material Design 3**：采用最新的Material Design设计语言
- **响应式布局**：适配不同屏幕尺寸的Android设备
- **深色模式支持**：自动适应系统主题设置
- **直观操作**：简洁明了的用户交互设计
- **实时反馈**：即时的视觉反馈和状态更新

### 数据模型详解

#### 情绪记录 (MoodEntry)
```kotlin
data class MoodEntry(
    val id: String,                    // 唯一标识符
    val mood: MoodType,                // 情绪类型 (VERY_GOOD, GOOD, NEUTRAL, BAD, VERY_BAD)
    val timeOfDay: TimeOfDay,          // 时间段 (MORNING, AFTERNOON, EVENING)
    val date: LocalDate,               // 记录日期
    val timestamp: LocalDateTime,      // 记录时间戳
    val notes: String                  // 备注信息
)
```

#### 生活方式记录 (LifestyleEntry)
```kotlin
data class LifestyleEntry(
    val id: String,                    // 唯一标识符
    val date: LocalDate,               // 记录日期
    val sleepHours: Float,             // 睡眠小时数
    val waterGlasses: Int,             // 饮水杯数
    val didOvertime: Boolean,          // 是否加班
    val exerciseMinutes: Int,          // 运动分钟数
    val stressLevel: Int,              // 压力水平 (0-10)
    val socialInteractionHours: Float, // 社交时间
    val notes: String                  // 备注信息
)
```

## 🔮 未来规划

### 短期目标
- [ ] 集成Room数据库实现数据持久化
- [ ] 添加数据导出功能
- [ ] 实现情绪趋势分析图表
- [ ] 添加提醒通知功能

### 长期目标
- [ ] 云端数据同步
- [ ] 多用户支持
- [ ] AI驱动的健康建议
- [ ] 社区分享功能
- [ ] 专业心理健康资源集成

## 🧪 测试

### 单元测试
```bash
./gradlew test
```

### UI测试
```bash
./gradlew connectedAndroidTest
```

### 测试覆盖率
项目包含基础的单元测试和UI测试框架，使用：
- JUnit 4 进行单元测试
- Espresso 进行UI自动化测试
- Compose Testing 进行Compose组件测试

### 测试策略
- **单元测试**: 测试ViewModel的业务逻辑和数据处理
- **集成测试**: 测试组件间的交互和数据流
- **UI测试**: 测试用户界面的交互和导航流程
- **性能测试**: 确保应用在各种设备上的流畅运行

## 🔧 开发指南

### 代码规范
- 遵循 [Kotlin编码规范](https://kotlinlang.org/docs/coding-conventions.html)
- 使用 [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)
- 采用MVVM架构模式
- 使用Compose最佳实践

### 分支管理
- `main`: 主分支，包含稳定的生产代码
- `develop`: 开发分支，用于集成新功能
- `feature/*`: 功能分支，用于开发新功能
- `bugfix/*`: 修复分支，用于修复问题

### 提交规范
使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：
```
feat: 添加新功能
fix: 修复问题
docs: 更新文档
style: 代码格式调整
refactor: 代码重构
test: 添加或修改测试
chore: 构建过程或辅助工具的变动
```

## 📊 性能优化

### 内存管理
- 使用Compose的状态管理避免内存泄漏
- 合理使用remember和rememberSaveable
- 避免在Composable中创建重对象

### UI性能
- 使用LazyColumn进行列表优化
- 避免不必要的重组
- 使用derivedStateOf优化计算状态

### 构建优化
- 启用R8代码压缩和混淆
- 使用ProGuard规则优化APK大小
- 配置多APK支持不同架构

## 🔒 安全与隐私

### 数据安全
- **本地存储**: 当前版本数据仅存储在设备本地内存中
- **无网络传输**: 应用不会向外部服务器发送任何个人数据
- **隐私保护**: 所有心理健康数据完全私密，仅用户本人可访问
- **数据加密**: 未来版本将实现本地数据加密存储

### 隐私政策要点
- 不收集任何个人身份信息
- 不使用第三方分析服务
- 不包含广告或跟踪代码
- 用户完全控制自己的数据

### 权限说明
应用当前不需要任何特殊权限：
- 无需网络访问权限
- 无需存储权限
- 无需位置信息权限
- 无需相机或麦克风权限

## 🚨 免责声明

### 医疗免责声明
本应用仅用于个人健康记录和自我监测目的，不能替代专业的医疗建议、诊断或治疗。如果您正在经历严重的心理健康问题，请立即寻求专业医疗帮助。

### 紧急情况
如果您有自伤或伤害他人的想法，请立即联系：
- 紧急服务: 120 (中国大陆)
- 心理危机干预热线: 400-161-9995
- 当地心理健康服务机构

## 🤝 贡献指南

### 如何贡献
1. Fork 项目到您的GitHub账户
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 遵循代码规范进行开发
4. 添加相应的测试用例
5. 提交更改 (`git commit -m 'feat: Add some AmazingFeature'`)
6. 推送到分支 (`git push origin feature/AmazingFeature`)
7. 创建 Pull Request

### 贡献类型
- 🐛 Bug修复
- ✨ 新功能开发
- 📚 文档改进
- 🎨 UI/UX优化
- ⚡ 性能优化
- 🧪 测试覆盖率提升

### 代码审查
所有Pull Request都需要经过代码审查：
- 确保代码质量和规范
- 验证功能正确性
- 检查测试覆盖率
- 评估性能影响

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 项目Issues: [GitHub Issues](https://github.com/your-repo/issues)
- 邮箱: your-email@example.com

## 🙏 致谢

感谢所有为心理健康意识和Android开发社区做出贡献的开发者和研究人员。

---

**注意**: 这是一个教育和演示项目，不应替代专业的心理健康咨询和治疗。如需专业帮助，请咨询合格的心理健康专家。
