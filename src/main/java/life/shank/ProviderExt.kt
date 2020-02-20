@file:Suppress("NOTHING_TO_INLINE")

package life.shank

import life.shank.Shank.internalInstancesInScopesCache
import life.shank.Shank.internalSingletonInstanceCache

inline fun <T, F : Function<T>> Provider<T, F>.createOrGetScopedInstance(scope: Scope, instanceHashCode: Int, factory: () -> T): T {
    val cachedInstance = findCached<T>(scope, instanceHashCode)
    if (cachedInstance != null) {
        return cachedInstance
    }

    val newInstance = factory()
    var scopeInstancesCache = internalInstancesInScopesCache[scope]
    if (scopeInstancesCache == null) {
        scopeInstancesCache = HashcodeHashMap()
        internalInstancesInScopesCache.put(scope, scopeInstancesCache)
    }
    scopeInstancesCache.put(instanceHashCode, newInstance)
    return newInstance
}

inline fun <T> findCached(scope: Scope, instanceHashCode: Int): T? {
    val scopeInstances = internalInstancesInScopesCache[scope] ?: return null
    val scopeInstance = scopeInstances[instanceHashCode]
    @Suppress("UNCHECKED_CAST")
    return scopeInstance as? T?
}

inline fun <T, F : Function<T>> Provider<T, F>.createOrGetSingletonInstance(instanceHashCode: Int, factory: () -> T): T {
    @Suppress("UNCHECKED_CAST")
    val cachedInstance = internalSingletonInstanceCache[instanceHashCode] as? T
    if (cachedInstance != null) {
        return cachedInstance
    }

    val newInstance = factory()
    internalSingletonInstanceCache.put(instanceHashCode, newInstance)
    return newInstance
}

inline fun shankInternalMashHashCodes(obj1: Any?, obj2: Any?, vararg otherObj: Any?): Int {
    var hashCode = obj1.hashCode() * 31 + obj2.hashCode() * 127
    otherObj.forEach { hashCode = hashCode * 31 + it.hashCode() * 127 }
    return hashCode
}
