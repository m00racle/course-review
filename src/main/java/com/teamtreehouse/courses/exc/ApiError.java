package com.teamtreehouse.courses.exc;


/*
* We make this class to handle various exceptions related to Api functions. This class extends RuntimeException class
* so that we do not have to declare it everywhere because it will be a Runtime error, thus when the app runs so does
* this class
* */
public class ApiError extends RuntimeException {
    /*
    * first for this kind of error we need to give proper status and it will be final when we do this no middle process
    * in this class can alter it
    * */
    private final int status;

    /*
    * Then we make some kind of constructor but here we also demanding to get and argument of msg passed into the
    * constructor method. The message will be passed into the superclass of this class which is the RuntimeException
    * class.
    *
    * Then the status will be proceed as this variable in this constructor.
    * */
    public ApiError(int status, String msg){
        super(msg);
        this.status = status;
    }

    /*
    * we need a getter to utilize the status we just make from the constructor so that we can pass it when appropriate
    * */

    public int getStatus() {
        return status;
    }
}
