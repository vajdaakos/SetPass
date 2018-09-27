package set_pass;

@SuppressWarnings("SameParameterValue")
class Database {
    private final String name;
    private final String URL;
    private final int port;

    @SuppressWarnings("SameParameterValue")
    Database(String name, String URL, int port) {
        this.name = name;
        this.URL = URL;
        this.port = port;
    }

    String getURL() {
        return URL;
    }

    String getName() {
        return name;
    }

    int getPort() {
        return port;
    }
}
