package me.xofu.simplechunk.listeners;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.title.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    private SimpleChunk instance;

    public PlayerListener(SimpleChunk instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();

        if(player.hasPermission("chunk.bypass")) {
            return;
        }

        if(!instance.getClaimManager().isClaimed(event.getBlockClicked().getChunk())) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
                return;
            }
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(event.getBlockClicked().getLocation());
        if(Bukkit.getPlayer(claim.getOwner()) == player || claim.getAllowedPlayers().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();

        if(player.hasPermission("chunk.bypass")) {
            return;
        }

        if(!instance.getClaimManager().isClaimed(event.getBlockClicked().getChunk())) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
                return;
            }
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(event.getBlockClicked().getLocation());
        if(Bukkit.getPlayer(claim.getOwner()) == player || claim.getAllowedPlayers().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block block = event.getClickedBlock();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            int blockId = block.getTypeId();
            if (blockId != 64 &&
                    blockId != 96 &&
                    blockId != 107 &&
                    blockId != 77 &&
                    blockId != 69 &&
                    blockId != 54 &&
                    blockId != 61 &&
                    blockId != 62 &&
                    blockId != 23 &&
                    blockId != 117 &&
                    blockId != 193 &&
                    blockId != 194 &&
                    blockId != 195 &&
                    blockId != 196 &&
                    blockId != 197 &&
                    blockId != 146 &&
                    blockId != 145 &&
                    blockId != 149 &&
                    blockId != 150 &&
                    blockId != 151 &&
                    blockId != 154 &&
                    blockId != 158 &&
                    blockId != 219 &&
                    blockId != 220 &&
                    blockId != 221 &&
                    blockId != 222 &&
                    blockId != 223 &&
                    blockId != 224 &&
                    blockId != 225 &&
                    blockId != 226 &&
                    blockId != 227 &&
                    blockId != 228 &&
                    blockId != 229 &&
                    blockId != 230 &&
                    blockId != 231 &&
                    blockId != 232 &&
                    blockId != 233 &&
                    blockId != 234 &&
                    blockId != 116 &&
                    blockId != 390 &&
                    blockId != 356 &&
                    blockId != 404 &&
                    blockId != 389 &&
                    blockId != 342 &&
                    blockId != 408 &&
                    blockId != 379 &&
                    blockId != 138) {
                return;
            }
        }else{
            if(block.getType() != Material.WOOD_PLATE &&
                    block.getType() != Material.STONE_PLATE &&
                    block.getType() != Material.GOLD_PLATE &&
                    block.getType() != Material.IRON_PLATE) {
                return;
            }
        }

        if(player.hasPermission("chunk.bypass")) {
            return;
        }

        if(!instance.getClaimManager().isClaimed(block.getChunk())) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
                return;
            }
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(block.getLocation());
        if(Bukkit.getPlayer(claim.getOwner()) == player || claim.getAllowedPlayers().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_DO_THAT")));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (instance.getClaimManager().getClaimAt(event.getTo()) == instance.getClaimManager().getClaimAt(event.getFrom())) {
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(event.getTo());
        Claim lastClaim = instance.getClaimManager().getClaimAt(event.getFrom());

        boolean title = instance.getConfig().getBoolean("Title_messages");

        if(claim == null) {
            if(lastClaim != null) {
                if(title) {
                    new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_WILDERNESS"))).send(player, 10, 10, 10);
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_WILDERNESS")));
            }
            return;
        }

        if(claim.isForsale()) {
            if(title) {
                new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_FORSALE_CLAIM")).replace("%owner%", instance.getServer().getOfflinePlayer(claim.getOwner()).getName()).replace("%price%", String.valueOf(claim.getPrice()))).send(player, 10, 10, 10);
                return;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_FORSALE_CLAIM").replace("%owner%", instance.getServer().getOfflinePlayer(claim.getOwner()).getName()).replace("%price%", String.valueOf(claim.getPrice()))));
            return;
        }

        if(lastClaim == null) {
            if(!claim.isServer()) {
                if(title) {
                    new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_CLAIM")).replace("%owner%", instance.getServer().getOfflinePlayer(claim.getOwner()).getName())).send(player, 10, 10, 10);
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_CLAIM")).replace("%owner%", instance.getServer().getOfflinePlayer(claim.getOwner()).getName()));
                return;
            }
            if(title) {
                new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_SERVER"))).send(player, 10, 10, 10);
                return;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_SERVER")));
            return;
        }

        if(claim.getOwner().equals(lastClaim.getOwner())) {
            return;
        }

        if(claim.isServer()) {
            if(title) {
                new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_SERVER"))).send(player, 10, 10, 10);
                return;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_SERVER")));
            return;
        }

        if(title) {
            new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_CLAIM")).replace("%owner%", instance.getServer().getOfflinePlayer(claim.getOwner()).getName())).send(player, 10, 10, 10);
            return;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_CLAIM")).replace("%owner%", instance.getServer().getOfflinePlayer(claim.getOwner()).getName()));
    }
}
