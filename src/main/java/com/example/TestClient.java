package com.example;

import java.util.Map;
import org.json.JSONObject;
import com.example.BuildInfo;

public class TestClient {
    public static void main(String[] args) throws Exception {
	BuildInfo.printBuildInfo();
        // --- Example 1: GET with query parameters ---
        String query = HttpHelper.buildQuery(Map.of(
            "foo", "bar",
            "baz", "qux"
        ));
        HttpResponse getResponse = RawHttpRestClient.fetch("GET",
            "https://postman-echo.com/get?" + query,
            Map.of("User-Agent", "MyRawClient/1.0"),
            null,
            true
        );
        System.out.println("GET Response:");
        System.out.println("Status: " + getResponse.getStatusLine());
        System.out.println("Headers: " + getResponse.getHeaders());
        System.out.println("Body:\n" + getResponse.getBody());

        // --- Example 2: POST with form data ---
        HttpResponse postFormResponse = RawHttpRestClient.fetch("POST",
            "https://postman-echo.com/post",
            Map.of("User-Agent", "MyRawClient/1.0",
                   "Content-Type", "application/x-www-form-urlencoded"),
            Map.of("username", "alice", "password", "secret123"),
            true
        );
        System.out.println("\nPOST Form Response:");
        System.out.println("Status: " + postFormResponse.getStatusLine());
        System.out.println("Headers: " + postFormResponse.getHeaders());
        System.out.println("Body:\n" + postFormResponse.getBody());

        // --- Example 3: POST with JSON payload ---
        HttpResponse postJsonResponse = RawHttpRestClient.fetch("POST",
            "https://postman-echo.com/post",
            Map.of("User-Agent", "MyRawClient/1.0",
                   "Content-Type", "application/json"),
            Map.of("username", "alice", "password", "secret123"),
            true
        );
        System.out.println("\nPOST JSON Response:");
        System.out.println("Status: " + postJsonResponse.getStatusLine());
        System.out.println("Headers: " + postJsonResponse.getHeaders());
        System.out.println("Body:\n" + postJsonResponse.getBody());

        // --- Example 4: PUT with JSON payload ---
        HttpResponse putResponse = RawHttpRestClient.fetch("PUT",
            "https://postman-echo.com/put",
            Map.of("User-Agent", "MyRawClient/1.0",
                   "Content-Type", "application/json"),
            Map.of("update", "new value", "status", "active"),
            true
        );
        System.out.println("\nPUT Response:");
        System.out.println("Status: " + putResponse.getStatusLine());
        System.out.println("Headers: " + putResponse.getHeaders());
        System.out.println("Body:\n" + putResponse.getBody());

        // --- Example 5: DELETE request ---
        HttpResponse deleteResponse = RawHttpRestClient.fetch("DELETE",
            "https://postman-echo.com/delete",
            Map.of("User-Agent", "MyRawClient/1.0"),
            null,
            true
        );

	// Parse JSON body
	// JSONObject obj = JsonHelper.parseBody(getResponse);
	// JSONObject obj = getResponse.json();
	// System.out.println("Foo param: " + obj.getJSONObject("args").getString("foo"));
	// System.out.println("Baz param: " + obj.getJSONObject("args").getString("baz"));
	System.out.println("Foo param: " + getResponse.getJsonString("args.foo"));
	System.out.println("Baz param: " + getResponse.getJsonString("args.baz"));
	System.out.println("Username: " + postJsonResponse.getJsonString("json.username"));
	System.out.println("Password: " + postJsonResponse.getJsonString("json.password"));
	System.out.println("Content-Length: " + getResponse.getJsonInt("headers.content-length"));
	// System.out.println("Active: " + putResponse.getJsonBoolean("json.active"));
	System.out.println("Status: " + putResponse.getJsonString("json.status"));
	System.out.println("Update: " + putResponse.getJsonString("json.update"));

	/* System.out.println("Keys under args: " + getResponse.listJsonKeys("args"));
	System.out.println("Keys under headers: " + getResponse.listJsonKeys("headers"));
	System.out.println("Keys under json: " + putResponse.listJsonKeys("json"));

	System.out.println("Args entries: " + getResponse.listJsonEntries("args"));
	System.out.println("Headers entries: " + getResponse.listJsonEntries("headers"));
	System.out.println("JSON entries: " + putResponse.listJsonEntries("json"));

	System.out.println("Header keys: " + getResponse.listJsonKeys("headers"));
	System.out.println("Args entries: " + getResponse.listJsonEntries("args"));

	// Example if response had an array
	System.out.println("Files array: " + postJsonResponse.listJsonArray("files"));

	System.out.println("Explore args: " + getResponse.exploreJson("args"));
	System.out.println("Explore headers: " + getResponse.exploreJson("headers"));
	System.out.println("Explore json: " + putResponse.exploreJson("json"));

	System.out.println("Pretty args:\n" + getResponse.prettyJson("args"));
	System.out.println("Pretty headers:\n" + getResponse.prettyJson("headers"));
	System.out.println("Pretty json:\n" + putResponse.prettyJson("json"));

	System.out.println("Full response (pretty):\n" + getResponse.prettyBody());
	System.out.println("Full response (pretty):\n" + postJsonResponse.prettyBody());
	System.out.println("Full response (pretty):\n" + putResponse.prettyBody());

	System.out.println("Full body:\n" + getResponse.prettyBody());
	System.out.println("Explore headers:\n" + getResponse.exploreJson("headers"));
	System.out.println("Args foo: " + getResponse.getJsonString("args.foo"));

	// System.out.println("Full body:\n" + postJsonResponse.prettyBody());
	// System.out.println("Explore headers:\n" + postJsonResponse.exploreJson("headers"));
	// System.out.println("Username: " + postJsonResponse.getJsonString("json.username")); */

	// --- Demo: JSON Toolkit in action ---

	System.out.println("=== GET Response ===");
	System.out.println("Full body:\n" + getResponse.prettyBody());
	System.out.println("Explore headers:\n" + getResponse.exploreJson("headers"));
	System.out.println("Args foo: " + getResponse.getJsonString("args.foo"));
	System.out.println("Args baz: " + getResponse.getJsonString("args.baz"));

	System.out.println("\n=== POST JSON Response ===");
	System.out.println("Full body:\n" + postJsonResponse.prettyBody());
	System.out.println("Explore json:\n" + postJsonResponse.exploreJson("json"));
	System.out.println("Username: " + postJsonResponse.getJsonString("json.username"));
	System.out.println("Password: " + postJsonResponse.getJsonString("json.password"));

	System.out.println("\n=== PUT Response ===");
	System.out.println("Full body:\n" + putResponse.prettyBody());
	System.out.println("Explore json:\n" + putResponse.exploreJson("json"));
	System.out.println("Status: " + putResponse.getJsonString("json.status"));
	System.out.println("Update: " + putResponse.getJsonString("json.update"));
	
        System.out.println("\nDELETE Response:");
        System.out.println("Status: " + deleteResponse.getStatusLine());
        System.out.println("Headers: " + deleteResponse.getHeaders());
        System.out.println("Body:\n" + deleteResponse.getBody());

	// --- Demo: Array handling ---
	System.out.println("\n=== Array Response ===");
	HttpResponse arrayResponse = new HttpResponse(
    		"HTTP/1.1 200 OK",
    		Map.of("Content-Type", "application/json"),
    		"{ \"items\": [\"apple\", \"banana\", \"cherry\"], \"numbers\": [1,2,3,4] }"
	);

	System.out.println("Full body:\n" + arrayResponse.prettyBody());
	System.out.println("Explore items:\n" + arrayResponse.exploreJson("items"));
	System.out.println("List items: " + arrayResponse.listJsonArray("items"));
	System.out.println("List numbers: " + arrayResponse.listJsonArray("numbers"));
    }
}






