package com.teamtreehouse.testing;

/*
* This class is made to accomodate the Api response testing. from the com/teamtreehouse/courses/ApiTest.java
* */
public class ApiResponse {
    /*
    * basically there are two things we expect to find and look up in a response
    * the status of the request processing and the body resulted from the request
    *
    * Since it was determinded from the process of the URI request both of those variables should be final
    * */
    private final int status;
    private final String body;

    /*
    * we make constructor to build the object of ApiResponse for testing by receieveing the response from the URI
    * and pass it into constructors
    * */

    public ApiResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    /*
    * Since all variables passed into the constructors are final that means we do not need setters.
    *
    * We just going to build getters to make assertion later
    * */

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
