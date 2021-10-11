package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework;

import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for GamsterGameTestExtensionTest
 */
public class HamsterGameTestExtensionTestBase {

    /**
     * The current test environment
     * The SimpleHamsterGame used to initialize is set in the extending class
     */
    protected HamsterGameTestEnvironment testEnvironment;

    /**
     * Sets the testEnvironment
     * @param testEnvironment the HamsterGameTestEnvironment which is testEnvironment is set to
     */
    @BeforeEach
    public void setup(final HamsterGameTestEnvironment testEnvironment) {
        this.testEnvironment = testEnvironment;
    }
}