/* public class TestClient {
    public static void main(String[] args) throws Exception {
        // --- Example 1: GET with query parameters ---
        String query = HttpHelper.buildQuery(Map.of(
            "foo", "bar",
            "baz", "qux"
        ));
        RawHttpRestClient.fetch("GET", "https://postman-echo.com/get?" + query, Map.of(
            "User-Agent", "MyRawClient/1.0"
        ), null, true);

        // --- Example 2: POST with form data ---
        RawHttpRestClient.fetch("POST", "https://postman-echo.com/post", Map.of(
            "User-Agent", "MyRawClient/1.0",
            "Content-Type", "application/x-www-form-urlencoded"
        ), Map.of(
            "username", "alice",
            "password", "secret123"
        ), true);

        // --- Example 3: POST with JSON payload ---
        RawHttpRestClient.fetch("POST", "https://postman-echo.com/post", Map.of(
            "User-Agent", "MyRawClient/1.0",
            "Content-Type", "application/json"
        ), Map.of(
            "username", "alice",
            "password", "secret123"
        ), true);

        // --- Example 4: PUT with JSON payload ---
        RawHttpRestClient.fetch("PUT", "https://postman-echo.com/put", Map.of(
            "User-Agent", "MyRawClient/1.0",
            "Content-Type", "application/json"
        ), Map.of(
            "update", "new value",
            "status", "active"
        ), true);

        // --- Example 5: DELETE request ---
        RawHttpRestClient.fetch("DELETE", "https://postman-echo.com/delete", Map.of(
            "User-Agent", "MyRawClient/1.0"
        ), null, true);
    }
} */





