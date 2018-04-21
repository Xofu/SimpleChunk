package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.command.SubCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SimpleChunkSetMaxClaimsCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkSetMaxClaimsCommand(SimpleChunk instance) {
        super("setmaxclaims");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("simplechunk.command.setmaxclaims")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length <= 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/simplechunk setmaxclaims <group> <amount>")));
            return;
        }

        List<String> groups = new ArrayList<>();
        for(String group: instance.getPermission().getGroups()) {
            groups.add(group);
        }

        if(groups == null || !groups.contains(args[1])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_A_GROUP").replace("%group%", args[1])));
            return;
        }

        if(!StringUtils.isNumeric(args[2])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_AN_AMOUNT").replace("%amount%", args[2])));
            return;
        }

        instance.getConfig().set("Max_claims." + args[1].toLowerCase(), args[2]);
        instance.saveConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("GROUP_MAXCLAIMS_SET").replace("%group%", args[1].toLowerCase()).replace("%amount%", args[2])));
    }
}
