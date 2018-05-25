package me.xofu.simplechunk.task.tasks;

import me.xofu.simplechunk.SimpleChunk;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class ClaimSaveTask extends BukkitRunnable {

    private SimpleChunk instance;

    public ClaimSaveTask(SimpleChunk instance) {
        this.instance = instance;

        runTaskTimerAsynchronously(instance, 1, 36000);
    }

    @Override
    public void run() {
        instance.getClaimManager().save();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[SimpleChunk] " + instance.getClaimManager().getClaims().size() + " claims has been saved");
    }
}
