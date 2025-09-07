package swen90006.irms;

public class InvalidIncidentIDException extends Exception {
    public InvalidIncidentIDException(String incidentID) {
        super("Invalid incident ID format: " + incidentID);
    }
}
