package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SimpleChunkPurgeClaimCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkPurgeClaimCommand(SimpleChunk instance) {
        super("purgeclaim");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_A_PLAYER")));
            return;
        }

        Player player = (Player) sender;
        if(!player.hasPermission("simplechunk.command.purgeclaim")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(!instance.getClaimManager().isClaimed(player.getLocation().getChunk())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_CLAIMED")));
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(player.getLocation());
        if(claim.isServer()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANNOT_PURGE_SERVER")));
            return;
        }

        instance.getClaimManager().removeClaim(claim);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CLAIM_PURGED")));
    }
}
