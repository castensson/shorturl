package xyz.castensson.server;

import com.beust.jcommander.JCommander;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import xyz.castensson.server.storage.StorageBinder;
import xyz.castensson.server.util.ServerArguments;

/**
 * Main class for the short url service. Responsible for preparing and starting the jetty server
 * that will serve requests
 *
 * Created by tobcas on 2018-02-25.
 */
public class ShortServer {

    private Server server;
    private ServerArguments serverArguments;

    public ShortServer(ServerArguments serverArguments) {

        this.serverArguments = serverArguments;
    }

    public void start() throws Exception {

        server = new Server();

        // Create and setup http connector (no https available)
        HttpConfiguration http_config = new HttpConfiguration();
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(Integer.parseInt(serverArguments.port));
        if(serverArguments.host != null) {
            http.setHost(serverArguments.host);
        }
        server.setConnectors(new Connector[] {http});

        // Setup jersey resource config to scan for REST endpoint and handle storage bindings
        // Also set up a servlet catching all paths for further distribution to these endpoints
        ResourceConfig config = new ResourceConfig();
        config.packages("xyz.castensson.server");
        config.register(new StorageBinder());
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet , "/*");

        server.start();
        server.join();
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            server.destroy();
        }
    }

    public static void main(String[] args) throws Exception {

        // Parse the arguments into an instance of ServerArguments
        ServerArguments parsedArgs = new ServerArguments();
        new JCommander(parsedArgs).parse(args);

        final ShortServer applicationServer = new ShortServer(parsedArgs);
        try {
            applicationServer.start();
        } finally {
            applicationServer.stop();
        }
    }


}

