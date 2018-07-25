package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

/**Entry 2: Accessing Data
 *
 * This class is named after a convention that the name of the interface is always becomes the suffix and the name of
 * the class add as the prefix. For example the name of the interface is List and one of the implementing class name is
 * ArrayList.
 *
 * Thus this is the implementation of CourseDao interface with Sql2o to make a new class
 *
 *
 * */
public class Sql2oCourseDao implements CourseDao{

    private final Sql2o sql2o;

    // constructor
    /* When we want to use SQL2o we still need to configure it although not as complex as Hibernate. We still need to tell
     * it where the database is located. We can config our SQL2o database location hard coded in this class but then again
     * we need to do that all over again for different implmenting classes. Moreover, when other user or test method want
     * to access it with different database it will be impractical since they have to modify the source code.
     *
     * So we will make the database here as dependency and we will inject it into our object at run time when we created it.
     * to do this we need to build a constructor to this class that require to pass SQL2o object.
     * */
    public Sql2oCourseDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public void add(Course course) throws DaoException {
        // add a Course using SQL code
        /*okay now we will make Sql2o in acition. In add method we want to Insert name and url of the course and make id as
         * auto built. The way we use sql2o is by making a string on the SQL code and the value we want to use is linked by
         * adding a colon as prefix of the variable.
         * */
        String sql = "INSERT INTO courses(name, url) VALUES (:name, :url)";

        // try to make a connection
        /*Then we need to make a connection to the database which is closable so we can use try catch block for it. We need to
         * make sql2o.open it will close in the end of the SQL code. Similar with hibernate always begin transaction and close
         * in the end.
         * */
        try (Connection con = sql2o.open()){
            /*One thing about this try block: it told the connection to the database to create a query which already
             * Stored in the sql String variable above. Then the value which care :name and :url will come using POJO
             * from object called course.
             *
             * We want to update meaning if the course already exist modify if not create a new one.
             *
             * Then lastly get key in this case it should be object but we cast it into an int which this will be the
             * id of the last query
             * */
            int id = (int) con.createQuery(sql)
                    .bind(course)
                    .executeUpdate()
                    .getKey();

            /*
            * Next we need to make setId as automatic setting using id generated using getKey() method above.
            * */
            course.setId(id);
        } catch (Sql2oException ex){
            /*
            * just in case we have problem when INSERT SQL code using sql2o we need to send a message using the
            * DaoException object that we just created.
            *
            * We will add original exception that is ex and our own message
            * */
            throw new DaoException(ex,
                    "Problem adding course");
        }
    }

    @Override
    public List<Course> findAll() {
        return null;
    }
}
