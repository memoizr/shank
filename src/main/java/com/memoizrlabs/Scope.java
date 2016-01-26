package com.memoizrlabs;

import com.memoizrlabs.functions.Action0;
import com.memoizrlabs.functions.Action1;

import java.util.Map;

import static com.memoizrlabs.poweroptional.Optional.optionOf;

public final class Scope {

    private Object scopeObect;
    private Action0 action = () -> {
    };

    private Scope(Object scopeObect) {
        this.scopeObect = scopeObect;
    }

    public static Scope scope(Object scopeObect) {
        return new Scope(scopeObect);
    }

    public void clear() {
        action.call();
    }

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
