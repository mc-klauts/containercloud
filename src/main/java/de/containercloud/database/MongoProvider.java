package de.containercloud.database;

import de.containercloud.database.handler.MongoServiceHandler;
import de.containercloud.database.handler.MongoTaskHandler;
import de.containercloud.database.handler.MongoTemplateHandler;
import lombok.Getter;

@Getter
public class MongoProvider {

    @Getter
    private static MongoProvider INSTANCE;

    private final MongoServiceHandler serviceHandler;
    private final MongoTaskHandler taskHandler;
    private final MongoTemplateHandler templateHandler;

    public MongoProvider(MongoDatabaseHandler databaseHandler) {
        INSTANCE = this;

        this.serviceHandler = new MongoServiceHandler(databaseHandler);
        this.taskHandler = new MongoTaskHandler(databaseHandler);
        this.templateHandler = new MongoTemplateHandler(databaseHandler);
    }

}
