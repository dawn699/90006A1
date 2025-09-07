package swen90006.irms;

public class AccessLevelException extends Exception {
    public AccessLevelException(String analystName) {
        super("Access denied: Analyst " + analystName + " does not have SUPERVISOR access.");
    }
}
