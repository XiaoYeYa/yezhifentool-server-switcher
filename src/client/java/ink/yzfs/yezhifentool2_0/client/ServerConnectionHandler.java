package ink.yzfs.yezhifentool2_0.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class ServerConnectionHandler {

    public static void connectToServer(MinecraftClient client, String serverAddress, String serverName) {
        // 创建服务器信息
        ServerInfo serverInfo = new ServerInfo(serverName, serverAddress, ServerInfo.ServerType.OTHER);

        try {
            // 检查是否在单人世界或多人服务器中
            boolean inWorld = client.world != null;
            boolean inSinglePlayer = client.isInSingleplayer();

            System.out.println("Current state - inWorld: " + inWorld + ", inSinglePlayer: " + inSinglePlayer);

            if (inWorld) {
                if (inSinglePlayer) {
                    // 单人世界特殊处理 - 直接在主线程处理
                    System.out.println("Disconnecting from single player world...");

                    // 直接断开连接，不使用client.execute避免死锁
                    client.disconnect();

                    // 在后台线程中等待并连接
                    new Thread(() -> {
                        try {
                            // 等待断开连接完成
                            Thread.sleep(2000);

                            // 设置主菜单并连接
                            client.execute(() -> {
                                client.setScreen(new TitleScreen());
                                connectToServerInternal(client, serverAddress, serverName, serverInfo);
                            });

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.err.println("Connection interrupted: " + e.getMessage());
                        }
                    }, "ServerSwitcher-SinglePlayer").start();

                } else {
                    // 多人服务器处理 - 直接在主线程处理
                    System.out.println("Disconnecting from multiplayer server...");

                    // 直接设置屏幕和断开连接，不使用client.execute避免死锁
                    client.setScreen(new TitleScreen());
                    client.disconnect();

                    // 在后台线程中等待并连接
                    new Thread(() -> {
                        try {
                            // 等待断开连接完成
                            Thread.sleep(500);

                            // 连接到新服务器
                            client.execute(() -> {
                                connectToServerInternal(client, serverAddress, serverName, serverInfo);
                            });

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.err.println("Connection interrupted: " + e.getMessage());
                        }
                    }, "ServerSwitcher-Multiplayer").start();
                }
            } else {
                // 如果不在游戏中，直接连接
                connectToServerInternal(client, serverAddress, serverName, serverInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect to server: " + serverAddress);
        }
    }

    private static void connectToServerInternal(MinecraftClient client, String serverAddress, String serverName, ServerInfo serverInfo) {
        try {
            // 解析服务器地址
            ServerAddress parsedAddress = ServerAddress.parse(serverAddress);

            // 确保我们有一个合适的父屏幕
            TitleScreen titleScreen = new TitleScreen();

            System.out.println("Attempting to connect to: " + serverName + " (" + serverAddress + ")");

            // 使用ConnectScreen.connect静态方法进行连接
            ConnectScreen.connect(
                titleScreen,          // 使用标题屏幕作为父屏幕
                client,               // MinecraftClient实例
                parsedAddress,        // 服务器地址
                serverInfo,           // 服务器信息
                false,                // quickPlay设为false
                null                  // cookieStorage设为null
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect to server internally: " + serverAddress);

            // 连接失败时回到主菜单
            client.execute(() -> {
                client.setScreen(new TitleScreen());
            });
        }
    }
}
