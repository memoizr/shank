package com.memoizrlabs;

import static com.memoizrlabs.Provider.createProvider;

public final class NamedScopedCache extends ScopedCache {

    private String name;

    NamedScopedCache(String name) {
        super(name);
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public <T> T provideNew(Class<T> desiredObjectClass) {
        return (T) createProvider(Shank.getFactory(desiredObjectClass, name)).call();
    }

    @SuppressWarnings("unchecked")
    public <A, T> T provideNew(Class<T> desiredObjectClass, A a) {
        return (T) createProvider(Shank.getFactory(desiredObjectClass, name), a).call();
    }

    @SuppressWarnings("unchecked")
    public <A, B, T> T provideNew(Class<T> desiredObjectClass, A a, B b) {
        return (T) createProvider(Shank.getFactory(desiredObjectClass, name), a, b).call();
    }

    @SuppressWarnings("unchecked")
    public <A, B, C, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c) {
        return (T) createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c).call();
    }

    @SuppressWarnings("unchecked")
    public <A, B, C, D, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c, D d) {
        return (T) createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c, d).call();
    }

    public ScopedCache with(Scope scope) {
        return new ScopedCache(scope, name);
    }
}
