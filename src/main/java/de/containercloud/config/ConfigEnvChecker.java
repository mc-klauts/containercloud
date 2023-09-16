package de.containercloud.config;

public class ConfigEnvChecker {

    public ConfigEnvChecker(ConfigHandler configHandler) {

        if (dataBaseHost() != null)
            configHandler.setDataBase("host", dataBaseHost());

        if (dataBasePort() != null)
            configHandler.setDataBase("port", dataBasePort());

        if (dataBaseDataBase() != null)
            configHandler.setDataBase("database", dataBaseDataBase());

        if (dataBaseUser() != null)
            configHandler.setDataBase("user", dataBaseUser());

        if (dataBasePassword() != null)
            configHandler.setDataBase("password", dataBasePassword());

        if (dataBaseSrv() != null)
            configHandler.setDataBase("enableSrv", dataBaseSrv().equalsIgnoreCase("true"));
        else
            configHandler.setDataBase("enableSrv", false);

    }

    public String dataBaseHost() {
        return System.getenv("database.host");
    }

    public String dataBasePort() {
        return System.getenv("database.port");
    }

    public String dataBaseDataBase() {
        return System.getenv("database.database");
    }

    public String dataBaseUser() {
        return System.getenv("database.user");
    }

    public String dataBasePassword() {
        return System.getenv("database.password");
    }

    public String dataBaseSrv() {
        return System.getenv("database.srv");
    }
}
