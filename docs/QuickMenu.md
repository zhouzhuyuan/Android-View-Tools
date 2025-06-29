# QuickMenu 使用文档

QuickMenu 是一个用于 Android 应用的快速弹出菜单组件，支持自定义内容、位置和动画，适用于需要在界面上弹出操作菜单的场景。

## 目录

- [基本用法](#基本用法)
- [常用方法说明](#常用方法说明)
- [高级用法](#高级用法)
- [注意事项](#注意事项)
- [示例](#示例)

## 基本用法

### 1. 初始化 QuickPopupMenu

```kotlin
val quickMenu = QuickPopupMenu(context)
```

### 2. 设置菜单内容（可自定义布局）

```kotlin
quickMenu.setContentView(R.layout.your_menu_layout)
```

### 3. 显示菜单

- 依附某个锚点 View 弹出：

```kotlin
quickMenu.show(anchorView)
```

- 在指定位置弹出：

```kotlin
quickMenu.showAtLocation(parentView, x = 100, y = 200)
```

## 常用方法说明

- `setContentView(layoutResId: Int)`  
  设置菜单内容布局。

- `show(anchorView: View, xOffset: Int = 0, yOffset: Int = 0)`  
  依附锚点 View 弹出菜单，可设置偏移量。

- `showAtLocation(parent: View, x: Int = 0, y: Int = 0, xOffset: Int = 0, yOffset: Int = 0)`  
  在父 View 的指定位置弹出菜单。

- `dismiss()`  
  隐藏菜单。

- `setOnDismissListener(listener: () -> Unit)`  
  设置菜单关闭监听。

## 高级用法

- 支持自定义动画、背景、阴影等属性。
- 可通过 `getContentView()` 获取内容 View，进行事件绑定或动态修改。

## 注意事项

- 弹出菜单前请确保 anchorView 或 parentView 已经 attach 到窗口。
- 菜单内容布局建议使用 wrap_content 以适配不同内容。

## 示例

```kotlin
val quickMenu = QuickPopupMenu(context)
quickMenu.setContentView(R.layout.menu_layout)
quickMenu.getContentView().findViewById<View>(R.id.menu_item).setOnClickListener {
    // 处理点击
    quickMenu.dismiss()
}
quickMenu.show(anchorView)
```

如需更多自定义用法，请参考源码或联系维护者。
