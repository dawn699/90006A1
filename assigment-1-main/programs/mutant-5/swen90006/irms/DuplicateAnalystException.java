package swen90006.irms;

public class DuplicateAnalystException extends Exception {
    public DuplicateAnalystException(String analystName) {
        super("Analyst is already registered: " + analystName);
    }
}
