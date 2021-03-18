.PHONY: all datamodel webserver assignment clean

all: datamodel webserver assignment

datamodel:
	cd datamodel && mvn package install

webserver:
	cd webserver && mvn compile test assembly:single
	cp webserver/target/webserver-0.1-jar-with-dependencies.jar \
	   maljae-webserver.jar

assignment:
	cd assignment && mvn compile test assembly:single
	cp assignment/target/assignment-0.1-jar-with-dependencies.jar \
	   maljae-assign.jar

clean:
	cd datamodel  && mvn clean
	cd assignment && mvn clean
	cd webserver  && mvn clean
