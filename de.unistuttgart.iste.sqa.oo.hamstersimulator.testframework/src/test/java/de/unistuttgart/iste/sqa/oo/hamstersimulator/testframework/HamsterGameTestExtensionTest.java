package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests the extension functionality
 */
@ExtendWith(HamsterGameResolver.class)
@HamsterTest(game = "de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.TestSimpleHamsterGame")
public class HamsterGameTestExtensionTest extends HamsterGameTestExtensionTestBase {
    /**
     * Tests that the BeforeEach method in the super class works as expected
     */
    @Test
    public void testTestEnvironmentPresent() {
        assertNotNull(this.testEnvironment);
    }
}
