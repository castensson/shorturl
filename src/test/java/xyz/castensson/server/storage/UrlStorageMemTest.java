package xyz.castensson.server.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import xyz.castensson.server.model.UrlData;

/**
 * Created by tobcas on 2018-02-27.
 */
public class UrlStorageMemTest {

    static final String HTTP_CASTENSSON_XYZ = "http://castensson.xyz";
    static final String HTTP_GOOGLE_COM = "http://google.com";
    UrlStorageMemImpl storage = new UrlStorageMemImpl();

    @Before
    public void clearStorage(){
        storage.clear();
    }

    @Test
    public void create() throws UrlStorageException {
        UrlData urlData = storage.create(HTTP_CASTENSSON_XYZ);
        assertEquals(HTTP_CASTENSSON_XYZ, urlData.getLongUrl());
        assertEquals(1000, urlData.getId());
        assertEquals("qi", urlData.getShortUrl());
        urlData = storage.create(HTTP_GOOGLE_COM);
        assertEquals(HTTP_GOOGLE_COM, urlData.getLongUrl());
        assertEquals(1001, urlData.getId());
        assertEquals("qj", urlData.getShortUrl());
    }

    @Test
    public void lookupLong() throws UrlStorageException {
        storage.create(HTTP_CASTENSSON_XYZ);
        UrlData urlData = storage.lookupLong(HTTP_CASTENSSON_XYZ);
        assertNotNull(urlData);
        assertEquals(HTTP_CASTENSSON_XYZ, urlData.getLongUrl());
        urlData = storage.lookupLong(HTTP_GOOGLE_COM);
        assertNull(urlData);
    }

    @Test
    public void lookupShort() throws UrlStorageException {
        UrlData urlData = storage.create(HTTP_CASTENSSON_XYZ);
        UrlData urlDataLookup = storage.lookupShort(urlData.getShortUrl());
        assertNotNull(urlData);
        assertEquals(urlData.getId(), urlDataLookup.getId());
        assertEquals(urlData.getLongUrl(), urlDataLookup.getLongUrl());
        assertEquals(urlData.getShortUrl(), urlDataLookup.getShortUrl());
        assertEquals(HTTP_CASTENSSON_XYZ, urlDataLookup.getLongUrl());
        urlData = storage.lookupShort("DUMMY");
        assertNull(urlData);
    }

    @Test
    public void reuseOnCreate() throws UrlStorageException {
        UrlData urlData = storage.create(HTTP_CASTENSSON_XYZ);
        UrlData urlDataReuse = storage.create(HTTP_CASTENSSON_XYZ);
        assertEquals(urlData.getId(), urlDataReuse.getId());
        assertEquals(urlData.getLongUrl(), urlDataReuse.getLongUrl());
        assertEquals(urlData.getShortUrl(), urlDataReuse.getShortUrl());
    }
}
