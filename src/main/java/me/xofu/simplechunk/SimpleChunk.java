package me.xofu.simplechunk;

import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.claim.ClaimManager;
import me.xofu.simplechunk.command.CommandManager;
import me.xofu.simplechunk.command.commands.ChunkCommand;
import me.xofu.simplechunk.command.commands.SimpleChunkCommand;
import me.xofu.simplechunk.data.FileManager;
import me.xofu.simplechunk.listeners.BlockListener;
import me.xofu.simplechunk.listeners.EntityListener;
import me.xofu.simplechunk.listeners.PlayerListener;
import me.xofu.simplechunk.listeners.VehichleListener;
import me.xofu.simplechunk.task.TaskManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleChunk extends JavaPlugin {

    private FileManager fileManager;
    private ClaimManager claimManager;
    private CommandManager commandManager;
    private TaskManager taskManager;

    private Economy economy = null;
    private Permission permission = null;

    @Override
    public void onEnable() {
        fileManager = new FileManager(this);
        claimManager = new ClaimManager(this);
        commandManager = new CommandManager(this);
        taskManager = new TaskManager(this);

        registerCommands();
        registerListeners();

        setupEconomy();
        setupPermissions();

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        claimManager.save();
    }

    private void registerCommands() {
        commandManager.register(new ChunkCommand(this));
        commandManager.register(new SimpleChunkCommand(this));
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new BlockListener(this), this);
        pm.registerEvents(new EntityListener(this), this);
        pm.registerEvents(new VehichleListener(this), this);
    }

    public FileManager getFileManager() { return fileManager; }

    public ClaimManager getClaimManager() {
        return claimManager;
    }

    public TaskManager getTaskManager() { return taskManager; }

    public Economy getEconomy() {
        return economy;
    }

    public Permission getPermission() {
        return permission;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
