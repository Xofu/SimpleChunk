package me.xofu.simplechunk.command.commands;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.command.Command;
import me.xofu.simplechunk.command.commands.subcommands.simplechunk.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SimpleChunkCommand extends Command {

    private SimpleChunk instance;

    public SimpleChunkCommand(SimpleChunk instance) {
        super(instance,"simplechunk", false);
        this.instance = instance;

        super.addSubCommand(new SimpleChunkSetMaxClaimsCommand(instance))
                .addSubCommand(new SimpleChunkSetPriceCommand(instance))
                .addSubCommand(new SimpleChunkSetSaleCommand(instance))
                .addSubCommand(new SimpleChunkClaimCommand(instance))
                .addSubCommand(new SimpleChunkUnclaimCommand(instance))
                .addSubCommand(new SimpleChunkPurgeClaimCommand(instance))
                .addSubCommand(new SimpleChunkPurgePlayerCommand(instance))
                .addSubCommand(new SimpleChunkPurgeAllCommand(instance))
                .addSubCommand(new SimpleChunkSetRefundCommand(instance))
                .addSubCommand(new SimpleChunkSetOwnerCommand(instance))
                .addSubCommand(new SimpleChunkReloadCommand(instance));
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender.hasPermission("simplechunk.command.help"))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        for(String line: instance.getConfig().getStringList("SIMPLECHUNK_HELP")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%claims%", String.valueOf(instance.getClaimManager().getClaims().size()))));
        }
    }
}
