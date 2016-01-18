package com.memoizrlabs;

import java.util.HashMap;
import java.util.Map;

import static com.memoizrlabs.Provider.createProvider;
import static com.memoizrlabs.poweroptional.Optional.optionOf;

public class ScopedCache {

    private Scope scope = Scope.scope(this);
    private String name = "";

    ScopedCache(Scope scope) {
        this.scope = scope;
        this.scope.subscribe(() -> Shank.clearNamedScope(scope));
    }

    ScopedCache(Scope scope, String name) {
        this.scope = scope;
        this.name = name;
    }

    ScopedCache(String name) {
        this.name = name;
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
    public <A, B, C, D, V> V provideSingleton(Class<V> desiredObjectClass, A a, B b, C c, D d) {
        return providerHelper(desiredObjectClass,
                createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c, d));
    }

    @SuppressWarnings("unchecked")
    private <V> V providerHelper(Class<V> desiredObjectClass, Provider provider) {
        return optionOf(Shank.scopedCache.get(scope))
                .map(currentScopeMap -> optionOf(currentScopeMap.get(desiredObjectClass))
                        .map(namedObjectMap -> optionOf(namedObjectMap.get(name))
                                .map(providerMap -> (V) optionOf(providerMap.get(provider))
                                        .orElseGet(() -> {
                                            V desiredObject = (V) provider.call();
                                            providerMap.put(provider, desiredObject);
                                            return desiredObject;
                                        }))
                                .orElseGet(() -> {
                                    V desiredObject = (V) provider.call();
                                    namedObjectMap.put(name, new HashMap<Provider, Object>() {{
                                        put(provider, desiredObject);
                                    }});
                                    return desiredObject;
                                }))
                        .orElseGet(() -> {
                            final Map<String, Map<Provider, Object>> namedMap = new HashMap<>();
                            V desiredObject = (V) provider.call();
                            namedMap.put(name, new HashMap<Provider, Object>() {{
                                put(provider, desiredObject);
                            }});
                            currentScopeMap.put(desiredObjectClass, namedMap);
                            return desiredObject;
                        }))
                .orElseGet(() -> {
                    final Map<Class, Map<String, Map<Provider, Object>>> scopedMap = new HashMap<>();
                    final Map<String, Map<Provider, Object>> namedMap = new HashMap<>();
                    V desiredObject = (V) provider.call();
                    namedMap.put(name, new HashMap<Provider, Object>() {{
                        put(provider, desiredObject);
                    }});
                    scopedMap.put(desiredObjectClass, namedMap);
                    Shank.scopedCache.put(scope, scopedMap);
                    return desiredObject;
                });
    }
}
