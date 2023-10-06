package de.containercloud.impl.task;

import de.containercloud.api.MinecraftVersion;
import de.containercloud.api.Volume;

public class TaskVolumeImpl implements Volume {
    @Override
    public boolean cleanUp() {
        return false;
    }
}
