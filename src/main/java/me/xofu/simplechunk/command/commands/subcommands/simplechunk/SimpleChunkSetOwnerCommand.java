package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SimpleChunkSetOwnerCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkSetOwnerCommand(SimpleChunk instance) {
        super("setowner");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_A_PLAYER")));
            return;
        }

        Player player = (Player) sender;
        if(!player.hasPermission("simplechunk.command.setowner")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length == 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/simplechunk setowner <player>")));
            return;
        }

        if(instance.getServer().getPlayer(args[1]) == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_NOT_ONLINE").replace("%player%", args[1])));
            return;
        }

        Player targetPlayer = instance.getServer().getPlayer(args[1]);

        if(!instance.getClaimManager().isClaimed(player.getLocation().getChunk())) {
            Claim claim = new Claim(targetPlayer.getUniqueId(), player.getLocation().getChunk(), false);
            instance.getClaimManager().addClaim(claim);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_OWNER_CHANGED").replace("%player%", targetPlayer.getName())));
            return;
        }

        Claim claim = instance.getClaimManager().getClaimAt(player.getLocation());
        if(claim.isServer()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANNOT_CHANGE_OWNER_SERVERCLAIM")));
            return;
        }

        instance.getClaimManager().removeClaim(claim);
        claim.setOwner(targetPlayer.getUniqueId());
        instance.getClaimManager().addClaim(claim);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CHUNK_OWNER_CHANGED").replace("%player%", targetPlayer.getName())));
    }
}
