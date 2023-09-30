package de.containercloud.api.template;

public interface Template {

    /**
     * @return path inside docker container
     * */
    String internalPath();

    /**
     * @return path outside docker container
     * */
    String externalPath();
    String name();

}
