package com.memoizrlabs;

import rx.functions.Action0;

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
