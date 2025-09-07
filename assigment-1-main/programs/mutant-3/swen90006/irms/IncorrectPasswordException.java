package swen90006.irms;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String analystName, String password) {
        super("Incorrect password: " + password + " for analyst " + analystName);
    }
}
