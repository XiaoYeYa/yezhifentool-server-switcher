package ink.yzfs.yezhifentool2_0.client;

import ink.yzfs.yezhifentool2_0.config.ServerConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Yezhifentool2_0Client implements ClientModInitializer {

    private static KeyBinding serverSwitchKey;

    @Override
    public void onInitializeClient() {
        // 初始化配置文件（这会创建默认配置文件如果不存在）
        ServerConfig.getInstance();
        System.out.println("Server config initialized!");

        // 注册快捷键
        serverSwitchKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.yezhifentool2_0.server_switch", // 翻译键
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O, // 默认按键为O
                "category.yezhifentool2_0.general" // 分类
        ));

        // 注册客户端tick事件来检测按键
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (serverSwitchKey.wasPressed()) {
                if (client.currentScreen == null) {
                    // 检查是否在单人游戏中，如果是则不打开界面
                    if (client.isInSingleplayer()) {
                        // 在聊天中显示提示信息
                        if (client.inGameHud != null && client.inGameHud.getChatHud() != null) {
                            client.inGameHud.getChatHud().addMessage(
                                net.minecraft.text.Text.literal("§c[服务器切换] §f单人游戏中不支持快捷传送，请使用聊天命令 !!c")
                            );
                        }
                    } else {
                        client.setScreen(new ServerSwitchScreen());
                    }
                }
            }
        });

        // 注册聊天命令处理器
        ChatCommandHandler.register();
    }
}
