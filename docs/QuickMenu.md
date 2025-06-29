# QuickMenu 使用文档

QuickMenu（QuickPopupMenu）是一个灵活的 Android 弹出菜单组件，支持分组、单选/多选、图标自定义、分割线等丰富功能，适用于需要快速弹出操作菜单的场景。

## 目录

- [基本用法](#基本用法)
- [菜单项配置](#菜单项配置)
- [菜单弹出方式](#菜单弹出方式)
- [常用方法说明](#常用方法说明)
- [高级用法](#高级用法)
- [注意事项](#注意事项)
- [示例](#示例)

## 基本用法

1. 定义菜单项：

    ```kotlin
    val items = listOf(
        MenuItem("编辑", R.drawable.ic_edit) { /* 点击回调 */ },
        MenuItem("删除", R.drawable.ic_delete) { /* 点击回调 */ }
    )
    ```

2. 创建菜单并显示：

    ```kotlin
    val quickMenu = QuickPopupMenu(context, items)
    quickMenu.show(anchorView)
    ```

## 菜单项配置

- `MenuItem` 支持：
  - 文本、图标、点击回调
  - 分组（selectGroup）、单选/多选（selectMode）
  - 是否选中（isSelected）

- `MenuConfig` 可选：
  - 图标位置（左/右）
  - 是否显示分割线

## 菜单弹出方式

- 依附锚点 View 弹出：

    ```kotlin
    quickMenu.show(anchorView, verticalGravity, horizontalGravity, xOffset, yOffset)
    ```

- 在指定位置弹出：

    ```kotlin
    quickMenu.showAtLocation(parentView, x, y, xOffset, yOffset)
    ```

## 常用方法说明

- `show(anchorView, verticalGravity, horizontalGravity, xOffset, yOffset)`
  - 依附锚点弹出，支持重力和偏移。
- `showAtLocation(parent, x, y, xOffset, yOffset)`
  - 在父视图指定坐标弹出。
- `dismiss()`
  - 关闭菜单。

## 高级用法

- 支持分组互斥选择（同组单选/多选）。
- 支持自定义菜单项布局（可扩展 MenuItemView）。
- 支持菜单项选中状态、分割线、图标位置等自定义。

## 注意事项

- 弹出前请确保 anchorView 或 parentView 已 attach 到窗口。
- 菜单项建议使用 wrap_content 适配不同内容。
- 菜单项点击后菜单会自动关闭。

## 示例

```kotlin
val items = listOf(
    MenuItem("编辑", R.drawable.ic_edit) { /* 编辑操作 */ },
    MenuItem("删除", R.drawable.ic_delete) { /* 删除操作 */ }
)
val config = MenuConfig(iconPosition = MenuConfig.IconPosition.LEFT, showDivider = true)
val quickMenu = QuickPopupMenu(context, items, config)
quickMenu.show(anchorView)
```

如需更多自定义用法，请参考源码或联系维护者。
