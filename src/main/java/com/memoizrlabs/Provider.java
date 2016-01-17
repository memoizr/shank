package com.memoizrlabs;

import com.memoizrlabs.functions.Func0;
import com.memoizrlabs.functions.Func1;
import com.memoizrlabs.functions.Func2;
import com.memoizrlabs.functions.Func3;
import com.memoizrlabs.functions.Func4;
import com.memoizrlabs.functions.Function;

abstract class Provider<T> implements Func0<T> {

    public static Provider createProvider(Function func) {
        return new Provider0<>(func);
    }

    public static <A> Provider createProvider(Function func, A a) {
        return new Provider1<>(func, a);
    }

    public static <A, B> Provider createProvider(Function func, A a, B b) {
        return new Provider2<>(func, a, b);
    }

    public static <A, B, C> Provider createProvider(Function func, A a, B b, C c) {
        return new Provider3<>(func, a, b, c);
    }

    public static <A, B, C, D> Provider createProvider(Function func, A a, B b, C c, D d) {
        return new Provider4<>(func, a, b, c, d);
    }

    @Override
    abstract public T call();

    abstract public String argumentsToString();

    static class Provider0<T> extends Provider<T> {

        private final Function func;

        public Provider0(Function func) {
            this.func = func;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T call() {
            return (T) ((Func0) func).call();
        }

        @Override
        public String argumentsToString() {
            return "no";
        }
    }

    static class Provider1<A, T> extends Provider<T> {

        private final Function func;
        private A a;

        public Provider1(Function func, A a) {
            this.func = func;
            this.a = a;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T call() {
            return (T) ((Func1) func).call(a);
        }

        @Override
        public String argumentsToString() {
            return a.getClass().getSimpleName();
        }
    }

    static class Provider2<A, B, T> extends Provider<T> {

        private final Function func;
        private A a;
        private B b;

        public Provider2(Function func, A a, B b) {
            this.func = func;
            this.a = a;
            this.b = b;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T call() {
            return (T) ((Func2) func).call(a, b);
        }

        @Override
        public String argumentsToString() {
            return a.getClass().getSimpleName() + ", " + b.getClass().getSimpleName();
        }
    }

    static class Provider3<A, B, C, T> extends Provider<T> {

        private final Function func;
        private A a;
        private B b;
        private C c;

        public Provider3(Function func, A a, B b, C c) {
            this.func = func;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T call() {
            return (T) ((Func3) func).call(a, b, c);
        }

        @Override
        public String argumentsToString() {
            return a.getClass().getSimpleName() + ", " + b.getClass().getSimpleName() + ", " + c.getClass()
                    .getSimpleName();
        }
    }

    static class Provider4<A, B, C, D, T> extends Provider<T> {

        private final Function func;
        private A a;
        private B b;
        private C c;
        private D d;

        public Provider4(Function func, A a, B b, C c, D d) {
            this.func = func;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T call() {
            return (T) ((Func4) func).call(a, b, c, d);
        }

        @Override
        public String argumentsToString() {
            return a.getClass().getSimpleName() + ", " + b.getClass().getSimpleName() + ", " + c.getClass()
                    .getSimpleName() + ", " + d.getClass().getSimpleName();
        }
    }
}
