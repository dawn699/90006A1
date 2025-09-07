package swen90006.irms;

public class InvalidRatingException extends Exception {
    public InvalidRatingException(int rating) {
        super("Invalid rating value: " + rating + ". Rating must be between 0 and 9.");
    }
}
