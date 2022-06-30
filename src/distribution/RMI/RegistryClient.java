package distribution.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import models.RegistryServerInfo;

public class RegistryClient {
    public Registry registry;

    public RegistryClient(RegistryServerInfo registryServerInfo) throws RemoteException {
        registry = LocateRegistry.getRegistry("127.0.0.1", 8000); // 获取注册中心引用
    }

    public Registry getRegistry() {
        return registry;
    }
}
