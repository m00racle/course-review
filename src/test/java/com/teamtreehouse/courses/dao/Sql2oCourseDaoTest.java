package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.model.Course;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

/**
 * This is the test code for DAO implementation
 * we will set up the database (H2 database) using in-memory database version thus we put in it the script we made in
 * db/init.sql
 *
 * */
public class Sql2oCourseDaoTest {

    private Sql2oCourseDao dao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        /*
        * First we need to build the connection string to use the JDBC
        * we call JDBC and declare H2 database as the database version which we use the in memory version of it (mem)
        * and we called that database "testing"
        *
        * next part during the initialization (INIT) we want JDBC to run a script written in db/init.sql (the
        * resources folder is a classpath)
        * */
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";

        /*
        * we make a new Sql2o object and passed in the connectionString which by default will asks for username and
        * password but for this test is by default blank or ("")
        * */
        Sql2o sql2o = new Sql2o(connectionString,"","");

        /*
        * Now we make our DAO by passing the sql2o object (remember the constructor demands the Sql2o object to be
        * passed in)
        * */
        dao = new Sql2oCourseDao(sql2o);

        /*
        * WARNING: one thing about in memory version of a database is that the data will be erased when the connection
        * to that database is closed. That is the point of being in-memory, to be volatile.
        *
        * In order to avoid this we need to keep the connection alive through entire test so that's not being wiped out
        * */
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        /*
        * remember we kept the connection alive through out the test to prevent the database to be erased after it
        * being closed since it was in-memory version of database.
        *
        * In this terDown @After method we must do the right thing and close it down since it means the test is
        * officially over.
        * */
        conn.close();
    }

    @Test
    public void addingCourseSetsId() throws Exception {
        /*
        * this test if we add new course it will automatically sets the id to that course
        * The setId is set in the Sql2oCourseDao class method when we wrote the code that implements the add method
        *
        * first let's make the Course object we want to add to the database
        * */
        Course course = newTestCourse();

        /*
        * let's fetch the Id of the Course to see if it is automatically set
        * */
        int originalCourseId = course.getId();

        /*
        * Now the action part: we will add the newly created Course object to the dao
        * */
        dao.add(course);

        /*
        * Next we assert that it is not equal to the originalCourseId since it will be auto generated and incremented
        * Thus we will use assertNotEqual to ensure originalCourseId is not the same with course.getId after it was
        * added into dao
        * */
        assertNotEquals(originalCourseId, course.getId());
    }

    @Test
    public void addedCoursesAreReturnedFromFindAll() throws Exception{
        /*
        * this will test if we call the Sql2oCourseDao method findAll it will returned all added courses prior to the
        * call
        *
        * In this session though since the findAll is not yet being coded it should return an exception
        *
        * Okay fist let's make our new course
        * */
        Course course = newTestCourse();

        /*
        * then we add the newly created Course object into DAO
        * */
        dao.add(course);

        /*
        * then we assert if indeed the course is returned in form of a list
        * since the course added is only 1 then the sizer of the returned findAll list is must be 1
        * */
        assertEquals(1, dao.findAll().size());
    }

    @Test
    public void noCoursesReturnsEmptyList() throws Exception {
        /*
        * this test make sure even when there are no Course objects added into the data base it will still returns a
        * List but without any object in it (empty list) rather than returns null
        *
        * This is because we can still use the empty list as data to be shared to the user that there is no courses
        * added yet to the database
        *
        * To do this we just go straight to the asserts since we do not need to set any Courses */
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void existingCoursesCanBeFoundById() throws Exception {
        /*
        * This test the findById method in Sql2oCourseDao class implements CourseDao
        * Arrange:
        * make new course and add into DAO
        *
        * Then try to find it using dao.findById method using the id of the newly created course
        *
        * Assert that it is foundCourse object is = course object
        * */
        Course course = newTestCourse();
        dao.add(course);

        Course foundCourse = dao.findById(course.getId());

        assertEquals(course, foundCourse);
    }

    /**
     * This new private method is the result of a Refactor that code new Course initialization as the arrangement
     * prior to some tests here.
     *
     * We do not use @Before test method since although almost all tests do this arrangement not all test need it
     * */
    private Course newTestCourse() {
        return new Course("Test", "http://what.com");
    }
}