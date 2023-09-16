package de.dasshorty.shortcloud.impl.task;

import de.dasshorty.shortcloud.api.MinecraftVersion;
import de.dasshorty.shortcloud.api.task.TaskVolume;

public class TaskVolumeImpl implements TaskVolume {
    @Override
    public boolean cleanUp() {
        return false;
    }

    @Override
    public MinecraftVersion version() {
        return null;
    }
}
