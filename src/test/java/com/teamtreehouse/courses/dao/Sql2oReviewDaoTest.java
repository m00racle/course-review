package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;
import com.teamtreehouse.courses.model.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

/*
* Now after we implemented the ReviewDao interface into Sql2oReviewDao we need to test it
* most of the one we do is similar to what we have done in the courseDao
* However, please remember all test require us to develop a course before creating a review thus it is logical to put
* creating a class in the @Before method
* */
public class Sql2oReviewDaoTest {

    private Sql2oReviewDao reviewDao;
    private Connection conn;
    private Course course;

    @Before
    public void setUp() throws Exception {
        /*
        * let's start by configuring the database in use
        * */
        String connString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";

        /*
        * NEXT we initialize Sql2o
        * */
        Sql2o sql2o = new Sql2o(connString, "", "");

        /*
        * Initializing Dao WARNING remeber we need two kinds of dao one for the Course to add and another for the review
        * */
        Sql2oCourseDao courseDao = new Sql2oCourseDao(sql2o); //this is only needed here so it does not have to be field
        reviewDao = new Sql2oReviewDao(sql2o);

        /*
        * Open connection to Sql2o
        * and add the new course where we can test our reviews in action
        * */
        conn = sql2o.open();

        /*
        * Creating and Adding ne course to the data base
        * Remeber to put Course object in the field since it will be used throughout all tests
        * */
        course = new Course("Test", "http://testcourse.com");
        courseDao.add(course);
    }

    @After
    public void tearDown() throws Exception {
        /*
        * After a test is finish just close the connections
        * */
        conn.close();
    }

    /*
    * Lets make a method to creates new review that can be called numerous times
    * */
    private Review newTestreview(){
        return new Review(course.getId(), 5, "this is a test review");
    }

    @Test
    public void addedReviewSetsId() throws Exception {
        /*
        * Arrange: create one new review
        * */
        Review review = newTestreview();

        /*
        * fetch the original id of the review
        * */
        int originalId = review.getId();

        /*
        * Act: Add review to the reviewDao
        * */
        reviewDao.add(review);

        /*
        * Assert: Original Id is not the same as review.id after it was added to ReviewDao
        * */
        assertNotEquals(originalId, review.getId());
    }

    @Test
    public void addedReviewsAreAllReturnedFromFindAll() throws Exception {
        /*
        * Arrange: we make and add two reviews on the database
        * */
        reviewDao.add(newTestreview());
        reviewDao.add(newTestreview());

        /*
        * Asserts that all 2 reviews are listed from findAll()
        * */
        assertEquals(2, reviewDao.findAll().size());
    }

    @Test
    public void allReviewsAreReturnedWhenCourseExist() throws Exception {
        /*
        * Arrange: adding 3 new reviews for the course (same course)
        * */
        reviewDao.add(newTestreview());
        reviewDao.add(newTestreview());
        reviewDao.add(newTestreview());

        /*
        * Asserts: the size of the list of reviews from the reviewDao using findByCourseId(course.getId) is 3
        * */
        assertEquals(3, reviewDao.findByCourseId(course.getId()).size());

    }

    @Test
    public void noReviewsReturnsEmptyList() throws Exception {
        /*
        * Asserts: all List from findAll() and findByCourseId retunrs empty list
        * */
        assertEquals("findAll should returns empty ", 0, reviewDao.findAll().size());
        assertEquals("find by CourseID should returns empty",
                0,
                reviewDao.findByCourseId(course.getId()).size());
    }

    @Test(expected = DaoException.class) //this is the assertion for this method
    public void addingReviewsToNonExistingCourseWillInvokeException() throws Exception {
        /*
        * making new review but intended to non existing courses
        * just pick large number as courseId value parameter as argument
        * */
        Review review = new Review(46, 5, "Test comment");

        /*
        * act: adding the newly created review into database and it should invoke exception (DaoException that is)
        * that said problem adding reviews
        * */
        reviewDao.add(review);
    }
}