package xyz.castensson.server;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import xyz.castensson.server.model.UrlData;
import xyz.castensson.server.storage.UrlStorage;
import xyz.castensson.server.storage.UrlStorageException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Class containing the REST operation for the URL shortening service.
 *
 * Created by tobcas on 2018-02-25.
 */
@Path("/")
public class RestOperations {
    private final Logger logger = Log.getLogger("ShortServer");

    @Context
    UriInfo uri;

    @Inject
    UrlStorage storage;

    @GET
    @Path("{url}")
    public Response rootLookup(@PathParam("url") String url) {
        return lookup(url);
    }

    @GET
    @Path("lookup/{url}")
    public Response lookup(@PathParam("url") String url) {
        UrlData urlData;
        try {
            urlData = storage.lookupShort(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if(urlData != null){
            try {
                URI uri = (new URL(urlData.getLongUrl()).toURI());
                return Response.seeOther(uri).build();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("create")
    @Produces(MediaType.TEXT_HTML)
    public String createGet() {
        String shortenForm = new Scanner(RestOperations.class.getResourceAsStream("/html/index.html"), "UTF-8").useDelimiter("\\A").next();
        shortenForm = shortenForm.replace("<baseurl>", uri.getBaseUri().toString());
        return shortenForm;
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String createPost(@FormParam("url") String url) {
        UrlData urlData;
        try {
            urlData = storage.lookupLong(url);
        } catch (UrlStorageException e) {
            return "Failed to shorten your url due to " + e.toString();
        }
        if(urlData != null){
            logger.info("URL already shortened, reusing stored URL with id " + urlData.getId());
        } else {
            try {
                urlData = storage.create(url);
                logger.info(String.format("URL successfully shortened to %s, id in storage %s ", urlData.getShortUrl(), urlData.getId()));
            } catch (UrlStorageException e) {
                return "Failed to create short url, " + e.toString();
            }
        }

        String result = new Scanner(RestOperations.class.getResourceAsStream("/html/result.html"), "UTF-8").useDelimiter("\\A").next();
        return result.replace("<shorturl>", "" + uri.getBaseUri() + urlData.getShortUrl()).replace("<longurl>", urlData.getLongUrl());
    }
}
