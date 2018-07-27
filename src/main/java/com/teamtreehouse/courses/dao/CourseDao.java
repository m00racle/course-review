package com.teamtreehouse.courses.dao;


import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;

import java.util.List;

/**Entry 2: Accessing Data
 * This is the standard Dao interface
 *
 * we need to make it fairly generic since it is an interface
 * we need to be able to:
 * 1. add new course
 * 2. list all available courses
 * */
public interface CourseDao {
    void add(Course course) throws DaoException;

    List<Course> findAll();

    Course findById(int id);
}
