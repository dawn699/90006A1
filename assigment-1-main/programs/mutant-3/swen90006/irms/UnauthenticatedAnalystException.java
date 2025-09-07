package swen90006.irms;

public class UnauthenticatedAnalystException extends Exception {
    public UnauthenticatedAnalystException(String analystName) {
        super("Analyst is not authenticated: " + analystName);
    }
}