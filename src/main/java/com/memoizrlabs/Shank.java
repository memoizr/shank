package com.memoizrlabs;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

public class Shank {

    private static final Map<Class, Object> unscopedCache = new HashMap<>();
    private static final Map<Class, Map<String, Object>> unscopedNamedCache = new HashMap<>();

    private static final Map<Class, Func0> factoryRegister = new HashMap<>();
    private static final Map<Class, Map<String, Func0>> namedFactoryRegister = new HashMap<>();

    private static final Map<Class, Map<Class, Object>> scopedCache = new HashMap<>();

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

    public static <T> void registerFactory(Class<T> objectClass, Func0<T> factory) {
        factoryRegister.put(objectClass, factory);
    }

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

    public static void clearAll() {
        unscopedCache.clear();
    }

    private static void clearScope(Class objectClass) {
        scopedCache.remove(objectClass);
    }

    public static <T> void clearUnscoped(Class<T> objectClass) {
        unscopedCache.remove(objectClass);
    }

    public static <T> ScopedCache withScope(Class<T> objectClass) {
        return new ScopedCache(objectClass);
    }

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
