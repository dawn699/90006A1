package swen90006.irms;

public class DuplicateIncidentException extends Exception {
    public DuplicateIncidentException(String incidentID) {
        super("Incident ID already exists: " + incidentID);
    }
}