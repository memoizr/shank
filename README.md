# Shank
Shank is a simple, fast, powerful and flexible dependency injection framework for Java (1.7 and up) and other compatible JVM languages. It is conceptually based on the Service Locator Pattern (Gamma 1994), and Typesafe Etherogeneous Containers (Bloch 2008). 

#### Main feature
- New instances
- Named singletons
- Scoped singletons
- Named scoped singletons
- Extremely fast initialization (>1ms per factory even on a slow phone) 
- Lazy object instatiation
- Callback for scope destruction (handy for teardown logic)
- Dynamic injection (pass parameters to factory at runtime when requesting objects)

####Some advantages over other frameworks
- No reflection
- No code generation (no magic!)
- Plain old Java
- Very small (\~250 methods)

#### Usage

Specify what classes you want to instantiate and how, here's a simple example:
```java
Shank.registerFactory(Example.class, Example::new);
```

You can then get new instances of `Example` by using:
```java
Example example = Shank.provideNew(Example.class);
```

You could use this instead to get a *global singleton*, every time you ask for it you get the same instance, for as long as the application stays in memory:
```java
Example example = Shank.provideSingleton(Example.class);
```


Sometimes you want to get the same instance of an object only sometimes, so you can create a scope, and for long as you ask for that object by using equal scopes, you get the same instance:
```java
Shank.with(scope("reference")).provide(StreamPresenter.class);
```

Here 

# New instance with dynamic parameters: 
This returns a new instance of `UserPresenter`. An anonymous factory for 'UserPresenter' must have been already registered.
```java
Shank.provideNew(UserPresenter.class, userId);
```


Named new object:
returns a new instance of `Scheduler`, as provided by the named factory.
```java
Shank.named("io").provideNew(Scheduler.class);
```

Named singleton:
returns a cached (or new if none exists) instance of `Scheduler` as provided by the named factory.
```java
Shank.named("io").provideSingleton(Scheduler.class);
```


## How you create objects:

Anonymous factory:

Named factory:
```java
Shank.registerNamedFactory(Example.class, "ui", AndroidSchedulers::mainThread);
```

## Where you put the factories:
Modules:
```java
final class AppModule implements ShankModule {
    @Override
    public void registerFactories() {
        Shank.registerFactory(SessionHandler.class, DefaultSessionHandler::new);

        Shank.registerNamedFactory(Scheduler.class, "ui", AndroidSchedulers::mainThread);
        Shank.registerNamedFactory(Scheduler.class, "io", Schedulers::io);
    }
}
```

## How you build the dependency graph:
```java
ShankModuleInitializer.initializeModules(
        new AppModule(),
        new DataModule(),
        new LoginModule()
);
```



####Motivation
Dependency injection (DI) is a pattern, a way of building software, it's not a particular framework. However, there are advantages in defining a standard way of going about doing DI, so a framework can help define those standards. The typical way of injecting dependencies in Java is to pass them through a constructor. Unfortunately this is not always possible to do, for example in the case of Android Activities and Fragments, where the constructor is used exclusively by the Framework and the user of those classes has no control over their instatiation. Again, a DI framework can be of great help.

Guice is a very popular solution, but it does not always meet the performance requirements of some low-power devices. Dagger arose as a more performant and extremely compelling alternative, but it too has a few disadvantages: the building of the Object Graph, the relationship between all the different components and modules, for example are somewhat conceptually hard to understand. In my experience I found that in large teams and complex projects it becomes problematic to ensure everyone is on board and fully competent in using Dagger. There might be room for a simpler framework which could solve much of the same problems, while trying to avoid the conceptual overhead of complex graphs. Shank is an attempt at filling that need. Shank is like a much less sophisticated Dagger. But it still gets the job done.

####Samples
https://github.com/memoizr/shank-samples
https://github.com/memoizr/twitter-demo

####Gradle
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