/* public class TestClient {
    public static void main(String[] args) throws Exception {
        // --- Example 1: GET with query parameters ---
        String query = HttpHelper.buildQuery(Map.of(
            "foo", "bar",
            "baz", "qux"
        ));
        RawHttpRestClient.fetch("GET", "https://postman-echo.com/get?" + query, Map.of(
            "User-Agent", "MyRawClient/1.0"
        ), null, true);

        // --- Example 2: POST with form data ---
        RawHttpRestClient.fetch("POST", "https://postman-echo.com/post", Map.of(
            "User-Agent", "MyRawClient/1.0",
            "Content-Type", "application/x-www-form-urlencoded"
        ), Map.of(
            "username", "alice",
            "password", "secret123"
        ), true);

        // --- Example 3: POST with JSON payload ---
        RawHttpRestClient.fetch("POST", "https://postman-echo.com/post", Map.of(
            "User-Agent", "MyRawClient/1.0",
            "Content-Type", "application/json"
        ), Map.of(
            "username", "alice",
            "password", "secret123"
        ), true);
    }
} */





/* public class TestClient {
    public static void main(String[] args) throws Exception {
        // Build query dynamically
        String query = QueryHelper.buildQuery(Map.of(
            "foo", "bar",
            "baz", "qux"
        ));

        // Construct full URL
        String url = "https://postman-echo.com/get?" + query;

        // Call your unified client
        RawHttpRestClient.fetch("GET", url, Map.of(
            "User-Agent", "MyRawClient/1.0"
        ), null, true);

	// Build form data
	String formData = FormHelper.buildForm(Map.of(
    		"username", "alice",
    		"password", "secret123"
	));

	// Send POST request with form data
	RawHttpRestClient.fetch("POST", "https://postman-echo.com/post", Map.of(
    		"User-Agent", "MyRawClient/1.0",
   		 "Content-Type", "application/x-www-form-urlencoded"
	), formData, true);
	
	// Example usage
	// --- JSON payload ---
	String jsonBody = "{\"username\":\"alice\",\"password\":\"secret123\"}";
	RawHttpRestClient.fetch("POST", "https://postman-echo.com/post", Map.of(
    		"User-Agent", "MyRawClient/1.0",
    		"Content-Type", "application/json"
	), jsonBody, true);

	// --- Form payload ---
	String formBody = FormHelper.buildForm(Map.of(
    		"username", "alice",
    		"password", "secret123"
	));
	RawHttpRestClient.fetch("POST", "https://postman-echo.com/post", Map.of(
    		"User-Agent", "MyRawClient/1.0",
    		"Content-Type", "application/x-www-form-urlencoded"
	), formBody, true);

	// Build headers dynamically
	String headers = HeaderHelper.buildHeaders(Map.of(
    		"User-Agent", "MyRawClient/1.0",
    		"Content-Type", "application/json",
    		"Authorization", "Bearer myToken123"
	));

	// Use in your request
	StringBuilder request = new StringBuilder();
	request.append("POST /post HTTP/1.1\r\n");
	request.append("Host: postman-echo.com\r\n");
	request.append(headers); // insert all headers here
	request.append("Connection: close\r\n\r\n");
	request.append("{\"username\":\"alice\",\"password\":\"secret123\"}");
   }
} */