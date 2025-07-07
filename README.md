# yezhifentool-server-switcher

在聊天栏发送!!jx或!!zf可切换子主服务器，或者按下快捷键打开模组的UI进行切换服务器。
使用请时刻遵循《夜之粉玩家条约及服规》最新修订版: https://docs.qq.com/doc/DYW5hclNianllVnVo。
该模组归属于YEZHIFEN-Tools、夜之粉快捷工具系列模组（注：本模组只允许在夜之粉系列服务器内使用，如果用于其他服务器造成问题后果自负）。


### 💬 聊天命令
- `!!jx` - 快速连接到镜像服
- `!!zf` - 快速连接到主服
- `!!c` - 显示当前服务器状态
- `!!c <ip:端口>` - 连接到自定义服务器（端口默认25565）

### ⚙️ 配置系统
- 自动生成配置文件：`.minecraft/config/yezhifentool-servers.json`
- 支持自定义服务器名称和地址
- 配置文件热加载


## 📦 安装方法

1. 确保已安装 [Fabric Loader](https://fabricmc.net/use/installer/) 和 [Fabric API](https://modrinth.com/mod/fabric-api)
2. 下载最新版本的模组文件
3. 将 `.jar` 文件放入 `.minecraft/mods` 文件夹
4. 启动游戏

## 🎯 默认服务器配置

- **夜之粉主服**: `sv.baby:25571`
- **镜像服**: `sv.baby:25565`

## 🔧 自定义配置

首次启动后，模组会在 `.minecraft/config/` 目录下生成 `yezhifentool-servers.json` 配置文件：

```json
{
  "mainServerName": "夜之粉主服",
  "mainServerAddress": "sv.baby:25571",
  "mirrorServerName": "镜像服",
  "mirrorServerAddress": "sv.baby:25565"
}
```

你可以编辑此文件来自定义服务器信息。

## 🎮 使用方法

### 快捷键方式
1. 在多人游戏或主菜单中按 `O` 键
2. 选择要连接的服务器
3. 点击按钮即可切换

### 聊天命令方式
在聊天框中输入以下命令：
- `!!jx` - 连接镜像服
- `!!zf` - 连接主服
- `!!c` - 查看当前状态
- `!!c mc.hypixel.net` - 连接到 Hypixel（示例）

## 🚫 限制说明

- 单人游戏模式下不支持服务器切换功能
- 快捷键和UI按钮在单人模式下会被禁用
- 聊天命令在所有模式下都可用，但仅在多人模式下生效

## 📋 更新日志

### v1.0.0
- ✅ 基础服务器切换功能
- ✅ 快捷键支持（O键）
- ✅ 聊天命令支持
- ✅ 配置文件系统
- ✅ 单人游戏保护
- ✅ 美观的UI界面

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE.txt](LICENSE.txt) 文件了解详情。

## 🔗 相关链接

- [Fabric 官网](https://fabricmc.net/)
- [Minecraft Mod 开发文档](https://fabricmc.net/wiki/)


