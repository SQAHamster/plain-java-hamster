package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * Parameter resolver which can resolve TestUtils parameters
 * Uses the game value from the HamsterTest annotation
 */
public class HamsterGameResolver implements ParameterResolver {

    /**
     * Determine if this resolver supports resolution of an argument for the
     * {@link Parameter} in the supplied {@link ParameterContext} for the supplied
     * {@link ExtensionContext}. <br>
     * This extension supports TestUtils parameters
     *
     * @param parameterContext the context for the parameter for which an argument should
     * be resolved; != null
     * @param extensionContext the extension context for the Executable
     * about to be invoked; != null
     * @return true if the parameter can be resolved
     */
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(TestUtils.class);
    }

    /**
     * Resolves a TestUtils parameter
     *
     * <p>This method is only called by the framework if {@link #supportsParameter}
     * previously returned {@code true} for the same {@link ParameterContext}
     * and {@link ExtensionContext}.
     *
     * @param parameterContext the context for the parameter for which an argument should
     * be resolved; != null
     * @param extensionContext the extension context for the Executable
     * about to be invoked; != null
     * @return the TestUtils with a new instance of the specified SimpleHamsterGame
     */
    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        if (!this.supportsParameter(parameterContext, extensionContext)) {
            throw new ParameterResolutionException("Unsupported parameter type");
        }
        final Class<? extends SimpleHamsterGame> simpleHamsterGameClass = HamsterGameResolver.getHamsterGameClass(parameterContext)
                .orElseThrow(() -> new ParameterResolutionException("The given class is no SimpleHamsterGame or doesn't exist"));

        try {
            final SimpleHamsterGame hamsterGame = simpleHamsterGameClass.getDeclaredConstructor().newInstance();
            return new TestUtils(hamsterGame);
        } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ParameterResolutionException("The simple hamster game to instantiate has public constructor without parameters", e);
        }
    }

    /**
     * Gets the Class of the SimpleHamsterGame specified via the HamsterTest annotation
     * @param parameterContext the context for the parameter which an argument should be resolved, != null
     * @return an empty optional if the class is not found or does not extend SimpleHamsterGame,
     *         otherwise an optional with the Class of the specified SimpleHamsterGame
     */
    private static Optional<Class<? extends SimpleHamsterGame>> getHamsterGameClass(final ParameterContext parameterContext) {
        try {
            final HamsterTest hamsterTest = parameterContext.getDeclaringExecutable().getDeclaringClass()
                    .getAnnotation(HamsterTest.class);
            if (hamsterTest == null) {
                return Optional.empty();
            }
            final String hamsterClassName = hamsterTest.game();
            final Class<?> simpleHamsterGame = Class.forName(hamsterClassName);
            if (!SimpleHamsterGame.class.isAssignableFrom(simpleHamsterGame)) {
                return Optional.empty();
            }
            return Optional.of((Class<? extends SimpleHamsterGame>) simpleHamsterGame);
        } catch (final ClassNotFoundException classEx) {
            return Optional.empty();
        }
    }
}
