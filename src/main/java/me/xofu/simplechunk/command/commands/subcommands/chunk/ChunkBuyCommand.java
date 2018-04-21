package me.xofu.simplechunk.command.commands.subcommands.chunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import me.xofu.simplechunk.utils.ChunkUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkBuyCommand extends SubCommand {

    private SimpleChunk instance;

    public ChunkBuyCommand(SimpleChunk instance) {
        super("buy");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(!player.hasPermission("chunk.use") && !player.hasPermission("chunk.command.buy")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(!instance.getClaimManager().isClaimed(player.getLocation().getChunk())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_CLAIMED")));
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(player.getLocation());
        if(!claim.isForsale()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_FORSALE")));
            return;
        }

        if(claim.getOwner() == player.getUniqueId()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANNOT_BUY_OWN_CLAIM")));
            return;
        }

        if(player.hasPermission("chunk.bypass")) {
            instance.getClaimManager().removeClaim(claim);
            Claim newClaim = new Claim(player.getUniqueId(), player.getWorld().getChunkAt(player.getLocation()), false);
            instance.getClaimManager().addClaim(newClaim);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("BOUGHT_CLAIM").replace("%price%", String.valueOf(claim.getPrice()))));

            if(instance.getConfig().getBoolean("Spawn_torches")) {
                for(Location corner : ChunkUtils.getChunkCorners(claim.getChunk())) {
                    corner.getBlock().setType(Material.TORCH);
                }
            }
            return;
        }

        if(instance.getClaimManager().hasClaim(player.getUniqueId())) {
            if(instance.getClaimManager().getClaimsByUUID(player.getUniqueId()).size() >= instance.getClaimManager().getMaxClaimsByPlayer(player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("TOO_MANY_CLAIMS").replace("%limit%", String.valueOf(instance.getClaimManager().getMaxClaimsByPlayer(player)))));
                return;
            }
        }

        if(instance.getEconomy().getBalance(player) < claim.getPrice()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_ENOUGH_MONEY")));
            return;
        }

        instance.getClaimManager().removeClaim(claim);
        Claim newClaim = new Claim(player.getUniqueId(), player.getWorld().getChunkAt(player.getLocation()), false);
        instance.getClaimManager().addClaim(newClaim);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("BOUGHT_CLAIM").replace("%price%", String.valueOf(claim.getPrice()))));
        instance.getEconomy().withdrawPlayer(player, claim.getPrice());
    }
}
