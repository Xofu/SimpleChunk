package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.command.SubCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SimpleChunkSetPriceCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkSetPriceCommand(SimpleChunk instance) {
        super("setprice");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("simplechunk.command.setprice")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length == 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/simplechunk setprice <price>")));
            return;
        }

        if(!StringUtils.isNumeric(args[1])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_AN_AMOUNT").replace("%amount%", args[1])));
            return;
        }

        instance.getConfig().set("Claim_price", args[1]);
        instance.saveConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CLAIM_PRICE_SET").replace("%price%", args[1])));
    }
}
