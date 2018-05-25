package me.xofu.simplechunk.command;

import me.xofu.simplechunk.SimpleChunk;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements CommandExecutor {

    private SimpleChunk instance;

    private String command;
    private boolean player;
    private List<SubCommand> subCommands = new ArrayList<>();

    public Command(SimpleChunk instance, String command, boolean player) {
        this.instance = instance;

        this.command = command;
        this.player = player;
    }

    public Command addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
        return this;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String string, String[] args) {
        if(player) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_A_PLAYER")));
                return false;
            }
        }

        if(args.length > 0) {
            for(SubCommand subCommand: subCommands) {
                if(subCommand.substitute(args[0]) != null) {
                    subCommand.onCommand(sender, args);
                    return false;
                }
            }
        }
        onCommand(sender, args);
        return false;
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    public String getCommand() {
        return command;
    }
}
