package models;

import java.net.URI;

public class RegistryServerInfo {
    public int id;
    public URI uri;

    public RegistryServerInfo(int id, URI uri) {
        this.id = id;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
