package swen90006.irms;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;


public class IRMS {
    /**
     * The minimum length of a username
     */
    public final static int MINIMUM_ANALYST_NAME_LENGTH = 4;

    /**
     * The minimum length of a password
     */
    public final static int MINIMUM_PASSWORD_LENGTH = 10;

    /**
     * The maximum length of a password
     */
    public final static int MAXIMUM_PASSWORD_LENGTH = 16;

    /**
     * Length of a badge ID
     */
    public final static int BADGE_ID_LENGTH = 4;

    /**
     * The authentication status of a user: not authenticated,
     * or authenticated with a password.
     */
    private enum AuthenticationStatus {AUTHENTICATED, NOT_AUTHENTICATED}

    /**
     * Enum representing the roles that a user can have in the system.
     */
    public enum Role {MEMBER, SUPERVISOR}

    /**
     * Stores the password for each registered user, keyed by username.
     */
    private Map<String, String> passwords;

    /**
     * Stores the assigned role (MEMBER or SUPERVISOR) for each user, keyed by username.
     */
    private Map<String, Role> roles;

    /**
     * Stores the current authentication status (AUTHENTICATED or NOT_AUTHENTICATED) for each user, keyed by username.
     */
    private Map<String, AuthenticationStatus> authenticationStatus;

    /**
     * Set of valid supervisor badge IDs used to grant supervisor access.
     */
    private Set<String> validBadges = new HashSet<>(Arrays.asList("1234", "1235"));
    //private final List<String> validBadges = new ArrayList<>(Arrays.asList("1234", "1235"));

    /**
     * Stores reported incidents in the system.
     * Each incident is represented as a SimpleEntry with the incident ID as the key
     * and the rating as the value.
     */
    private List<SimpleEntry<String, Integer>> incidents;

    /**
     * Constructs a new Incident Registration Management System (IRMS) instance
     * with no registered users or incident records.
     */
    public IRMS() {
        passwords = new HashMap<>();
        roles = new HashMap<>();
        authenticationStatus = new HashMap<>();
        incidents = new ArrayList<>();
    }

    /**
     * Validates whether the given analystName meets the specified criteria.
     *
     * <p>The analystName must satisfy the following conditions:
     * <ul>
     *   <li>It must be at least four characters long.</li>
     *   <li>It must contain only lower-case and upper-case letters.</li>
     * </ul>
     *
     * @param analystName the analystName to be validated
     * @return {@code true} if the analystName is valid according to the criteria; {@code false} otherwise
     */
    private boolean isValidAnalystName(String analystName) {
        // Check if the analystName is at least 4 characters long
        if (analystName.length() < MINIMUM_ANALYST_NAME_LENGTH) {
            return false;
        }

        // Check if the analystName contains only lower- and upper-case letters
        for (char c : analystName.toCharArray()) {
            if (!('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z')) {
                return false;
            }
        }

        // If both checks pass, the analystName is valid
        return true;
    }

