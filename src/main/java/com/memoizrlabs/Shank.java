package com.memoizrlabs;

import com.memoizrlabs.functions.Func0;
import com.memoizrlabs.functions.Func1;
import com.memoizrlabs.functions.Func2;
import com.memoizrlabs.functions.Func3;
import com.memoizrlabs.functions.Func4;
import com.memoizrlabs.functions.Function;

import java.util.HashMap;
import java.util.Map;

import static com.memoizrlabs.Provider.createProvider;
import static com.memoizrlabs.poweroptional.Optional.optionOf;

/**
 * This class will cache items provided by factories, and provideSingleton them to the
 * user according to specified scope, naming and lifetime constraints.
 */
public final class Shank {

    static final Map<Class, Map<Provider, Object>> unscopedCache = new HashMap<>();
    static final Map<Class, Map<String, Function>> factoryRegister = new HashMap<>();
    static final Map<Scope, Map<Class, Map<String, Map<Provider, Object>>>> scopedCache = new HashMap<>();
    private static final String NO_NAME = "";

    private Shank() {
    }

    /**
     * @see Shank#registerFactory(Class, Func4)
     */
    public static <T> void registerFactory(Class<T> objectClass, Func0<T> factory) {
        registerNamedFactory(objectClass, NO_NAME, factory);
    }

    /**
     * @see Shank#registerFactory(Class, Func4)
     */
    public static <A, T> void registerFactory(Class<T> objectClass, Func1<A, T> factory) {
        registerNamedFactory(objectClass, NO_NAME, factory);
    }

    /**
     * @see Shank#registerFactory(Class, Func4)
     */
    public static <A, B, T> void registerFactory(Class<T> objectClass, Func2<A, B, T> factory) {
        registerNamedFactory(objectClass, NO_NAME, factory);
    }

    /**
     * @see Shank#registerFactory(Class, Func4)
     */
    public static <A, B, C, T> void registerFactory(Class<T> objectClass, Func3<A, B, C, T> factory) {
        registerNamedFactory(objectClass, NO_NAME, factory);
    }

    /**
     * Registers a factory for the specified class.
     *
     * @param objectClass is the class of the object that will be produced.
     * @param factory     is a factory method that will provideSingleton an instance of
     *                    the object.
     */
    public static <A, B, C, D, T> void registerFactory(Class<T> objectClass, Func4<A, B, C, D, T> factory) {
        registerNamedFactory(objectClass, NO_NAME, factory);
    }

    /**
     * @see Shank#provideNew(Class, Object, Object, Object, Object)
     */
    public static <T> T provideNew(Class<T> desiredObjectClass) {
        return createProvider(getFactory(desiredObjectClass, NO_NAME)).call();
    }

    /**
     * @see Shank#provideNew(Class, Object, Object, Object, Object)
     */
    public static <A, T> T provideNew(Class<T> desiredObjectClass, A a) {
        return createProvider(getFactory(desiredObjectClass, NO_NAME), a).call();
    }

    /**
     * @see Shank#provideNew(Class, Object, Object, Object, Object)
     */
    public static <A, B, T> T provideNew(Class<T> desiredObjectClass, A a, B b) {
        return  createProvider(getFactory(desiredObjectClass, NO_NAME), a, b).call();
    }

