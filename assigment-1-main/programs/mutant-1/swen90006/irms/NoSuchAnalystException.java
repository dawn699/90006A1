package swen90006.irms;

public class NoSuchAnalystException extends Exception {
    public NoSuchAnalystException(String analystName) {
        super("Analyst name does not exist: " + analystName);
    }
}
