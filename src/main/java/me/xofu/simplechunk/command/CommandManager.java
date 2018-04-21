package me.xofu.simplechunk.command;

import me.xofu.simplechunk.SimpleChunk;

public class CommandManager {

    private SimpleChunk instance;

    public CommandManager(SimpleChunk instance) {
        this.instance = instance;
    }

    public void register(Command command) {
        instance.getCommand(command.getCommand()).setExecutor(command);
    }
}
