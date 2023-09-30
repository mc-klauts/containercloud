package de.containercloud.impl.service;

import de.containercloud.api.service.Service;
import de.containercloud.api.service.ServiceVolume;
import de.containercloud.wrapper.CloudWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ServiceVolumeImpl implements ServiceVolume {

    private final String associatedService;
    private final String volumeName;

    @Override
    public boolean cleanUp() {
        return false;
    }

    @Override
    public Service serviceUsingVolume() {
        return CloudWrapper.getINSTANCE().getServiceManager().withId(this.associatedService);
    }

    @Override
    public String volumeId() {
        return volumeName;
    }
}
