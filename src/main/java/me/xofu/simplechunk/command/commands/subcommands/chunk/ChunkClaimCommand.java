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

public class ChunkClaimCommand extends SubCommand {

    private SimpleChunk instance;

    public ChunkClaimCommand(SimpleChunk instance) {
        super("claim");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(!player.hasPermission("chunk.use") && !player.hasPermission("chunk.command.claim")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(!player.getWorld().getName().equalsIgnoreCase(instance.getConfig().getString("world"))) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_IN_CLAIM_WORLD")));
            return;
        }

        if(instance.getClaimManager().isClaimed(player.getLocation().getChunk())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ALREADY_CLAIMED")));
            return;
        }

        if(player.hasPermission("chunk.bypass")) {
            Claim claim = new Claim(player.getUniqueId(), player.getLocation().getChunk(), false);
            instance.getClaimManager().addClaim(claim);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_CLAIMED")));

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

        if(instance.getConfig().getBoolean("First_claim_free") && instance.getClaimManager().getClaimsByUUID(player.getUniqueId()).isEmpty()) {
            Claim claim = new Claim(player.getUniqueId(), player.getLocation().getChunk(), false);
            instance.getClaimManager().addClaim(claim);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_CLAIMED")));
            for(Location corner: ChunkUtils.getChunkCorners(claim.getChunk())) {
                corner.getBlock().setType(Material.TORCH);
            }
            return;
        }

        if(instance.getEconomy().getBalance(player) < Double.parseDouble(instance.getConfig().getString("Claim_price"))) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_ENOUGH_MONEY")));
            return;
        }

        Claim claim = new Claim(player.getUniqueId(), player.getLocation().getChunk(), false);
        instance.getClaimManager().addClaim(claim);

        instance.getEconomy().withdrawPlayer(player, Double.parseDouble(instance.getConfig().getString("Claim_price")));
        player.sendMessage(Integer.valueOf(instance.getConfig().getString("Claim_price")) != 0 ? ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_CLAIMED_FOR").replace("%price%", instance.getConfig().getString("Claim_price"))) : ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_CLAIMED")));
        if(instance.getConfig().getBoolean("Spawn_torches")) {
            for(Location corner : ChunkUtils.getChunkCorners(claim.getChunk())) {
                corner.getBlock().setType(Material.TORCH);
            }
        }
    }
}
