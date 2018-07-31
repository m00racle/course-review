package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Review;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oReviewDao implements ReviewDao {
    private Sql2o sql2o;

    /*
    * constructor for dao review
    * */

    public Sql2oReviewDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Review review) throws DaoException {
        /*
        * making the SQL code: The naming course_id is located to denotes the column name in the init.sql BUT then
        * naming of the values input MUST be the same as in the com.teamtreehouse.courses.model.Review model which
        * written as courseId thus in the value of SQL string it must be named :courseId NOT :course_id!!!
        *
        * Note that the courseId is a foreign key but the implementation of making sure that the foreign key is valid
        * or not might not be here
        * */
        String sql = "INSERT INTO reviews(course_id, rating, comment) VALUES (:courseId, :rating, :comment)";

        /*
        * making connection using Sql2o (using try for self closing)
        * */
        try (Connection conn = sql2o.open()){

            /*
            * lets make the id primary key for the review added
            * */
            int id = (int)conn.createQuery(sql)
                    .bind(review)
                    .executeUpdate()
                    .getKey();

            /*
            * using the id from the query above we set the id for the newly added review
            * */
            review.setId(id);
        } catch (Sql2oException ex){

            /*
            * handle the exception to DaoException
            * */
            throw new DaoException(ex, "Problem adding review");
        }
    }

    @Override
    public List<Review> findAll() {
        /*
        * we need to make try block that serves self closing to fetch all reviews and turn into a Review class object
        *
        * Here we need to add Column Mapping since it generate error on testing complaining that it did not know how to
        * map "COURSE_ID" this will map it ino courseId variable in the Review class object!
        * WARNING: same property must be added for findByCourseId below!
        * */
        try (Connection conn = sql2o.open()){
            return conn.createQuery("SELECT * FROM reviews")
                    .addColumnMapping("COURSE_ID", "courseId")
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Review> findByCourseId(int courseId) {
        /*
        * This time it is specific to courseId thus we need to list all reviews related to given courseId
        * Remember it is specific to courseId not the review's id. And also it is fetch all not just fetch first
        * */
        try (Connection conn = sql2o.open()){
            return conn.createQuery("SELECT * FROM reviews WHERE course_id = :courseId")
                    .addColumnMapping("COURSE_ID", "courseId")
                    .addParameter("courseId", courseId) //-> this we transform courseId into course_id
                    .executeAndFetch(Review.class);
        }
    }
}
