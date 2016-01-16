package com.memoizrlabs;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action0;
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

    private static final Map<Class, Map<String, Object>> unscopedCache = new HashMap<>();
    private static final Map<Class, Map<String, Function>> namedFactoryRegister = new HashMap<>();
    private static final Map<Object, Map<Class, Map<String, Object>>> scopedCache = new HashMap<>();

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
     * @see Shank#provideNamedNew(Class, String, Object, Object, Object, Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> T provideNamedNew(Class<T> desiredObjectClass, String name) {
        return (T) createProvider(getFactory(desiredObjectClass, name)).call();
    }

    /**
     * @see Shank#provideNamedNew(Class, String, Object, Object, Object, Object)
     */
    @SuppressWarnings("unchecked")
    public static <A, T> T provideNamedNew(Class<T> desiredObjectClass, String name, A a) {
        return (T) createProvider(getFactory(desiredObjectClass, name), a).call();
    }

    /**
     * @see Shank#provideNamedNew(Class, String, Object, Object, Object, Object)
     */
    @SuppressWarnings("unchecked")
    public static <A, B, T> T provideNamedNew(Class<T> desiredObjectClass, String name, A a, B b) {
        return (T) createProvider(getFactory(desiredObjectClass, name), a, b).call();
    }

    /**
     * @see Shank#provideNamedNew(Class, String, Object, Object, Object, Object)
     */
    @SuppressWarnings("unchecked")
    public static <A, B, C, T> T provideNamedNew(Class<T> desiredObjectClass, String name, A a, B b, C c) {
        return (T) createProvider(getFactory(desiredObjectClass, name), a, b, c).call();
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
    public static <A, B, C, D, T> T provideNamedNew(Class<T> desiredObjectClass, String name, A a, B b, C c, D d) {
        return (T) createProvider(getFactory(desiredObjectClass, name), a, b, c, d).call();
    }

    // Singleton providers

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
     * @see Shank#provideNamedSingleton(Class, String, Object, Object, Object, Object)
     */
    public static <T> T provideNamedSingleton(Class<T> desiredObjectClass, String name) {
        return namedProviderHelper(desiredObjectClass, name, createProvider(getFactory(desiredObjectClass, name)));
    }

    /**
     * @see Shank#provideNamedSingleton(Class, String, Object, Object, Object, Object)
     */
    public static <A, T> T provideNamedSingleton(Class<T> desiredObjectClass, String name, A a) {
        return namedProviderHelper(desiredObjectClass, name,
                createProvider(getFactory(desiredObjectClass, name), a));
    }

    /**
     * @see Shank#provideNamedSingleton(Class, String, Object, Object, Object, Object)
     */
    public static <A, B, T> T provideNamedSingleton(Class<T> desiredObjectClass, String name, A a, B b) {
        return namedProviderHelper(desiredObjectClass, name,
                createProvider(getFactory(desiredObjectClass, name), a, b));
    }

    /**
     * @see Shank#provideNamedSingleton(Class, String, Object, Object, Object, Object)
     */
    public static <A, B, C, T> T provideNamedSingleton(Class<T> desiredObjectClass, String name, A a, B b, C c) {
        return namedProviderHelper(desiredObjectClass, name,
                createProvider(getFactory(desiredObjectClass, name), a, b, c));
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
     * @param a                  is the first parameter to be passed to the registered factory.
     * @param b                  is the second parameter to be passed to the registered factory.
     * @param c                  is the third parameter to be passed to the registered factory.
     * @param d                  is the fourth parameter to be passed to the registered factory.
     * @return an instance of the desired object as provideSingletond by the registered
     * factory.
     */
    public static <A, B, C, D, T> T provideNamedSingleton(Class<T> desiredObjectClass, String name, A a, B b, C c,
            D d) {
        return namedProviderHelper(desiredObjectClass, name,
                createProvider(getFactory(desiredObjectClass, name), a, b, c, d));
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
     * Create a builder to associate a scope to a class.
     *
     * @param scope the class associated to a scope.
     * @return a ScopedCache builder.
     */
    public static ScopedCache with(Scope scope) {
        return new ScopedCache(scope);
    }

    private static void clearNamedScope(Object scope) {
        scopedCache.remove(scope);
    }

    @SuppressWarnings("unchecked")
    private static <T> T providerHelper(Class<T> desiredObjectClass, Provider provider) {
        return namedProviderHelper(desiredObjectClass, "", provider);
    }

    @SuppressWarnings("unchecked")
    private static <T> T namedProviderHelper(Class<T> desiredObjectClass, String name, Provider provider) {
        final Map<String, Object> desiredObjectMap = unscopedCache.get(desiredObjectClass);

        T desiredObject;
        try {
            if (desiredObjectMap == null) {
                final Map<String, Object> map = new HashMap<>();
                desiredObject = (T) provider.call();
                map.put(name, desiredObject);
                unscopedCache.put(desiredObjectClass, map);
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

    private static <T> String getErrorMessage(Class<T> desiredObjectClass, Provider provider) {
        return "No factory with " + provider.argumentsToString() + " arguments registered for " + desiredObjectClass
                .getSimpleName();
    }

    private static <T> void registerNamedFactoryRaw(Class<T> objectClass, String factoryName, Function factory) {
        final Map<String, Function> factoryMap = namedFactoryRegister.get(objectClass);

        if (factoryMap == null) {
            final Map<String, Function> map = new HashMap<>();
            map.put(factoryName, factory);
            namedFactoryRegister.put(objectClass, map);
        } else {
            factoryMap.put(factoryName, factory);
        }
    }

    private static <T> Function getFactory(Class<T> desiredObjectClass) {
        return getFactory(desiredObjectClass, "");
    }

    private static <T> Function getFactory(Class<T> desiredObjectClass, String name) {
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

    public static final class Scope {

        private Object scopeObect;
        private Action0 action = () -> {
        };

        private Scope(Object scopeObect) {
            this.scopeObect = scopeObect;
        }

        public static Scope scope(Object scopeObect) {
            return new Scope(scopeObect);
        }

        public void clear() {
            action.call();
        }

        void subscribe(Action0 action) {
            this.action = action;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Scope scope = (Scope) o;

            return !(scopeObect != null ? !scopeObect.equals(scope.scopeObect) : scope.scopeObect != null);

        }

        @Override
        public int hashCode() {
            return scopeObect != null ? scopeObect.hashCode() : 0;
        }
    }

    public static final class ScopedCache {

        private final Scope scope;
        private String name = "";

        private ScopedCache(Scope scope) {
            this.scope = scope;
            this.scope.subscribe(() -> clearNamedScope(ScopedCache.this.scope));
        }

        public ScopedCache named(String name) {
            this.name = name;
            return this;
        }

        /**
         * @see ScopedCache#provide(Class, Object, Object, Object, Object)
         */
        public <V> V provide(Class<V> desiredObjectClass) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass, name)));
        }

        /**
         * @see ScopedCache#provide(Class, Object, Object, Object, Object)
         */
        public <A, V> V provide(Class<V> desiredObjectClass, A a) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass, name), a));
        }

        /**
         * @see ScopedCache#provide(Class, Object, Object, Object, Object)
         */
        public <A, B, V> V provide(Class<V> desiredObjectClass, A a, B b) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass, name), a, b));
        }

        /**
         * @see ScopedCache#provide(Class, Object, Object, Object, Object)
         */
        public <A, B, C, V> V provide(Class<V> desiredObjectClass, A a, B b, C c) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass, name), a, b, c));
        }

        /**
         * Provides the desired object. Throws a NoFactoryException if no
         * factory is registered for the class of the desired object. The object
         * returned will be created the first time this method is called and
         * all subsequent calls will return a cached instance of the same
         * object. When the lifetime observable emits an item, the object will
         * be removed from cache, and the next time this method is called a new
         * instance will be returned, which will be bound to the events from
         * the newly supplied observable.
         *
         * @param desiredObjectClass is the class of the desired object.
         * @param a                  is the first parameter to be passed to the registered factory.
         * @param b                  is the second parameter to be passed to the registered factory.
         * @param c                  is the third parameter to be passed to the registered factory.
         * @param d                  is the fourth parameter to be passed to the registered factory.
         * @return an instance of the desired object as provided by the
         * registered factory.
         */
        public <A, B, C, D, V> V provide(Class<V> desiredObjectClass, A a, B b, C c, D d) {
            return providerHelper(desiredObjectClass, createProvider(getFactory(desiredObjectClass, name), a, b, c, d));
        }

        @SuppressWarnings("unchecked")
        private <V> V providerHelper(Class<V> desiredObjectClass, Provider provider) {
            final Map<Class, Map<String, Object>> currentScopeMap = scopedCache.get(scope);

            V desiredObject;
            if (currentScopeMap == null) {
                final Map<Class, Map<String, Object>> scopedMap = new HashMap<>();
                final Map<String, Object> namedMap = new HashMap<>();
                try {
                    desiredObject = (V) provider.call();
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException(getErrorMessage(desiredObjectClass, provider));
                }
                namedMap.put(name, desiredObject);
                scopedMap.put(desiredObjectClass, namedMap);
                scopedCache.put(scope, scopedMap);
            } else {
                Map<String, Object> stringObjectMap = currentScopeMap.get(desiredObjectClass);
                Object o = stringObjectMap.get(name);
                if (o != null) {
                    desiredObject = (V) o;
                } else {
                    try {
                        desiredObject = (V) provider.call();
                    } catch (ClassCastException e) {
                        throw new IllegalArgumentException(getErrorMessage(desiredObjectClass, provider));
                    }
                    stringObjectMap.put(name, desiredObject);
                }
            }

            return desiredObject;
        }
    }
}
