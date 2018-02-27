package xyz.castensson.server.storage;

import xyz.castensson.server.model.UrlData;
import xyz.castensson.server.util.UrlShortener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.*;

/**
 * Created by tobcas on 2018-02-26.
 */
public class UrlStorageBasicJDBCImpl implements UrlStorage {

    private boolean dbPrepared = false;
    static final String DB_URL = System.getProperty("db.url");
    static final String USER = System.getProperty("db.user");;
    static final String PASS = System.getProperty("db.pwd");;


    @Override
    public UrlData create(String longURL) throws UrlStorageException {
        int id = getNextId();
        UrlData urlData = new UrlData();
        urlData.setId(id);
        urlData.setLongUrl(longURL);
        urlData.setShortUrl(UrlShortener.encode(id));
        try {
            prepareDB();
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            statement.execute(String.format("INSERT INTO URLS VALUES (%s, '%s', '%s')", id, urlData.getShortUrl(), URLEncoder.encode(longURL, "UTF-8")));
            conn.commit();
            conn.close();
        } catch (SQLException | ClassNotFoundException | UnsupportedEncodingException e) {
            throw new UrlStorageException("Failed during create", e);
        }
        return urlData;
    }

    @Override
    public UrlData lookupShort(String shortURL) throws UrlStorageException {
        int id = UrlShortener.decode(shortURL);
        UrlData result = null;
        try {
            prepareDB();
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT ID, SHORTURL, LONGURL FROM URLS WHERE ID = %s", id));
            if(resultSet.next()){
                result = new UrlData();
                result.setId(resultSet.getInt(1));
                result.setShortUrl(resultSet.getString(2));
                result.setLongUrl(URLDecoder.decode(resultSet.getString(3), "UTF-8"));
            }
            conn.close();

        } catch (SQLException | UnsupportedEncodingException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new UrlStorageException("Failed to lookup using shortUrl", e);
        }
        return result;
    }

    @Override
    public UrlData lookupLong(String longURL) throws UrlStorageException {
        UrlData result = null;
        try {
            prepareDB();
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT ID, SHORTURL, LONGURL FROM URLS WHERE LONGURL = '%s'",  URLEncoder.encode(longURL, "UTF-8")));

            if(resultSet.next()){
                result = new UrlData();
                result.setId(resultSet.getInt(1));
                result.setShortUrl(resultSet.getString(2));
                result.setLongUrl(URLDecoder.decode(resultSet.getString(3), "UTF-8"));
            }
            conn.close();
        } catch (SQLException | UnsupportedEncodingException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new UrlStorageException("Failed to lookup using longUrl", e);
        }
        return result;
    }


    /**
     * Sets up table, sequences and indexes for the storage.
     * Only verified on H2DB atm.
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void prepareDB() throws ClassNotFoundException, SQLException {
        if(dbPrepared){
            return;
        }
        Class.forName(System.getProperty("db.driver"));
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS URLS(ID INT PRIMARY KEY, SHORTURL VARCHAR(255), LONGURL VARCHAR(255))");
        conn.commit();
        statement.execute("CREATE SEQUENCE IF NOT EXISTS URL_ID_SEQ START WITH 10000");
        statement.execute("CREATE INDEX IF NOT EXISTS SHORTURL_IDX ON URLS(SHORTURL)");
        statement.execute("CREATE INDEX IF NOT EXISTS LONGURL_IDX ON URLS(LONGURL)");
        conn.commit();
        conn.close();
        dbPrepared = true;
    }

    /**
     * Get next id from sequence
     * @return
     */
    private int getNextId(){
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT NEXT VALUE FOR URL_ID_SEQ");
            int id = -1;
            while (res.next()) {
                id = res.getInt(1);
            }
            conn.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,USER,PASS);
    }

}
