package xyz.castensson.server.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Random;

/**
 * Created by tobcas on 2018-02-27.
 */
public class UrlShortnerTest {

    static final int[] testIndexes = new int[] {10000, 20000, 50000, 100000, 1000000};
    static final String[] testResults = new String[] {"cLs", "fmK", "naC", "Aa4", "emjc"};

    @Test
    public void encode(){
        String shortUrl10000 = UrlShortener.encode(testIndexes[0]);
        assertEquals(testResults[0], shortUrl10000);
        String shortUrl20000 = UrlShortener.encode(testIndexes[1]);
        assertEquals(testResults[1], shortUrl20000);
        String shortUrl50000 = UrlShortener.encode(testIndexes[2]);
        assertEquals(testResults[2], shortUrl50000);
        String shortUrl100000 = UrlShortener.encode(testIndexes[3]);
        assertEquals(testResults[3], shortUrl100000);
        String shortUrl1000000 = UrlShortener.encode(testIndexes[4]);
        assertEquals(testResults[4], shortUrl1000000);
    }

    @Test
    public void decode(){
        String shortUrl10000 = UrlShortener.encode(testIndexes[0]);
        int decoded1000 = UrlShortener.decode(shortUrl10000);
        assertEquals(testIndexes[0], decoded1000);
        String shortUrl20000 = UrlShortener.encode(testIndexes[1]);
        int decoded2000 = UrlShortener.decode(shortUrl20000);
        assertEquals(testIndexes[1], decoded2000);
        String shortUrl50000 = UrlShortener.encode(testIndexes[2]);
        int decoded5000 = UrlShortener.decode(shortUrl50000);
        assertEquals(testIndexes[2], decoded5000);
        String shortUrl100000 = UrlShortener.encode(testIndexes[3]);
        int decoded10000 = UrlShortener.decode(shortUrl100000);
        assertEquals(testIndexes[3], decoded10000);
        String shortUrl1000000 = UrlShortener.encode(testIndexes[4]);
        int decoded100000 = UrlShortener.decode(shortUrl1000000);
        assertEquals(testIndexes[4], decoded100000);
    }

    @Test
    public void randomEncodeDecode(){
        Random random = new Random(System.currentTimeMillis());
        int random1 = random.nextInt(10000000);
        String encodedRandom1 = UrlShortener.encode(random1);
        int decodedRandom1 = UrlShortener.decode(encodedRandom1);
        assertEquals(random1, decodedRandom1);
        assertEquals(encodedRandom1, UrlShortener.encode(decodedRandom1));
    }
}
