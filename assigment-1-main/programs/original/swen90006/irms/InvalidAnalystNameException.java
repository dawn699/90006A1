package swen90006.irms;

public class InvalidAnalystNameException extends Exception {
    public InvalidAnalystNameException(String analystName) {
        super("Analyst name " + analystName + " does not comply with the requirements\n" +
                "\t- must contains at least " +
                IRMS.MINIMUM_ANALYST_NAME_LENGTH + " characters" +
                " and contain only letters (a-z, A-Z)");
    }
}
