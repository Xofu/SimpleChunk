package me.xofu.simplechunk.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {

    private String name;
    private List<String> aliases = new ArrayList<>();

    public SubCommand(String name) {
        this.name = name.toLowerCase();
    }

    public SubCommand addAlias(String alias) {
        aliases.add(alias.toLowerCase());
        return this;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String substitute(String alias) {
        alias = alias.toLowerCase();
        if(alias.equals(name) || aliases.contains(alias)) {
            return name;
        }
        return null;
    }

    public abstract void onCommand(CommandSender sender, String[] args);
}
