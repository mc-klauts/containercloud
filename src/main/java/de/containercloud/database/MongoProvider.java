package de.containercloud.database;

import de.containercloud.config.ConfigHandler;
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

    public MongoProvider(MongoDatabaseHandler databaseHandler, ConfigHandler configHandler) {
        INSTANCE = this;

        this.serviceHandler = new MongoServiceHandler(databaseHandler, configHandler);
        this.taskHandler = new MongoTaskHandler(databaseHandler, configHandler);
        this.templateHandler = new MongoTemplateHandler(databaseHandler, configHandler);
    }

}
