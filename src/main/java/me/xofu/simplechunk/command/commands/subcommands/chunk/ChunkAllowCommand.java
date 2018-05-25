package me.xofu.simplechunk.command.commands.subcommands.chunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkAllowCommand extends SubCommand {

    private SimpleChunk instance;

    public ChunkAllowCommand(SimpleChunk instance) {
        super("allow");
        super.addAlias("trust").addAlias("add");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(!player.hasPermission("chunk.use") && !player.hasPermission("chunk.command.allow")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length == 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/chunk allow <player>")));
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

        if(args[1].equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANNOT_ALLOW_YOURSELF")));
            return;
        }

        if(Bukkit.getPlayer(args[1]) == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_NOT_ONLINE").replace("%player%", args[1])));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if(claim.getAllowedPlayers().contains(target.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_ALREADY_ALLOWED").replace("%player%", target.getName())));
            return;
        }

        instance.getClaimManager().removeClaim(claim);
        claim.allowPlayer(target.getUniqueId());
        instance.getClaimManager().addClaim(claim);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_ALLOWED").replace("%player%", target.getName())));
    }
}
