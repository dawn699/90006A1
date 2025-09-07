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

}
