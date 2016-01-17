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
 * This class will cache items provided by factories, and provide them to the
 * user according to specified scope, naming and lifetime constraints.
 */
public final class Shank {

    static final Map<Class, Object> unscopedCache = new HashMap<>();
    static final Map<Class, Map<String, Function>> factoryRegister = new HashMap<>();
    static final Map<Scope, Map<Class, Map<String, Object>>> scopedCache = new HashMap<>();
    private static final String NO_NAME = "";

    private Shank() {
    }

    /**
     * @see Shank#registerFactory(Class, Func4)
     */
    public static <T> void registerFactory(Class<T> objectClass, Func0<T> factory) {
        registerNamedFactory(objectClass, "", factory);
    }

    /**
     * @see Shank#registerFactory(Class, Func4)
     */
    public static <A, T> void registerFactory(Class<T> objectClass, Func1<A, T> factory) {
        registerNamedFactory(objectClass, "", factory);
    }

    /**
     * @see Shank#registerFactory(Class, Func4)
     */
    public static <A, B, T> void registerFactory(Class<T> objectClass, Func2<A, B, T> factory) {
        registerNamedFactory(objectClass, "", factory);
    }

    /**
     * @see Shank#registerFactory(Class, Func4)
     */
    public static <A, B, C, T> void registerFactory(Class<T> objectClass, Func3<A, B, C, T> factory) {
        registerNamedFactory(objectClass, "", factory);
    }

    /**
     * Registers a factory for the specified class of object.
     *
     * @param objectClass is the class of the object that will be produced.
     * @param factory     is a factory method that will provide an instance of
     *                    the object.
     */
    public static <A, B, C, D, T> void registerFactory(Class<T> objectClass, Func4<A, B, C, D, T> factory) {
        registerNamedFactory(objectClass, "", factory);
    }

    /**
     * @see Shank#provideNew(Class, Object, Object, Object, Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> T provideNew(Class<T> desiredObjectClass) {
        return (T) createProvider(getFactory(desiredObjectClass, "")).call();
    }

    /**
     * @see Shank#provideNew(Class, Object, Object, Object, Object)
     */
    @SuppressWarnings("unchecked")
    public static <A, T> T provideNew(Class<T> desiredObjectClass, A a) {
        return (T) createProvider(getFactory(desiredObjectClass, ""), a).call();
    }

    /**
     * @see Shank#provideNew(Class, Object, Object, Object, Object)
     */
    @SuppressWarnings("unchecked")
    public static <A, B, T> T provideNew(Class<T> desiredObjectClass, A a, B b) {
        return (T) createProvider(getFactory(desiredObjectClass, ""), a, b).call();
    }

    /**
     * @see Shank#provideNew(Class, Object, Object, Object, Object)
     */
    @SuppressWarnings("unchecked")
    public static <A, B, C, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c) {
        return (T) createProvider(getFactory(desiredObjectClass, ""), a, b, c).call();
    }

    /**
     * Provides the desired object. The object  returned will be created the
     * first time this method is called, and all subsequent calls will return a
     * cached instance of the same  object. Throws a NoFactoryException if no
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
    @SuppressWarnings("unchecked")
    public static <A, B, C, D, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c, D d) {
        return (T) createProvider(getFactory(desiredObjectClass, ""), a, b, c, d).call();
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
     * cached instance of the same  object. Throws a NoFactoryException if no
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
    public static <A, B, C, D, T> T provideSingleton(Class<T> desiredObjectClass, A a, B b, C c, D d) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b, c, d));
    }

    /**
     * @see Shank#registerNamedFactory(Class, String, Func4)
     */
    public static <T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func0 factory) {
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
     * @param factory     is a factory method that will provide an instance of
     *                    the object.
     */
    public static <A, B, C, D, T> void registerNamedFactory(Class<T> objectClass, String factoryName,
            Func4<A, B, C, D, T> factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    /**
     * Clears the entire cache.
     */
    public static void clearAll() {
        unscopedCache.clear();
        scopedCache.clear();
    }

    /**
     * Clears the scope associated to an unscoped class.
     *
     * @param objectClass is the class of the object.
     */
    public static <T> void clearUnscoped(Class<T> objectClass) {
        unscopedCache.remove(objectClass);
    }

    /**
     * Create a builder to associate a naming scope to a class.
     *
     * @param name the String associated to a scope.
     * @return a NamedScopedCache builder.
     */
    public static NamedScopedCache named(String name) {
        return new NamedScopedCache(name);
    }

    /**
     * Create a builder to associate a scope to a class.
     *
     * @param scope the scope
     * @return a ScopedCache builder.
     */
    public static ScopedCache with(Scope scope) {
        return new ScopedCache(scope);
    }

    static void clearNamedScope(Scope scope) {
        scopedCache.remove(scope);
    }

    @SuppressWarnings("unchecked")
    static <T> T providerHelper(Class<T> desiredObjectClass, Provider provider) {
        try {
            return (T) optionOf(unscopedCache.get(desiredObjectClass))
                    .orElseGet(() -> {
                        T desiredObject = (T) provider.call();
                        unscopedCache.put(desiredObjectClass, desiredObject);
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

    private static <T> void registerNamedFactoryRaw(Class<T> objectClass, String factoryName, Function factory) {
        optionOf(factoryRegister.get(objectClass))
                .doIfPresent(factoryMap -> factoryMap.put(factoryName, factory))
                .doIfEmpty(() -> factoryRegister.put(objectClass, new HashMap<String, Function>() {{
                    put(factoryName, factory);
                }}));
    }

    static <T> Function getFactory(Class<T> desiredObjectClass) {
        return getFactory(desiredObjectClass, NO_NAME);
    }

    static <T> Function getFactory(Class<T> desiredObjectClass, String name) {
        return optionOf(factoryRegister.get(desiredObjectClass))
                .doIfEmpty(() -> {
                    throw new NoFactoryException("There is no factory for " + desiredObjectClass.getCanonicalName());
                })
                .map(namedFactoryMap -> namedFactoryMap.get(name))
                .orElseThrow(() -> new NoFactoryException(
                        "There is no factory for " + desiredObjectClass.getCanonicalName() + " with name " + name));
    }

    static void clearFactories() {
        factoryRegister.clear();
    }
}
