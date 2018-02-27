package xyz.castensson.server.util;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tobcas on 2018-02-25.
 * Container class for command parameters
 */
public class ServerArguments {
    @Parameter()
    public List<String> commands = new ArrayList<>();
    @Parameter(names = {"--host", "-h"})
    public String host = null;
    @Parameter(names = {"--port", "-p"})
    public String port = "8889";
    @Parameter(names = {"--storage", "-s"})
    public String storage = "MEMORY";
    @Parameter(names = {"--databasefile", "-d"})
    public String dpPropertiesFile = null;
}