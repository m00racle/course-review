package com.teamtreehouse.courses.exc;

/**Entry 2: Accessing Data
 * This exception is created when building the CourseDao interface com.teamtreehouse.courses.dao.CourseDao
 *
 * Basically what it does it to create a message but still includes the original exception message there
 * */
public class DaoException extends Exception {
    private final Exception originalException;

    public DaoException(Exception originalException, String msg){
        super(msg);
        this.originalException = originalException;
    }
}
