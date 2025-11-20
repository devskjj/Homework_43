import Server.BaseServer;
import Server.Utility;
import com.sun.net.httpserver.HttpServer;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            HttpServer server = BaseServer.makeServer();
            Utility.initRoutes(server);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
