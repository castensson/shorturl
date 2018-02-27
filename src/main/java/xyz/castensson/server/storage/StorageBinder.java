package xyz.castensson.server.storage;

import org.eclipse.jetty.util.log.Log;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Binder for url storage. Adds possibilities to have multiple storage implementations.
 * Created by tobcas on 2018-02-25.
 * */
public class StorageBinder extends AbstractBinder {
    @Override
    protected void configure() {
        if("JDBC".equals(System.getProperty("url.storage"))){
            Log.getLogger("ShortServer").info("Using JDBC storage");
            bind(UrlStorageBasicJDBCImpl.class).to(UrlStorage.class);
        } else {
            Log.getLogger("ShortServer").info("Using memory storage");
            bind(UrlStorageMemImpl.class).to(UrlStorage.class);
        }
    }
}