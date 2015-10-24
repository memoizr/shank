package com.memoizrlabs;

/**
 * The module initializer.
 */
public final class ShankModuleInitializer {

    private ShankModuleInitializer() {
    }

    /**
     * Iterates through the ShankModules provided as parameters and registers
     * the factories specified within them.
     *
     * @param shankModules is the list of modules which will be initialized.
     *                     Modules will be initialized in the order
     *                     specified, therefore each module will be able to
     *                     use factories specified in preceding modules.
     */
    public static void initializeModules(ShankModule... shankModules) {
        for (ShankModule shankModule : shankModules) {
            shankModule.registerFactories();
        }
    }
}
