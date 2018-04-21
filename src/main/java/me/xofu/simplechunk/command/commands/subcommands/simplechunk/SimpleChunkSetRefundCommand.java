package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.command.SubCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SimpleChunkSetRefundCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkSetRefundCommand(SimpleChunk instance) {
        super("setrefund");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("simplechunk.command.setrefund")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length == 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/simplechunk setrefund <refund>")));
            return;
        }

        if(!StringUtils.isNumeric(args[1])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_AN_AMOUNT").replace("%amount%", args[1])));
            return;
        }

        instance.getConfig().set("Claim_refund", args[1]);
        instance.saveConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CLAIM_REFUND_SET").replace("%refund%", args[1])));
    }
}
