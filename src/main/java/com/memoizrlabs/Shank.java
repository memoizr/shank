package com.memoizrlabs;

import rx.Observable;
import rx.functions.Func0;

import java.util.HashMap;
import java.util.Map;

/**
 * This class will cache items provided by factories, and provide them to the
 * user according to specified scope, naming and lifetime constraints.
 */
public class Shank {

    private static final Map<Class, Object> unscopedCache = new HashMap<>();
    private static final Map<Class, Map<String, Object>> unscopedNamedCache = new HashMap<>();

    private static final Map<Class, Func0> factoryRegister = new HashMap<>();
    private static final Map<Class, Map<String, Func0>> namedFactoryRegister = new HashMap<>();

    private static final Map<Class, Map<Class, Object>> scopedCache = new HashMap<>();

    /**
     * Provides the desired object. The object  returned will be created the
     * first time this method is called, and all subsequent calls will return a
     * cached instance of the same  object. Throws a NoFactoryException if no
     * factory is registered for the class of the desired object.
     *
     * @param desiredObjectClass is the class of the desired object.
     * @return an instance of the desired object as provided by the registered
     * factory.
     */
    @SuppressWarnings("unchecked")
    public static <T> T provide(Class<T> desiredObjectClass) {

        T desiredObject = (T) unscopedCache.get(desiredObjectClass);
        if (desiredObject == null) {
            Func0 objectFactory = getFactory(desiredObjectClass);
            desiredObject = (T) objectFactory.call();
            unscopedCache.put(desiredObjectClass, desiredObject);
        }
        return desiredObject;
    }

    private static <T> Func0 getFactory(Class<T> desiredObjectClass) {
        final Func0 objectFactory = factoryRegister.get(desiredObjectClass);
        if (objectFactory == null) {
            throw new NoFactoryException("There is no factory for " + desiredObjectClass.getCanonicalName());
        }
        return objectFactory;
    }

    /**
     * Provides the desired object associated to the specified string
     * identifier. The object  returned will be created the first time this
     * method is called, and all subsequent calls will return a cached instance
     * of the same  object. Throws a NoFactoryException if no factory is
     * registered for the class of the desired object with the specified string
     * identifier.
     *
     * @param desiredObjectClass is the class of the desired object.
     * @param name               is the string identifier associated to a
     *                           particular factory.
     * @return an instance of the desired object as provided by the registered
     * factory.
     */
    @SuppressWarnings("unchecked")
    public static <T> T provideNamed(Class<T> desiredObjectClass, String name) {
        final Map<String, Object> desiredObjectMap = unscopedNamedCache.get(desiredObjectClass);
        final Func0 objectFactory = getNamedFactory(desiredObjectClass, name);

        T desiredObject;

        if (desiredObjectMap == null) {
            final Map<String, Object> map = new HashMap<>();
            desiredObject = (T) objectFactory.call();
            map.put(name, desiredObject);
            unscopedNamedCache.put(desiredObjectClass, map);
        } else {
            desiredObject = (T) desiredObjectMap.get(name);
            if (desiredObject == null) {
                desiredObject = (T) objectFactory.call();
            }
        }
        return desiredObject;
    }

    private static <T> Func0 getNamedFactory(Class<T> desiredObjectClass, String name) {
        final Map<String, Func0> namedFactoryMap = namedFactoryRegister.get(desiredObjectClass);

        if (namedFactoryMap == null) {
            throw new NoFactoryException("There is no factory for " + desiredObjectClass.getCanonicalName());
        }

        final Func0 namedFactory = namedFactoryMap.get(name);

        if (namedFactory == null) {
            throw new NoFactoryException("There is no factory for " + desiredObjectClass.getCanonicalName() + " with name " + name);
        }

        return namedFactory;
    }

