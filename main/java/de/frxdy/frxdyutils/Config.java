package de.frxdy.frxdyutils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private static File file;
    private static YamlConfiguration config;

    public Config(String name) throws NullPointerException {
        File dir = new File("./plugins/FrxdyUtils/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        file = new File(dir, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public void set(String path, Object value) throws IOException {
        config.set(path, value);
        config.save(file);
    }

    public Object get(String path) {
        return !contains(path) ? null : config.get(path);
    }
}
