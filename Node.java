import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.Headers;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.HttpServer;

import java.util.*;

public class Node {
  private HttpServer server;
  private Integer id;
  private Http http;

  public Node(Integer port, Http http) throws IOException {
    this.http = http;
    this.server = HttpServer.create(new InetSocketAddress(port), 0);
    this.setupEndpoints();
    this.server.setExecutor(null);
    this.server.start();
  }

  public Node(Integer port) throws IOException {
    this(port, new Http());
  }

  public void connectToNetwork(String address) throws IOException {
    this.http.post(address + "/nodes", "This is the post data");
  }

  private void setupEndpoints() {
    this.server.createContext("/", new HttpHandler() {
      @Override
      public void handle(HttpExchange exchange) throws IOException {
        byte[] response = "Hello".getBytes();
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
      }
    });

    this.server.createContext("/nodes", new HttpHandler() {
      @Override
        public void handle(HttpExchange httpExchange) throws IOException {
 
            // Serve for POST requests only
            if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
 
                try {
 
                    // REQUEST Headers
                    Headers requestHeaders = httpExchange.getRequestHeaders();
                    Set<Map.Entry<String, List<String>>> entries = requestHeaders.entrySet();
 
                    int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
 
                    // REQUEST Body
                    InputStream requestBody = httpExchange.getRequestBody();
 
                    byte[] data = new byte[contentLength];
                    int length = requestBody.read(data);
 
                    // RESPONSE Headers
                    Headers responseHeaders = httpExchange.getResponseHeaders();
 
                    // Send RESPONSE Headers
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, contentLength);
 
                    // RESPONSE Body
                    OutputStream os = httpExchange.getResponseBody();
 
                    os.write(data);
 
                    httpExchange.close();

                    System.out.println("POST DATA " + new String(data));
 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });
  }
}
  