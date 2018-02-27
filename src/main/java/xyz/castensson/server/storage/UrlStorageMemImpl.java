package xyz.castensson.server.storage;

import xyz.castensson.server.model.UrlData;
import xyz.castensson.server.util.UrlShortener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple memory storage using two maps to be able to go both way in lookup.
 * Created by tobcas on 2018-02-25.
 */
public class UrlStorageMemImpl implements UrlStorage {

    private static final AtomicInteger index = new AtomicInteger(1000);
    private static final Map<String, String> shortKeyStorage = new HashMap<>();
    private static final Map<String, String> longKeyStorage = new HashMap<>();

    @Override
    public UrlData create(String longURL) throws UrlStorageException {
        if(longKeyStorage.containsKey(longURL)){
            return lookupLong(longURL);
        }
        int myIndex = index.getAndIncrement();
        UrlData urlData = new UrlData();
        urlData.setId(myIndex);
        urlData.setLongUrl(longURL);
        urlData.setShortUrl(UrlShortener.encode(myIndex));
        shortKeyStorage.put(urlData.getShortUrl(), longURL);
        longKeyStorage.put(longURL, urlData.getShortUrl());
        return urlData;
    }

    @Override
    public UrlData lookupShort(String url) throws UrlStorageException {
        if(!shortKeyStorage.containsKey(url)){
            return null;
        }
        UrlData urlData = new UrlData();
        urlData.setId(UrlShortener.decode(url));
        urlData.setShortUrl(url);
        urlData.setLongUrl(shortKeyStorage.get(url));
        return urlData;
    }

    @Override
    public UrlData lookupLong(String url) throws UrlStorageException {
        if(!longKeyStorage.containsKey(url)){
            return null;
        }
        UrlData urlData = new UrlData();
        urlData.setLongUrl(url);
        urlData.setShortUrl(longKeyStorage.get(url));
        urlData.setId(UrlShortener.decode(longKeyStorage.get(url)));
        return urlData;
    }

    // Used for tests
    public void clear(){
        longKeyStorage.clear();
        shortKeyStorage.clear();
    }
}
