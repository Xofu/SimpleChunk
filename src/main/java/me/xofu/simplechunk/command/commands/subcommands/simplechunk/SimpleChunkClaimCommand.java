package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import me.xofu.simplechunk.utils.ChunkUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SimpleChunkClaimCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkClaimCommand(SimpleChunk instance) {
        super("claim");
        this.instance = instance;
    }


    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_A_PLAYER")));
            return;
        }

        Player player = (Player) sender;
        if(!player.hasPermission("simplechunk.command.claim")) {
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

        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Claim claim = new Claim(uuid, player.getLocation().getChunk(), true);
        instance.getClaimManager().addClaim(claim);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_CLAIMED_FOR_SERVER")));

        if(instance.getConfig().getBoolean("Spawn_torches")) {
            for (Location corner : ChunkUtils.getChunkCorners(claim.getChunk())) {
                corner.getBlock().setType(Material.TORCH);
            }
        }
    }
}
