## Basic http server build on sockets in Java

The server is capable parsing HTTP requests and responding with accordance to the HTTP 1.1 standard

Example endpoints include 

```GET /echo/[text] ```
which returns the text

```GET /user-agent ``` responds with header value of user agent

```GET /files/[filename] ``` send a file from server (if it exists) as byte stream (tested with a web browser, curl and postman)

```POST /files/[filename] ``` send file to a server and save it there


I'm planning on expanding this project with some more elegant ways of handling the endpoints and adding support for more types of requests. This project mainly serves as a learning opportunity about http standard.
