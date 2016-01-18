package com.memoizrlabs;

import static com.memoizrlabs.Provider.createProvider;

public final class NamedScopedCache extends ScopedCache {

    private String name;

    NamedScopedCache(String name) {
        super(name);
        this.name = name;
    }

    public <T> T provideNew(Class<T> desiredObjectClass) {
        return createProvider(Shank.getFactory(desiredObjectClass, name)).call();
    }

    public <A, T> T provideNew(Class<T> desiredObjectClass, A a) {
        return createProvider(Shank.getFactory(desiredObjectClass, name), a).call();
    }

    public <A, B, T> T provideNew(Class<T> desiredObjectClass, A a, B b) {
        return createProvider(Shank.getFactory(desiredObjectClass, name), a, b).call();
    }

    public <A, B, C, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c) {
        return createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c).call();
    }

    public <A, B, C, D, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c, D d) {
        return createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c, d).call();
    }

    public ScopedCache with(Scope scope) {
        return new ScopedCache(scope, name);
    }
}
