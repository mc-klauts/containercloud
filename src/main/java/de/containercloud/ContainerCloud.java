package de.containercloud;

public class ContainerCloud {

    public static void main(String[] args) {
        new Thread(ContainerCloudInstance::new).start();
    }

}
