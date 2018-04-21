package me.xofu.simplechunk.command.commands;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.command.Command;
import me.xofu.simplechunk.command.commands.subcommands.chunk.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkCommand extends Command {

    private SimpleChunk instance;

    public ChunkCommand(SimpleChunk instance) {
        super(instance,"chunk", true);
        this.instance = instance;

        super.addSubCommand(new ChunkClaimCommand(instance))
                .addSubCommand(new ChunkUnclaimCommand(instance))
                .addSubCommand(new ChunkAllowCommand(instance))
                .addSubCommand(new ChunkDisallowCommand(instance))
                .addSubCommand(new ChunkSellCommand(instance))
                .addSubCommand(new ChunkBuyCommand(instance))
                .addSubCommand(new ChunkInfoCommand(instance))
                .addSubCommand(new ChunkListCommand(instance));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        for(String line: instance.getConfig().getStringList("CHUNK_HELP")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }
}
