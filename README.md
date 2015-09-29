# Maven Local Repo Fixer
Maven Local Repo Fixer (aka MLRF) is a lightweight, standalone Java console application to clean and update corrupted jars and poms in local Maven repository.

## Installation
Be sure you have installed Java Runtime Environment (JRE) >= 1.7.

Download last MLRF version and extract it in a folder of your choice.

## Configuration

### Repository list
Add to the file repoList a line for any repository you need to update corrupted jars.

If your company has a custom repository, you can add it as well.

To know which url to add, paste the download url of an artifact of your choice and *remove the part starting from the outer package*.

e.g. for mvnrepository.com

    http://central.maven.org/maven2/org/apache/logging/log4j/log4j-core/2.4/log4j-core-2.4.jar

you must add only
    
    http://central.maven.org/maven2/
    

### Proxy
If you are under proxy edit config.properties file.

* proxy_enabled = true
* proxy_server = <server address>
* proxy_port = <proxy port>

Proxies with authentication (user/pass) are not yet supported.


## Usage

    java -jar maven-local-repo-fixer-x.x.jar <path-of-maven-repository>
e.g.

    java -jar maven-local-repo-fixer-0.3.jar C:\Users\User\.m2\repository

Note: if the maven-local-repo-fixer-x.x.jar is exactly in repository folder, you can launch it without any argument:

    java -jar maven-local-repo-fixer-x.x.jar

Wait for the time needed (for huge repositories a long time can pass without any log in console, don't worry!) and you will find a short report in console and a more detailed one in **mlrf.log** file (if you haven't changed the log file name from config.properties :)).

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

To generate a local installation bundle, just run

    mvn clean install installation-bundle


## History
**0.3** first public release


## License
MLRF is under MIT License (see LICENSE.md).