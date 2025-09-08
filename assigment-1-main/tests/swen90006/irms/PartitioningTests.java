package swen90006.irms;

import org.junit.*;

import static org.junit.Assert.*;

public class PartitioningTests {
    // The IRMS instance variable irms is shared across all test methods in this class
    protected IRMS irms;

     /**
     * The setup method annotated with "@Before" runs before each test.
     * By default, it initializes the IRMS instance and creates a dummy user.
     * Use this method to set up any common test data or state.
     */

    @Before
    public void setUp() throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        irms = new IRMS();
        irms.registerAnalyst("analystA", "Password1!");

    }

     /**
     * The teardown method annotated with "@After" runs after each test.
     * It's useful for cleaning up resources or resetting states.
     * Currently, this method doesn't perform any actions, but you can customize it as needed.
     */
    @After
    public void tearDown() {
        // No resources to clean up in this example, but this is where you would do so if needed
    }

    /**
     * This is a basic example test annotated with "@Test" to demonstrate how to use assertions in JUnit.
     * The assertEquals method checks if the expected value matches the actual value.
     */

    @Test 
    public void aTest(){
        final int expected = 2;
        final int actual = 1 + 1;
        // Use of assertEquals to verify that the expected value matches the actual value
        assertEquals(expected, actual);
    }

    /**
     * This test checks if the InvalidAnalystNameException is correctly thrown when registering with an invalid analyst name.
     * The expected exception is specified in the @Test annotation.
     */
    @Test(expected = InvalidAnalystNameException.class)
    public void anExceptionTest()
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        // Test registration with an invalid username
        // to test whether the appropriate exception is thrown.
        irms.registerAnalyst("aa", "Password1!");
    }

     /**
     * This is an example of a test that is designed to fail.
     * It shows how to include an error message to provide feedback when a test doesn't pass.
     */
    @Test
    public void aFailedTest() {
        // This test currently fails to demonstrate how JUnit reports errors
        final int expected = 2;
        final int actual = 1 + 2;
        // Uncomment the following line to observe a test failure.
        assertEquals("Some failure message", expected, actual);
    }

    // ADD YOUR TESTS HERE
    // This is the section where you will add your own tests.
    // Follow the examples above to create your tests.

        // ---------- Helpers (shared across tests) ----------
    private void register(String name, String pwd)
            throws DuplicateAnalystException, InvalidAnalystNameException, InvalidPasswordException {
        irms.registerAnalyst(name, pwd);
    }

    private void login(String name, String pwd)
            throws NoSuchAnalystException, IncorrectPasswordException {
        irms.authenticate(name, pwd);
    }

    private void makeSupervisor(String name) throws Exception {
        // IT channel; independent of authentication
        irms.requestSupervisorAccess(name, "1234");
    }

    // Quickly submit one seed incident as a user (auth first).
    private void submitAs(String name, String pwd, String id, int rating) throws Exception {
        login(name, pwd);
        irms.submitIncident(name, id, rating);
    }

    // ======================== registerAnalyst ========================
    /** EC1 Duplicate name -> DuplicateAnalystException */
    @Test(expected = DuplicateAnalystException.class)
    public void test_registerAnalyst_EC1_duplicateName() throws Exception {
        irms.registerAnalyst("analystA", "GoodPass1!");
    }

    /** EC2 Invalid name: length < 4 -> InvalidAnalystNameException */
    @Test(expected = InvalidAnalystNameException.class)
    public void test_registerAnalyst_EC2_nameTooShort() throws Exception {
        register("abc", "GoodPass1!");
    }

    /** EC3 Invalid name: illegal chars -> InvalidAnalystNameException */
    @Test(expected = InvalidAnalystNameException.class)
    public void test_registerAnalyst_EC3_nameIllegalChars() throws Exception {
        register("john_", "GoodPass1!");
    }

    /** EC4 Invalid password: length < 10 -> InvalidPasswordException */
    @Test(expected = InvalidPasswordException.class)
    public void test_registerAnalyst_EC4_pwdTooShort() throws Exception {
        register("john", "A1!aaaaa"); // 8 chars
    }

    /** EC6 Invalid password: length > 16 -> InvalidPasswordException */
    @Test(expected = InvalidPasswordException.class)
    public void test_registerAnalyst_EC6_pwdTooLong() throws Exception {
        register("john", "Abcdefghij12345!!"); // 17 chars
    }

    /** EC7 Invalid password: missing special -> InvalidPasswordException */
    @Test(expected = InvalidPasswordException.class)
    public void test_registerAnalyst_EC7_missingSpecial() throws Exception {
        register("john", "Abcdef1234");
    }

    /** EC8 Invalid password: missing digit -> InvalidPasswordException */
    @Test(expected = InvalidPasswordException.class)
    public void test_registerAnalyst_EC8_missingDigit() throws Exception {
        register("john", "Abcdef!@#$");
    }

    /** EC9 Invalid password: missing letter -> InvalidPasswordException */
    @Test(expected = InvalidPasswordException.class)
    public void test_registerAnalyst_EC9_missingLetter() throws Exception {
        register("john", "12345!@#$%");
    }

    /** EC10 Invalid password: letters only -> InvalidPasswordException */
    @Test(expected = InvalidPasswordException.class)
    public void test_registerAnalyst_EC10_lettersOnly() throws Exception {
        register("john", "Abcdefghij");
    }

    /** EC11 Invalid password: digits only -> InvalidPasswordException */
    @Test(expected = InvalidPasswordException.class)
    public void test_registerAnalyst_EC11_digitsOnly() throws Exception {
        register("john", "1234567890");
    }

    /** EC12 Invalid password: specials only -> InvalidPasswordException */
    @Test(expected = InvalidPasswordException.class)
    public void test_registerAnalyst_EC12_specialsOnly() throws Exception {
        register("john", "!@#$%^&*()"); // 10 specials
    }

    /** EC5 Valid registration -> success; role=MEMBER; auth=NOT_AUTHENTICATED */
    @Test
    public void test_registerAnalyst_EC5_success() throws Exception {
        try {
            register("john", "GoodPass1!"); // 10 chars, includes letter+digit+special
        } catch (Exception e) {
            fail("Valid registration should not throw, but threw: " + e);
        }
        assertTrue(irms.isRegistered("john"));
        assertFalse(irms.isAuthenticated("john"));
        assertEquals(IRMS.Role.MEMBER, irms.getAnalystRole("john"));
    }

    // ======================== authenticate ========================
    /** EC1 Unregistered name -> NoSuchAnalystException */
    @Test(expected = NoSuchAnalystException.class)
    public void test_authenticate_EC1_unregistered() throws Exception {
        irms.authenticate("ghost", "anything!");
    }

    /** EC2 Correct credentials -> status AUTHENTICATED, no exception */
    @Test
    public void test_authenticate_EC2_success() throws Exception {
        try {
            irms.authenticate("analystA", "Password1!");
        } catch (Exception e) {
            fail("authenticate with correct credentials should not throw, but threw: " + e);
        }
        assertTrue(irms.isAuthenticated("analystA"));
    }

    /** EC3 Wrong password -> set NOT_AUTHENTICATED + IncorrectPasswordException */
    @Test
    public void test_authenticate_EC3_wrongPassword_setsNotAuthAndThrows() throws Exception {
        try {
            irms.authenticate("analystA", "Wrong!!!!");
            fail("Expected IncorrectPasswordException");
        } catch (IncorrectPasswordException e) {
            assertFalse(irms.isAuthenticated("analystA"));
        }
    }

    // ======================== requestSupervisorAccess ========================
    /** EC1 Unregistered analyst -> NoSuchAnalystException */
    @Test(expected = NoSuchAnalystException.class)
    public void test_requestSupervisorAccess_EC1_unregistered() throws Exception {
        irms.requestSupervisorAccess("ghost", "1234");
    }

    /** EC2 Invalid badge ID -> InvalidBadgeIDException */
    @Test(expected = InvalidBadgeIDException.class)
    public void test_requestSupervisorAccess_EC2_invalidBadge() throws Exception {
        register("john", "GoodPass1!");
        irms.requestSupervisorAccess("john", "9999");
    }

    /** EC3 Already SUPERVISOR -> idempotent success (no state change) */
    @Test
    public void test_requestSupervisorAccess_EC3_alreadySupervisor() throws Exception {
        try {
            irms.requestSupervisorAccess("analystA", "1234");
            assertEquals(IRMS.Role.SUPERVISOR, irms.getAnalystRole("analystA"));
            // Call again with another valid badge
            irms.requestSupervisorAccess("analystA", "1235");
        } catch (Exception e) {
            fail("Idempotent SUPERVISOR request should not throw, but threw: " + e);
        }
        assertEquals(IRMS.Role.SUPERVISOR, irms.getAnalystRole("analystA"));
    }

    /** EC4 Promote MEMBER -> SUPERVISOR (success) */
    @Test
    public void test_requestSupervisorAccess_EC4_promoteSuccess() throws Exception {
        register("charlie", "GoodPass1!");
        try {
            irms.requestSupervisorAccess("charlie", "1234");
        } catch (Exception e) {
            fail("Valid badge should promote MEMBER to SUPERVISOR, but threw: " + e);
        }
        assertEquals(IRMS.Role.SUPERVISOR, irms.getAnalystRole("charlie"));
    }

    // ======================== submitIncident ========================
    /** EC1 Unregistered -> NoSuchAnalystException (inside isAuthenticated) */
    @Test(expected = NoSuchAnalystException.class)
    public void test_submitIncident_EC1_unregistered() throws Exception {
        irms.submitIncident("ghost", "I1", 5);
    }

    /** EC2 Not authenticated -> UnauthenticatedAnalystException */
    @Test(expected = UnauthenticatedAnalystException.class)
    public void test_submitIncident_EC2_notAuthenticated() throws Exception {
        irms.submitIncident("analystA", "I1", 5);
    }

    /** EC3 Duplicate incidentID -> DuplicateIncidentException */
    @Test
    public void test_submitIncident_EC3_duplicateID() throws Exception {
        // First submit (MEMBER + empty list -> success)
        submitAs("analystA", "Password1!", "DUP1", 5);
        try {
            irms.submitIncident("analystA", "DUP1", 7);
            fail("Expected DuplicateIncidentException");
        } catch (DuplicateIncidentException e) {
            assertFalse(irms.isSavedIncident("DUP1", 7));
        }
    }

    /** EC4 rating < 0 -> InvalidRatingException */
    @Test(expected = InvalidRatingException.class)
    public void test_submitIncident_EC4_ratingBelowZero() throws Exception {
        login("analystA", "Password1!");
        irms.submitIncident("analystA", "RLOW1", -1);
    }

    /** EC5 rating > 9 -> InvalidRatingException */
    @Test(expected = InvalidRatingException.class)
    public void test_submitIncident_EC5_ratingAboveNine() throws Exception {
        login("analystA", "Password1!");
        irms.submitIncident("analystA", "RHIGH1", 10);
    }

    /** EC6 Role=SUPERVISOR -> accept any incident */
    @Test
    public void test_submitIncident_EC6_supervisorAcceptsAny() throws Exception {
        try {
            makeSupervisor("analystA");
            login("analystA", "Password1!");
            irms.submitIncident("analystA", "S1", 0);
            irms.submitIncident("analystA", "S2", 9);
        } catch (Exception e) {
            fail("SUPERVISOR should accept any rating, but threw: " + e);
        }
        assertTrue(irms.isSavedIncident("S1", 0));
        assertTrue(irms.isSavedIncident("S2", 9));
    }

    /** EC7 MEMBER + empty list -> success */
    @Test
    public void test_submitIncident_EC7_memberEmptyListSuccess() throws Exception {
        login("analystA", "Password1!");
        try {
            irms.submitIncident("analystA", "M0", 0); // empty + valid
        } catch (Exception e) {
            fail("Empty list + MEMBER should accept any valid rating, but threw: " + e);
        }
        assertTrue(irms.isSavedIncident("M0", 0));
    }

    /** EC8 MEMBER + non-empty + rating > lowest -> success */
    @Test
    public void test_submitIncident_EC8_memberHigherThanLowestSuccess() throws Exception {
        login("analystA", "Password1!");
        irms.submitIncident("analystA", "M1", 3); // seed; lowest = 3
        try {
            irms.submitIncident("analystA", "M2", 4); // > lowest
            irms.submitIncident("analystA", "M3", 4);
        } catch (Exception e) {
            fail("rating > lowest should be accepted for MEMBER, but threw: " + e);
        }
        assertTrue(irms.isSavedIncident("M3", 4));
    }

    /** EC9 MEMBER + non-empty + rating ≤ lowest -> IncidentRejectException */
    @Test
    public void test_submitIncident_EC9_memberNotHigherReject() throws Exception {
        login("analystA", "Password1!");
        irms.submitIncident("analystA", "M1", 3); // seed; lowest = 3
        irms.submitIncident("analystA", "M2", 4); // > lowest
        try {
            irms.submitIncident("analystA", "M4", 3); // == lowest
            fail("Expected IncidentRejectException");
        } catch (IncidentRejectException e) {
            assertFalse(irms.isSavedIncident("M4", 3));
        }
    }

    // ======================== getIncident ========================
    /** EC1 Unregistered -> NoSuchAnalystException */
    @Test(expected = NoSuchAnalystException.class)
    public void test_getIncident_EC1_unregistered() throws Exception {
        irms.getIncident("ghost", 0);
    }

    /** EC2 index < 0 -> IndexOutOfBoundsException */
    @Test(expected = IndexOutOfBoundsException.class)
    public void test_getIncident_EC2_indexBelowZero() throws Exception {
        login("analystA", "Password1!");
        irms.submitIncident("analystA", "G2-A", 4);
        irms.submitIncident("analystA", "G2-B", 7);
        irms.getIncident("analystA", -1);
    }

    /** EC4 index ≥ N -> IndexOutOfBoundsException */
    @Test(expected = IndexOutOfBoundsException.class)
    public void test_getIncident_EC4_indexAtOrAboveN() throws Exception {
        login("analystA", "Password1!");
        irms.submitIncident("analystA", "G2-A", 4);
        irms.submitIncident("analystA", "G2-B", 7);
        irms.getIncident("analystA", 2);
    }

    /** EC3 valid index (0..N-1) -> returns <id, rating> */
    @Test
    public void test_getIncident_EC3_validIndexReturns() throws Exception {
        login("analystA", "Password1!");
        irms.submitIncident("analystA", "G1", 7);
        java.util.AbstractMap.SimpleEntry<String,Integer> e = null;
        try {
            e = irms.getIncident("analystA", 0);
        } catch (Exception ex) {
            fail("Valid index should return an incident, but threw: " + ex);
        }
        assertEquals("G1", e.getKey());
        assertEquals(Integer.valueOf(7), e.getValue());
    }

}

