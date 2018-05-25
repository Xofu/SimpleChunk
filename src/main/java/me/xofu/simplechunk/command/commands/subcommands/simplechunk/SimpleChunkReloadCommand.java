package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SimpleChunkReloadCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkReloadCommand(SimpleChunk instance) {
        super("reload");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        instance.reloadConfig();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CONFIG_RELOADED")));
    }
}