    /**
     * Registers a factory for the specified class of object.
     *
     * @param objectClass is the class of the object that will be produced.
     * @param factory     is a factory method that will provide an instance of
     *                    the object.
     */
    public static <T> void registerFactory(Class<T> objectClass, Func0<T> factory) {
        factoryRegister.put(objectClass, factory);
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
    public static <T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func0<T> factory) {
        final Map<String, Func0> factoryMap = namedFactoryRegister.get(objectClass);
        if (factoryMap == null) {
            final Map<String, Func0> map = new HashMap<>();
            map.put(factoryName, factory);
            namedFactoryRegister.put(objectClass, map);
        } else {
            factoryMap.put(factoryName, factory);
        }
    }

    /**
     * Clears the entire cache.
     */
    public static void clearAll() {
        unscopedCache.clear();
        scopedCache.clear();
    }

    /**
     * Clears the scope associated to a scoped class.
     *
     * @param objectClass is the class associated to a scope.
     */
    private static void clearScope(Class objectClass) {
        scopedCache.remove(objectClass);
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
     * Create a builder to associate a scope to a class.
     *
     * @param objectClass the class associated to a scope.
     * @return a ScopedCache builder.
     */
    public static <T> ScopedCache withScope(Class<T> objectClass) {
        return new ScopedCache(objectClass);
    }

    /**
     * Create a builder to associate a scope to a class, lifetime bound.
     *
     * @param objectClass         the class associated to a scope.
     * @param whenLifetimeExpires an observable which is expected to fire when
     *                            the lifetime of the object the cache is bound
     *                            to expires.
     * @return a ScopedCache builder.
     */
    public static <T> ScopedCache withBoundScope(Class<T> objectClass,
                                                 Observable<Object> whenLifetimeExpires) {
        return new ScopedCache(objectClass, whenLifetimeExpires);
    }

    static class NoFactoryException extends RuntimeException {
        public NoFactoryException(String message) {
            super(message);
        }
    }

    public static class ScopedCache {

        private final Class scope;
        private final Observable<Object> whenLifetimeEnds;

        ScopedCache(Class scope) {
            this(scope, null);
        }

        ScopedCache(Class scope, Observable<Object> whenLifetimeEnds) {
            this.scope = scope;
            this.whenLifetimeEnds = whenLifetimeEnds;
            if (this.whenLifetimeEnds != null) {
                this.whenLifetimeEnds.take(1).subscribe(s -> {
                    clearScope(ScopedCache.this.scope);
                });
            }
        }

        /**
         * Provides the desired object. Throws a NoFactoryException if no
         * factory is registered for the class of the desired object. The object
         * returned will be created the first time this method is called, and
         * all subsequent calls will return a cached instance of the same
         * object. When the lifetime observable emits an item, the object will
         * be removed from cache, and the next time this method is called a new
         * instance will be returned, which will also be cached unti the
         * observable fires again.
         *
         * @param desiredObjectClass is the class of the desired object.
         * @return an instance of the desired object as provided by the
         * registered factory.
         */
        public <V> V provide(Class<V> desiredObjectClass) {
            final Map<Class, Object> currentScopeMap = scopedCache.get(scope);
            final Func0 objectFactory = getFactory(desiredObjectClass);

            if (currentScopeMap == null) {
                return createObjectAndScope(desiredObjectClass, objectFactory);
            } else {
                return getObjectOrCreateIfNull(desiredObjectClass, currentScopeMap, objectFactory);
            }
        }

        @SuppressWarnings("unchecked")
        private <V> V createObjectAndScope(Class<V> desiredObjectClass, Func0 objectFactory) {
            final Map<Class, Object> resultMap = new HashMap<>();
            final Object desiredObject = objectFactory.call();
            resultMap.put(desiredObjectClass, desiredObject);
            scopedCache.put(scope, resultMap);
            return (V) desiredObject;
        }

        @SuppressWarnings("unchecked")
        private <V> V getObjectOrCreateIfNull(Class<V> desiredObjectClass, Map<Class, Object> currentScopeMap, Func0 objectFactory) {
            Object desiredObject = currentScopeMap.get(desiredObjectClass);
            if (desiredObject == null) {
                desiredObject = objectFactory.call();
                currentScopeMap.put(desiredObjectClass, desiredObject);
            }
            return (V) desiredObject;
        }
    }
}
