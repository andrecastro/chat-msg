# JAVASPACE - JMS - SOAP

## Understanding the structure

This project is divided in some maven modules:

* The modules:
    - chat-msg-core: This is the module representing the core of the chat with all shared classes
    - chat-msg-ui: This is the module representing the chat itself
    - chat-msg-webservice: This is the module representing the webservice
    - chat-msg-admin: This is the module representing admin where will consume the webservice

## Dependencies

- Java 8
- Maven 3
- JavaSpace (Apache River, outrigger, ...)
- OpenJMS

## Hands on

To make it works, you need to install the root module:
    `cd chat-msg && mvn install`

All projects will build with success, except **chat-msg-admin** that will try to run the **wsimport** command but
will fail because the webservice is not running.
After running that command, refresh your IDE to make sure all projects import their dependencies correctly.

To run the **chat-msg-webservice**, run the class *App.java* inside the *br.edu.ifce.ppd.chat*.
If you want to change the URL of the webservice you can set -Dchat.webservice.url=URL as a VM option
(the default is: "http://localhost:9990").

After start the webservice, you can install the **chat-msg-admin**
    `cd chat-msg-admin && mvn install`
Remember that if you changed the webservice URL in the step before you need to change the URL inside
*chat-msg-admin/pom.xml* to the correct URL. (Please do not change any other configuration)

To run the **chat-msg-ui**, run the class *App.java* inside the *br.edu.ifce.ppd.chat*.
If you want to change the URLs of the openjms you can set -Djava.naming.provider.url.service=URL and
-Djava.naming.provider.url.admin=URL as a VM option. (the default is: "tcp://localhost:3035")

**NOTE:** You need to start the apache river and the OpenJMS before starting the **chat-msg-ui**.
