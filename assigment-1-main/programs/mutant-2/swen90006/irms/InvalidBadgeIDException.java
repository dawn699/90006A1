package swen90006.irms;

public class InvalidBadgeIDException extends Exception {
    public InvalidBadgeIDException(String badgeID) {
        super("Invalid badge ID: " + badgeID);
    }
}
