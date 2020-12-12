package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.RecordingHamsterGameTestEnvironment;

/**
 * Parameter resolver which can resolve HamsterGameTestEnvironment (and supported subclasses) parameters.
 * Uses the game value from the HamsterTest annotation to find out which SimpleHamsterGame
 * is instantiated
 * If the HamsterTest annotation is used, this should be added as an extension via ExtendWith <br>
 * Currently, the following types are supported:
 * <ul>
 *     <li>HamsterGameTestEnvironment</li>
 *     <li>RecordingHamsterGameTestEnvironment</li>
 * </ul>
 * If the TestClass has the HamsterTest annotation, that one is used, otherwise the annotation
 * of the class with declares the method which gets the environment injected is used
 * @see org.junit.jupiter.api.extension.ExtendWith
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
        final Class<?> type = parameterContext.getParameter().getType();
        return type.equals(HamsterGameTestEnvironment.class)
                || type.equals(RecordingHamsterGameTestEnvironment.class);
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

        final SimpleHamsterGame game = createSimpleHamsterGame(parameterContext, extensionContext);
        final Class<?> type = parameterContext.getParameter().getType();
        return resolveTestEnvironment(game, type);
    }

    /**
     * Creates a new instance of the SimpleHamsterGame specified with the HamsterTest annotation.
     * @param parameterContext he context for the parameter for which an argument should
     *        be resolved, used to get the annotation
     * @param extensionContext the extension context for the Executable
     *        about to be invoked; != null
     * @return the created SimpleHamsterGame
     * @throws ParameterResolutionException if it is not possible to create an instance of the specified SimpleHamsterGame
     */
    private SimpleHamsterGame createSimpleHamsterGame(final ParameterContext parameterContext,
                                                      final ExtensionContext extensionContext) throws ParameterResolutionException {
        final Class<? extends SimpleHamsterGame> simpleHamsterGameClass
                = HamsterGameResolver.getHamsterGameClass(parameterContext, extensionContext)
                .orElseThrow(() -> new ParameterResolutionException("The given class is no SimpleHamsterGame or doesn't exist"));
        try {
            return simpleHamsterGameClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ParameterResolutionException("The simple hamster game to instantiate has public constructor without parameters", e);
        }
    }

    /**
     * Creates a new HamsterGameTestEnvironment based on the specified type with the provided game.
     * @param game the SimpleHamsterGame the test environment is based of
     * @param type the type of test environment to create
     * @return the HamsterGameTestEnvironment based on game
     */
    private HamsterGameTestEnvironment resolveTestEnvironment(final SimpleHamsterGame game, final Class<?> type) {
        if (type.equals(HamsterGameTestEnvironment.class)) {
            return new HamsterGameTestEnvironment(game);
        } else if (type.equals(RecordingHamsterGameTestEnvironment.class)) {
            return new RecordingHamsterGameTestEnvironment(game);
        } else {
            throw new IllegalStateException("cannot resolve type: " + type.getName());
        }
    }

    /**
     * Gets the Class of the SimpleHamsterGame specified via the HamsterTest annotation.
     * @param parameterContext the parameter context for the parameter which an argument should be resolved, != null
     * @param extensionContext the extension context for the Executable about to be invoked; != null
     * @return an empty optional if the class is not found or does not extend SimpleHamsterGame,
     *         otherwise an optional with the Class of the specified SimpleHamsterGame
     */
    @SuppressWarnings("unchecked")
    private static Optional<Class<? extends SimpleHamsterGame>> getHamsterGameClass(final ParameterContext parameterContext,
                                                                                    final ExtensionContext extensionContext) {
        try {
            Optional<HamsterTest> hamsterTest = getHamsterTestAnnotation(parameterContext, extensionContext);
            if (hamsterTest.isPresent()) {
                final String hamsterClassName = hamsterTest.get().game();
                final Class<?> declaredGameClass = Class.forName(hamsterClassName);
                if (!SimpleHamsterGame.class.isAssignableFrom(declaredGameClass)) {
                    return Optional.empty();
                }
                return Optional.of((Class<? extends SimpleHamsterGame>) declaredGameClass);
            } else {
                return Optional.empty();
            }
        } catch (final ClassNotFoundException classEx) {
            return Optional.empty();
        }
    }

    /**
     * Gets the HamsterTest annotation based on on the parameterContext and the extensionContext
     * If the test class has the annotation, this one is returned, otherwise the one of the declaring class
     * is returned. If none is present, an empty Optional is returned
     * @param parameterContext the parameter context for the parameter which an argument should be resolved, != null
     * @param extensionContext the extension context for the Executable about to be invoked; != null
     * @return the HamsterTest annotation if possible, otherwise an empty Optional
     */
    private static Optional<HamsterTest> getHamsterTestAnnotation(final ParameterContext parameterContext,
                                                                  final ExtensionContext extensionContext) {
        final Optional<HamsterTest> annotationFromTestClass = extensionContext.getTestClass()
                .map(testClass -> testClass.getAnnotation(HamsterTest.class));
        if (annotationFromTestClass.isPresent()) {
            return annotationFromTestClass;
        } else {
            return Optional.ofNullable(parameterContext.getDeclaringExecutable().getDeclaringClass()
                    .getAnnotation(HamsterTest.class));
        }
    }
}
