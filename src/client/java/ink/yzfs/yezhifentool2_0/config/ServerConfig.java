package ink.yzfs.yezhifentool2_0.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "yezhifentool-servers.json");
    
    private static ServerConfig instance;
    
    // 服务器配置
    public String mainServerName = "夜之粉主服";
    public String mainServerAddress = "sv.baby:25571";
    public String mirrorServerName = "镜像服";
    public String mirrorServerAddress = "sv.baby:25565";
    
    public static ServerConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }
    
    private static ServerConfig load() {
        System.out.println("Loading server config from: " + CONFIG_FILE.getAbsolutePath());
        
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ServerConfig config = GSON.fromJson(reader, ServerConfig.class);
                if (config != null) {
                    System.out.println("Server config loaded successfully!");
                    return config;
                }
            } catch (IOException e) {
                System.err.println("Failed to load server config: " + e.getMessage());
            }
        }
        
        // 如果配置文件不存在或加载失败，创建默认配置
        System.out.println("Creating default server config...");
        ServerConfig defaultConfig = new ServerConfig();
        defaultConfig.save();
        return defaultConfig;
    }
    
    public void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
                System.out.println("Server config saved to: " + CONFIG_FILE.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to save server config: " + e.getMessage());
        }
    }
    
    public String getMainServerAddress() {
        return mainServerAddress;
    }
    
    public String getMirrorServerAddress() {
        return mirrorServerAddress;
    }
    
    public String getMainServerName() {
        return mainServerName;
    }
    
    public String getMirrorServerName() {
        return mirrorServerName;
    }
}
