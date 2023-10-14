package de.containercloud.database;


public record CloudMongoCollection(CollectionTypes type, String collectionName) {

    public enum CollectionTypes {
        SERVICE("services"),
        TASK("tasks"),
        VERIFICATION("verification"),
        TEMPLATE("templates"),
        DATA("cloud-data");

        private final String type;

        CollectionTypes(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}
