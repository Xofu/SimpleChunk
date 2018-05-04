package me.xofu.simplechunk.data;

import me.xofu.simplechunk.SimpleChunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataFile {

    private SimpleChunk instance;

    private String name;
    private File file;
    private FileConfiguration config;

    public DataFile(String name, SimpleChunk instance) {
        this.instance = instance;

        this.name = name;
        this.file = new File(instance.getDataFolder(), name);

        if(!file.exists()) {
            file.mkdirs();

            if(instance.getResource(name) == null) {
                try {
                    file.createNewFile();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                instance.saveResource(name, false);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            getConfig().save(file);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
