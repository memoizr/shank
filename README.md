[![Build Status](https://travis-ci.org/memoizr/shank.svg?branch=master)](https://travis-ci.org/memoizr/shank)
[![codecov](https://codecov.io/gh/memoizr/shank/branch/master/graph/badge.svg)](https://codecov.io/gh/memoizr/shank)
[![](https://jitpack.io/v/memoizr/shank.svg)](https://jitpack.io/#memoizr/shank)
[![GitHub license](https://img.shields.io/github/license/kotlintest/kotlintest.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
# Shank

### TLDR TUTORIAL:

### 1. Declare your modules by extending `ShankModule`, where you specify how the object should be created
```kotlin
object MyModule: ShankModule() {
    val alwaysNew = new { -> Random() }
    val myClassDependency = singleton { -> MyClass() }
    val dependencyWithParam = singleton { p: String -> OtherClass(p) }
}
```

### 2. Then you register them at your earliest convenience
```kotlin
registerModules(MyModule, SomeOtherModule, ...)
```

### 3. Then you can inject your dependencies anywhere
```kotlin
fun myCode() {
    ...
    blah.foo()
    ...

    myClassDependency().callSomeMethodOfMyClass()

    dependencyWithParam("foooo").callSomeMethodOfOtherClass()

    alwaysNew().nextInt()

    ...
    blah.blah()
    ...
}
```

That's the end of the tutorial, you know how this library works. Now you can go have some actual coffee.

