package com.teamtreehouse.courses.model;

import java.util.Objects;

/**Entry 1: Models
 * This is the class where we build our Course model
 * There will be related to Review model using one to many relation
 * One Course can have many reviews.
 *
 * Here we will build id, name, and url variables for a Course object
 *
 * for method:
 * 1. constructor
 * 2. getter and setter
 * 3. equals and hash code (use IntelliJ default not Java 7)
 * */
public class Course {
    private int id;
    private String name;
    private String url;

    public Course(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        if (!name.equals(course.name)) return false;
        return url.equals(course.url);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }
}
