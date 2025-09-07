package swen90006.irms;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String password) {
        super("Password \"" + password + "\" does not comply with the requirements:\n" +
                "\t- Must be between " + IRMS.MINIMUM_PASSWORD_LENGTH + " and " +
                     IRMS.MAXIMUM_PASSWORD_LENGTH + " characters long,\n" +
                "\t- Must contain at least one letter (a-z, A-Z),\n" +
                "\t- Must contain at least one digit (0-9),\n" +
                "\t- Must contain at least one special character (not a letter or digit),\n" +
                "\t- Must not contain any space characters.");
    }
}
