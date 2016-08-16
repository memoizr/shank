package com.memoizrlabs;

import com.memoizrlabs.functions.Func0;
import com.memoizrlabs.functions.Func1;
import com.memoizrlabs.functions.Func2;
import com.memoizrlabs.functions.Func3;
import com.memoizrlabs.functions.Func4;
import com.memoizrlabs.functions.Function;

abstract class Provider<T> implements Func0<T> {

    static <R> Provider<R> createProvider(Function<R> func) {
        return new Provider0<>(func);
    }

    static <A, R> Provider<R> createProvider(Function<R> func, A a) {
        return new Provider1<>(func, a);
    }

    static <A, B, R> Provider<R> createProvider(Function<R> func, A a, B b) {
        return new Provider2<>(func, a, b);
    }

    static <A, B, C, R> Provider<R> createProvider(Function<R> func, A a, B b, C c) {
        return new Provider3<>(func, a, b, c);
    }

    static <A, B, C, D, R> Provider<R> createProvider(Function<R> func, A a, B b, C c, D d) {
        return new Provider4<>(func, a, b, c, d);
    }

    @Override
    abstract public T call();

    abstract String argumentsToString();

    static class Provider0<T> extends Provider<T> {

        private final Function func;

        public Provider0(Function func) {
            this.func = func;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T call() {
            Object call;
            Func0 func = (Func0) this.func;
            try {
                call = func.call();
            } catch (Throwable e) {
                throw new InstantiationException(e);
            }
            return (T) call;
        }

        @Override
        public String argumentsToString() {
            return "no";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Provider0<?> provider0 = (Provider0<?>) o;

            return !(func != null ? !func.equals(provider0.func) : provider0.func != null);

        }

        @Override
        public int hashCode() {
            return func != null ? func.hashCode() : 0;
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
            Object call;
            Func1 func = (Func1) this.func;
            try {
                call = func.call(a);
            } catch (Throwable e) {
                throw new InstantiationException(e);
            }
            return (T) call;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Provider1<?, ?> provider1 = (Provider1<?, ?>) o;

            if (func != null ? !func.equals(provider1.func) : provider1.func != null) {
                return false;
            }
            return !(a != null ? !a.equals(provider1.a) : provider1.a != null);

        }

        @Override
        public int hashCode() {
            int result = func != null ? func.hashCode() : 0;
            result = 31 * result + (a != null ? a.hashCode() : 0);
            return result;
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
            Object call;
            Func2 func = (Func2) this.func;
            try {
                call = func.call(a, b);
            } catch (Throwable e) {
                throw new InstantiationException(e);
            }
            return (T) call;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Provider2<?, ?, ?> provider2 = (Provider2<?, ?, ?>) o;

            if (func != null ? !func.equals(provider2.func) : provider2.func != null) {
                return false;
            }
            if (a != null ? !a.equals(provider2.a) : provider2.a != null) {
                return false;
            }
            return !(b != null ? !b.equals(provider2.b) : provider2.b != null);

        }

        @Override
        public int hashCode() {
            int result = func != null ? func.hashCode() : 0;
            result = 31 * result + (a != null ? a.hashCode() : 0);
            result = 31 * result + (b != null ? b.hashCode() : 0);
            return result;
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
            Object call;
            Func3 func = (Func3) this.func;
            try {
                call = func.call(a, b, c);
            } catch (Throwable e) {
                throw new InstantiationException(e);
            }
            return (T) call;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Provider3<?, ?, ?, ?> provider3 = (Provider3<?, ?, ?, ?>) o;

            if (func != null ? !func.equals(provider3.func) : provider3.func != null) {
                return false;
            }
            if (a != null ? !a.equals(provider3.a) : provider3.a != null) {
                return false;
            }
            if (b != null ? !b.equals(provider3.b) : provider3.b != null) {
                return false;
            }
            return !(c != null ? !c.equals(provider3.c) : provider3.c != null);

        }

        @Override
        public int hashCode() {
            int result = func != null ? func.hashCode() : 0;
            result = 31 * result + (a != null ? a.hashCode() : 0);
            result = 31 * result + (b != null ? b.hashCode() : 0);
            result = 31 * result + (c != null ? c.hashCode() : 0);
            return result;
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
            Object call;
            Func4 func = (Func4) this.func;
            try {
                call = func.call(a, b, c, d);
            } catch (Throwable e) {
                throw new InstantiationException(e);
            }
            return (T) call;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Provider4<?, ?, ?, ?, ?> provider4 = (Provider4<?, ?, ?, ?, ?>) o;

            if (func != null ? !func.equals(provider4.func) : provider4.func != null) {
                return false;
            }
            if (a != null ? !a.equals(provider4.a) : provider4.a != null) {
                return false;
            }
            if (b != null ? !b.equals(provider4.b) : provider4.b != null) {
                return false;
            }
            if (c != null ? !c.equals(provider4.c) : provider4.c != null) {
                return false;
            }
            return !(d != null ? !d.equals(provider4.d) : provider4.d != null);

        }

        @Override
        public int hashCode() {
            int result = func != null ? func.hashCode() : 0;
            result = 31 * result + (a != null ? a.hashCode() : 0);
            result = 31 * result + (b != null ? b.hashCode() : 0);
            result = 31 * result + (c != null ? c.hashCode() : 0);
            result = 31 * result + (d != null ? d.hashCode() : 0);
            return result;
        }

        @Override
        public String argumentsToString() {
            return a.getClass().getSimpleName() + ", " + b.getClass().getSimpleName() + ", " + c.getClass()
                    .getSimpleName() + ", " + d.getClass().getSimpleName();
        }
    }
}
