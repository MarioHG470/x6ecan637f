package com.example.myproject;

import com.example.myproject.client.HttpClientUtil;
import com.example.myproject.model.Post;
import com.example.myproject.model.User;
import com.example.myproject.model.Todo;
import com.example.myproject.model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class App {

    // --- OutputManager nested for simplicity ---
    static class OutputManager {
        private static final ObjectMapper mapper = new ObjectMapper();

        public static void printJson(Object data, boolean pretty) {
            try {
                if (pretty) {
                    mapper.enable(SerializationFeature.INDENT_OUTPUT);
                }
                String json = mapper.writeValueAsString(data);
                System.out.println(json); // stdout
            } catch (Exception e) {
                System.err.println("Error printing JSON: " + e.getMessage());
            }
        }

        public static void log(String message) {
            System.err.println("[LOG] " + message); // stderr
        }
    }

    public static void main(String[] args) {
        try {
            HttpClientUtil client = new HttpClientUtil();

            if (args.length == 0) {
                System.err.println("Usage: java -jar MyProject.jar <resource> [id1 id2 ...] [--limit=N] [--json] [--pretty]");
                System.err.println("Valid resources: post(s), user(s), todo(s), comment(s)");
                return;
            }

            String resource = args[0].toLowerCase();

            // --- Detect flags ---
            int limit = Integer.MAX_VALUE;
            boolean jsonFlag = false;
            boolean prettyFlag = false;

            for (String arg : args) {
                if (arg.startsWith("--limit")) {
                    String[] parts = arg.split("=");
                    if (parts.length == 2) {
                        limit = Integer.parseInt(parts[1]);
                    }
                }
                if (arg.equals("--json")) {
                    jsonFlag = true;
                }
                if (arg.equals("--pretty")) {
                    prettyFlag = true;
                }
            }

            OutputManager.log("Fetching resource: " + resource);

            // --- Case: plural form means fetch all ---
            switch (resource) {
                case "posts":
                    String postsJson = client.get("https://jsonplaceholder.typicode.com/posts", String.class);
                    Post[] posts = new ObjectMapper().readValue(postsJson, Post[].class);
                    if (jsonFlag) {
                        OutputManager.printJson(slice(posts, limit), prettyFlag);
                    } else {
                        for (int i = 0; i < Math.min(limit, posts.length); i++) {
                            System.out.println(posts[i]);
                        }
                    }
                    return;

                case "users":
                    String usersJson = client.get("https://jsonplaceholder.typicode.com/users", String.class);
                    User[] users = new ObjectMapper().readValue(usersJson, User[].class);
                    if (jsonFlag) {
                        OutputManager.printJson(slice(users, limit), prettyFlag);
                    } else {
                        for (int i = 0; i < Math.min(limit, users.length); i++) {
                            System.out.println(users[i]);
                        }
                    }
                    return;

                case "todos":
                    String todosJson = client.get("https://jsonplaceholder.typicode.com/todos", String.class);
                    Todo[] todos = new ObjectMapper().readValue(todosJson, Todo[].class);
                    if (jsonFlag) {
                        OutputManager.printJson(slice(todos, limit), prettyFlag);
                    } else {
                        for (int i = 0; i < Math.min(limit, todos.length); i++) {
                            System.out.println(todos[i]);
                        }
                    }
                    return;

                case "comments":
                    String commentsJson = client.get("https://jsonplaceholder.typicode.com/comments", String.class);
                    Comment[] comments = new ObjectMapper().readValue(commentsJson, Comment[].class);
                    if (jsonFlag) {
                        OutputManager.printJson(slice(comments, limit), prettyFlag);
                    } else {
                        for (int i = 0; i < Math.min(limit, comments.length); i++) {
                            System.out.println(comments[i]);
                        }
                    }
                    return;
            }

            // --- Case: singular + IDs ---
            if (args.length < 2) {
                System.err.println("You must provide at least one ID for resource: " + resource);
                return;
            }

            for (int i = 1; i < args.length; i++) {
                if (args[i].startsWith("--")) continue; // skip flags
                int id = Integer.parseInt(args[i]);

                String url = "https://jsonplaceholder.typicode.com/" + resource + "s/" + id;
                String json = client.get(url, String.class);

                switch (resource) {
                    case "post":
                        Post post = new ObjectMapper().readValue(json, Post.class);
                        if (jsonFlag) OutputManager.printJson(post, prettyFlag);
                        else System.out.println(post);
                        break;

                    case "user":
                        User user = new ObjectMapper().readValue(json, User.class);
                        if (jsonFlag) OutputManager.printJson(user, prettyFlag);
                        else System.out.println(user);
                        break;

                    case "todo":
                        Todo todo = new ObjectMapper().readValue(json, Todo.class);
                        if (jsonFlag) OutputManager.printJson(todo, prettyFlag);
                        else System.out.println(todo);
                        break;

                    case "comment":
                        Comment comment = new ObjectMapper().readValue(json, Comment.class);
                        if (jsonFlag) OutputManager.printJson(comment, prettyFlag);
                        else System.out.println(comment);
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    // --- Utility: slice arrays for limit ---
    private static <T> T[] slice(T[] array, int limit) {
        if (limit >= array.length) return array;
        return java.util.Arrays.copyOfRange(array, 0, limit);
    }
}


/* package com.example.myproject;

import com.example.myproject.client.HttpClientUtil;
import com.example.myproject.model.Post;
import com.example.myproject.model.User;
import com.example.myproject.model.Todo;
import com.example.myproject.model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) {
        try {
            HttpClientUtil client = new HttpClientUtil();
            ObjectMapper mapper = new ObjectMapper();

            if (args.length == 0) {
                System.out.println("Usage: java -jar MyProject.jar <resource> [id1 id2 ...] [--limit=N] [--json]");
                System.out.println("Valid resources: post(s), user(s), todo(s), comment(s)");
                return;
            }

            String resource = args[0].toLowerCase();

            // Detect flags
            int limit = Integer.MAX_VALUE;
            boolean rawJson = false;
            for (String arg : args) {
                if (arg.startsWith("--limit")) {
                    String[] parts = arg.split("=");
                    if (parts.length == 2) {
                        limit = Integer.parseInt(parts[1]);
                    }
                }
                if (arg.equals("--json")) {
                    rawJson = true;
                }
            }

            // Case: plural form means fetch all
            switch (resource) {
                case "posts":
                    String postsJson = client.get("https://jsonplaceholder.typicode.com/posts", String.class);
                    if (rawJson) {
                        System.out.println(postsJson);
                    } else {
                        Post[] posts = mapper.readValue(postsJson, Post[].class);
                        for (int i = 0; i < Math.min(limit, posts.length); i++) {
                            System.out.println(posts[i]);
                        }
                    }
                    return;

                case "users":
                    String usersJson = client.get("https://jsonplaceholder.typicode.com/users", String.class);
                    if (rawJson) {
                        System.out.println(usersJson);
                    } else {
                        User[] users = mapper.readValue(usersJson, User[].class);
                        for (int i = 0; i < Math.min(limit, users.length); i++) {
                            System.out.println(users[i]);
                        }
                    }
                    return;

                case "todos":
                    String todosJson = client.get("https://jsonplaceholder.typicode.com/todos", String.class);
                    if (rawJson) {
                        System.out.println(todosJson);
                    } else {
                        Todo[] todos = mapper.readValue(todosJson, Todo[].class);
                        for (int i = 0; i < Math.min(limit, todos.length); i++) {
                            System.out.println(todos[i]);
                        }
                    }
                    return;

                case "comments":
                    String commentsJson = client.get("https://jsonplaceholder.typicode.com/comments", String.class);
                    if (rawJson) {
                        System.out.println(commentsJson);
                    } else {
                        Comment[] comments = mapper.readValue(commentsJson, Comment[].class);
                        for (int i = 0; i < Math.min(limit, comments.length); i++) {
                            System.out.println(comments[i]);
                        }
                    }
                    return;
            }

            // Case: singular + IDs
            if (args.length < 2) {
                System.out.println("You must provide at least one ID for resource: " + resource);
                return;
            }

            for (int i = 1; i < args.length; i++) {
                if (args[i].startsWith("--")) continue; // skip flags
                int id = Integer.parseInt(args[i]);

                String url = "https://jsonplaceholder.typicode.com/" + resource + "s/" + id;
                String json = client.get(url, String.class);

                if (rawJson) {
                    System.out.println(json);
                } else {
                    switch (resource) {
                        case "post":
                            Post post = mapper.readValue(json, Post.class);
                            System.out.println(post);
                            break;

                        case "user":
                            User user = mapper.readValue(json, User.class);
                            System.out.println(user);
                            break;

                        case "todo":
                            Todo todo = mapper.readValue(json, Todo.class);
                            System.out.println(todo);
                            break;

                        case "comment":
                            Comment comment = mapper.readValue(json, Comment.class);
                            System.out.println(comment);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} */



/* package com.example.myproject;

import com.example.myproject.model.Post;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) throws Exception {
        String json = "{ \"id\": 1, \"title\": \"Hello\", //\"body\": \"World\", \"userId\": 99 }";

        ObjectMapper mapper = new ObjectMapper();
        Post post = mapper.readValue(json, Post.class);

        System.out.println(post);
    }
} */
