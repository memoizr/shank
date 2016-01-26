package com.memoizrlabs;

import com.memoizrlabs.functions.Func0;
import com.memoizrlabs.functions.Func1;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.memoizrlabs.Scope.scope;
import static com.memoizrlabs.Shank.registerNamedFactory;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ShankAcceptanceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        Shank.clearAll();
        Shank.clearFactories();
    }

    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWithNoArgs_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, (Func0<List>) ArrayList::new);

        List providedNewInstance = Shank.provideNew(List.class);
        List otherProvidedNewInstance = Shank.provideNew(List.class);

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
        assertThat(providedNewInstance, is(Collections.emptyList()));
        assertThat(otherProvidedNewInstance, is(Collections.emptyList()));
    }

    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWith_1_Args_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, Collections::singletonList);

        List providedNewInstance = Shank.provideNew(List.class, "a");
        List otherProvidedNewInstance = Shank.provideNew(List.class, "a");

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
        assertThat(providedNewInstance, is(singletonList("a")));
        assertThat(otherProvidedNewInstance, is(singletonList("a")));
    }


    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWith_2_Args_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, (a, b) -> asList(a, b));

        List providedNewInstance = Shank.provideNew(List.class, "a", "b");
        List otherProvidedNewInstance = Shank.provideNew(List.class, "a", "b");

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
        assertThat(providedNewInstance, is(asList("a", "b")));
        assertThat(otherProvidedNewInstance, is(asList("a", "b")));
    }

    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWith_3_Args_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, (a, b, c) -> asList(a, b, c));

        List providedNewInstance = Shank.provideNew(List.class, "a", "b", "c");
        List otherProvidedNewInstance = Shank.provideNew(List.class, "a", "b", "c");

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
        assertThat(providedNewInstance, is(asList("a", "b", "c")));
        assertThat(otherProvidedNewInstance, is(asList("a", "b", "c")));
    }

    @Test
    public void provideNewInstanceProvides_whenObjectHasFactoryWith_4_Args_provideNewInstanceEveryTime() {
        Shank.registerFactory(List.class, (a, b, c, d) -> asList(a, b, c, d));

        List providedNewInstance = Shank.provideNew(List.class, "a", "b", "c", "d");
        List otherProvidedNewInstance = Shank.provideNew(List.class, "a", "b", "c", "d");

        assertTrue(providedNewInstance != null);
        assertTrue(otherProvidedNewInstance != null);
        assertTrue(providedNewInstance != otherProvidedNewInstance);
        assertThat(providedNewInstance, is(asList("a", "b", "c", "d")));
        assertThat(otherProvidedNewInstance, is(asList("a", "b", "c", "d")));
    }

    @Test
    public void provideNamedNewInstanceInstanceProvides_whenObjectHasFactoryWithNoArgs_provideNamedNewInstanceInstanceEveryTime() {
        registerNamedFactory(List.class, "one", (Func0) ArrayList::new);
        registerNamedFactory(List.class, "two", (Func0) LinkedList::new);

        List firstProvidedNewInstance = Shank.named("one").provideNew(List.class);
        List secondProvidedNewInstance = Shank.named("one").provideNew(List.class);

        List firstOtherProvidedNewInstance = Shank.named("two").provideNew(List.class);
        List secondOtherProvidedNewInstance = Shank.named("two").provideNew(List.class);

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
        registerNamedFactory(List.class, "one", (String a) -> new ArrayList<String>() {{
            add(a);
        }});
        registerNamedFactory(List.class, "two", (String a) -> new LinkedList<String>() {{
            add(a);
        }});

        List firstProvidedNewInstance = Shank.named("one").provideNew(List.class, "a");
        List secondProvidedNewInstance = Shank.named("one").provideNew(List.class, "a");

        List firstOtherProvidedNewInstance = Shank.named("two").provideNew(List.class, "a");
        List secondOtherProvidedNewInstance = Shank.named("two").provideNew(List.class, "a");

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
        registerNamedFactory(List.class, "one", (String a, String b) -> new ArrayList<String>() {{
            add(a);
            add(b);
        }});
        registerNamedFactory(List.class, "two", (String a, String b) -> new LinkedList<String>() {{
            add(a);
            add(b);
        }});

        List firstProvidedNewInstance = Shank.named("one").provideNew(List.class, "a", "b");
        List secondProvidedNewInstance = Shank.named("one").provideNew(List.class, "a", "b");

        List firstOtherProvidedNewInstance = Shank.named("two").provideNew(List.class, "a", "b");
        List secondOtherProvidedNewInstance = Shank.named("two").provideNew(List.class, "a", "b");

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
        registerNamedFactory(List.class, "one", (String a, String b, String c) -> new ArrayList<String>() {{
            add(a);
            add(b);
            add(c);
        }});
        registerNamedFactory(List.class, "two", (String a, String b, String c) -> new LinkedList<String>() {{
            add(a);
            add(b);
            add(c);
        }});

        List firstProvidedNewInstance = Shank.named("one").provideNew(List.class, "a", "b", "c");
        List secondProvidedNewInstance = Shank.named("one").provideNew(List.class, "a", "b", "c");
        List firstOtherProvidedNewInstance = Shank.named("two").provideNew(List.class, "a", "b", "c");
        List secondOtherProvidedNewInstance = Shank.named("two").provideNew(List.class, "a", "b", "c");

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
        registerNamedFactory(List.class, "one",
                (String a, String b, String c, String d) -> new ArrayList<String>() {{
                    add(a);
                    add(b);
                    add(c);
                    add(d);
                }});
        registerNamedFactory(List.class, "two",
                (String a, String b, String c, String d) -> new LinkedList<String>() {{
                    add(a);
                    add(b);
                    add(c);
                    add(d);
                }});

        List firstProvidedNewInstance = Shank.named("one").provideNew(List.class, "a", "b", "c", "d");
        List secondProvidedNewInstance = Shank.named("one").provideNew(List.class, "a", "b", "c", "d");
        List firstOtherProvidedNewInstance = Shank.named("two").provideNew(List.class, "a", "b", "c", "d");
        List secondOtherProvidedNewInstance = Shank.named("two").provideNew(List.class, "a", "b", "c", "d");

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
    public void provideSingleton_whenObjectHasFactory_provideSingletonsRightObject() {
        Shank.registerFactory(A.class, A::new);

        A provideSingleton = Shank.provideSingleton(A.class);

        assertTrue(provideSingleton != null);
    }

    @Test
    public void provideSingleton_whenObjectHasNoFactory_throwsMeaninfulErrro() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("No factory with String, String, String arguments registered for List");

        Shank.registerFactory(List.class, (a, b) -> asList("a", "b"));

        Shank.provideSingleton(List.class, "a", "b", "c");
    }

    @Test
    public void provideScopedSingleton_whenObjectHasNoFactory_throwsMeaninfulErrro() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("No factory with String, String, String arguments registered for List");

        Shank.registerFactory(List.class, (a, b) -> asList("a", "b"));

        Shank.with(scope("foo")).provideSingleton(List.class, "a", "b", "c");
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

        final ClassWithConstructorParameters provideSingletond = Shank
                .provideSingleton(ClassWithConstructorParameters.class, "a", 1);

        assertTrue(provideSingletond != null);
        assertThat(provideSingletond.a, is("a"));
        assertThat(provideSingletond.b, is(1));
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith0Arguments_provideSingletonsRightObject() {
        registerNamedFactory(A.class, "first", ChildOfA::new);
        registerNamedFactory(A.class, "second", OtherChildOfA::new);

        A first = Shank.named("first").provideSingleton(A.class);
        A second = Shank.named("second").provideSingleton(A.class);

        assertTrue(first != null);
        assertTrue(second != null);
        assertTrue(second != first);
        assertTrue(first instanceof ChildOfA);
        assertTrue(second instanceof OtherChildOfA);
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith1Arguments_provideSingletonsRightObject() {
        registerNamedFactory(List.class, "name", (a) -> Collections.singletonList("a"));

        final List provideSingletond = Shank.named("name").provideSingleton(List.class, "a");
        assertThat(provideSingletond, is(Collections.singletonList("a")));
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith2Arguments_provideSingletonsRightObject() {
        registerNamedFactory(List.class, "name", (a, b) -> asList(a, b));

        final List provideSingletond = Shank.named("name").provideSingleton(List.class, "a", "b");
        assertThat(provideSingletond, is(asList("a", "b")));
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith3Arguments_provideSingletonsRightObject() {
        registerNamedFactory(List.class, "name", (a, b, c) -> asList(a, b, c));

        final List provideSingletond = Shank.named("name").provideSingleton(List.class, "a", "b", "c");
        assertThat(provideSingletond, is(asList("a", "b", "c")));
    }

    @Test
    public void provideSingleton_whenObjectHasNamedFactoryWith4Arguments_provideSingletonsRightObject() {
        registerNamedFactory(List.class, "name", (a, b, c, d) -> asList(a, b, c, d));

        final List provideSingletond = Shank.named("name").provideSingleton(List.class, "a", "b", "c", "d");
        assertThat(provideSingletond, is(asList("a", "b", "c", "d")));
    }

    @Test
    public void provideSingleton_whenObjectHasFactory_provideSingletonsPolymorphicObject() {
        Shank.registerFactory(A.class, ChildOfA::new);

        final A provideSingletond = Shank.provideSingleton(A.class);

        assertTrue(provideSingletond != null);
        assertTrue(provideSingletond instanceof ChildOfA);
    }

    @Test
    public void provideSingleton_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimes_provideSingletonsSameInstance() {
        Shank.registerFactory(A.class, A::new);

        final A provideSingletond = Shank.provideSingleton(A.class);
        final A secondProvided = Shank.provideSingleton(A.class);

        assertTrue(provideSingletond == secondProvided);
    }

    @Test
    public void provideSingleton_withScope_1_arg_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimesWithDifferentArguments_provideSingletonsForEqualArguments() {
        Shank.registerFactory(List.class, (x) -> asList(x));

        final Scope scope = scope("a");
        List firstWithA = Shank.with(scope).provideSingleton(List.class, "a");
        List secondWithA = Shank.with(scope).provideSingleton(List.class, "a");

        List firstWithB = Shank.with(scope).provideSingleton(List.class, "b");
        List secondWithB = Shank.with(scope).provideSingleton(List.class, "b");

        assertTrue(firstWithA == secondWithA);
        assertTrue(firstWithA != firstWithB);
        assertTrue(firstWithB == secondWithB);
    }

    @Test
    public void provideSingleton_withScope_with_2_args_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimesWithDifferentArguments_provideSingletonsForEqualArguments() {
        Shank.registerFactory(List.class, (a, b) -> asList(a, b));

        final Scope scope = scope("a");
        List firstWithA = Shank.with(scope).provideSingleton(List.class, "a", "b");
        List secondWithA = Shank.with(scope).provideSingleton(List.class, "a", "b");

        List firstWithB = Shank.with(scope).provideSingleton(List.class, "b", "c");
        List secondWithB = Shank.with(scope).provideSingleton(List.class, "b", "c");

        assertTrue(firstWithA == secondWithA);
        assertTrue(firstWithA != firstWithB);
        assertTrue(firstWithB == secondWithB);
    }

    @Test
    public void provideSingleton_withScope_with_3_args_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimesWithDifferentArguments_provideSingletonsForEqualArguments() {
        Shank.registerFactory(List.class, (a, b, c) -> asList(a, b, c));

        final Scope scope = scope("a");
        List firstWithA = Shank.with(scope).provideSingleton(List.class, "a", "b", "c");
        List secondWithA = Shank.with(scope).provideSingleton(List.class, "a", "b", "c");

        List firstWithB = Shank.with(scope).provideSingleton(List.class, "b", "c", "d");
        List secondWithB = Shank.with(scope).provideSingleton(List.class, "b", "c", "d");

        assertTrue(firstWithA == secondWithA);
        assertTrue(firstWithA != firstWithB);
        assertTrue(firstWithB == secondWithB);
    }

    @Test
    public void provideSingleton_withScope_with_4_args_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimesWithDifferentArguments_provideSingletonsForEqualArguments() {
        Shank.registerFactory(List.class, (a, b, c, d) -> asList(a, b, c, d));

        final Scope scope = scope("a");
        List firstWithA = Shank.with(scope).provideSingleton(List.class, "a", "b", "c", "d");
        List secondWithA = Shank.with(scope).provideSingleton(List.class, "a", "b", "c", "d");

        List firstWithB = Shank.with(scope).provideSingleton(List.class, "b", "c", "d", "e");
        List secondWithB = Shank.with(scope).provideSingleton(List.class, "b", "c", "d", "e");

        assertTrue(firstWithA == secondWithA);
        assertTrue(firstWithA != firstWithB);
        assertTrue(firstWithB == secondWithB);
    }

    @Test
    public void provideSingleton_with_1_arg_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimesWithDifferentArguments_provideSingletonsForEqualArguments() {
        Shank.registerFactory(List.class, (x) -> asList(x));

        List firstWithA = Shank.provideSingleton(List.class, "a");
        List secondWithA = Shank.provideSingleton(List.class, "a");

        List firstWithB = Shank.provideSingleton(List.class, "b");
        List secondWithB = Shank.provideSingleton(List.class, "b");

        assertTrue(firstWithA == secondWithA);
        assertTrue(firstWithA != firstWithB);
        assertTrue(firstWithB == secondWithB);
    }

    @Test
    public void provideSingleton_with_2_args_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimesWithDifferentArguments_provideSingletonsForEqualArguments() {
        Shank.registerFactory(List.class, (a, b) -> asList(a, b));

        List firstWithA = Shank.provideSingleton(List.class, "a", "b");
        List secondWithA = Shank.provideSingleton(List.class, "a", "b");

        List firstWithB = Shank.provideSingleton(List.class, "b", "c");
        List secondWithB = Shank.provideSingleton(List.class, "b", "c");

        assertTrue(firstWithA == secondWithA);
        assertTrue(firstWithA != firstWithB);
        assertTrue(firstWithB == secondWithB);
    }

    @Test
    public void provideSingleton_with_3_args_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimesWithDifferentArguments_provideSingletonsForEqualArguments() {
        Shank.registerFactory(List.class, (a, b, c) -> asList(a, b, c));

        List firstWithA = Shank.provideSingleton(List.class, "a", "b", "c");
        List secondWithA = Shank.provideSingleton(List.class, "a", "b", "c");

        List firstWithB = Shank.provideSingleton(List.class, "b", "c", "d");
        List secondWithB = Shank.provideSingleton(List.class, "b", "c", "d");

        assertTrue(firstWithA == secondWithA);
        assertTrue(firstWithA != firstWithB);
        assertTrue(firstWithB == secondWithB);
    }

    @Test
    public void provideSingleton_with_4_args_whenObjectHasFactory_and_provideSingletonIsCalledMultipleTimesWithDifferentArguments_provideSingletonsForEqualArguments() {
        Shank.registerFactory(List.class, (a, b, c, d) -> asList(a, b, c, d));

        List firstWithA = Shank.provideSingleton(List.class, "a", "b", "c", "d");
        List secondWithA = Shank.provideSingleton(List.class, "a", "b", "c", "d");

        List firstWithB = Shank.provideSingleton(List.class, "b", "c", "d", "e");
        List secondWithB = Shank.provideSingleton(List.class, "b", "c", "d", "e");

        assertTrue(firstWithA == secondWithA);
        assertTrue(firstWithA != firstWithB);
        assertTrue(firstWithB == secondWithB);
    }

    @Test
    public void clear_whenScopeIsClearedExecuteFinalizerForEachObjectInScope() {
        List<String> list1 = new ArrayList<String>() {{add("a");}};
        List<String> list2 = new ArrayList<String>() {{add("b");}};
        registerNamedFactory(List.class, "1", () -> list1);
        registerNamedFactory(List.class, "2", () -> list2);
        Scope scope = Scope.scope(new Object());
        Shank.named("1").with(scope).provideSingleton(List.class);
        Shank.named("2").with(scope).provideSingleton(List.class);

        scope.clearWithFinalAction((object) -> {
            if (object instanceof List) {
                ((List<String>) object).add("a");
            }
        });

        assertThat(list1, is(asList("a", "a")));
        assertThat(list2, is(asList("b", "a")));
    }

    @Test
    public void clear_whenObjectIsRemoved_provideSingletonsNewInstance() {
        Shank.registerFactory(A.class, A::new);

        final A provideSingletond = Shank.provideSingleton(A.class);

        Shank.clearUnscoped(A.class);

        final A secondProvided = Shank.provideSingleton(A.class);

        assertTrue(provideSingletond != secondProvided);
    }

    @Test
    public void withScope_and_0_argumentsreturnsObjectForScope() {
        Shank.registerFactory(A.class, A::new);

        final A provided = Shank.with(scope(B.class)).provideSingleton(A.class);

        assertTrue(provided != null);
    }

    @Test
    public void withScope_and_1_arguments_returnsObjectForScope() {
        Shank.registerFactory(List.class, Collections::singletonList);

        final List provided = Shank.with(scope(B.class)).provideSingleton(List.class, "a");

        assertTrue(provided != null);
        assertThat(provided, is(Collections.singletonList("a")));
    }

    @Test
    public void withScope_and_2_arguments_returnsObjectForScope() {
        Shank.registerFactory(List.class, (a, b) -> asList(a, b));

        final List provided = Shank.with(scope(B.class)).provideSingleton(List.class, "a", "b");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b")));
    }

    @Test
    public void withScope_and_3_arguments_returnsObjectForScope() {
        Shank.registerFactory(List.class, (a, b, c) -> asList(a, b, c));

        final List provided = Shank.with(scope(B.class)).provideSingleton(List.class, "a", "b", "c");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b", "c")));
    }

    @Test
    public void withScope_and_4_arguments_returnsObjectForScope() {
        Shank.registerFactory(List.class, (a, b, c, d) -> asList(a, b, c, d));

        final List provided = Shank.with(scope(B.class)).provideSingleton(List.class, "a", "b", "c", "d");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b", "c", "d")));
    }

    @Test
    public void withScope_whenCalledMultipleTimes_returnsSameObjectForScope() {
        Shank.registerFactory(A.class, A::new);

        final A provided = Shank.with(scope(B.class)).provideSingleton(A.class);
        final A otherProvided = Shank.with(scope(B.class)).provideSingleton(A.class);

        assertTrue(provided == otherProvided);
    }

    @Test
    public void withObjectScope_whenCalledMultipleTimes_returnsSameObjectForScope() {
        Shank.registerFactory(A.class, A::new);

        final A provided = Shank.with(scope("foo")).provideSingleton(A.class);
        final A otherProvided = Shank.with(scope("foo")).provideSingleton(A.class);

        assertTrue(provided == otherProvided);
    }

    @Test
    public void withScope_andWithClearScope_whenCalledMultipleTimes_returnsDifferentObjectForScope() {
        Shank.registerFactory(A.class, A::new);
        Scope scope = scope(OtherChildOfA.class);

        final A provided = Shank.with(scope).provideSingleton(A.class);
        scope.clear();
        scope = scope(OtherChildOfA.class);
        final A otherProvided = Shank.with(scope).provideSingleton(A.class);

        assertTrue(provided != otherProvided);
        assertNotNull(provided);
        assertNotNull(otherProvided);
    }

    @Test
    public void withDifferentEqualScope_andWithClearScopeWithFinalizingAction_clearsScope() {
        Shank.registerFactory(A.class, A::new);
        Shank.registerFactory(B.class, B::new);

        Scope scope = scope("");
        final A providedA = Shank.with(scope).provideSingleton(A.class);
        Scope secondScope = scope("");
        final B providedB = Shank.with(secondScope).provideSingleton(B.class);
        secondScope.clearWithFinalAction(o -> {});
        Scope thirdScope = scope("");
        final A otherProvidedA = Shank.with(thirdScope).provideSingleton(A.class);

        assertTrue(providedA != otherProvidedA);
        assertNotNull(providedA);
        assertNotNull(otherProvidedA);
    }

    @Test
    public void withDifferentEqualScope_andWithClearScopeWithErrorInFinalizingAction_clearsScope() {
        Shank.registerFactory(A.class, A::new);
        Shank.registerFactory(B.class, B::new);

        Scope scope = scope("");
        final A providedA = Shank.with(scope).provideSingleton(A.class);
        Scope secondScope = scope("");
        final B providedB = Shank.with(secondScope).provideSingleton(B.class);
        try {
            secondScope.clearWithFinalAction(o -> {
                throw new RuntimeException();
            });
        } catch (RuntimeException e) {
        }
        Scope thirdScope = scope("");
        final A otherProvidedA = Shank.with(thirdScope).provideSingleton(A.class);

        assertTrue(providedA != otherProvidedA);
        assertNotNull(providedA);
        assertNotNull(otherProvidedA);
    }

    @Test
    public void withDifferentEqualScope_andWithClearScope_clearsScope() {
        Shank.registerFactory(A.class, A::new);
        Shank.registerFactory(B.class, B::new);

        Scope scope = scope("");
        final A providedA = Shank.with(scope).provideSingleton(A.class);
        Scope secondScope = scope("");
        final B providedB = Shank.with(secondScope).provideSingleton(B.class);
        secondScope.clear();
        Scope thirdScope = scope("");
        final A otherProvidedA = Shank.with(thirdScope).provideSingleton(A.class);

        assertTrue(providedA != otherProvidedA);
        assertNotNull(providedA);
        assertNotNull(otherProvidedA);
    }

    @Test
    public void withDifferentEqualNamedScope_andWithClearScope_clearsScope() {
        registerNamedFactory(A.class, "a", A::new);
        registerNamedFactory(A.class, "aa", A::new);
        registerNamedFactory(B.class, "b", B::new);

        Scope scope = scope("");
        final A providedA = Shank.with(scope).named("a").provideSingleton(A.class);
        Scope secondScope = scope("");
        final B providedB = Shank.with(secondScope).named("b").provideSingleton(B.class);
        secondScope.clear();
        Scope thirdScope = scope("");
        final A otherProvidedA = Shank.with(thirdScope).named("a").provideSingleton(A.class);

        assertTrue(providedA != otherProvidedA);
        assertNotNull(providedA);
        assertNotNull(otherProvidedA);
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_0_arguments() {
        registerNamedFactory(A.class, "first", ChildOfA::new);
        registerNamedFactory(A.class, "second", OtherChildOfA::new);

        A first = Shank.with(scope(B.class)).named("first").provideSingleton(A.class);
        A second = Shank.with(scope(B.class)).named("second").provideSingleton(A.class);

        assertTrue(first != null);
        assertTrue(second != null);
        assertTrue(second != first);
        assertTrue(first instanceof ChildOfA);
        assertTrue(second instanceof OtherChildOfA);
    }

    @Test
    public void withScope_invertedBuilder_whenHasNamedFactory_returnsObjectForNameAndScope_with_0_arguments() {
        registerNamedFactory(A.class, "first", ChildOfA::new);
        registerNamedFactory(A.class, "second", OtherChildOfA::new);

        A first = Shank.named("first").with(scope(B.class)).provideSingleton(A.class);
        A second = Shank.named("second").with(scope(B.class)).provideSingleton(A.class);

        assertTrue(first != null);
        assertTrue(second != null);
        assertTrue(second != first);
        assertTrue(first instanceof ChildOfA);
        assertTrue(second instanceof OtherChildOfA);
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_1_arguments() {
        registerNamedFactory(List.class, "list", (Func1<Object, List>) Arrays::asList);

        List provided = Shank.with(scope("a scope")).named("list").provideSingleton(List.class, "a");

        assertTrue(provided != null);
        assertThat(provided, is(Collections.singletonList("a")));
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_2_arguments() {
        registerNamedFactory(List.class, "list", (a, b) -> asList(a, b));

        List provided = Shank.with(scope("a scope")).named("list").provideSingleton(List.class, "a", "b");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b")));
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_3_arguments() {
        registerNamedFactory(List.class, "list", (a, b, c) -> asList(a, b, c));

        List provided = Shank.with(scope("a scope")).named("list").provideSingleton(List.class, "a", "b", "c");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b", "c")));
    }

    @Test
    public void withScope_whenHasNamedFactory_returnsObjectForNameAndScope_with_4_arguments() {
        registerNamedFactory(List.class, "list", (a, b, c, d) -> asList(a, b, c, d));

        List provided = Shank.with(scope("a scope")).named("list").provideSingleton(List.class, "a", "b", "c", "d");

        assertTrue(provided != null);
        assertThat(provided, is(asList("a", "b", "c", "d")));
    }

    @Test
    public void withScope_whenHasNamedFactory_andScopeIsCleared_returnsDifferentObjects() {
        registerNamedFactory(A.class, "object", A::new);

        Scope scope = scope(B.class);
        A first = Shank.with(scope).named("object").provideSingleton(A.class);
        scope.clear();
        A second = Shank.with(scope).named("object").provideSingleton(A.class);

        assertTrue(first != null);
        assertTrue(second != null);
        assertTrue(second != first);
    }

    @Test
    public void withObjectScope_whenHasNamedFactory_andScopeIsCleared_returnsDifferentObjects() {
        registerNamedFactory(A.class, "object", A::new);

        Scope scope = scope("id");
        A first = Shank.with(scope).named("object").provideSingleton(A.class);
        scope.clear();
        A other = Shank.with(scope).named("object").provideSingleton(A.class);

        assertTrue(first != null);
        assertTrue(other != null);
        assertTrue(other != first);
    }

    @Test
    public void withScope_whenHasNamedFactory_calledMultipleTimes_returnsSameObjectForNameAndScope() {
        registerNamedFactory(A.class, "first", A::new);
        registerNamedFactory(A.class, "second", A::new);

        A first = Shank.with(scope(B.class)).named("first").provideSingleton(A.class);
        A second = Shank.with(scope(B.class)).named("second").provideSingleton(A.class);
        A otherFirst = Shank.with(scope(B.class)).named("first").provideSingleton(A.class);
        A otherSecond = Shank.with(scope(B.class)).named("second").provideSingleton(A.class);

        assertThat(first, is(otherFirst));
        assertThat(second, is(otherSecond));
    }

    @Test
    public void withScope_afterRegisteringFirstObject_secondObjectIsAlsoProvided() {
        Shank.registerFactory(A.class, A::new);
        Shank.registerFactory(B.class, B::new);

        final Scope scope = scope(B.class);
        A first = Shank.with(scope).provideSingleton(A.class);
        B second = Shank.with(scope).provideSingleton(B.class);

        assertNotNull(first);
        assertNotNull(second);
    }

    @Test
    public void provide_whenObjectHasNoFactory_throwsException() throws Exception {
        expectedException.expect(NoFactoryException.class);
        expectedException.expectMessage("There is no factory for " + B.class.getCanonicalName());
        Shank.provideNew(B.class);
    }

    @Test
    public void provide_whenObjectHasNoNamedFactory_throwsException() throws Exception {
        expectedException.expect(NoFactoryException.class);
        expectedException
                .expectMessage("There is no factory for " + B.class.getCanonicalName() + " with name " + "noSuchName");
        Shank.registerFactory(B.class, B::new);
        Shank.named("noSuchName").provideNew(B.class);
    }

    static class ClassWithConstructorParameters {

        private String a;
        private Integer b;

        ClassWithConstructorParameters(String a, Integer b) {
            this.a = a;
            this.b = b;
        }
    }

    static class A {

    }

    static class ChildOfA extends A {

    }

    static class OtherChildOfA extends A {

    }

    static class B {

    }
}
