package com.example.myproject.model;

public class Post {
    public int id;
    public String title;
    public String body;
    public int userId;

    @Override
    public String toString() {
        return "Post{id=" + id + ", title='" + title + "', body='" + body + "', userId=" + userId + "}";
    }
}
