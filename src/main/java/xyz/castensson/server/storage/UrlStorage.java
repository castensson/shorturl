package xyz.castensson.server.storage;

import xyz.castensson.server.model.UrlData;

/**
 * Created by tobcas on 2018-02-25.
 */
public interface UrlStorage {

     UrlData create(String longURL) throws UrlStorageException;
     UrlData lookupShort(String shortURL) throws UrlStorageException;
     UrlData lookupLong(String longUrl) throws UrlStorageException;
}
