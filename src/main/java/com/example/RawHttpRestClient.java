package com.example;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URI;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class RawHttpRestClient {

    public static HttpResponse fetch(String method, String url,
                                     Map<String, String> headers,
                                     Map<String, String> paramsOrBody,
                                     boolean logBody) throws Exception {

        DateTimeFormatter logNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        DateTimeFormatter logLineFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        String logFileName = "http_client_" +
                LocalDateTime.now().atZone(ZoneId.systemDefault()).format(logNameFormatter) + ".log";

        try (PrintWriter logWriter = new PrintWriter(new FileWriter(logFileName, true))) {
            String timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(logLineFormatter);
            logWriter.println("[" + timestamp + "] " + method + " " + url);

            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = (uri.getPort() == -1) ? (scheme.equalsIgnoreCase("https") ? 443 : 80) : uri.getPort();
            String path = uri.getPath().isEmpty() ? "/" : uri.getPath();
            if (uri.getQuery() != null) {
                path += "?" + uri.getQuery();
            }

            Socket socket;
            if ("https".equalsIgnoreCase(scheme)) {
                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = factory.createSocket(host, port);
            } else {
                socket = new Socket(host, port);
            }

            try (socket) {
                OutputStream out = socket.getOutputStream();

                // Build request
                StringBuilder request = new StringBuilder();
                request.append(method).append(" ").append(path).append(" HTTP/1.1\r\n");
                request.append("Host: ").append(host).append("\r\n");
                request.append(HttpHelper.buildHeaders(headers));

                // Build body if present
                String bodyString = null;
                if (paramsOrBody != null && !paramsOrBody.isEmpty()) {
                    String contentType = headers.getOrDefault("Content-Type", "");
                    if (contentType.equalsIgnoreCase("application/x-www-form-urlencoded")) {
                        bodyString = HttpHelper.buildForm(paramsOrBody);
                    } else if (contentType.equalsIgnoreCase("application/json")) {
                        StringBuilder json = new StringBuilder("{");
                        int i = 0;
                        for (Map.Entry<String, String> entry : paramsOrBody.entrySet()) {
                            if (i++ > 0) json.append(",");
                            json.append("\"").append(entry.getKey()).append("\":\"")
                                .append(entry.getValue()).append("\"");
                        }
                        json.append("}");
                        bodyString = json.toString();
                    }
                }

                // Add Content-Length if body exists
                if (bodyString != null) {
                    int contentLength = bodyString.getBytes(StandardCharsets.UTF_8).length;
                    request.append("Content-Length: ").append(contentLength).append("\r\n");
                }

                request.append("Connection: close\r\n\r\n");

                if (bodyString != null) {
                    request.append(bodyString);
                }

                out.write(request.toString().getBytes(StandardCharsets.UTF_8));
                out.flush();

                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String statusLine = in.readLine();
                logWriter.println("Status: " + statusLine);

                Map<String, String> responseHeaders = new LinkedHashMap<>();
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    logWriter.println(line);
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        responseHeaders.put(parts[0].trim(), parts[1].trim());
                    }
                }

                StringBuilder bodyBuilder = new StringBuilder();
                if (logBody) {
                    while ((line = in.readLine()) != null) {
                        bodyBuilder.append(line).append("\n");
                        logWriter.println(line);
                    }
                }

                return new HttpResponse(statusLine, responseHeaders, bodyBuilder.toString());
            }
        }
    }
}








