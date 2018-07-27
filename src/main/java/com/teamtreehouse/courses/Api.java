package com.teamtreehouse.courses;

import com.google.gson.Gson;
import com.teamtreehouse.courses.dao.CourseDao;
import com.teamtreehouse.courses.dao.Sql2oCourseDao;
import com.teamtreehouse.courses.model.Course;
import org.sql2o.Sql2o;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * After adding the Gradle Spark dependency we go here to build our API to let other outside apps to reach us
 * */
public class Api {
    public static void main(String[] args) {
        /*
        * create new sql2o object and instatiate as new dao object
        *
        * here we will use H2 database jdbc into a file in the home directory named reviews.db
        *
        * Then we will run the script in the db/init.sql
        * */
        Sql2o sql2o = new Sql2o("jdbc:h2:~/reviews.db; INIT=RUNSCRIP from 'classpath:db/init.sql'");

        /*
        * remember if the database already exist we have the IF EXIST clause in our init.sql thus it will not
        * overwrite our existing table.
        *
        * Now we going to initialization of our dao using the CourseDao interface and Sql2oCourseDao as implementation
        * */
        CourseDao courseDao = new Sql2oCourseDao(sql2o);

        /*
        * Now we need to allow the users of our API to create a new course
        * we use POST request method from the Spark framework that has an overriden counterpart: REST standard practice
        * dictates we directed the POST request to the plural resource's name. -> uri: "/courses"
        *
        * First as always we call post(<uri>, <matcher to request type in this case application/json>, then the lambda
        * on how to handle the POST request
        *
        * So basically we need to get a JSON that's being passed across (we make sure it JSON by application/json). To
        * get it there is a method on the request object called body that method will return the JSON that the user sent
        * across.
        *
        * Thus we want to create a brand new course from this JSON sent by the POST request. This is a form of
        * serialization. First we are going to consume the JSON from the request and in the end we're going to produce
        * JSON as response.
        *
        * We can create our serialization code but there are many tools free available in the web. Google even has one
        * called gson which has google/gson as GitHub repo. We need to add the dependency of gson into our Gradle. (We
        * will use the older version to keep the session practical-> version 2.5 it is)
        *
        * Now we going to initialize the Gson object first to be used by any other object Sql2o and also to handle the
        * JSON serialization.
        *
        * Then in the POST request handler we are going to  use it to serialize data from req.body and determine the
        * class we want to put it in (which in this case Course.class)
        *
        * NOTE: gson does not use the getters and setters in the POJO style Course class to fetch data, Instead it uses
        * its' own private fields to do that. Check the Learn More section on Endpoints session documentation.
        * */
        Gson gson = new Gson();

        /*
        * Next we will put the newly added course to the DAO to be inputted to the database
        * don't forget be a good API citizen and send a response status 201 which means it was successfully created,
        * also we want to tell them that our respinse is also formatted as JSON ("application/json")
        * then we will return the course (at the moment it is still a gson object and not yet transformed into JSON.
        *
        * Thus in this post spark handler we need to specify another parameter after the lambda. This parameter will use
        * a method reference trick to gson class which has a method called toJson (response transformer) which accept
        * an object and return a String.
        * */

        post("/courses", "application/json", (req, res)->{
            Course course = gson.fromJson(req.body(), Course.class);
            courseDao.add(course); // use DAO to add the new course to database
            res.status(201); // status: created success
            // res.type("application/json");-> no need for this anymore the after method below already takes care of it
            return course;
        }, gson::toJson);

        /*
        * Next we want to write the get portion of the course. Which here we will use the GET request to the same URI
        * "/courses"
        *
        * we only build it if the GET request is using the application/json.
        *
        * Then we use lambda to return courseDao findAll() and using method reference turn that gson object to json
        * */

        get("/courses", "application/json",
                (req, res) -> courseDao.findAll(), gson::toJson);

        /*
        * We also need to be able to find a course by its id
        *
        * To do this we use GET request. Since the id will be in the GET request as parameter we can just get it using
        * request.params(id) and extract it as int id. We use wrapper class Integer to parse it into integer but NOTE
        * that this is dangerous because it can be bad if the object is not really an int.
        *
        * */

        get("/courses/:id", "application/json", (req, res) ->{
            int id = Integer.parseInt(req.params("id"));// TODO: yan - what if this is not found?
            Course course = courseDao.findById(id);
            return course;
        }, gson::toJson);

        /*
        * Even if both of our get and post request ensure to return a JSON object by definition to gson::toJson
        * method reference we still need to filter it out to ensure no non JSON data is returned as response to our
        * GET and POST request
        *
        * This filter will be located after each request (POST and GET) is finished being processed. Basically we told
        * after that all response will be using JSON object ("application/json")
        * */
        after((req, res)-> {
            res.type("application/json");
        });
    }
}
