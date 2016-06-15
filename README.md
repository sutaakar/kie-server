# kie-server

Repository used for implementation of stuff around kie server, extensions and such.

ping-example: 
- 
Minimalistic extension for kie server. So far implemented just REST interface.

Testing URL:    
http://localhost:`<port>`/`<context root>`/services/rest/server/containers/1/hello/`<some name>`

Installation: copy built jars to WEB-INF/lib folder of kie server.

jbpm-executor-example: 
- 
Example of standalone jBPM executor and its basic usage.
Shown usage with in-memory storage and with database backend, database handled by H2.