/* import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URI;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RawHttpRestClient {

    public static void fetch(String method, String url, Map<String, String> headers,
                             Map<String, String> paramsOrBody, boolean logBody) throws Exception {

        DateTimeFormatter logNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        DateTimeFormatter logLineFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        String logFileName = "http_client_" +
                LocalDateTime.now().atZone(ZoneId.systemDefault()).format(logNameFormatter) + ".log";

        try (PrintWriter logWriter = new PrintWriter(new FileWriter(logFileName, true))) {
            String timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(logLineFormatter);
            logWriter.println("[" + timestamp + "] " + method + " " + url);

            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = (uri.getPort() == -1) ? (scheme.equalsIgnoreCase("https") ? 443 : 80) : uri.getPort();
            String path = uri.getPath().isEmpty() ? "/" : uri.getPath();
            if (uri.getQuery() != null) {
                path += "?" + uri.getQuery();
            }

            Socket socket;
            if ("https".equalsIgnoreCase(scheme)) {
                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = factory.createSocket(host, port);
            } else {
                socket = new Socket(host, port);
            }

            try (socket) {
                OutputStream out = socket.getOutputStream();

                // Build request
                StringBuilder request = new StringBuilder();
                request.append(method).append(" ").append(path).append(" HTTP/1.1\r\n");
                request.append("Host: ").append(host).append("\r\n");
                request.append(HttpHelper.buildHeaders(headers));

                // Build body if present
                String bodyString = null;
                if (paramsOrBody != null && !paramsOrBody.isEmpty()) {
                    String contentType = headers.getOrDefault("Content-Type", "");
                    if (contentType.equalsIgnoreCase("application/x-www-form-urlencoded")) {
                        bodyString = HttpHelper.buildForm(paramsOrBody);
                    } else if (contentType.equalsIgnoreCase("application/json")) {
                        StringBuilder json = new StringBuilder("{");
                        int i = 0;
                        for (Map.Entry<String, String> entry : paramsOrBody.entrySet()) {
                            if (i++ > 0) json.append(",");
                            json.append("\"").append(entry.getKey()).append("\":\"")
                                .append(entry.getValue()).append("\"");
                        }
                        json.append("}");
                        bodyString = json.toString();
                    }
                }

                // Add Content-Length if body exists
                if (bodyString != null) {
                    int contentLength = bodyString.getBytes(StandardCharsets.UTF_8).length;
                    request.append("Content-Length: ").append(contentLength).append("\r\n");
                }

                request.append("Connection: close\r\n\r\n");

                if (bodyString != null) {
                    request.append(bodyString);
                }

                out.write(request.toString().getBytes(StandardCharsets.UTF_8));
                out.flush();

                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String statusLine = in.readLine();
                System.out.println("Status: " + statusLine);
                logWriter.println("Status: " + statusLine);

                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    System.out.println(line);
                    logWriter.println(line);
                }

                if (logBody) {
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                        logWriter.println(line);
                    }
                }
            }
        }
    }
} */




/* import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URI;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RawHttpRestClient {

    public static void fetch(String method, String url, Map<String, String> headers, Map<String, String> paramsOrBody, boolean logBody) throws Exception {
        DateTimeFormatter logNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        DateTimeFormatter logLineFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        String logFileName = "http_client_" +
                LocalDateTime.now().atZone(ZoneId.systemDefault()).format(logNameFormatter) + ".log";

        try (PrintWriter logWriter = new PrintWriter(new FileWriter(logFileName, true))) {
            String timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(logLineFormatter);
            logWriter.println("[" + timestamp + "] " + method + " " + url);

            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = (uri.getPort() == -1) ? (scheme.equalsIgnoreCase("https") ? 443 : 80) : uri.getPort();
            String path = uri.getPath().isEmpty() ? "/" : uri.getPath();

            Socket socket;
            if ("https".equalsIgnoreCase(scheme)) {
                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = factory.createSocket(host, port);
            } else {
                socket = new Socket(host, port);
            }

            try (socket) {
                OutputStream out = socket.getOutputStream();

                // Build request
                StringBuilder request = new StringBuilder();
                request.append(method).append(" ").append(path).append(" HTTP/1.1\r\n");
                request.append("Host: ").append(host).append("\r\n");
                request.append(HttpHelper.buildHeaders(headers)); // use helper
                request.append("Connection: close\r\n\r\n");

                // Handle body depending on Content-Type
                if (paramsOrBody != null && !paramsOrBody.isEmpty()) {
                    String contentType = headers.getOrDefault("Content-Type", "");
                    if (contentType.equalsIgnoreCase("application/x-www-form-urlencoded")) {
                        request.append(HttpHelper.buildForm(paramsOrBody));
                    } else if (contentType.equalsIgnoreCase("application/json")) {
                        // Build JSON manually from map
                        request.append("{");
                        int i = 0;
                        for (Map.Entry<String, String> entry : paramsOrBody.entrySet()) {
                            if (i++ > 0) request.append(",");
                            request.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
                        }
                        request.append("}");
                    }
                }

                out.write(request.toString().getBytes());
                out.flush();

                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String statusLine = in.readLine();
                System.out.println("Status: " + statusLine);
                logWriter.println("Status: " + statusLine);

                // Log headers
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    System.out.println(line);
                    logWriter.println(line);
                }

                // Log body if enabled
                if (logBody) {
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                        logWriter.println(line);
                    }
                }
            }
        }
    }
} */






