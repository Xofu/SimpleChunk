package me.xofu.simplechunk.listeners;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.title.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
    public void onMove(PlayerMoveEvent event) {
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
                new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_FORSALE_CLAIM")).replace("%owner%", Bukkit.getPlayer(claim.getOwner()).getName()).replace("%price%", String.valueOf(claim.getPrice()))).send(player, 10, 10, 10);
                return;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_FORSALE_CLAIM").replace("%owner%", Bukkit.getPlayer(claim.getOwner()).getName()).replace("%price%", String.valueOf(claim.getPrice()))));
            return;
        }

        if(lastClaim == null) {
            if(!claim.isServer()) {
                if(title) {
                    new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_CLAIM")).replace("%owner%", Bukkit.getPlayer(claim.getOwner()).getName())).send(player, 10, 10, 10);
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_CLAIM")).replace("%owner%", Bukkit.getPlayer(claim.getOwner()).getName()));
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
            new TitleMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_CLAIM")).replace("%owner%", Bukkit.getPlayer(claim.getOwner()).getName())).send(player, 10, 10, 10);
            return;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ENTERING_CLAIM")).replace("%owner%", Bukkit.getPlayer(claim.getOwner()).getName()));
    }
}
