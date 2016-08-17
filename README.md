[![Build Status](https://travis-ci.org/memoizr/shank.svg?branch=master)](https://travis-ci.org/memoizr/shank)
[![codecov](https://codecov.io/gh/memoizr/shank/branch/master/graph/badge.svg)](https://codecov.io/gh/memoizr/shank)
[![](https://jitpack.io/v/memoizr/shank.svg)](https://jitpack.io/#memoizr/shank)
[![GitHub license](https://img.shields.io/github/license/kotlintest/kotlintest.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
# Shank
Shank is a simple, fast, powerful and flexible dependency injection framework for Java (1.7 and up) and other compatible JVM languages. It is conceptually based on the Service Locator Pattern (Gamma 1994), and typesafe etherogeneous Containers (Bloch 2008). It was designed for use in memory and performance limited applications, e.g. Android apps.

#### Main features
- New instances
- Named singletons
- Scoped singletons
- Named scoped singletons
- Fast initialization (<1ms per factory even on a slow phone) 
- Lazy object instatiation
- Callback for scope destruction (handy for teardown logic)
- Dynamic injection (pass parameters to factory at runtime when requesting objects)

#### Some advantages over other frameworks
- No reflection
- No code generation (no magic!)
- Plain old Java
- Very small (\~250 methods)
- Consistent and simple API
- Very little boilerplate

### Quick introduction
Say you have a `UserPresenter` class that you want to use in your codebase in places where you can't or don't want to inject the dependency through a constructor (such as into an Android `Activity`).

First you specify the class you want to create and how by registering a factory:
```java
Shank.registerFactory(UserPresenter.class, UserPresenter::new);
```

Here `UserPresenter::new` is simply Java 8 syntax for calling a parameterless constructor, `() -> new UserPresenter()` would also work, or if you're still stuck in the past and can't use lambdas you can use an anonymous inner class. Of course Kotlin is also an option: `{ -> UserPresenter() }`

*Note that registering a factory for the same object twice will end up in the previous factory being overwritten. This behaviour is particularly useful in testing, e.g. a mock could be provided in place of the real object.*

You can then get new instances of `UserPresenter` by using:
```java
UserPresenter mUserPresenter = Shank.provideNew(UserPresenter.class);
```

Or you could use this instead to get a *global singleton*; every time you ask for it you get the same instance, for as long as the application stays in memory:
```java
UserPresenter mUserPresenter = Shank.provideSingleton(UserPresenter.class);
```

But in some cases you may want to get the same instance of an object only sometimes, and more importantly get rid of the object at some point. This is what `Scope` is for, and for as long as you ask for that object by using equal scopes, you get the same instance:

```java
Scope mScope = Scope.scope(UserActivity.class)
Shank.with(mScope).provideSingleton(UserPresenter.class);
```

When you create a `Scope` you can pass in any object that implements `Serializable` and has a meaningful `equals` and `hashcode` implementation (`Scope` is used internally as a key to a cache so it better know when two objects are equal or else it won't work!).

The neat thing about scopes is that you can clear them and the singletons will be flushed from the cache, becoming candidates for GC. Next time you'll get a new singleton instance. Of course you are in complete control of when to clear the scope by using:
 
```java 
scope.clear();
```
 
This is very useful in Android as you can free up memory when it's no longer needed:

```java 
@Override
public void onDestroy() {
    super.onDestroy();
    if (isFinishing()) {
        mScope.clear();
    }
}
```

Note that this will work even if you call `clear()` on different `Scope` instances, as long as they are logically equal.

You can also pass in a callback to `clear((object) -> {})`, which will be called for every object in the scope immediately before it's released. This may be handy if you need to execute some teardown logic.

#### Dynamic parameters: 

Sometimes you want to pass in arguments to the factory. Let's say that our `UserPresenter` now is interested in knowing the `UserId` as well, which we have already in our Activity (say it was passed in as a serializable extra or something).

First we want to make sure we can create the object properly by registering the appropriate factory:
```java
Shank.registerFactory(UserPresenter.class, (UserId userId) -> new UserPresenter(userId));
```

Then you can ask for a new instance by passing in the parameter after the name of the class you want:

```java
UserPresenter mUserPresenter = Shank.provideNew(UserPresenter.class, mUserId);
```

Up to four parameters are supported. *Please note that order and type matter: they need to match the ones specified in the factory, failing to do so will result in a `NoFactoryException`*

This works with `Scope` and singletons as well:
```java
Scope mScope = Scope.scope(UserActivity.class)
UserPresenter mUserPresenter = Shank.with(mScope).provideSingleton(UserPresenter.class, mUserId);
```

If you ask for this dependency with logically different parameters, you'll get different instances:

```java
UserId mUserId1 = new UserId("one");
UserId mUserId2 = new UserId("two");
// mUserId1.equals(mUserId2) -> false

UserPresenter mUserPresenter1 = Shank.with(mScope).provideSingleton(UserPresenter.class, mUserId1);
UserPresenter mUserPresenter2 = Shank.with(mScope).provideSingleton(UserPresenter.class, mUserId2);
// mUserPresenter1 == mUserPresenter2 -> false (different instances)
```

#### Named factories
Sometimes you may want to provide the same class but of a different variety. Say you want to provide different RX `Scheduler`s, `io` and `main`. To do that, you can register named factories:

```java
Shank.registerNamedFactory(Scheduler.class, "main", AndroidSchedulers::mainThread);
Shank.registerNamedFactory(Schedluer.class, "io", Schedulers::io);
```

At this point you can retrieve them with this syntax:

```java
Scheduler io = Shank.named("io").provideNew(Scheduler.class);
Scheduler main = Shank.named("main").provideNew(Scheduler.class);
```

To get a singleton you can use the familiar syntax:

```java
Scheduler io = Shank.named("io").provideSingleton(Scheduler.class);
```

Named objects can be tied to a `Scope` too:
```java
Scheduler io = Shank.with(mScope).named("io").provideSingleton(Scheduler.class);
```
`with` and `named` return a builder-type object, so ordering doesn't matter. The following is equivalent to the previous example:
```java
Scheduler io = Shank.named("io").with(mScope).provideSingleton(Scheduler.class);
```

#### Modules
Projects have a way of getting large quickly, so it's nice to be able to organize object creation code in a neat way. `ShankModule` is intended to be used as a place to put all your factories that should go together. Modules should have a single responsibility (to register factories that create your objects) and cohesive (focused around a feature, for example):
```java
final class AppModule implements ShankModule {
    @Override
    public void registerFactories() {
        Shank.registerNamedFactory(Scheduler.class, "main", AndroidSchedulers::mainThread);
        Shank.registerNamedFactory(Scheduler.class, "io", Schedulers::io);
    }
}
```

For convenience you can initialize all your modules together by using `ShankModuleInitializer.initializeModules(ShankModule... modules)`.
This needs to be done before your client code starts asking for dependencies, so this should be done as early as possible. On Android this usually means the `App` class. 
```java
ShankModuleInitializer.initializeModules(
        new AppModule(),
        new AnalyticsModule(),
        new LoginModule()
);
```
Reading this routine should give you a good idea of what factories are registered and when. For example, in the above snippet you can use factories registered in `AppModule` in `AnalyticsModule`, and in `LoginModule` you can use any factory registered in the previous two modules, for example:

```java
final class LoginModule implements ShankModule {
    @Override
    public void registerFactories() {
        Shank.registerFactory(LoginPresenter.class, () -> {
            Scheduler main = Shank.named("main").provideSingleton(Scheduler.class);
            Scheduler io = Shank.named("io").provideSingleton(Scheduler.class);
            Analytics analytics = Shank.provideNew(Analytics.class);
            return new LoginPresenter(analytics, main, io); 
        }
    }
}
```

Please note that dependencies for a factory should be obtained inside the lambda factory, so that they are created only when needed. Failing to do so is inefficient and it will be likely to cause bugs.

And that's it! We found this framework to be quite flexible and powerful when using it in a large Android app. The above examples only scratch the surface of what is possible to do. We are still finding new ways to leverage the flexibility as our codebase grows. We hope you'll also enjoy using this tool, and any feedback is always welcome.

### Use it

#### Gradle
Step 1: add the repository.
```groovy
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```
Step 2: add the dependency.
```groovy
dependencies {
    compile 'com.github.memoizr:shank:v1.3.1'
}
```

### Resources
Classic CoffeMaker example: https://github.com/memoizr/shank-samples

=====================================================================

#### About DI
 Dependency injection (DI) is a pattern, a way of building software, it's not a particular framework. However, there are advantages in defining a standard way of going about doing DI, so a framework can help define those standards. The typical way of injecting dependencies in Java is to pass them through a constructor. Unfortunately this is not always possible to do, for example in the case of Android Activities and Fragments, where the constructor is used exclusively by the Framework and the user of those classes has no control over their instatiation. Again, a DI framework can be of great help.

Guice is a very popular solution, but it does not always meet the performance requirements of some low-power devices. Dagger arose as a more performant and extremely compelling alternative, but it too has a few disadvantages: the building of the Object Graph, the relationship between all the different components and modules, for example are somewhat conceptually hard to understand. In my experience I found that in large teams and complex projects it becomes problematic to ensure everyone is on board and fully competent in using Dagger. There might be room for a simpler framework which could solve much of the same problems, while trying to avoid the conceptual overhead of complex graphs. Shank is an attempt at filling that need. Shank is like a much less sophisticated Dagger. But it still gets the job done.
