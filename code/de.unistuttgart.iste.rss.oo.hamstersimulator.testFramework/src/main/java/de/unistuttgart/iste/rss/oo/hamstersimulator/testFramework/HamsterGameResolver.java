package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class HamsterGameResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        //return parameterContext.getParameter().getType().equals(TestUtils.class) && HamsterGameResolver.getHamsterGameClass(parameterContext) != null;
        return false;
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        /*if (!this.supportsParameter(parameterContext, extensionContext)) {
            throw new ParameterResolutionException("Unsupported parameter type");
        }
        final Class<? extends SimpleHamsterGame> simpleHamsterGameClass = HamsterGameResolver.getHamsterGameClass(parameterContext);
        if (simpleHamsterGameClass == null) {
            throw new ParameterResolutionException("The given class is no SimpleHamsterGame or doesn't exist");
        }
        try {
            final SimpleHamsterGame hamsterGame = simpleHamsterGameClass.getDeclaredConstructor().newInstance();
            return new TestUtils(hamsterGame);
        } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ParameterResolutionException("The simple hamster game to instantiate has public constructor without parameters", e);
        }*/
        return null;
    }

    /*private static Class<? extends SimpleHamsterGame> getHamsterGameClass(final ParameterContext parameterContext) {
        try {
            final Annotation hamsterTest = parameterContext.getDeclaringExecutable().getDeclaringClass().getAnnotation(HamsterTest.class);
            if (hamsterTest == null) {
                return null;
            }
            final String hamsterClassName = ((HamsterTest) hamsterTest).game();
            final Class<?> simpleHamsterGame = Class.forName(hamsterClassName);
            if (!SimpleHamsterGame.class.isAssignableFrom(simpleHamsterGame)) {
                return null;
            }
            return ((Class<? extends SimpleHamsterGame>) simpleHamsterGame);
        } catch (final ClassNotFoundException classEx) {
            return null;
        }
    }*/
}
