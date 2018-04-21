package me.xofu.simplechunk.listeners;

import me.xofu.simplechunk.SimpleChunk;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityListener implements Listener {

    private SimpleChunk instance;

    public EntityListener(SimpleChunk instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(event.blockList() == null) {
            return;
        }

        if(instance.getConfig().getBoolean("Explode_inside_claims")) {
            return;
        }

        if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
            if(event.blockList().isEmpty()) {
                return;
            }
            event.blockList().clear();
            return;
        }

        List<Block> claimedBlocks = new ArrayList<>();

        for(Block block: event.blockList()) {
            if(instance.getClaimManager().isClaimed(block.getChunk())) {
                claimedBlocks.add(block);
            }
        }

        if(claimedBlocks.isEmpty()) {
            return;
        }

        event.blockList().removeAll(claimedBlocks);
    }

    @EventHandler
    public void onEntityBreakDoor(EntityBreakDoorEvent event) {
        if(event.getEntityType() != EntityType.ZOMBIE) {
            return;
        }

        if(!instance.getClaimManager().isClaimed(event.getBlock().getChunk())) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                event.setCancelled(true);
                return;
            }
            return;
        }

        event.setCancelled(true);
    }
}
