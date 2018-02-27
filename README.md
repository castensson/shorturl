# shorturl

Note that application has been developed and tested on Ubuntu 14.04 but should be runnable on any machine running Java 8.
Instructions are assuming working in the terminal of a Linux machine.

Clone this repo and move into its directory.
*git clone https://github.com/castensson/shorturl.git*

Build using:
*./gradlew assemble test assembleDist*

Under build you will now find distrubutions under the folder
*build/distributions*

Copy the desired distribution to your folder of choice and unpack it.

Move into the folder named *shorturl-1.0-SNAPSHOT/bin*

Run the application using *./shorturl [-p port_number]*

The application will now start in memory mode, that is storing shortened urls in memory. Stopping the process will thus reset it.

By default the webserver will start on port 8889 if no specific port is provided using -p

Open a browser and point it to http://localhost:<port_number>/create


