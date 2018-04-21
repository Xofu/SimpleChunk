package me.xofu.simplechunk.command.commands.subcommands.chunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkDisallowCommand extends SubCommand {

    private SimpleChunk instance;

    public ChunkDisallowCommand(SimpleChunk instance) {
        super("disallow");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(!player.hasPermission("chunk.use") && !player.hasPermission("chunk.command.disallow")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length == 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/chunk disallow <player>")));
            return;
        }

        if(!instance.getClaimManager().isClaimed(player.getLocation().getChunk())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_CLAIMED")));
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(player.getLocation());
        if(!claim.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_YOUR_CLAIM")));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if(!claim.getAllowedPlayers().contains(target.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_NOT_ALLOWED").replace("%player%", target.getName())));
            return;
        }

        instance.getClaimManager().removeClaim(claim);
        claim.disallowPlayer(target.getUniqueId());
        instance.getClaimManager().addClaim(claim);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_DISALLOWED").replace("%player%", target.getName())));
    }
}
