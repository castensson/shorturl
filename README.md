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

Open a browser and point it to http://localhost:8889/create (or the port of choice)

Enter url to be shortened and hit enter/click submit to get the result.

** JDBC MODE **
The application has a very basic implementation of storing the shortened URLs in a SQL database.
Currently it has only been verified using [H2 Database Engine](http://www.h2database.com/html/main.html).
For simplicity the libs for running H2 has been provided as part of the build.

To start an instance of H2 with default test setup, from within *shorturl-1.0-SNAPSHOT/bin*, execute the following command:
*java -jar ../lib/h2-1.4.196.jar &*

This starts up an instance of H2 as a background process and the console is available at [http://localhost:8082/login.jsp](http://localhost:8082/login.jsp)
If you choose to connect using the console, make sure to set *JDBC URL* to **jdbc:h2:tcp://localhost/~/shorturl;AUTO_SERVER=TRUE**
Otherwise only one connection is allowed and the console will block the application from accessing the database.

Create a text file named h2.properties with the following contents.
*db.url=jdbc:h2:~/shorturl;AUTO_SERVER=TRUE*  
*db.driver=org.h2.Driver*  
*db.user=sa*  
*db.password=*  

Start the application from shorturl-1.0-SNAPSHOT/bin* using the following syntax:
*./shorturl -s JDBC -d /path/to/h2.properties*

The server will now start in JDBC mode and persist urls between restarts. Shorten an URL and using the console of H2 you
can see that a storage table has been created along with a sequence and some indexes on the table.



