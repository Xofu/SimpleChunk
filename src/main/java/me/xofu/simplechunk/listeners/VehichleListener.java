package me.xofu.simplechunk.listeners;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;

public class VehichleListener implements Listener {

    private SimpleChunk instance;

    public VehichleListener(SimpleChunk instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onVehichleMove(VehicleMoveEvent event) {
        if(!(event.getVehicle() instanceof Minecart) && (!(event.getVehicle() instanceof HopperMinecart))) {
            return;
        }
        Vehicle vehicle = event.getVehicle();

        Claim claim = instance.getClaimManager().getClaimAt(event.getFrom());
        Claim toClaim = instance.getClaimManager().getClaimAt(event.getTo());

        if(claim == toClaim) {
            return;
        }

        if(claim == null && toClaim != null) {
            return;
        }

        if(toClaim == null && claim != null) {
            if(!instance.getConfig().getBoolean("Interact_with_wilderness")) {
                destroy(vehicle);
                return;
            }
            return;
        }

        if(claim.getOwner().equals(toClaim.getOwner())) {
            return;
        }

        destroy(vehicle);
    }

    private void destroy(Vehicle vehicle) {
        vehicle.remove();
        switch(vehicle.getType()) {
            case MINECART:
                vehicle.getWorld().dropItemNaturally(vehicle.getLocation(), new ItemStack(Material.MINECART));
                break;
            case MINECART_HOPPER:
                vehicle.getWorld().dropItemNaturally(vehicle.getLocation(), new ItemStack(Material.HOPPER_MINECART));
                break;
            case MINECART_CHEST:
                vehicle.getWorld().dropItemNaturally(vehicle.getLocation(), new ItemStack(Material.STORAGE_MINECART));
                break;
            case MINECART_FURNACE:
                vehicle.getWorld().dropItemNaturally(vehicle.getLocation(), new ItemStack(Material.POWERED_MINECART));
                break;
            case MINECART_TNT:
                vehicle.getWorld().dropItemNaturally(vehicle.getLocation(), new ItemStack(Material.EXPLOSIVE_MINECART));
                break;
            default:
                vehicle.getWorld().dropItemNaturally(vehicle.getLocation(), new ItemStack(Material.MINECART));
        }
    }
}
