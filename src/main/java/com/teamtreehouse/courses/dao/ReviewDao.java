package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Review;

import java.util.List;

/**Entry 2: Accessing Data
 * This interface is to make Review database manager
 *
 * we need to be able to:
 * 1. add new review
 * 2. find all review
 * 3. find all review specific to a courseId
 * */
public interface ReviewDao {
    void add(Review review) throws DaoException;

    List<Review> findAll();

    List<Review> findByCourseId(int courseId);
}
