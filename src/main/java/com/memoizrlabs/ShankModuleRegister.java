package com.memoizrlabs;

public final class ShankModuleRegister {
    public static void registerModules(ShankModule... shankModules) {
        for (final ShankModule shankModule : shankModules) {
            shankModule.registerFactories();
        }
    }
}
