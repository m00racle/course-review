package com.teamtreehouse.courses.model;

import java.util.Objects;

/**Entry 1: Model
 *
 * This will model the Object Review than will be linked to a course object
 *
 * Thus we need these variables:
 * 1. id --> not mentioned in constructor we will auto generate using SQL
 * 2. courseId --> in construction thus user cannot create a review without course ID that the review intended for!
 * 3. rating
 * 4. commnet
 *
 * we also need constructors, getters and setters, and also equals and hash codes (use the IntelliJ default not Java 7)
 * */
public class Review {
    private int id;
    private int courseId;
    private int rating;
    private String comment;

    public Review(int courseId, int rating, String comment) {
        this.courseId = courseId;
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != review.id) return false;
        if (courseId != review.courseId) return false;
        if (rating != review.rating) return false;
        return comment != null ? comment.equals(review.comment) : review.comment == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + courseId;
        result = 31 * result + rating;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
