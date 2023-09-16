package de.dasshorty.shortcloud.database;

import lombok.Getter;

public record CloudMongoCollection(CollectionTypes type, String collectionName) {

    @Getter
    public enum CollectionTypes {
        SERVICE("services"),
        TASK("tasks"),
        VOLUME("volumes"),
        DATA("cloud-data");

        private final String type;
        CollectionTypes(String type) {
            this.type = type;
        }
    }

}
