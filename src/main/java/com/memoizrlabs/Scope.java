package com.memoizrlabs;

import com.memoizrlabs.functions.Action0;
import com.memoizrlabs.functions.Action1;

import java.io.Serializable;
import java.util.Map;

import static com.memoizrlabs.poweroptional.Optional.optionOf;

/**
 * The scope that cached objects should be bound to.
 */
public final class Scope implements Serializable {

    private final Serializable scopeObect;
    private Action0 action = () -> {
    };

    private Scope(Serializable scopeObect) {
        this.scopeObect = scopeObect;
    }

    /**
     * Creates a new scope that the cached objects should be bound to.
     *
     * @param scopeObect the object this scope should be tied to.
     * @return the scope.
     */
    public static Scope scope(Serializable scopeObect) {
        return new Scope(scopeObect);
    }

    /**
     * Clears cache for the scope.
     */
    public void clear() {
        action.call();
    }

    /**
     * Clears the cache for the scope and runs an action for each object
     * in the scope.
     *
     * @param destroyAction the action to be performed on each object in the scope.
     */
    public void clearWithFinalAction(Action1<Object> destroyAction) {
        try {
            optionOf(Shank.scopedCache.get(this)).doIfPresent(scopeMap -> {
                for (Map.Entry<Class, Map<String, Map<Provider, Object>>> scopedEntrySet : scopeMap.entrySet()) {
                    for (Map.Entry<String, Map<Provider, Object>> namedEntrySet : scopedEntrySet.getValue()
                            .entrySet()) {
                        for (Map.Entry<Provider, Object> objectMap : namedEntrySet.getValue().entrySet()) {
                            destroyAction.call(objectMap.getValue());
                        }
                    }
                }
            });
        } finally {
            action.call();
        }
    }

    void subscribe(Action0 action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Scope scope = (Scope) o;

        return !(scopeObect != null ? !scopeObect.equals(scope.scopeObect) : scope.scopeObect != null);

    }

    @Override
    public int hashCode() {
        return scopeObect != null ? scopeObect.hashCode() : 0;
    }
}
