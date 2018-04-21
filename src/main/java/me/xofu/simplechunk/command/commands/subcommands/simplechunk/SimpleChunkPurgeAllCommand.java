package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SimpleChunkPurgeAllCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkPurgeAllCommand(SimpleChunk instance) {
        super("purgeall");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("simplechunk.command.purgeall")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(instance.getClaimManager().getClaims().isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_CLAIMS")));
            return;
        }

        instance.getClaimManager().getClaims().removeAll(instance.getClaimManager().getClaims());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ALL_CLAIMS_PURGED")));
    }
}