/* import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URI;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RawHttpRestClient {
    public static void main(String[] args) throws Exception {
        // Example GET
        fetch("GET", "https://postman-echo.com/get?foo=bar", Map.of(
            "User-Agent", "MyRawClient/1.0"
        ), null, true);

        // Example POST
        fetch("POST", "https://postman-echo.com/post", Map.of(
            "User-Agent", "MyRawClient/1.0",
            "Content-Type", "application/json"
        ), "{\"hello\":\"world\"}", true);

        // Example PUT
        fetch("PUT", "https://postman-echo.com/put", Map.of(
            "User-Agent", "MyRawClient/1.0",
            "Content-Type", "application/json"
        ), "{\"update\":\"new value\"}", true);

        // Example DELETE
        fetch("DELETE", "https://postman-echo.com/delete", Map.of(
            "User-Agent", "MyRawClient/1.0"
        ), null, true);
    }

    static void fetch(String method, String url, Map<String, String> headers, String body, boolean logBody) throws Exception {
        DateTimeFormatter logNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        DateTimeFormatter logLineFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        String logFileName = "http_client_" +
                LocalDateTime.now().atZone(ZoneId.systemDefault()).format(logNameFormatter) + ".log";

        try (PrintWriter logWriter = new PrintWriter(new FileWriter(logFileName, true))) {
            String timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(logLineFormatter);
            logWriter.println("[" + timestamp + "] " + method + " " + url);

            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = (uri.getPort() == -1) ? (scheme.equalsIgnoreCase("https") ? 443 : 80) : uri.getPort();
            String path = uri.getPath().isEmpty() ? "/" : uri.getPath();
            if (uri.getQuery() != null) {
                path += "?" + uri.getQuery();
            }

            Socket socket;
            if ("https".equalsIgnoreCase(scheme)) {
                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = factory.createSocket(host, port);
            } else {
                socket = new Socket(host, port);
            }

            try (socket) {
                OutputStream out = socket.getOutputStream();

                // Build request
                StringBuilder request = new StringBuilder();
                request.append(method).append(" ").append(path).append(" HTTP/1.1\r\n");
                request.append("Host: ").append(host).append("\r\n");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
                }
                if (body != null) {
                    request.append("Content-Length: ").append(body.getBytes().length).append("\r\n");
                }
                request.append("Connection: close\r\n\r\n");

                if (body != null) {
                    request.append(body);
                }

                out.write(request.toString().getBytes());
                out.flush();

                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String statusLine = in.readLine();
                System.out.println("Status: " + statusLine);
                logWriter.println("Status: " + statusLine);

                // Log headers
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    System.out.println(line);
                    logWriter.println(line);
                }

                // Log body if enabled
                if (logBody) {
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                        logWriter.println(line);
                    }
                }
            }
        }
    }
} */