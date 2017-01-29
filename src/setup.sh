# To compile :
# javac -d destDir Hello.java Server.java Client.java
#
# To set up registry you need hello.class files as class path:
#
# rmiregistry -J-Djava.rmi.server.codebase=fe:///Users/apple/Documents/cs550/test/registry/
#
#
#
# To set up server you need the server class and Hello (i.e registry file)
#
# java -classpath /Users/apple/Documents/cs5/test/server/ -Djava.rmi.server.codebase=file:///Users/apple/Documents/cs550/test/server hello.Server
#
#
# To set up server you need the client class and Hello (i.e registry file)
# java -classpath /Users/apple/Documents/cs550/test/client hello.Client

