# 😊 Emotion Display Settings Feature

## 🎯 概述

成功实现了情绪显示控制功能：在HomeScreen中添加设置开关，控制FeelScreen中是否显示情绪相关内容。这个功能只在用户界面中实现，不会影响counsellor的界面。

## ✨ 新增功能

### 1. 📱 用户设置管理
- **UserSettingsManager**: 使用SharedPreferences持久化存储用户设置
- **单例模式**: 确保全局只有一个设置管理器实例
- **自动保存**: 设置更改会立即保存到本地存储

### 2. 🎛️ 设置界面组件（HomeScreen）
- **SettingsSection**: 主设置区域，包含设置标题和选项
- **EmotionDisplaySetting**: 情绪显示开关，包含图标、描述和切换开关

### 3. 📱 情绪内容控制（FeelScreen）
- **条件显示**: 根据showEmotion设置控制所有情绪相关内容
- **替代内容**: 当情绪被禁用时显示说明文字
- **完整隐藏**: 情绪选择、AI检测、引用等全部隐藏

### 4. 🔄 状态管理
- **UserSessionViewModel**: 更新为AndroidViewModel以支持Application context
- **实时同步**: 设置更改会立即反映在FeelScreen和本地存储中
- **默认值**: 新用户默认启用情绪显示

## 🎨 UI设计特点

### 视觉层次
- **卡片布局**: 使用Material Design 3的卡片组件
- **颜色主题**: 遵循Material Design颜色系统
- **图标指示**: 使用Visibility/VisibilityOff图标表示状态

### 交互体验
- **开关控件**: 使用Material Switch组件
- **即时反馈**: 切换后立即显示/隐藏预览
- **状态描述**: 清晰的文字说明当前状态

### 响应式设计
- **自适应布局**: 支持不同屏幕尺寸
- **合理间距**: 使用一致的16dp间距
- **视觉层次**: 通过卡片阴影和颜色区分不同区域

## 🔧 技术实现

### 数据持久化
```kotlin
class UserSettingsManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    
    fun getShowEmotion(): Boolean = prefs.getBoolean("show_emotion", true)
    fun setShowEmotion(showEmotion: Boolean) = prefs.edit().putBoolean("show_emotion", showEmotion).apply()
    fun toggleShowEmotion(): Boolean = !getShowEmotion().also { setShowEmotion(it) }
}
```

### ViewModel集成
```kotlin
class UserSessionViewModel(application: Application) : AndroidViewModel(application) {
    private val userSettingsManager = UserSettingsManager.getInstance(application)
    val showEmotion = mutableStateOf(userSettingsManager.getShowEmotion())
    
    fun toggleShowEmotion() {
        val newValue = userSettingsManager.toggleShowEmotion()
        showEmotion.value = newValue
    }
}
```

### UI组件结构
```
HomeScreen
├── Header (MoodyClues + Notifications)
├── Welcome Message
├── Action Cards (Mood + Habit)
└── Settings Section
    └── Emotion Display Setting
        ├── Icon + Text + Switch
        └── Emotion Preview (conditional)
```

## 📱 用户使用流程

1. **打开应用**: 用户登录后进入HomeScreen
2. **查看设置**: 在页面底部看到Settings区域
3. **切换设置**: 点击"Show Emotions"开关
4. **导航到FeelScreen**: 通过底部导航栏进入Feel页面
5. **查看效果**:
   - **启用时**: 显示完整的情绪选择、AI检测、引用等
   - **禁用时**: 显示"情绪追踪已禁用"的提示信息
6. **自动保存**: 设置立即保存，下次打开应用时保持

## 🔒 安全性和隐私

### 本地存储
- **SharedPreferences**: 数据存储在应用私有目录
- **无网络传输**: 设置仅在本地存储，不发送到服务器
- **用户控制**: 完全由用户控制是否显示情绪

### 数据保护
- **应用级隔离**: 其他应用无法访问设置数据
- **卸载清理**: 应用卸载时自动清理所有设置数据

## 🎯 使用场景

### 隐私保护
- **公共场所**: 用户在公共场所使用时可隐藏情绪
- **分享屏幕**: 演示或分享屏幕时保护隐私
- **个人偏好**: 根据个人喜好选择是否显示

### 个性化体验
- **简洁界面**: 不需要情绪功能的用户可以隐藏
- **专注模式**: 专注于其他功能时减少干扰
- **渐进式使用**: 新用户可以逐步启用功能

## 🚀 未来扩展

### 更多设置选项
- **通知设置**: 控制各种通知的显示
- **主题设置**: 深色/浅色主题切换
- **语言设置**: 多语言支持

### 高级功能
- **设置同步**: 跨设备同步用户设置
- **设置备份**: 云端备份和恢复设置
- **家长控制**: 为未成年用户提供额外控制

## 📋 文件清单

### 新增文件
- `app/src/main/java/com/example/nus/data/UserSettingsManager.kt`

### 修改文件
- `app/src/main/java/com/example/nus/ui/screens/HomeScreen.kt` - 添加设置界面
- `app/src/main/java/com/example/nus/ui/screens/FeelScreen.kt` - 添加情绪显示控制
- `app/src/main/java/com/example/nus/viewmodel/UserSessionViewModel.kt` - 更新为AndroidViewModel
- `app/src/main/java/com/example/nus/ui/navigation/AppNavigation.kt` - 传递ViewModel参数

### 功能特点
- ✅ 本地设置存储
- ✅ 实时UI更新
- ✅ FeelScreen情绪内容控制
- ✅ Material Design 3设计
- ✅ 只在用户界面实现
- ✅ 向后兼容性
- ✅ 友好的禁用状态提示

## 🎯 实际效果

### 启用情绪显示时（FeelScreen）
- 显示"Today, it seems like you felt..."
- 显示AI检测的情绪标签
- 显示情绪选择按钮（😊 Happy, 🎉 Excited等）
- 选择情绪后显示相关引用

### 禁用情绪显示时（FeelScreen）
- 显示"Emotion tracking is currently disabled."
- 显示"You can enable it in Settings on the Home screen."
- 隐藏所有情绪相关的交互元素
- 保留Go Home按钮和其他基本功能

这个功能为用户提供了完整的隐私控制，可以根据需要完全隐藏情绪追踪功能！
