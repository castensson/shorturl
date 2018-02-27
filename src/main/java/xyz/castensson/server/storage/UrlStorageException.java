package xyz.castensson.server.storage;

/**
 * Created by tobcas on 2018-02-26.
 */
public class UrlStorageException extends Exception {

    public UrlStorageException(String message){
        super(message);
    }
    public UrlStorageException(String message, Throwable cause){
        super(message, cause);
    }
}
