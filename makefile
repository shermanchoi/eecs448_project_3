defaultTest:
	mvn test -Durl=jdbc:mysql://localhost:3306/sys?useSSL=false -Duser=root -Dpassword=password

defaultBuild:
	mvn install -Durl=jdbc:mysql://localhost:3306/sys?useSSL=false -Duser=root -Dpassword=password

defaultRun:
	sudo java -jar ./target/project-1.0-jar-with-dependencies.jar

testRun:
	sudo java -jar ./target/project-1.0-jar-with-dependencies.jar jdbc:mysql://localhost:3306/sys?useSSL=false root password

defaultAdminStatusGive:
	sudo java -jar ./target/project-1.0-jar-with-dependencies.jar -a1 ${forumUser}

defaultAdminStatusTake:
	sudo java -jar ./target/project-1.0-jar-with-dependencies.jar -a0 ${forumUser}

specificServerAdminStatusGive:
	sudo java -jar ./target/project-1.0-jar-with-dependencies.jar ${url} ${user} ${password} -a1 ${forumUser}

specificServerAdminStatusTake:
	sudo java -jar ./target/project-1.0-jar-with-dependencies.jar ${url} ${user} ${password} -a0 ${forumUser}

specificServerTest:
	mvn test -Durl=${url} -Duser=${user} -Dpassword=${password}

specificServerBuild:
	mvn install -Durl=${url} -Duser=${user} -Dpassword=${password}

specificServerRun:
	sudo java -jar ./target/project-1.0-jar-with-dependencies.jar ${url} ${user} ${password}

buildSkipTests:
	mvn install -DskipTests
