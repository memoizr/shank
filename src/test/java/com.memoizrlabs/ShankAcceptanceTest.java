package com.memoizrlabs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.memoizrlabs.Shank.Scope.scope;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ShankAcceptanceTest {

    @Before
    public void setUp() {
        Shank.clearAll();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWithNoArgs_provideNewInstanceEveryTime() {
        Shank.registerFactory(ArrayList.class, () -> new ArrayList<>());

        final Object providedNewInstance = Shank.provideNew(FooObject.class);
        final Object otherProvidedNewInstance = Shank.provideNew(FooObject.class);

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance instanceof FooObject);
        assertTrue(otherProvidedNewInstance instanceof FooObject);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
    }

    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWith_1_Args_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, Collections::singletonList);

        final Object providedNewInstance = Shank.provideNew(List.class, "a");
        final Object otherProvidedNewInstance = Shank.provideNew(List.class, "a");

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance instanceof List);
        assertTrue(otherProvidedNewInstance instanceof List);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
    }


    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWith_2_Args_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, (a, b) -> asList(a, b));

        final Object providedNewInstance = Shank.provideNew(List.class, "a", "b");
        final Object otherProvidedNewInstance = Shank.provideNew(List.class, "a", "b");

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance instanceof List);
        assertTrue(otherProvidedNewInstance instanceof List);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
    }

    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWith_3_Args_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, (a, b, c) -> asList(a, b, c));

        final Object providedNewInstance = Shank.provideNew(List.class, "a", "b", "c");
        final Object otherProvidedNewInstance = Shank.provideNew(List.class, "a", "b", "c");

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance instanceof List);
        assertTrue(otherProvidedNewInstance instanceof List);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
    }

    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWith_4_Args_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, (a, b, c, d) -> asList(a, b, c, d));

        final Object providedNewInstance = Shank.provideNew(List.class, "a", "b", "c", "d");
        final Object otherProvidedNewInstance = Shank.provideNew(List.class, "a", "b", "c", "d");

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance instanceof List);
        assertTrue(otherProvidedNewInstance instanceof List);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
    }

    @Test
    public void provideNamedNewInstanceInstanceProvides_whenObjectHasFactoryWithNoArgs_provideNamedNewInstanceInstanceEveryTime() {
        Shank.registerNamedFactory(List.class, "one", () -> new ArrayList<>());
        Shank.registerNamedFactory(List.class, "two", () -> new LinkedList<>());

        final Object firstProvidedNewInstance = Shank.provideNamedNew(List.class, "one");
        final Object secondProvidedNewInstance = Shank.provideNamedNew(List.class, "one");

        final Object firstOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two");
        final Object secondOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two");

        assertTrue(firstProvidedNewInstance != null);
        assertTrue(firstOtherProvidedNewInstance != null);
        assertTrue(secondProvidedNewInstance != null);
        assertTrue(secondOtherProvidedNewInstance != null);
        assertTrue(firstProvidedNewInstance instanceof ArrayList);
        assertTrue(firstOtherProvidedNewInstance instanceof LinkedList);
        assertTrue(secondProvidedNewInstance instanceof ArrayList);
        assertTrue(secondOtherProvidedNewInstance instanceof LinkedList);

        assertTrue(firstProvidedNewInstance != secondProvidedNewInstance);
        assertTrue(firstOtherProvidedNewInstance != secondOtherProvidedNewInstance);
    }

    @Test
    public void provideNamedNewInstanceInstanceProvides_whenObjectHasFactoryWith_1_Args_provideNamedNewInstanceInstanceEveryTime() {
        Shank.registerNamedFactory(List.class, "one", (String a) -> new ArrayList<String>(){{add(a);}});
        Shank.registerNamedFactory(List.class, "two", (String a) -> new LinkedList<String>(){{add(a);}});

        final Object firstProvidedNewInstance = Shank.provideNamedNew(List.class, "one", "a");
        final Object secondProvidedNewInstance = Shank.provideNamedNew(List.class, "one", "a");

        final Object firstOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two", "a");
        final Object secondOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two", "a");

        assertTrue(firstProvidedNewInstance != null);
        assertTrue(firstOtherProvidedNewInstance != null);
        assertTrue(secondProvidedNewInstance != null);
        assertTrue(secondOtherProvidedNewInstance != null);
        assertTrue(firstProvidedNewInstance instanceof ArrayList);
        assertTrue(firstOtherProvidedNewInstance instanceof LinkedList);
        assertTrue(secondProvidedNewInstance instanceof ArrayList);
        assertTrue(secondOtherProvidedNewInstance instanceof LinkedList);

        assertTrue(firstProvidedNewInstance != secondProvidedNewInstance);
        assertTrue(firstOtherProvidedNewInstance != secondOtherProvidedNewInstance);
    }

    @Test
    public void provideNamedNewInstanceInstanceProvides_whenObjectHasFactoryWith_2_Args_provideNamedNewInstanceInstanceEveryTime() {
        Shank.registerNamedFactory(List.class, "one", (String a, String b) -> new ArrayList<String>(){{add(a); add(b);}});
        Shank.registerNamedFactory(List.class, "two", (String a, String b) -> new LinkedList<String>(){{add(a); add(b);}});

        final Object firstProvidedNewInstance = Shank.provideNamedNew(List.class, "one", "a", "b");
        final Object secondProvidedNewInstance = Shank.provideNamedNew(List.class, "one", "a", "b");

        final Object firstOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two", "a", "b");
        final Object secondOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two", "a", "b");

        assertTrue(firstProvidedNewInstance != null);
        assertTrue(firstOtherProvidedNewInstance != null);
        assertTrue(secondProvidedNewInstance != null);
        assertTrue(secondOtherProvidedNewInstance != null);
        assertTrue(firstProvidedNewInstance instanceof ArrayList);
        assertTrue(firstOtherProvidedNewInstance instanceof LinkedList);
        assertTrue(secondProvidedNewInstance instanceof ArrayList);
        assertTrue(secondOtherProvidedNewInstance instanceof LinkedList);

        assertTrue(firstProvidedNewInstance != secondProvidedNewInstance);
        assertTrue(firstOtherProvidedNewInstance != secondOtherProvidedNewInstance);
    }

    @Test
    public void provideNamedNewInstanceInstanceProvides_whenObjectHasFactoryWith_3_Args_provideNamedNewInstanceInstanceEveryTime() {
        Shank.registerNamedFactory(List.class, "one", (String a, String b, String c) -> new ArrayList<String>(){{add(a); add(b); add(c);}});
        Shank.registerNamedFactory(List.class, "two", (String a, String b, String c) -> new LinkedList<String>(){{add(a); add(b); add(c);}});

        final Object firstProvidedNewInstance = Shank.provideNamedNew(List.class, "one", "a", "b", "c");
        final Object secondProvidedNewInstance = Shank.provideNamedNew(List.class, "one", "a", "b", "c");

        final Object firstOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two", "a", "b", "c");
        final Object secondOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two", "a", "b", "c");

        assertTrue(firstProvidedNewInstance != null);
        assertTrue(firstOtherProvidedNewInstance != null);
        assertTrue(secondProvidedNewInstance != null);
        assertTrue(secondOtherProvidedNewInstance != null);
        assertTrue(firstProvidedNewInstance instanceof ArrayList);
        assertTrue(firstOtherProvidedNewInstance instanceof LinkedList);
        assertTrue(secondProvidedNewInstance instanceof ArrayList);
        assertTrue(secondOtherProvidedNewInstance instanceof LinkedList);

        assertTrue(firstProvidedNewInstance != secondProvidedNewInstance);
        assertTrue(firstOtherProvidedNewInstance != secondOtherProvidedNewInstance);
    }

    @Test
    public void provideNamedNewInstanceInstanceProvides_whenObjectHasFactoryWith_4_Args_provideNamedNewInstanceInstanceEveryTime() {
        Shank.registerNamedFactory(List.class, "one", (String a, String b, String c, String d) -> new ArrayList<String>(){{add(a); add(b); add(c); add(d);}});
        Shank.registerNamedFactory(List.class, "two", (String a, String b, String c, String d) -> new LinkedList<String>(){{add(a); add(b); add(c); add(d);}});

        final Object firstProvidedNewInstance = Shank.provideNamedNew(List.class, "one", "a", "b", "c", "d");
        final Object secondProvidedNewInstance = Shank.provideNamedNew(List.class, "one", "a", "b", "c", "d");

        final Object firstOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two", "a", "b", "c", "d");
        final Object secondOtherProvidedNewInstance = Shank.provideNamedNew(List.class, "two", "a", "b", "c", "d");

        assertTrue(firstProvidedNewInstance != null);
        assertTrue(firstOtherProvidedNewInstance != null);
        assertTrue(secondProvidedNewInstance != null);
        assertTrue(secondOtherProvidedNewInstance != null);
        assertTrue(firstProvidedNewInstance instanceof ArrayList);
        assertTrue(firstOtherProvidedNewInstance instanceof LinkedList);
        assertTrue(secondProvidedNewInstance instanceof ArrayList);
        assertTrue(secondOtherProvidedNewInstance instanceof LinkedList);

        assertTrue(firstProvidedNewInstance != secondProvidedNewInstance);
        assertTrue(firstOtherProvidedNewInstance != secondOtherProvidedNewInstance);
    }

    // Singletons

    @Test
    public void provideSingleton_whenObjectHasFactory_provideSingletonsRightObject() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final Object provideSingletond = Shank.provideSingleton(FooObject.class);

        assertTrue(provideSingletond != null);
        assertTrue(provideSingletond instanceof FooObject);
    }

    @Test
    public void provideSingleton_whenObjectHasNoFactory_throwsMeaninfulErrro() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("No factory with String, String, String arguments registered for List");

        Shank.registerFactory(List.class, (a, b) -> asList("a", "b"));
        Shank.provideSingleton(List.class, "a", "b", "c");
    }

    @Test
    public void provideSingleton_whenObjectHasFactoryWith1Parameter_provideSingletonsRightObject() {
        Shank.registerFactory(List.class, Collections::singletonList);

        final List provideSingletond = Shank.provideSingleton(List.class, "a");

        assertTrue(provideSingletond != null);
        assertThat(provideSingletond, is(Collections.singletonList("a")));
    }

    @Test
    public void provideSingleton_whenObjectHasFactoryWith2Parameter_provideSingletonsRightObject() {
        Shank.registerFactory(List.class, (String a, String b) -> asList(a, b));

        final List provideSingletond = Shank.provideSingleton(List.class, "a", "b");

        assertTrue(provideSingletond != null);
        assertThat(provideSingletond, is(asList("a", "b")));
    }

    @Test
    public void provideSingleton_whenObjectHasFactoryWith3Parameter_provideSingletonsRightObject() {
        Shank.registerFactory(List.class, (a, b, c) -> asList(a, b, c));

        final List provideSingletond = Shank.provideSingleton(List.class, "a", "b", "c");

        assertTrue(provideSingletond != null);
        assertThat(provideSingletond, is(asList("a", "b", "c")));
    }

    @Test
    public void provideSingleton_whenObjectHasFactoryWith4Parameter_provideSingletonsRightObject() {
        Shank.registerFactory(List.class, (a, b, c, d) -> asList(a, b, c, d));

        final List provideSingletond = Shank.provideSingleton(List.class, "a", "b", "c", "d");

        assertTrue(provideSingletond != null);
        assertThat(provideSingletond, is(asList("a", "b", "c", "d")));
    }

    @Test
    public void provideSingleton_whenObjectHasFactoryWithParametersOfDifferentType_provideSingletonsRightObject() {
        Shank.registerFactory(ClassWithConstructorParameters.class, (String aString, Integer anInteger) ->
                new ClassWithConstructorParameters(aString, anInteger));

        final ClassWithConstructorParameters provideSingletond = Shank.provideSingleton(ClassWithConstructorParameters.class, "a", 1);

        assertTrue(provideSingletond != null);
        assertThat(provideSingletond.a, is("a"));
        assertThat(provideSingletond.b, is(1));
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith0Arguments_provideSingletonsRightObject() {
        Shank.registerNamedFactory(FooObject.class, "first", ChildFooObject::new);
        Shank.registerNamedFactory(FooObject.class, "second", OtherChildFooObject::new);

        final Object first = Shank.provideNamedSingleton(FooObject.class, "first");
        final Object second = Shank.provideNamedSingleton(FooObject.class, "second");

        assertTrue(first != null);
        assertTrue(second != null);
        assertTrue(second != first);
        assertTrue(first instanceof ChildFooObject);
        assertTrue(second instanceof OtherChildFooObject);
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith1Arguments_provideSingletonsRightObject() {
        Shank.registerNamedFactory(List.class, "name", (a) -> asList("a"));

        final List provideSingletond = Shank.provideNamedSingleton(List.class, "name", "a");
        assertThat(provideSingletond, is(Collections.singletonList("a")));
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith2Arguments_provideSingletonsRightObject() {
        Shank.registerNamedFactory(List.class, "name", (a, b) -> asList(a, b));

        final List provideSingletond = Shank.provideNamedSingleton(List.class, "name", "a", "b");
        assertThat(provideSingletond, is(asList("a", "b")));
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith3Arguments_provideSingletonsRightObject() {
        Shank.registerNamedFactory(List.class, "name", (a, b, c) -> asList(a, b, c));

        final List provideSingletond = Shank.provideNamedSingleton(List.class, "name", "a", "b", "c");
        assertThat(provideSingletond, is(asList("a", "b", "c")));
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith4Arguments_provideSingletonsRightObject() {
        Shank.registerNamedFactory(List.class, "name", (a, b, c, d) -> asList(a, b, c, d));

        final List provideSingletond = Shank.provideNamedSingleton(List.class, "name", "a", "b", "c", "d");
        assertThat(provideSingletond, is(asList("a", "b", "c", "d")));
    }

    @Test
    public void provideSingleton_whenObjectHasFactory_provideSingletonsPolymorphicObject() {
        Shank.registerFactory(FooObject.class, ChildFooObject::new);

        final FooObject provideSingletond = Shank.provideSingleton(FooObject.class);

        assertTrue(provideSingletond != null);
        assertTrue(provideSingletond instanceof ChildFooObject);
    }

    @Test
    public void provideSingleton_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimes_provideSingletonsSameInstance() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provideSingletond = Shank.provideSingleton(FooObject.class);
        final FooObject secondProvided = Shank.provideSingleton(FooObject.class);

        assertTrue(provideSingletond == secondProvided);
    }

    @Test
    public void clear_whenObjectIsRemoved_provideSingletonsNewInstance() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provideSingletond = Shank.provideSingleton(FooObject.class);

        Shank.clearUnscoped(FooObject.class);

        final FooObject secondProvided = Shank.provideSingleton(FooObject.class);

        assertTrue(provideSingletond != secondProvided);
    }

    @Test
    public void withScope_and_0_arguments_returnsObjectForScope() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provided = Shank.with(scope(OtherFooObject.class)).provide(FooObject.class);

        assertTrue(provided != null);
    }

    @Test
    public void withScope_and_1_arguments_returnsObjectForScope() {
        Shank.registerFactory(List.class, (a) -> asList(a));

        final List provided = Shank.with(scope(OtherFooObject.class)).provide(List.class, "a");

        assertTrue(provided != null);
        assertThat(provided, is(Collections.singletonList("a")));
    }

    @Test
    public void withScope_and_2_arguments_returnsObjectForScope() {
        Shank.registerFactory(List.class, (a, b) -> asList(a, b));

        final List provided = Shank.with(scope(OtherFooObject.class)).provide(List.class, "a", "b");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b")));
    }

    @Test
    public void withScope_and_3_arguments_returnsObjectForScope() {
        Shank.registerFactory(List.class, (a, b, c) -> asList(a, b, c));

        final List provided = Shank.with(scope(OtherFooObject.class)).provide(List.class, "a", "b", "c");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b", "c")));
    }

    @Test
    public void withScope_and_4_arguments_returnsObjectForScope() {
        Shank.registerFactory(List.class, (a, b, c, d) -> asList(a, b, c, d));

        final List provided = Shank.with(scope(OtherFooObject.class)).provide(List.class, "a", "b", "c", "d");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b", "c", "d")));
    }

    @Test
    public void withScope_whenCalledMultipleTimes_returnsSameObjectForScope() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provided = Shank.with(scope(OtherFooObject.class)).provide(FooObject.class);
        final FooObject otherProvided = Shank.with(scope(OtherFooObject.class)).provide(FooObject.class);

        assertTrue(provided == otherProvided);
    }

    @Test
    public void withObjectScope_whenCalledMultipleTimes_returnsSameObjectForScope() {
        Shank.registerFactory(FooObject.class, FooObject::new);

        final FooObject provided = Shank.with(scope("foo")).provide(FooObject.class);
        final FooObject otherProvided = Shank.with(scope("foo")).provide(FooObject.class);

        assertTrue(provided == otherProvided);
    }

    @Test
    public void withScope_andWithClearScope_whenCalledMultipleTimes_returnsDifferentObjectForScope() {
        Shank.registerFactory(FooObject.class, FooObject::new);
        Shank.Scope scope = scope(OtherChildFooObject.class);

        final FooObject provided = Shank.with(scope).provide(FooObject.class);
        scope.clear();

        scope = scope(OtherChildFooObject.class);
        final FooObject otherProvided = Shank.with(scope).provide(FooObject.class);

        assertTrue(provided != otherProvided);
        assertNotNull(provided);
        assertNotNull(otherProvided);
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_0_arguments() {
        Shank.registerNamedFactory(FooObject.class, "first", ChildFooObject::new);
        Shank.registerNamedFactory(FooObject.class, "second", OtherChildFooObject::new);

        FooObject first = Shank.with(scope(OtherFooObject.class)).provideNamed(FooObject.class, "first");
        FooObject second = Shank.with(scope(OtherFooObject.class)).provideNamed(FooObject.class, "second");

        assertTrue(first != null);
        assertTrue(second != null);
        assertTrue(second != first);
        assertTrue(first instanceof ChildFooObject);
        assertTrue(second instanceof OtherChildFooObject);
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_1_arguments() {
        Shank.registerNamedFactory(List.class, "list", (a) -> asList(a));
        List provided = Shank.with(scope("a scope")).provideNamed(List.class, "list", "a");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a")));
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_2_arguments() {
        Shank.registerNamedFactory(List.class, "list", (a, b) -> asList(a, b));
        List provided = Shank.with(scope("a scope")).provideNamed(List.class, "list", "a", "b");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b")));
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_3_arguments() {
        Shank.registerNamedFactory(List.class, "list", (a, b, c) -> asList(a, b, c));
        List provided = Shank.with(scope("a scope")).provideNamed(List.class, "list", "a", "b", "c");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b", "c")));
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_4_arguments() {
        Shank.registerNamedFactory(List.class, "list", (a, b, c, d) -> asList(a, b, c, d));
        List provided = Shank.with(scope("a scope")).provideNamed(List.class, "list", "a", "b", "c", "d");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b", "c", "d")));
    }

    @Test
    public void withScope_whenHasNamedFactory_andScopeIsCleared_returnsDifferentObjects() {
        Shank.registerNamedFactory(FooObject.class, "object", FooObject::new);
        final Shank.Scope scope = scope(OtherFooObject.class);
        FooObject first = Shank.with(scope).provideNamed(FooObject.class, "object");

        scope.clear();
        FooObject other = Shank.with(scope).provideNamed(FooObject.class, "object");

        assertTrue(first != null);
        assertTrue(other != null);
        assertTrue(other != first);
    }

    @Test
    public void withObjectScope_whenHasNamedFactory_andScopeIsCleared_returnsDifferentObjects() {
        Shank.registerNamedFactory(FooObject.class, "object", FooObject::new);

        final Shank.Scope scope = scope("foo");
        FooObject first = Shank.with(scope).provideNamed(FooObject.class, "object");
        scope.clear();
        FooObject other = Shank.with(scope).provideNamed(FooObject.class, "object");

        assertTrue(first != null);
        assertTrue(other != null);
        assertTrue(other != first);
    }

    @Test
    public void withScope_whenHasNamedFactory__calledMultipleTimes_returnsSameObjectForNameAndScope() {
        Shank.registerNamedFactory(FooObject.class, "first", ChildFooObject::new);
        Shank.registerNamedFactory(FooObject.class, "second", OtherChildFooObject::new);

        FooObject first = Shank.with(scope(OtherFooObject.class)).provideNamed(FooObject.class, "first");
        FooObject second = Shank.with(scope(OtherFooObject.class)).provideNamed(FooObject.class, "second");

        FooObject otherFirst = Shank.with(scope(OtherFooObject.class)).provideNamed(FooObject.class, "first");
        FooObject otherSecond = Shank.with(scope(OtherFooObject.class)).provideNamed(FooObject.class, "second");

        assertThat(first, is(otherFirst));
        assertThat(second, is(otherSecond));
    }

    @Test(expected = NoFactoryException.class)
    public void provide_whenObjectHasNoFactory_throwsException() throws Exception {
        Shank.provideNew(OtherFooObject.class);
    }

    static class FooObject {

        FooObject() {
        }

    }

    static class ClassWithConstructorParameters {

        private String a;
        private Integer b;

        ClassWithConstructorParameters(String a, Integer b) {
            this.a = a;
            this.b = b;
        }
    }

    static class ChildFooObject extends FooObject {
    }

    static class OtherChildFooObject extends FooObject {
    }

    static class OtherFooObject {
    }
}