    /**
     * @see Shank#provideNew(Class, Object, Object, Object, Object)
     */
    public static <A, B, C, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c) {
        return createProvider(getFactory(desiredObjectClass, NO_NAME), a, b, c).call();
    }

    /**
     * Provides a new instance of the desired object. Throws a NoFactoryException if no
     * factory is registered for the class of the desired object. Throws an
     * IllegalArgumentException when no factory is registered with the same
     * number of arguments.
     *
     * @param desiredObjectClass is the class of the desired object.
     * @param a                  is the first parameter to be passed to the registered factory.
     * @param b                  is the second parameter to be passed to the registered factory.
     * @param c                  is the third parameter to be passed to the registered factory.
     * @param d                  is the fourth parameter to be passed to the registered factory.
     * @return an instance of the desired object as provideSingletond by the registered factory.
     */
    public static <A, B, C, D, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c, D d) {
        return createProvider(getFactory(desiredObjectClass, NO_NAME), a, b, c, d).call();
    }

    /**
     * @see Shank#provideSingleton(Class, Object, Object, Object, Object)
     */
    public static <T> T provideSingleton(Class<T> desiredObjectClass) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass)));
    }

    /**
     * @see Shank#provideSingleton(Class, Object, Object, Object, Object)
     */
    public static <A, T> T provideSingleton(Class<T> desiredObjectClass, A a) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a));
    }

    /**
     * @see Shank#provideSingleton(Class, Object, Object, Object, Object)
     */
    public static <A, B, T> T provideSingleton(Class<T> desiredObjectClass, A a, B b) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b));
    }

    /**
     * @see Shank#provideSingleton(Class, Object, Object, Object, Object)
     */
    public static <A, B, C, T> T provideSingleton(Class<T> desiredObjectClass, A a, B b, C c) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b, c));
    }

    /**
     * Provides the desired object. The object  returned will be created the
     * first time this method is called, and all subsequent calls will return a
     * cached instance of the same  object, unless the same method is called with
     * different factory object parameters. Throws a NoFactoryException if no
     * factory is registered for the class of the desired object. Throws an
     * IllegalArgumentException when no factory is registered with the same
     * number of arguments.
     *
     * @param desiredObjectClass is the class of the desired object.
     * @param a                  is the first parameter to be passed to the corresponding factory.
     * @param b                  is the second parameter to be passed to the corresponding factory.
     * @param c                  is the third parameter to be passed to the corresponding factory.
     * @param d                  is the fourth parameter to be passed to the corresponding factory.
     * @return an instance of the desired object as provideSingletond by the corresponding factory.
     */
    public static <A, B, C, D, T> T provideSingleton(Class<T> desiredObjectClass, A a, B b, C c, D d) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b, c, d));
    }

    /**
     * @see Shank#registerNamedFactory(Class, String, Func4)
     */
    public static <T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func0<T> factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    /**
     * @see Shank#registerNamedFactory(Class, String, Func4)
     */
    public static <A, T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func1<A, T> factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    /**
     * @see Shank#registerNamedFactory(Class, String, Func4)
     */
    public static <A, B, T> void registerNamedFactory(Class<T> objectClass, String factoryName,
            Func2<A, B, T> factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    /**
     * @see Shank#registerNamedFactory(Class, String, Func4)
     */
    public static <A, B, C, T> void registerNamedFactory(Class<T> objectClass, String factoryName,
            Func3<A, B, C, T> factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    /**
     * Registers a factory for the specified class of object using the specified
     * string identifier.
     *
     * @param objectClass is the class of the object that will be produced.
     * @param factoryName is the string identifier associated to this factory.
     * @param factory     is a factory method that will provideSingleton an instance of
     *                    the object.
     */
    public static <A, B, C, D, T> void registerNamedFactory(Class<T> objectClass, String factoryName,
            Func4<A, B, C, D, T> factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    /**
     * Creates a builder for a named scope.
     *
     * @param name the String associated to a scope.
     * @return a NamedScopedCache builder.
     */
    public static NamedScopedCache named(String name) {
        return new NamedScopedCache(name);
    }

    /**
     * Creates a builder for a clearable scope.
     *
     * @param scope the scope
     * @return a ScopedCache builder.
     */
    public static ScopedCache with(Scope scope) {
        return new ScopedCache(scope);
    }

    /**
     * Clears the entire cache.
     */
    public static void clearAll() {
        unscopedCache.clear();
        scopedCache.clear();
    }
    static void clearNamedScope(Scope scope) {
        scopedCache.remove(scope);
    }

    @SuppressWarnings("unchecked")
    static <T> T providerHelper(Class<T> desiredObjectClass, Provider<T> provider) {
        try {
            return (T)
                    optionOf(unscopedCache.get(desiredObjectClass))
                            .map(providerMap -> optionOf(providerMap.get(provider)).orElseGet(() -> {
                                                T desiredObject = provider.call();
                                                providerMap.put(provider, desiredObject);
                                                return desiredObject;
                                            }))
                    .orElseGet(() -> {
                        T desiredObject = provider.call();
                        unscopedCache.put(desiredObjectClass, new HashMap<Provider, Object>(){{put(provider, desiredObject);}});
                        return desiredObject;
                    });
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(getErrorMessage(desiredObjectClass, provider));
        }
    }

    static <T> String getErrorMessage(Class<T> desiredObjectClass, Provider provider) {
        return "No factory with " + provider.argumentsToString() + " arguments registered for " + desiredObjectClass
                .getSimpleName();
    }

    private static <T> void registerNamedFactoryRaw(Class<T> objectClass, String factoryName, Function<T> factory) {
        optionOf(factoryRegister.get(objectClass))
                .doIfPresent(factoryMap -> factoryMap.put(factoryName, factory))
                .doIfEmpty(() -> factoryRegister.put(objectClass, new HashMap<String, Function>() {{
                    put(factoryName, factory);
                }}));
    }

    static <T> Function<T> getFactory(Class<T> desiredObjectClass) {
        return getFactory(desiredObjectClass, NO_NAME);
    }

    @SuppressWarnings("unchecked")
    static <T> Function<T> getFactory(Class<T> desiredObjectClass, String name) {
        return optionOf(factoryRegister.get(desiredObjectClass))
                .doIfEmpty(() -> {
                    throw new NoFactoryException("There is no factory for " + desiredObjectClass.getCanonicalName());
                })
                .map(namedFactoryMap -> namedFactoryMap.get(name))
                .orElseThrow(() -> new NoFactoryException(
                        "There is no factory for " + desiredObjectClass.getCanonicalName() + " with name " + name));
    }

    /**
     * Clears all the registered factories.
     */
    static void clearFactories() {
        factoryRegister.clear();
    }

    /**
     * Clears the scope associated to an unscoped class.
     *
     * @param objectClass is the class of the object.
     */
    static <T> void clearUnscoped(Class<T> objectClass) {
        unscopedCache.remove(objectClass);
    }
}
