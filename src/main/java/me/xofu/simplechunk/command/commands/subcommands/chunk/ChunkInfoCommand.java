package me.xofu.simplechunk.command.commands.subcommands.chunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChunkInfoCommand extends SubCommand {

    private SimpleChunk instance;

    public ChunkInfoCommand(SimpleChunk instance) {
        super("info");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(!player.hasPermission("chunk.use") && !player.hasPermission("chunk.command.info")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        for(String line: instance.getConfig().getStringList("CHUNK_INFO")) {
            if(instance.getClaimManager().getClaimAt(player.getLocation()) == null) {
                if(!line.contains("%members%")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%id%", player.getWorld().getChunkAt(player.getLocation()).getX() + "|" + player.getWorld().getChunkAt(player.getLocation()).getZ())
                            .replace("%owner%", instance.getConfig().getString("WILDERNESS_NAME"))));
                }
                continue;
            }

            if(instance.getClaimManager().getClaimAt(player.getLocation()).isServer()) {
                if(!line.contains("%members%")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%id%", player.getWorld().getChunkAt(player.getLocation()).getX() + "|" + player.getWorld().getChunkAt(player.getLocation()).getZ())
                            .replace("%owner%", instance.getConfig().getString("SERVER_NAME"))));
                }
                continue;
            }

            if(instance.getClaimManager().getClaimAt(player.getLocation()).getAllowedPlayers().isEmpty()) {
                if(!line.contains("%members%")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%id%", player.getWorld().getChunkAt(player.getLocation()).getX() + "|" + player.getWorld().getChunkAt(player.getLocation()).getZ())
                            .replace("%owner%", Bukkit.getOfflinePlayer(instance.getClaimManager().getClaimAt(player.getLocation()).getOwner()).getName())));
                }
            }

            if(!instance.getClaimManager().getClaimAt(player.getLocation()).getAllowedPlayers().isEmpty()) {
                String members = null;
                for(UUID member : instance.getClaimManager().getClaimAt(player.getLocation()).getAllowedPlayers()) {
                    String name = Bukkit.getOfflinePlayer(member).getName();
                    members = members != null ? members + ", " + name : name;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%id%", player.getWorld().getChunkAt(player.getLocation()).getX() + "|" + player.getWorld().getChunkAt(player.getLocation()).getZ())
                        .replace("%owner%", Bukkit.getOfflinePlayer(instance.getClaimManager().getClaimAt(player.getLocation()).getOwner()).getName()))
                        .replace("%members%", members));
            }
        }
    }
}
