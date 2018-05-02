Team 8 Project 3 EECS 448 Spring 2018 University of Kansas

The idea for project 3 is a social-media clone. The prototype should have basic posting and user functionality.

The project is currently set up using Maven. Use an IDE like eclipse to utilize it.

Creation of the JAR file ("project-1.0-jar-with-dependencies.jar") is done with the command "mvn install" on this directory.
	Using the command "mvn install -DskipTests" will create the JAR file and skip any tests that are normally done during compilation.


**Compile**
This server is built using Java and uses Maven to handle dependencies. To build the application follow the steps below.
1. Install Maven and Java. Instructions will vary based on your operating system.
2. Clone the GitHub Repository:
   - URL: https://github.com/SChoiGitHub/eecs448_project_3
   - Clone with SSH: git@github.com:SChoiGitHub/eecs448_project_3.git
3. Using the makefile build the application.
   - The command ‘make defaultBuild’ will test and build the JAR file using the server specifications:
     - Database URL: jdbc:mysql://localhost:3306/sys?useSSL=false -
     - Database User: root
     - Database Password: password
   - The command ‘make specificServerBuild’ will test and build the JAR file using a given server, but it requires three parameters:
     - url= your database url here
     - user= your username for the database here
     - password=your password for the database here

**Usage of Jar**
You can run the JAR file using the ‘makefile’ provided in the GitHub Repository.
- You can use the command ‘make defaultRun‘ to use the server we provide.
- You can use the command ‘specificServerRun’ to use a server you provide. There are three parameters you must provide:
  - url= your database url here
  - user= your username for the database here
  - password= your password for the database here
Admin status is also managed by the JAR file.
- You can make users admin using ‘defaultAdminStatusGive’.
- You can remove a user’s admin status using ‘defaultAdminStatusTake’.
For both of these commands, you need one arguement:
 - forumUser=<the username of the user affected>
If you are running your own server, you need to use ‘specificServerAdminStatusGive’ and ‘specificServerAdminStatusTake’ and have four parameters:
- url=<your database url here>
- user=<your username for the database here>
- password=<your password for the database here>
- forumUser=<the username of the user affected>
