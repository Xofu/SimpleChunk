package me.xofu.simplechunk.command.commands.subcommands.simplechunk;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.claim.Claim;
import me.xofu.simplechunk.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SimpleChunkPurgePlayerCommand extends SubCommand {

    private SimpleChunk instance;

    public SimpleChunkPurgePlayerCommand(SimpleChunk instance) {
        super("purgeplayer");
        this.instance = instance;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("simplechunk.command.purgeplayer")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
            return;
        }

        if(args.length == 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE").replace("%usage%", "/simplechunk purgeplayer <player>")));
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        if(!instance.getClaimManager().hasClaim(offlinePlayer.getUniqueId())) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_HAS_NO_CLAIMS").replace("%player%", args[1])));
            return;
        }

        for(Claim claim: instance.getClaimManager().getClaimsByUUID(offlinePlayer.getUniqueId())) {
            instance.getClaimManager().removeClaim(claim);
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_CLAIMS_PURGED").replace("%player%", offlinePlayer.getName())));
    }
}
