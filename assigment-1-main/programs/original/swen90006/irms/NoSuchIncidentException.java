package swen90006.irms;

public class NoSuchIncidentException extends Exception {
    public NoSuchIncidentException(String incidentID) {
        super("No such incident found with ID: " + incidentID);
    }
}
