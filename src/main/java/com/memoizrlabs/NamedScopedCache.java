package com.memoizrlabs;

import static com.memoizrlabs.Provider.createProvider;

public final class NamedScopedCache extends ScopedCache {

    private String name;

    NamedScopedCache(String name) {
        super(name);
        this.name = name;
    }

    /**
     * @see NamedScopedCache#provideNew(Class) (Class, Object, Object, Object, Object)
     */
    public <T> T provideNew(Class<T> desiredObjectClass) {
        return createProvider(Shank.getFactory(desiredObjectClass, name)).call();
    }

    /**
     * @see NamedScopedCache#provideNew(Class) (Class, Object, Object, Object, Object)
     */
    public <A, T> T provideNew(Class<T> desiredObjectClass, A a) {
        return createProvider(Shank.getFactory(desiredObjectClass, name), a).call();
    }

    /**
     * @see NamedScopedCache#provideNew(Class) (Class, Object, Object, Object, Object)
     */
    public <A, B, T> T provideNew(Class<T> desiredObjectClass, A a, B b) {
        return createProvider(Shank.getFactory(desiredObjectClass, name), a, b).call();
    }

    /**
     * @see NamedScopedCache#provideNew(Class) (Class, Object, Object, Object, Object)
     */
    public <A, B, C, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c) {
        return createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c).call();
    }

    /**
     * Provides a new instance of the class for the associated name.
     * The arguments will be passed to the corresponding factory, which will
     * need to have a matching signature. * Throws a NoFactoryException if
     * no factory is registered for the class of the desired object.
     * Throws an IllegalArgumentException when no factory is
     * registered with the same number of arguments.
     *
     * @param desiredObjectClass is the class of the desired object.
     * @param a                  is the first parameter to be passed to the registered factory.
     * @param b                  is the second parameter to be passed to the registered factory.
     * @param c                  is the third parameter to be passed to the registered factory.
     * @param d                  is the fourth parameter to be passed to the registered factory.
     * @return an instance of the desired object as provideSingletond by the registered factory.
     */
    public <A, B, C, D, T> T provideNew(Class<T> desiredObjectClass, A a, B b, C c, D d) {
        return createProvider(Shank.getFactory(desiredObjectClass, name), a, b, c, d).call();
    }

    /**
     * Returns a builder to associate a scope.
     * @param scope the scope to be bound to.
     */
    public ScopedCache with(Scope scope) {
        return new ScopedCache(scope, name);
    }
}
