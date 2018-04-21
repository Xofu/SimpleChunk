package me.xofu.simplechunk.command.commands.subcommands.chunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkSellCommand extends SubCommand {

    private SimpleChunk instance;

    public ChunkSellCommand(SimpleChunk instance) {
        super("sell");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(!player.hasPermission("chunk.use") && !player.hasPermission("chunk.command.sell")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length == 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/chunk sell <price>")));
            return;
        }

        if(!instance.getConfig().getBoolean("Claim_selling")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CLAIM_SELLING_DISABLED")));
            return;
        }

        if(!instance.getClaimManager().isClaimed(player.getLocation().getChunk())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_CLAIMED")));
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(player.getLocation());
        if(!claim.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_YOUR_CLAIM")));
            return;
        }

        if(!StringUtils.isNumeric(args[1]) && !args[1].equalsIgnoreCase("cancel")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_AN_AMOUNT").replace("%amount%", args[1])));
            return;
        }

        if(!claim.isForsale() && args[1].equalsIgnoreCase("cancel")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_FORSALE")));
            return;
        }

        if(claim.isForsale()) {
            if(args[1].equalsIgnoreCase("cancel")) {
                claim.setForsale(false);
                claim.setPrice(0);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CLAIM_FORSALE_CANCELLED")));
                return;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ALREADY_FORSALE")));
            return;
        }

        instance.getClaimManager().removeClaim(claim);
        claim.setForsale(true);
        claim.setPrice(Integer.parseInt(args[1]));
        instance.getClaimManager().addClaim(claim);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CLAIM_FORSALE").replace("%price%", args[1])));
        return;
    }
}
