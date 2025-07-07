package ink.yzfs.yezhifentool2_0.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ServerSwitchScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 25;

    // UI框架尺寸
    private static final int PANEL_WIDTH = 280;
    private static final int PANEL_HEIGHT = 160;
    private static final int PANEL_PADDING = 20;
    
    // 服务器信息
    private static final String MAIN_SERVER_IP = "sv.baby:25571";
    private static final String MIRROR_SERVER_IP = "sv.baby:25565";
    
    public ServerSwitchScreen() {
        super(Text.translatable("gui.yezhifentool2_0.server_switch.title"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // 计算面板位置
        int panelX = centerX - PANEL_WIDTH / 2;
        int panelY = centerY - PANEL_HEIGHT / 2;

        // 按钮在面板内的位置
        int buttonX = panelX + (PANEL_WIDTH - BUTTON_WIDTH) / 2;
        int buttonStartY = panelY + 50; // 留出标题空间

        // 检查是否在单人游戏中
        boolean inSinglePlayer = this.client != null && this.client.isInSingleplayer();

        // 夜之粉主服按钮
        ButtonWidget mainServerButton = ButtonWidget.builder(
                Text.translatable("gui.yezhifentool2_0.server_switch.main_server"),
                button -> {
                    if (!inSinglePlayer) {
                        connectToServer(MAIN_SERVER_IP, "夜之粉主服");
                    }
                })
                .dimensions(buttonX, buttonStartY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        mainServerButton.active = !inSinglePlayer; // 单人游戏中禁用按钮
        this.addDrawableChild(mainServerButton);

        // 镜像服按钮
        ButtonWidget mirrorServerButton = ButtonWidget.builder(
                Text.translatable("gui.yezhifentool2_0.server_switch.mirror_server"),
                button -> {
                    if (!inSinglePlayer) {
                        connectToServer(MIRROR_SERVER_IP, "镜像服");
                    }
                })
                .dimensions(buttonX, buttonStartY + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        mirrorServerButton.active = !inSinglePlayer; // 单人游戏中禁用按钮
        this.addDrawableChild(mirrorServerButton);

        // 取消按钮
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.cancel"),
                button -> this.close())
                .dimensions(buttonX, buttonStartY + BUTTON_SPACING * 2, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 先渲染默认背景（包括模糊效果）
        this.renderBackground(context, mouseX, mouseY, delta);

        // 绘制我们的面板
        this.renderPanel(context, mouseX, mouseY);

        // 渲染按钮等组件（在面板之后，这样按钮在面板上方）
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderPanel(DrawContext context, int mouseX, int mouseY) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // 计算面板位置
        int panelX = centerX - PANEL_WIDTH / 2;
        int panelY = centerY - PANEL_HEIGHT / 2;

        // 绘制主面板框架 - 完全不透明的背景
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xFF2D2D30);

        // 绘制面板外边框 - 亮色边框
        context.drawBorder(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 0xFFC6C6C6);

        // 绘制内部边框 - 深色边框形成凹陷效果
        context.drawBorder(panelX + 1, panelY + 1, PANEL_WIDTH - 2, PANEL_HEIGHT - 2, 0xFF555555);

        // 绘制标题区域背景 - 稍微亮一点的背景
        context.fill(panelX + 2, panelY + 2, panelX + PANEL_WIDTH - 2, panelY + 45, 0xFF3C3C41);

        // 渲染标题 - 在面板内部
        context.drawCenteredTextWithShadow(this.textRenderer, this.title,
                centerX, panelY + 12, 0xFFFFFF);

        // 渲染当前服务器状态
        String currentServer = getCurrentServerStatus();
        Text statusText = Text.literal("当前状态: " + currentServer);
        context.drawCenteredTextWithShadow(this.textRenderer, statusText,
                centerX, panelY + 25, 0xFFD700); // 金色文字

        // 渲染说明文本 - 在面板内部
        Text description = Text.translatable("gui.yezhifentool2_0.server_switch.description");
        context.drawCenteredTextWithShadow(this.textRenderer, description,
                centerX, panelY + 38, 0xAAAAAA);
    }

    private String getCurrentServerStatus() {
        if (this.client == null) {
            return "未知";
        }

        if (this.client.world == null) {
            return "主菜单";
        }

        if (this.client.isInSingleplayer()) {
            return "单人游戏 (不支持快捷传送)";
        }

        if (this.client.getCurrentServerEntry() != null) {
            String serverAddress = this.client.getCurrentServerEntry().address;
            if (serverAddress.contains("sv.baby:25571")) {
                return "夜之粉主服";
            } else if (serverAddress.contains("sv.baby:25565")) {
                return "镜像服";
            } else {
                return "其他服务器 (" + serverAddress + ")";
            }
        }

        return "未知状态";
    }

    private void connectToServer(String serverAddress, String serverName) {
        if (this.client != null) {
            // 创建服务器连接处理器
            ServerConnectionHandler.connectToServer(this.client, serverAddress, serverName);
            this.close();
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
