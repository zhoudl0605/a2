package core;

import distribution.Registry;

public class AppConfig {
    private static AppConfig instance = null;
    public int repo_id;
    public int rmi_port;
    public Registry registry;

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public AppConfig() {
        this(false);
    }

    public AppConfig(Boolean first) {
        if (first) {
            repo_id = 0;
            rmi_port = 8000;
        } else {
            repo_id = -1;
            rmi_port = -1;
        }

        registry = new Registry();
    }

    public void setRepoId(int id) {
        checkFirst();
        repo_id = id;
    }

    public int getRepoId() {
        checkFirst();
        return repo_id;
    }

    public void setRmiPort(int port) {
        checkFirst();
        rmi_port = port;
    }

    public int getRmiPort() {
        checkFirst();
        return rmi_port;
    }

    private void checkFirst() {
        if (repo_id == -1) {
            repo_id = 0;
            rmi_port = 8000;
        }
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Registry getRegistry() {
        return registry;
    }
}
