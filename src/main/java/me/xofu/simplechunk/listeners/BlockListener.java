package me.xofu.simplechunk.listeners;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListener implements Listener {

    private SimpleChunk instance;

    public BlockListener(SimpleChunk instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(player.hasPermission("chunk.bypass")) {
            return;
        }

        if(!instance.getClaimManager().isClaimed(event.getBlock().getChunk())) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
                return;
            }
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(event.getBlock().getLocation());
        if(Bukkit.getPlayer(claim.getOwner()) == player || claim.getAllowedPlayers().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(player.hasPermission("chunk.bypass")) {
            return;
        }

        if(!instance.getClaimManager().isClaimed(event.getBlock().getChunk())) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
                return;
            }
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(event.getBlock().getLocation());
        if(Bukkit.getPlayer(claim.getOwner()) == player || claim.getAllowedPlayers().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();

        if(event.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL && player.hasPermission("chunk.bypass")) {
            return;
        }

        if(!instance.getClaimManager().isClaimed(event.getBlock().getChunk())) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                return;
            }
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(event.getBlock().getLocation());
        if(event.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
            if (Bukkit.getPlayer(claim.getOwner()) == player || claim.getAllowedPlayers().contains(player.getUniqueId())) {
                return;
            }
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Claim claim = instance.getClaimManager().getClaimAt(event.getBlock().getLocation());
        Claim toClaim = instance.getClaimManager().getClaimAt(event.getToBlock().getLocation());

        if(claim == toClaim) {
            return;
        }

        if(claim == null && toClaim != null) {
            return;
        }

        if(toClaim == null && claim != null) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                return;
            }
            return;
        }

        if(claim.getOwner().equals(toClaim.getOwner())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        Claim claim = instance.getClaimManager().getClaimAt(event.getBlock().getLocation());
        Claim extendClaim = instance.getClaimManager().getClaimAt(event.getBlock().getRelative(event.getDirection()).getLocation());

        for(Block block: event.getBlocks()) {
            Block targetBlock = block.getRelative(event.getDirection());
            Claim targetClaim = instance.getClaimManager().getClaimAt(targetBlock.getLocation());

            if(claim == targetClaim) {
                continue;
            }

            if(claim == null && targetClaim != null) {
                continue;
            }

            if(targetClaim == null && claim != null) {
                if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                    event.setCancelled(true);
                    break;
                }
                continue;
            }

            if(claim.getOwner().equals(targetClaim.getOwner())) {
                continue;
            }

            event.setCancelled(true);
        }

        if(claim == extendClaim) {
            return;
        }

        if(claim == null && extendClaim != null) {
            return;
        }

        if(extendClaim == null && claim != null) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                return;
            }
            return;
        }

        if(claim.getOwner().equals(extendClaim.getOwner())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        Claim claim = instance.getClaimManager().getClaimAt(event.getBlock().getLocation());
        Claim retractClaim = instance.getClaimManager().getClaimAt(event.getBlock().getRelative(event.getDirection().getOppositeFace(), 2).getLocation());

        if(claim == retractClaim) {
            return;
        }

        if(claim == null && retractClaim != null) {
            return;
        }

        if(retractClaim == null && claim != null) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                return;
            }
            return;
        }

        if(claim.getOwner().equals(retractClaim.getOwner())) {
            return;
        }

        event.setCancelled(true);
    }
}
