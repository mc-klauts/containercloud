package de.containercloud.api.service;

import de.containercloud.api.Volume;

public interface ServiceVolume extends Volume {

    Service serviceUsingVolume();

    String volumeId();

}
