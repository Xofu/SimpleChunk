package me.xofu.simplechunk.command.commands.subcommands.simplechunk;


import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import me.xofu.simplechunk.utils.BooleanUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SimpleChunkSetSaleCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkSetSaleCommand(SimpleChunk instance) {
        super("setsale");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("simplechunk.command.setsale")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length == 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/simplechunk setsale <true:false>")));
            return;
        }

        if(!BooleanUtils.isBoolean(args[1])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_TRUE_OR_FALSE").replace("%args%", args[1])));
            return;
        }

        instance.getConfig().set("Claim_selling", Boolean.valueOf(args[1]));
        instance.saveConfig();

        if(BooleanUtils.isFalse(Boolean.valueOf(args[1]))) {
            for(Claim claim: instance.getClaimManager().getClaims()) {
                if(claim.isForsale()) {
                    claim.setForsale(false);
                    claim.setPrice(0);
                }
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CLAIM_SELLING_SET").replace("%state%", args[1].toLowerCase())));
    }
}