    /**
     * Registers a new analyst with an authentication status of NOT_AUTHENTICATED,
     * role as default MEMBER, the role is not taken as an input when registering.
     *
     * <p>The following requirements must be met:</p>
     *
     * <h3>Requirements:</h3>
     *
     * <ul>
     *   <li><strong>Analyst Name:</strong>
     *     <ul>
     *       <li>The name must not have been registered previously.</li>
     *       <li>Must be at least four characters long.</li>
     *       <li>Must contain only lower- and upper-case letters.</li>
     *     </ul>
     *   </li>
     *   <li><strong>Password:</strong>
     *     <ul>
     *       <li>Must be between 10 and 16 characters long (inclusive).</li>
     *       <li>Must contain at least one letter (a-z, A-Z).</li>
     *       <li>Must contain at least one digit (0-9).</li>
     *       <li>Must contain at least one special character (anything other than a-z, A-Z, or 0-9)</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <h3>Parameters:</h3>
     * <ul>
     *   <li><strong>analystName:</strong> The name for the analyst to be added.</li>
     *   <li><strong>password:</strong> The password for the analyst.</li>
     * </ul>
     *
     * <h3>Exceptions (thrown in order):</h3>
     * <ul>
     *   <li><strong>DuplicateAnalystException:</strong> Thrown if analyst name is already registered.</li>
     *   <li><strong>InvalidAnalystNameException:</strong> Thrown if analyst name does not meet the specified requirements.</li>
     *   <li><strong>InvalidPasswordException:</strong> Thrown if the password does not meet the specified requirements.</li>
     * </ul>
     *
     * <p>Exceptions are thrown in the following order:</p>
     * <ul>
     *   <li>DuplicateAnalystException is thrown first (i.e., existing analyst names are all valid).</li>
     *   <li>If the analyst name is invalid, then the password is not checked.</li>
     *   <li>If the analyst name is valid, then the password is checked.</li>
     * </ul>
     *
     * <h3>Assumptions:</h3>
     * <ul>
     *   <li>analystName and password are non-null.</li>
     *   <li>the role of a newly registered analyst is MEMBER.</li>
     * </ul>
     *
     * @param analystName The name for the analyst to be added.
     * @param password    The password for the user.
     * @throws DuplicateAnalystException   Thrown if the analyst name is already registered.
     * @throws InvalidAnalystNameException Thrown if the analyst name does not meet the specified requirements.
     * @throws InvalidPasswordException    Thrown if the password does not meet the specified requirements.
     */
    public void registerAnalyst(String analystName, String password)
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {

        // Check if the analyst is already registered
        if (passwords.containsKey(analystName)) {
            throw new DuplicateAnalystException(analystName);
        } else if (!isValidAnalystName(analystName)) {
            throw new InvalidAnalystNameException(analystName);
        } else {
            boolean hasLetter = false;
            boolean hasDigit = false;
            boolean hasSpecial = false;
            if (password.length() < MINIMUM_PASSWORD_LENGTH || password.length() > MAXIMUM_PASSWORD_LENGTH) {
                throw new InvalidPasswordException(password);
            } else {
                for (char c : password.toCharArray()) {
                    if ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z') {
                        hasLetter = true;
                    } else if ('0' <= c && c <= '9') {
                        hasDigit = true;
                    } else {
                        hasSpecial = true;
                    } 
                }

                if (!(hasLetter && hasDigit && hasSpecial)) {
                    throw new InvalidPasswordException(password);
                } else {
                    passwords.put(analystName, password);
                    roles.put(analystName, Role.MEMBER);
                    authenticationStatus.put(analystName, AuthenticationStatus.NOT_AUTHENTICATED);
                }
            }
        }
    }

    /**
     * Authenticates an analyst using their analyst name and password.
     * If the analyst exists and the password is correct, 
     * this method then sets the authentication status for {@code analystName} to {@code AUTHENTICATED}.
     * If either credential is incorrect, it sets the stored status to {@code NOT_AUTHENTICATED}
     * for a registered analyst and throws the appropriate exception. 
     *
     * @param analystName The name of the analyst attempting to authenticate.
     * @param password    The password associated with the analyst.
     * @throws NoSuchAnalystException     If the analyst name is not registered.
     * @throws IncorrectPasswordException If the password does not match the analyst’s stored password.
    */

    public void authenticate(String analystName, String password)
            throws NoSuchAnalystException, IncorrectPasswordException {
        //Check that the user exists
        if (!passwords.containsKey(analystName)) {
            throw new NoSuchAnalystException(analystName);
        }

        String storedCredentials = passwords.get(analystName);
        if (!storedCredentials.equals(password)) {
            authenticationStatus.put(analystName, AuthenticationStatus.NOT_AUTHENTICATED);
            throw new IncorrectPasswordException(analystName, password);
        } else {
            authenticationStatus.put(analystName, AuthenticationStatus.AUTHENTICATED);
        }
    }

    /**
     * Allows an analyst to request a supervisor role by providing a badge ID.
     *
     * @param analystName The name of the analyst requesting the supervisor role.
     * @param badgeID The badge ID being submitted for verification.
     * @throws NoSuchAnalystException If the analyst is not registered, or is not the previously authenticated analyst. 
     * @throws InvalidBadgeIDException If the badge ID is not valid.
     */

    public void requestSupervisorAccess(String analystName, String badgeID)
            throws NoSuchAnalystException, InvalidBadgeIDException {
        // Check if the analyst is registered
        if (!isRegistered(analystName)) {
            throw new NoSuchAnalystException(analystName);
        }

        // Check if badgeID is valid
       if (!validBadges.contains(badgeID)) {
            throw new InvalidBadgeIDException(badgeID);
        } else {
            roles.put(analystName, Role.SUPERVISOR);
        }

    }

    /**
     * Allows an authenticated analyst to register a new incident.
     *
     * @param analystName The name of the analyst submitting the incident.
     * @param incidentID The identifier for the incident. 
     * @param rating The severity rating, between 0 and 9 (inclusive).
     * @throws NoSuchAnalystException If the analyst is not registered.
     * @throws UnauthenticatedAnalystException If the analyst is not currently authenticated.
     * @throws DuplicateIncidentException If the incident ID already exists in the system.
     * @throws InvalidRatingException If the rating is outside the range [0–9].
     * @throws IncidentRejectException If the incident is rejected based on the analyst's role and the current list of incidents.
     */

    public void submitIncident(String analystName, String incidentID, int rating)
            throws NoSuchAnalystException, UnauthenticatedAnalystException, DuplicateIncidentException,
            InvalidRatingException,IncidentRejectException {

        if (!isAuthenticated(analystName)) {
            throw new UnauthenticatedAnalystException(analystName);
        }
        // Check for duplicate incident ID
        if (hasDuplicateIncidentID(incidentID)) {
            throw new DuplicateIncidentException(incidentID);
        }

        // Check rating is between 0 and 9
        if (rating < 0 || rating > 9) {
            throw new InvalidRatingException(rating);
        }

        // check the role of the analyst
        Role role = getAnalystRole(analystName);
        if (role == Role.SUPERVISOR) {
            // supervisor can submit any incident
            incidents.add(new SimpleEntry<>(incidentID, rating));

        } else {
            // if list is empty, allow any incident
            if (incidents.isEmpty()) {
                incidents.add(new SimpleEntry<>(incidentID, rating));
            } else {
                // if the list is not empty, check the lowest rating
                int lowestRating = findLowestRating();
                if (rating > lowestRating) {
                    // if the new incident's rating is higher than the lowest, add it
                    incidents.add(new SimpleEntry<>(incidentID, rating));
                } else {
                    // if the new incident's rating is not higher, reject it
                    throw new IncidentRejectException(incidentID, rating, lowestRating);
                }
            }
        }
    }

    /**
     * Allows an analyst to retrieve an incident by its 0-based index in the incident list. 
     * @param analystName The name of the analyst requesting the incident.
     * @param index The index of the incident to retrieve.
     * @return The incident at the specified index.
     * @throws NoSuchAnalystException If the analyst is not registered.
     * @throws IndexOutOfBoundsException If the index is out of bounds for the incidents list.
     */
    public SimpleEntry<String, Integer> getIncident(String analystName, int index)
            throws NoSuchAnalystException, IndexOutOfBoundsException {
        if (!isRegistered(analystName)) {
            throw new NoSuchAnalystException(analystName);
        }
        var res = incidents.get(index);
        return new SimpleEntry<>(res.getKey(), res.getValue());
    }


    /** 
     * Below are some helper functions for you to use in your tests.
     * You are not required to use all these in your tests, but they can certainly help you.
     * You **should not** add any more helper functions here. 
     */


    /**
     * Checks if the user with the given username is registered.
     * @param analystName The name of the analyst to check.
     * @return {@code true} if the analyst is registered
     * @throws NoSuchAnalystException if the analyst name does not exist in the system
     */ 
    public boolean isRegistered(String analystName) 
        throws NoSuchAnalystException
    {
        if (!passwords.containsKey(analystName)) {
            throw new NoSuchAnalystException(analystName);
        }
        return true;
    }

    /**
     * Checks if the user with the given username is authenticated.
     * @param analystName The name of the analyst to check.
     * @return {@code true} if the analyst is authenticated, false otherwise
     * @throws NoSuchAnalystException if the analyst name does not exist in the system
     */
    public boolean isAuthenticated(String analystName)
            throws NoSuchAnalystException
    {
        if (!passwords.containsKey(analystName)) {
            throw new NoSuchAnalystException(analystName);
        }
        if (authenticationStatus.get(analystName) != AuthenticationStatus.AUTHENTICATED) {
            return false;
        }
        return true;
    }

    /**
     * Returns the incident with the lowest rating from the list of incidents.
     * @return The lowest rating found in the list of incidents.
     * Assumes that the list is not empty.
     */
    private int findLowestRating() 
    {
        int lowest = incidents.get(0).getValue();
        for (int i = 1; i < incidents.size(); i++) {
            int value = incidents.get(i).getValue();
            if (value < lowest) {
                lowest = value;
            }
        }
        return lowest;
    }

    /**
     * Checks if the list of incidents contains a duplicate incident ID.
     * @param incidentID The incident ID to check for duplicates.
     * @return {@code true} if a duplicate incident ID is found, {@code false} otherwise.
     */
    public boolean hasDuplicateIncidentID(String incidentID) {
        if (incidents == null || incidents.isEmpty()) {
            return false;
        }
        for (SimpleEntry<String, Integer> entry : incidents) {
            if (entry.getKey().equals(incidentID)) {
                return true; 
            }
        }
        return false; 
    }

    /**
     * Return the role of a given analyst.
     * @param analystName The name of the analyst.
     * @return The role of the analyst (MEMBER or SUPERVISOR).
     * @throws NoSuchAnalystException If the analyst name does not exist in the system
     */
    public Role getAnalystRole(String analystName) throws NoSuchAnalystException {
        if (!roles.containsKey(analystName)) {
            throw new NoSuchAnalystException(analystName);
        }
        return roles.get(analystName);
    }

    /**
     * Return the rating of a given incident. 
     * @param incidentID The ID of the incident.
     * @return The rating of the incident.
     * @throws NoSuchIncidentException If the incident ID does not exist in the system
     */
    public int getIncidentRating(String incidentID) throws NoSuchIncidentException {
    for (SimpleEntry<String, Integer> entry : incidents) {
        if (entry.getKey().equals(incidentID)) {
            return entry.getValue();
        }
    }
    throw new NoSuchIncidentException("Incident ID not found: " + incidentID);
    }

    /**
     * Check if a given pair of incidentID and ratings is saved in the incidents list.
     * @param incidentID The ID of the incident to check.
     * @param ratings The rating of the incident to check.
     * @return {@code true} if the incident is found, {@code false} otherwise.
     */
    public boolean isSavedIncident(String incidentID, int ratings) {
        for (SimpleEntry<String, Integer> entry : incidents) {
            if (entry.getKey().equals(incidentID) && entry.getValue() == ratings) {
                return true;
            }
        }
        return false;
    }

}