package Routes;
import java.io.InputStream;
import java.net.HttpURLConnection;
import com.sun.net.httpserver.Headers;

import java.util.*;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class Route implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();

    switch (method.toUpperCase()) {
      case "GET":
      this.handleGet(exchange);
        break;
    
      case "POST":
        this.handlePost(exchange);
        break;
      
      default:
        break;
    }
  }

  public void handleGet(HttpExchange exchange) {
    try {
      // Headers requestHeaders = exchange.getRequestHeaders();
      // int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));

      // System.out.println("RemoteAddress " + exchange.getRemoteAddress());
      // System.out.println("RequestURI " + exchange.getRequestURI().getQuery());
      // System.out.println("RequestBody " + exchange.getRequestBody());
      this.get(exchange, this.queryToMap(exchange.getRequestURI().getQuery()));
      // exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
      exchange.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Map<String, String> queryToMap(String query) {
    Map<String, String> result = new HashMap<>();
    if(query == null) return result;
    for (String param : query.split("&")) {
        String[] entry = param.split("=");
        if (entry.length > 1) {
            result.put(entry[0], entry[1]);
        }else{
            result.put(entry[0], "");
        }
    }
    return result;
  }

  public void handlePost(HttpExchange exchange) {
    try {
 
      // REQUEST Headers
      Headers requestHeaders = exchange.getRequestHeaders();
      // Set<Map.Entry<String, List<String>>> entries = requestHeaders.entrySet();

      int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));

      // REQUEST Body
      InputStream requestBody = exchange.getRequestBody();

      byte[] data = new byte[contentLength];
      requestBody.read(data); //read data to array
      // RESPONSE Headers
      // Headers responseHeaders = exchange.getResponseHeaders();

      // Send RESPONSE Headers
      // exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, contentLength);
      // RESPONSE Body
      // OutputStream responseBody = exchange.getResponseBody();
      // responseBody.write(data);

      this.post(exchange, new String(data));

      exchange.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void get(HttpExchange exchange, Map<String, String> query) throws IOException {
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
  }

  public void post(HttpExchange exchange, String data) throws IOException {
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
  }
}
