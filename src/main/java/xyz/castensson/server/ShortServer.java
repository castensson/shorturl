package xyz.castensson.server;

import com.beust.jcommander.JCommander;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import xyz.castensson.server.storage.StorageBinder;
import xyz.castensson.server.util.ServerArguments;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main class for the short url service. Responsible for preparing and starting the jetty server
 * that will serve requests
 * <p>
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
        if (serverArguments.host != null) {
            http.setHost(serverArguments.host);
        }
        server.setConnectors(new Connector[]{http});

        // Setup jersey resource config to scan for REST endpoint and handle storage bindings
        // Also set up a servlet catching all paths for further distribution to these endpoints
        ResourceConfig config = new ResourceConfig();
        config.packages("xyz.castensson.server");
        config.register(new StorageBinder());
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

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

    /**
     * Helper method to setup environment properties need when running in JDBC mode
     * @param parsedArgs The parsed application arguments
     */
    private static void prepareJDBCStorage(ServerArguments parsedArgs) {
        if (parsedArgs.dpPropertiesFile == null) {
            System.err.println("Need to provide -d flag with database properties if running JDBC mode.");
            System.exit(-1);
        }
        System.setProperty("url.storage", "JDBC");
        InputStream input = null;
        try {
            input = new FileInputStream(parsedArgs.dpPropertiesFile);
            Properties prop = new Properties();
            prop.load(input);

            // Get the property value and validate all are there
            String dbUrl = prop.getProperty("db.url");
            String dbDriver = prop.getProperty("db.driver");
            String dbUser = prop.getProperty("db.user");
            String dbPassword = prop.getProperty("db.password");

            if (dbDriver == null || dbPassword == null || dbUser == null || dbUrl == null) {
                System.err.println("One or more properties was missing in " + parsedArgs.dpPropertiesFile);
                System.exit(-1);
            }
            // Put props in System properties
            System.setProperty("db.url", dbUrl);
            System.setProperty("db.driver", dbDriver);
            System.setProperty("db.user", dbUser);
            System.setProperty("db.password", dbPassword);

        } catch (IOException ex) {
            System.err.println("Failed to read properties needed to run in JDBC mode, " + ex);
            System.exit(-1);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {// Ignore}
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        // Parse the arguments into an instance of ServerArguments
        ServerArguments parsedArgs = new ServerArguments();
        new JCommander(parsedArgs).parse(args);

        // Check and see if we should run in JDBC mode
        if (parsedArgs.storage.equalsIgnoreCase("JDBC")) {
            prepareJDBCStorage(parsedArgs);
        }

        final ShortServer applicationServer = new ShortServer(parsedArgs);
        try {
            applicationServer.start();
        } finally {
            applicationServer.stop();
        }
    }
}


