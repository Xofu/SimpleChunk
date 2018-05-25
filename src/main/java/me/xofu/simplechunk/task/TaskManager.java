package me.xofu.simplechunk.task;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.task.tasks.ClaimSaveTask;

public class TaskManager {

    private SimpleChunk instance;

    private ClaimSaveTask claimSaveTask;

    public TaskManager(SimpleChunk instance) {
        this.instance = instance;

        claimSaveTask = new ClaimSaveTask(instance);
    }
}
