package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SimpleChunkUnclaimCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkUnclaimCommand(SimpleChunk instance) {
        super("unclaim");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_A_PLAYER")));
            return;
        }

        Player player = (Player) sender;
        if(!player.hasPermission("simplechunk.command.unclaim")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(!instance.getClaimManager().isClaimed(player.getLocation().getChunk())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_CLAIMED")));
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(player.getLocation());
        if(!claim.isServer()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_SERVER_CLAIM")));
            return;
        }

        instance.getClaimManager().removeClaim(claim);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_UNCLAIMED_FROM_SERVER")));
    }
}
