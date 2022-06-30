package repository_implementation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import core.IAggregate;
import core.IDistributedRepository;
import core.IRepository;
import server.Directory;

public class Repository implements IDistributedRepository {
    private static Repository instance = null;
    private Directory directory;
    private Map<String, IRepository> repositories;

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public Repository() {
        directory = new Directory();

        Repository.instance = this;
    }

    public String request(String request) {
        String[] tokens = request.split(" ");
        String command = "";
        String key = "";
        String value = "";

        // check number of token in tokens
        if (tokens.length == 1) {
            command = tokens[0];
        } else if (tokens.length == 2) {
            command = tokens[0];
            key = tokens[1];
        } else if (tokens.length == 3) {
            command = tokens[0];
            key = tokens[1];
            value = tokens[2];
        } else {
            return "ERROR: Invalid request";
        }

        switch (command) {
            case "add":
                directory.add(key, Integer.parseInt(value));
                return "OK";
            case "set":
                ArrayList<Integer> new_vals = new ArrayList<>();
                for (String s : value.split(",")) {
                    new_vals.add(Integer.parseInt(s));
                }
                directory.set(key, new_vals);
                return "OK";
            case "list":
                return directory.list();
            case "delete":
                directory.delete(key);
                return "OK";
            case "get":
                return directory.getValue(key);

            case "sum":
                return directory.sum(key);
        }

        return "ERROR: Invalid request";
    }

    public String hello() throws RemoteException {
        return "Hello from Repository";
    }

    @Override
    public IAggregate aggregate(String[] repids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int sum(String key) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IRepository find(String id) {
        // find the repository with the given id
        return repositories.get(id);
    }

}