package com.memoizrlabs;

/**
 * The prototype for ShankModules, implement this interface to create a
 * module which will be responsible to register factories
 */
public interface ShankModule {

    /**
     * Register the Shank factories
     */
    void registerFactories();
}
