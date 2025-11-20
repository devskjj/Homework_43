package Server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class BaseServer {

    private BaseServer() {
    }

    public static HttpServer makeServer() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 8089);
        System.out.printf("Server started at http:/%s:%s%n", address.getHostName(), address.getPort());
        HttpServer server = HttpServer.create(address, 50);
        System.out.println("        Done!");
        return server;
    }
}
