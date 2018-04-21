package me.xofu.simplechunk.command.commands.subcommands.chunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkUnclaimCommand extends SubCommand {

    private SimpleChunk instance;

    public ChunkUnclaimCommand(SimpleChunk instance) {
        super("unclaim");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(!player.hasPermission("chunk.use") && !player.hasPermission("chunk.command.unclaim")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
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

        if(instance.getClaimManager().getClaimsByUUID(player.getUniqueId()).size() == 1) {
            instance.getClaimManager().removeClaim(claim);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_UNCLAIMED")));
            return;
        }

        if(Integer.valueOf(instance.getConfig().getString("Claim_refund")) == 0) {
            instance.getClaimManager().removeClaim(claim);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_UNCLAIMED")));
            return;
        }

        instance.getClaimManager().removeClaim(claim);

        instance.getEconomy().depositPlayer(player, Double.valueOf(instance.getConfig().getString("Claim_refund")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_UNCLAIMED_AND_REFUNDED").replace("%refund%", instance.getConfig().getString("Claim_refund"))));
    }
}
