package com.memoizrlabs;

import java.util.HashMap;
import java.util.Map;

import static com.memoizrlabs.Provider.createProvider;
import static com.memoizrlabs.poweroptional.Optional.optionOf;

public class ScopedCache {

    private final Scope scope;
    private String name;

    ScopedCache(Scope scope) {
        this(scope, "");
    }

    ScopedCache(Scope scope, String name) {
        this.scope = scope;
        this.scope.subscribe(() -> Shank.clearNamedScope(scope));
        this.name = name;
    }

    ScopedCache(String name) {
        this(Scope.scope(""), name);
    }

    public ScopedCache named(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see ScopedCache#provideSingleton(Class, Object, Object, Object, Object)
     */
    public <V> V provideSingleton(Class<V> desiredObjectClass) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name)));
    }

    /**
     * @see ScopedCache#provideSingleton(Class, Object, Object, Object, Object)
     */
    public <A, V> V provideSingleton(Class<V> desiredObjectClass, A a) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name), a));
    }

    /**
     * @see ScopedCache#provideSingleton(Class, Object, Object, Object, Object)
     */
    public <A, B, V> V provideSingleton(Class<V> desiredObjectClass, A a, B b) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name), a, b));
    }

    /**
     * @see ScopedCache#provideSingleton(Class, Object, Object, Object, Object)
     */
    public <A, B, C, V> V provideSingleton(Class<V> desiredObjectClass, A a, B b, C c) {
        return providerHelper(desiredObjectClass, createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c));
    }

    /**
     * Provides the desired object. Throws a NoFactoryException if no
     * factory is registered for the class of the desired object. Calling this
     * method with different arguments will result in different objects
     * being returned. The object returned will be created the first time
     * this method is called and all subsequent calls will return a cached
     * instance of the same object. When the cache is cleared, the provided
     * object will be removed from cache, and the next time this method is called
     * a new instance will be returned.
     *
     * @param desiredObjectClass is the class of the desired object.
     * @param a                  is the first parameter to be passed to the corresponding factory.
     * @param b                  is the second parameter to be passed to the corresponding factory.
     * @param c                  is the third parameter to be passed to the corresponding factory.
     * @param d                  is the fourth parameter to be passed to the corresponding factory.
     * @return an instance of the desired object as provided by the corresponding factory.
     */
    public <A, B, C, D, V> V provideSingleton(Class<V> desiredObjectClass, A a, B b, C c, D d) {
        return providerHelper(desiredObjectClass,
                createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c, d));
    }

    @SuppressWarnings("unchecked")
    private <V> V providerHelper(Class<V> desiredObjectClass, Provider<V> provider) {
        try {
            return optionOf(Shank.scopedCache.get(scope))
                    .map(currentScopeMap -> optionOf(currentScopeMap.get(desiredObjectClass))
                            .map(namedMap -> optionOf(namedMap.get(name))
                                    .map(providerMap -> optionOf(getValue(provider, providerMap))
                                            .orElseGet(() -> getAndCacheObject(provider, providerMap)))
                                    .orElseGet(() -> getObjectAndCacheProviderMap(provider, namedMap)))
                            .orElseGet(() -> getAndCacheNamedMap(desiredObjectClass, provider, currentScopeMap)))
                    .orElseGet(() -> getAndCacheScope(desiredObjectClass, provider));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(Shank.getErrorMessage(desiredObjectClass, provider));
        }
    }

    private <V> V getValue(Provider<V> provider, Map<Provider, Object> providerMap) {
        return (V) providerMap.get(provider);
    }

    private <V> V getAndCacheScope(Class<V> desiredObjectClass, final Provider<V> provider)
            throws InstantiationException {
        final Map<Class, Map<String, Map<Provider, Object>>> scopedMap = new HashMap<>();
        final Map<String, Map<Provider, Object>> namedMap = new HashMap<>();
        scopedMap.put(desiredObjectClass, namedMap);
        Shank.scopedCache.put(scope, scopedMap);
        V desiredObject = provider.call();
        namedMap.put(name, new HashMap<Provider, Object>() {{
            put(provider, desiredObject);
        }});
        return desiredObject;
    }

    private <V> V getAndCacheNamedMap(Class<V> desiredObjectClass, final Provider<V> provider,
            Map<Class, Map<String, Map<Provider, Object>>> currentScopeMap) throws InstantiationException {
        final Map<String, Map<Provider, Object>> namedMap = new HashMap<>();
        currentScopeMap.put(desiredObjectClass, namedMap);
        V desiredObject = provider.call();
        namedMap.put(name, new HashMap<Provider, Object>() {{
            put(provider, desiredObject);
        }});
        return desiredObject;
    }

    private <V> V getObjectAndCacheProviderMap(final Provider<V> provider,
            Map<String, Map<Provider, Object>> namedObjectMap) throws InstantiationException {
        V desiredObject = provider.call();
        namedObjectMap.put(name, new HashMap<Provider, Object>() {{
            put(provider, desiredObject);
        }});
        return desiredObject;
    }

    private <V> V getAndCacheObject(Provider<V> provider, Map<Provider, Object> providerMap)
            throws InstantiationException {
        V desiredObject = provider.call();
        providerMap.put(provider, desiredObject);
        return desiredObject;
    }
}
