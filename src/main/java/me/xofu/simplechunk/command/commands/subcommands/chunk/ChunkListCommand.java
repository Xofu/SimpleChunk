package me.xofu.simplechunk.command.commands.subcommands.chunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import me.xofu.simplechunk.utils.ChunkUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkListCommand extends SubCommand {

    private SimpleChunk instance;

    public ChunkListCommand(SimpleChunk instance) {
        super("list");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(!player.hasPermission("chunk.use") && !player.hasPermission("chunk.command.list")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(instance.getClaimManager().getClaimsByUUID(player.getUniqueId()).size() == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("DONT_OWN_ANY_CLAIMS")));
            return;
        }

        int amount = instance.getClaimManager().getClaimsByUUID(player.getUniqueId()).size();

        for(String line: instance.getConfig().getStringList("CHUNK_LIST")) {
            if(line.contains("%id%")) {
                for (int i = 0; i < amount; i++) {
                    Claim claim = instance.getClaimManager().getClaimsByUUID(player.getUniqueId()).get(i);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%id%", claim.getChunk().getX() + "|" + claim.getChunk().getZ())
                            .replace("%x%", String.valueOf(ChunkUtils.getExactCoordinate(claim.getChunk().getX())))
                            .replace("%z%", String.valueOf(ChunkUtils.getExactCoordinate(claim.getChunk().getZ())))));
                }
                continue;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%claims%", String.valueOf(amount))));
        }
    }
}
