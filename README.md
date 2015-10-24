# Shank
Shank is a lightweight, simple and performant dependency injection framework for Java. It is conceptually based on the Service Locator Pattern (Gamma 1994), and Typesafe Etherogeneous Containers (Bloch 2008). The API has been very loosely inspired by Dagger.

####Main features
- Scoped singletons
- Bounded scoped singletons
- Named singletons
- Bounded named singletons
- No upfront object graph creation
- Lazy object instatiation

####Some advantages over other frameworks
- No reflection
- No code generation (no magic!)
- Plain old Java

####Usage
Plain object: 
returns a `StringFormatter` instance provided by the factory.
```      
Shank.provide(StringFormatter.class);
```

Plain object by name: 
returns a `Scheduler` instance associated to a string identifier "io" provided by the factory.
```      
Shank.provideNamed(Scheduler.class, "io");
```

Scoped singleton: 
returns the same `StreamPresenter` instance provided by the factory every time a `StreamActivity` asks for it.
```      
Shank.withScope(StreamActivity.class).provide(StreamPresenter.class);
```

Bound Scoped singleton: 
returns the same `StreamPresenter` instance provided by the factory every time a `StreamActivity` asks for it, until `endOfLifeObservable` emits an item. After that the cached object is removed from the cache.
```      
Shank.withBoundScope(StreamActivity.class, endOfLifeObservable).provide(StreamPresenter.class);
```

Anonymous factory:
```
Shank.registerFactory(SessionHandler.class, SessionHandler::new);
```

Named factory:
```
Shank.registerNamedFactory(Scheduler.class, "ui", AndroidSchedulers::mainThread);
```

Modules:
```
final class AppModule implements ShankModule {
    @Override
    public void registerFactories() {
        Shank.registerFactory(SessionHandler.class, DefaultSessionHandler::new);

        Shank.registerNamedFactory(Scheduler.class, "ui", AndroidSchedulers::mainThread);
        Shank.registerNamedFactory(Scheduler.class, "io", Schedulers::io);
    }
}
```

Module initialization:
```
ShankModuleRegister.registerModules(
        new AppModule(),
        new DataModule(),
        new LoginModule()
);
```



####Motivation
Dependency injection (DI) is a pattern, a way of building software, it's not a particular framework. However, there are advantages in defining a standard way of going about doing DI, so a framework can help define those standards. The typical way of injecting dependencies in Java is to pass them through a constructor. Unfortunately this is not always possible to do, for example in the case of Android Activities and Fragments, where the constructor is used exclusively by the Framework and the user of those classes has no control over their instatiation. Again, a DI framework can be of great help.

Guice is a very popular solution, but it does not always meet the performance requirements of some low-power devices. Dagger arose as a more performant and extremely compelling alternative, but it too has a few disadvantages: the building of the Object Graph, the relationship between all the different components and modules, for example are somewhat conceptually hard to understand. In my experience I found that in large teams and complex projects it becomes problematic to ensure everyone is on board and fully competent in using Dagger. There might be room for a simpler framework which could solve much of the same problems, while trying to avoid the conceptual overhead of complex graphs. Shank is an attempt at filling that need. Shank is like a much less sophisticated Dagger. But it still gets the job done.

####Gradle
Step 1: add the repository.
```
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```
Step 2: add the dependency.
```
dependencies {
    compile 'com.github.memoizr:shank:v0.1.12'
}
```
