package de.containercloud.impl.task;

import de.containercloud.api.MinecraftVersion;
import de.containercloud.api.task.TaskVolume;

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
