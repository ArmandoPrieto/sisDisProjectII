sisDisProjectII
===============

Ejecutar:
>java -cp . -Djava.rmi.server.codebase=file:Servidor/ -Djava.security.policy=server.policy GenericServer 10 
>java -cp . -Djava.rmi.server.codebase=file:Cliente/ -Djava.security.policy=client.policy  GenericClient  localhost  10

Compilar:
>javac -cp . Ejecutor.java Tarea.java Tarea_Impl.java GenericClient.java
>javac -cp . Ejecutor.java Tarea.java Ejecutor_Imp.java GenericServer.java

Para crear un .jar con todas las clases del directorio:
>jar cmf server.mf GenericServer.jar *.class
>jar cmf client.mf GenericClient.jar *.class

Para ejecutar el .jar desde consola:
>java -jar -Djava.security.policy=server.policy GenericServer.jar 10
>java -jar -Djava.security.policy=client.policy GenericClient.jar localhost  10
OJO asegurarse de que ambos estén conectados a la misma IP si no da error  java.io.EOFException.
Parece que no es necesario start rmiregistry!
