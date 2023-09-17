package de.containercloud.database;

import lombok.Getter;

public record CloudMongoCollection(CollectionTypes type, String collectionName) {

    @Getter
    public enum CollectionTypes {
        SERVICE("services"),
        TASK("tasks"),
        VOLUME("volumes"),
        TEMPLATE("templates"),
        DATA("cloud-data");

        private final String type;

        CollectionTypes(String type) {
            this.type = type;
        }
    }

}
