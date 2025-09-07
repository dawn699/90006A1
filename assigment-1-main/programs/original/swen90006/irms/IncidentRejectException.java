package swen90006.irms;

public class IncidentRejectException extends Exception {
    public IncidentRejectException(String incidentID, int newRating, int lowestRating) {
        super("Incident " + incidentID + " rejected: New rating " + newRating + " is not higher than lowest existing rating " + lowestRating + ".");
    }
}
