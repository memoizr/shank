package com.memoizrlabs;

import org.junit.Before;
import org.junit.Test;

import rx.functions.Func0;
import rx.subjects.PublishSubject;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class ShankTest {

    @Before
    public void setUp() {
        Shank.clearAll();
    }

    @Test
    public void provide_whenObjectHasFactory_providesRightObject() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final Object provided = Shank.provide(FooObject.class);

        assertTrue(provided != null);
        assertTrue(provided instanceof FooObject);
    }

    @Test
    public void provide_whenObjectHasNamedFactory_providesRightObject() {
        Shank.registerNamedFactory(FooObject.class, "first", ChildFooObject::new);
        Shank.registerNamedFactory(FooObject.class, "second", OtherChildFooObject::new);

        final Object first = Shank.provideNamed(FooObject.class, "first");
        final Object second = Shank.provideNamed(FooObject.class, "second");

        assertTrue(first != null);
        assertTrue(second != null);
        assertTrue(second != first);
        assertTrue(first instanceof ChildFooObject);
        assertTrue(second instanceof OtherChildFooObject);
    }

    @Test
    public void provide_whenObjectHasFactory_providesPolymorphicObject() {
        Shank.registerFactory(FooObject.class, ChildFooObject::new);

        final FooObject provided = Shank.provide(FooObject.class);

        assertTrue(provided != null);
        assertTrue(provided instanceof ChildFooObject);
    }

    @Test
    public void provide_whenObjectHasFactory_and_provideIsCalledMultipleTimes_providesSameInstance() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provided = Shank.provide(FooObject.class);
        final FooObject secondProvided = Shank.provide(FooObject.class);

        assertTrue(provided == secondProvided);
    }

    @Test
    public void clear_whenObjectIsRemoved_providesNewInstance() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provided = Shank.provide(FooObject.class);

        Shank.clearUnscoped(FooObject.class);

        final FooObject secondProvided = Shank.provide(FooObject.class);

        assertTrue(provided != secondProvided);
    }

    @Test
    public void withScope_returnsObjectForScope() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provided = Shank.withScope(OtherFooObject.class).provide(FooObject.class);

        assertTrue(provided != null);
    }

    @Test
    public void withScope_whenCalledMultipleTimes_returnsSameObjectForScope() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provided = Shank.withScope(OtherFooObject.class).provide(FooObject.class);
        final FooObject otherProvided = Shank.withScope(OtherFooObject.class).provide(FooObject.class);

        assertTrue(provided == otherProvided);
    }

    @Test
    public void withScope_andWithClearObservable_whenCalledMultipleTimes_returnsDifferentObjectForScope() {
        Shank.registerFactory(FooObject.class, FooObject::new);
        PublishSubject<Object> remove = PublishSubject.create();

        final FooObject provided = Shank.withBoundScope(OtherFooObject.class, remove).provide(FooObject.class);
        remove.onNext(new Object());
        final FooObject otherProvided = Shank.withScope(OtherFooObject.class).provide(FooObject.class);

        assertTrue(provided != otherProvided);
        assertNotNull(provided);
        assertNotNull(otherProvided);
    }

    @Test(expected = Shank.NoFactoryException.class)
    public void provide_whenObjectHasNoFactory_throwsException() throws Exception {
        Shank.provide(OtherFooObject.class);
    }

    static class FooObject {
    }

    static class ChildFooObject extends FooObject {
    }

    static class OtherChildFooObject extends FooObject {
    }

    static class OtherFooObject {
    }
}
