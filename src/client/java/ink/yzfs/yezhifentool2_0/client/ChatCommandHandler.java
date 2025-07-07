package ink.yzfs.yezhifentool2_0.client;

import ink.yzfs.yezhifentool2_0.config.ServerConfig;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatCommandHandler {

    public static void register() {
        // 注册聊天消息发送事件监听器
        ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ServerConfig config = ServerConfig.getInstance();

            // 检查是否是我们的快捷命令
            if (message.equals("!!jx")) {
                // 传送到镜像服
                client.execute(() -> {
                    ServerConnectionHandler.connectToServer(client, config.getMirrorServerAddress(), config.getMirrorServerName());
                });
                // 允许消息发送到服务器
            } else if (message.equals("!!zf")) {
                // 传送到主服
                client.execute(() -> {
                    ServerConnectionHandler.connectToServer(client, config.getMainServerAddress(), config.getMainServerName());
                });
                // 允许消息发送到服务器
            } else if (message.equals("!!c")) {
                // 显示当前服务器信息
                showCurrentServer(client);
                // 允许消息发送到服务器
            } else if (message.startsWith("!!c ")) {
                // 连接到指定服务器 !!c <ip:端口>
                String serverAddress = message.substring(4).trim();
                connectToCustomServer(client, serverAddress);
                // 允许消息发送到服务器
            }

            return true; // 允许所有消息正常发送
        });

        System.out.println("Chat command handler registered successfully!");
    }

    private static void showCurrentServer(MinecraftClient client) {
        String currentServer = getCurrentServerInfo(client);
        client.inGameHud.getChatHud().addMessage(Text.literal("§e[服务器切换] §f当前状态: §a" + currentServer));
    }

    private static void connectToCustomServer(MinecraftClient client, String serverAddress) {
        // 如果没有指定端口，默认使用25565
        if (!serverAddress.contains(":")) {
            serverAddress += ":25565";
        }

        final String finalServerAddress = serverAddress; // 创建final变量供lambda使用

        client.inGameHud.getChatHud().addMessage(Text.literal("§e[服务器切换] §f正在连接到: §a" + finalServerAddress));
        client.execute(() -> {
            ServerConnectionHandler.connectToServer(client, finalServerAddress, "自定义服务器");
        });
    }

    private static String getCurrentServerInfo(MinecraftClient client) {
        if (client.world == null) {
            return "主菜单";
        }

        if (client.isInSingleplayer()) {
            return "单人游戏";
        }

        if (client.getCurrentServerEntry() != null) {
            String address = client.getCurrentServerEntry().address;
            ServerConfig config = ServerConfig.getInstance();

            if (address.equals(config.getMainServerAddress())) {
                return config.getMainServerName() + " (" + address + ")";
            } else if (address.equals(config.getMirrorServerAddress())) {
                return config.getMirrorServerName() + " (" + address + ")";
            } else {
                return "其他服务器 (" + address + ")";
            }
        }

        return "未知状态";
    }
}
