
import distribution.Registry;
import service.RepositoryService;

public class App {
    public static void main(String[] args) throws Exception {
        // create registry server
        Registry registry = new Registry();
        // RepositoryService repositoryService = new RepositoryService(registry);

        while (!registry.isInitialized()) {
        }
        RepositoryService repositoryService = new RepositoryService(registry);

        // TODO: handle close event

    }
}
