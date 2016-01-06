package com.memoizrlabs;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Function;

import static com.memoizrlabs.Provider.createProvider;

/**
 * This class will cache items provided by factories, and provide them to the
 * user according to specified scope, naming and lifetime constraints.
 */
public final class Shank {

    private static final Map<Class, Object> unscopedCache = new HashMap<>();
    private static final Map<Class, Map<String, Object>> unscopedNamedCache = new HashMap<>();

    private static final Map<Class, Function> factoryRegister = new HashMap<>();
    private static final Map<Class, Map<String, Function>> namedFactoryRegister = new HashMap<>();

    private static final Map<Class, Map<Class, Object>> scopedCache = new HashMap<>();
    private static final Map<Class, Map<Class, Map<String, Object>>> scopedNamedCache = new HashMap<>();

    private Shank() {
    }

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
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass)));
    }

    public static <A, T> T provide(Class<T> desiredObjectClass, A a) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a));
    }

    public static <A, B, T> T provide(Class<T> desiredObjectClass, A a, B b) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b));
    }

    public static <A, B, C, T> T provide(Class<T> desiredObjectClass, A a, B b, C c) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b, c));
    }

    public static <A, B, C, D, T> T provide(Class<T> desiredObjectClass, A a, B b, C c, D d) {
        return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b, c, d));
    }

    private static <T> T providerHelper(Class<T> desiredObjectClass, Provider provider) {
        T desiredObject = (T) unscopedCache.get(desiredObjectClass);

        if (desiredObject == null) {
            try {
                desiredObject = (T) provider.call();
                unscopedCache.put(desiredObjectClass, desiredObject);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(getErrorMessage(desiredObjectClass, provider));
            }
        }
        return desiredObject;
    }

    private static <T> String getErrorMessage(Class<T> desiredObjectClass, Provider provider) {
        return "No factory with " + provider.argumentsToString() + " arguments registered for " + desiredObjectClass
                .getSimpleName();
    }

    private static <T> Function getFactory(Class<T> desiredObjectClass) {
        final Function objectFactory = factoryRegister.get(desiredObjectClass);

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
        return namedProviderHelper(desiredObjectClass, name, createProvider(getNamedFactory(desiredObjectClass, name)));
    }

    public static <A, T> T provideNamed(Class<T> desiredObjectClass, String name, A a) {
        return namedProviderHelper(desiredObjectClass, name,
                createProvider(getNamedFactory(desiredObjectClass, name), a));
    }

    public static <A, B, T> T provideNamed(Class<T> desiredObjectClass, String name, A a, B b) {
        return namedProviderHelper(desiredObjectClass, name,
                createProvider(getNamedFactory(desiredObjectClass, name), a, b));
    }

    public static <A, B, C, T> T provideNamed(Class<T> desiredObjectClass, String name, A a, B b, C c) {
        return namedProviderHelper(desiredObjectClass, name,
                createProvider(getNamedFactory(desiredObjectClass, name), a, b, c));
    }

    public static <A, B, C, D, T> T provideNamed(Class<T> desiredObjectClass, String name, A a, B b, C c, D d) {
        return namedProviderHelper(desiredObjectClass, name,
                createProvider(getNamedFactory(desiredObjectClass, name), a, b, c, d));
    }

    @SuppressWarnings("unchecked")
    private static <T> T namedProviderHelper(Class<T> desiredObjectClass, String name, Provider provider) {
        final Map<String, Object> desiredObjectMap = unscopedNamedCache.get(desiredObjectClass);

        T desiredObject;
        try {
            if (desiredObjectMap == null) {
                final Map<String, Object> map = new HashMap<>();
                desiredObject = (T) provider.call();
                map.put(name, desiredObject);
                unscopedNamedCache.put(desiredObjectClass, map);
            } else {
                desiredObject = (T) desiredObjectMap.get(name);
                if (desiredObject == null) {
                    desiredObject = (T) provider.call();
                }
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(getErrorMessage(desiredObjectClass, provider));
        }
        return desiredObject;
    }

    private static <T> Function getNamedFactory(Class<T> desiredObjectClass, String name) {
        final Map<String, Function> namedFactoryMap = namedFactoryRegister.get(desiredObjectClass);

        if (namedFactoryMap == null) {
            throw new NoFactoryException("There is no factory for " + desiredObjectClass.getCanonicalName());
        }

        final Function namedFactory = namedFactoryMap.get(name);

        if (namedFactory == null) {
            throw new NoFactoryException(
                    "There is no factory for " + desiredObjectClass.getCanonicalName() + " with name " + name);
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
    private static <T> void registerFactoryRaw(Class<T> objectClass, Function factory) {
        factoryRegister.put(objectClass, factory);
    }

    public static <T> void registerFactory(Class<T> objectClass, Func0<T> factory) {
        registerFactoryRaw(objectClass, factory);
    }

    public static <A, T> void registerFactory(Class<T> objectClass, Func1<A, T> factory) {
        registerFactoryRaw(objectClass, factory);
    }

    public static <A, B, T> void registerFactory(Class<T> objectClass, Func2<A, B, T> factory) {
        registerFactoryRaw(objectClass, factory);
    }

    public static <A, B, C, T> void registerFactory(Class<T> objectClass, Func3<A, B, C, T> factory) {
        registerFactoryRaw(objectClass, factory);
    }

    public static <A, B, C, D, T> void registerFactory(Class<T> objectClass, Func4<A, B, C, D, T> factory) {
        registerFactoryRaw(objectClass, factory);
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
    public static <T> void registerNamedFactoryRaw(Class<T> objectClass, String factoryName, Function factory) {
        final Map<String, Function> factoryMap = namedFactoryRegister.get(objectClass);

        if (factoryMap == null) {
            final Map<String, Function> map = new HashMap<>();
            map.put(factoryName, factory);
            namedFactoryRegister.put(objectClass, map);
        } else {
            factoryMap.put(factoryName, factory);
        }
    }

    public static <T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func0 factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    public static <T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func1 factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    public static <T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func2 factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    public static <T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func3 factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    public static <T> void registerNamedFactory(Class<T> objectClass, String factoryName, Func4 factory) {
        registerNamedFactoryRaw(objectClass, factoryName, factory);
    }

    /**
     * Clears the entire cache.
     */
    public static void clearAll() {
        unscopedCache.clear();
        scopedCache.clear();
        unscopedNamedCache.clear();
        scopedNamedCache.clear();
    }

    /**
     * Clears the scope associated to a scoped class.
     *
     * @param objectClass is the class associated to a scope.
     */
    private static void clearScope(Class objectClass) {
        scopedCache.remove(objectClass);
    }

    private static void clearNamedScope(Class objectClass) {
        scopedNamedCache.remove(objectClass);
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

    public static final class ScopedCache {

        private final Class scope;
        private final Observable<Object> whenLifetimeEnds;
        private boolean named;

        ScopedCache(Class scope) {
            this(scope, null);
        }

        ScopedCache(Class scope, Observable<Object> whenLifetimeEnds) {
            this.scope = scope;
            this.whenLifetimeEnds = whenLifetimeEnds;
            if (this.whenLifetimeEnds != null) {
                this.whenLifetimeEnds.take(1)
                        .subscribe(s -> {
                            if (named) {
                                clearNamedScope(ScopedCache.this.scope);
                            } else {
                                clearScope(ScopedCache.this.scope);
                            }
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
        @SuppressWarnings("unchecked")
        public <V> V provide(Class<V> desiredObjectClass) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass)));
        }

        public <A, V> V provide(Class<V> desiredObjectClass, A a) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a));
        }

        public <A, B, V> V provide(Class<V> desiredObjectClass, A a, B b) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b));
        }

        public <A, B, C, V> V provide(Class<V> desiredObjectClass, A a, B b, C c) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b, c));
        }

        public <A, B, C, D, V> V provide(Class<V> desiredObjectClass, A a, B b, C c, D d) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass), a, b, c, d));
        }

        private <T> T providerHelper(Class<T> desiredObjectClass, Provider provider) {
            final Map<Class, Object> currentScopeMap = scopedCache.get(scope);

            if (currentScopeMap == null) {
                return createObjectAndScope(desiredObjectClass, provider);
            } else {
                return getObjectOrCreateIfNull(desiredObjectClass, currentScopeMap, provider);
            }
        }

        private <V> V namedProviderHelper(Class<V> desiredObjectClass, String name, Provider provider) {
            named = true;
            final Map<Class, Map<String, Object>> currentScopeMap = scopedNamedCache.get(scope);

            V desiredObject;
            if (currentScopeMap == null) {
                final Map<Class, Map<String, Object>> scopedMap = new HashMap<>();
                final Map<String, Object> namedMap = new HashMap<>();
                desiredObject = (V) provider.call();
                namedMap.put(name, desiredObject);
                scopedMap.put(desiredObjectClass, namedMap);
                scopedNamedCache.put(scope, scopedMap);
            } else {
                Map<String, Object> stringObjectMap = currentScopeMap.get(desiredObjectClass);
                Object o = stringObjectMap.get(name);
                if (o != null) {
                    desiredObject = (V) o;
                } else {
                    desiredObject = (V) provider.call();
                    stringObjectMap.put(name, desiredObject);
                }
            }

            return desiredObject;
        }

        public <V> V provideNamed(Class<V> desiredObjectClass, String name) {
            return namedProviderHelper(desiredObjectClass, name, createProvider(getNamedFactory(desiredObjectClass, name)));
        }

        public <A, V> V provideNamed(Class<V> desiredObjectClass, String name, A a) {
            return namedProviderHelper(desiredObjectClass, name, createProvider(getNamedFactory(desiredObjectClass, name), a));
        }

        public <A, B, V> V provideNamed(Class<V> desiredObjectClass, String name, A a, B b) {
            return namedProviderHelper(desiredObjectClass, name, createProvider(getNamedFactory(desiredObjectClass, name), a, b));
        }

        public <A, B, C, V> V provideNamed(Class<V> desiredObjectClass, String name, A a, B b, C c) {
            return namedProviderHelper(desiredObjectClass, name, createProvider(getNamedFactory(desiredObjectClass, name), a, b, c));
        }

        public <A, B, C, D, V> V provideNamed(Class<V> desiredObjectClass, String name, A a, B b, C c, D d) {
            return namedProviderHelper(desiredObjectClass, name, createProvider(getNamedFactory(desiredObjectClass, name), a, b, c, d));
        }

        @SuppressWarnings("unchecked")
        private <V> V createObjectAndScope(Class<V> desiredObjectClass, Provider objectFactory) {
            final Map<Class, Object> resultMap = new HashMap<>();
            final Object desiredObject = objectFactory.call();

            resultMap.put(desiredObjectClass, desiredObject);
            scopedCache.put(scope, resultMap);

            return (V) desiredObject;
        }

        @SuppressWarnings("unchecked")
        private <V> V getObjectOrCreateIfNull(Class<V> desiredObjectClass, Map<Class, Object> currentScopeMap,
                Provider objectFactory) {
            Object desiredObject = currentScopeMap.get(desiredObjectClass);

            if (desiredObject == null) {
                desiredObject = objectFactory.call();
                currentScopeMap.put(desiredObjectClass, desiredObject);
            }
            return (V) desiredObject;
        }
    }
}
