import kotlin.reflect.KClass

infix fun <T: Any> T?.shouldBeInstanceOf(other: KClass<T>) = assert(other.isInstance(this)) {
    println("Expected:")
    println(this!!::class)
    println("Actual:")
    println(other)
}


infix fun <T: Any> T?.shouldBeEqualTo(other: T) = assert(this == other) {
    """Values should be equal but aren't;
    Expected:
    $this
    Actual:
    $other"""
}

infix fun <T: Any> T?.shouldNotBeEqualTo(other: T) = assert(this != other) {
    println("Values should not be equal but aren't; Value")
    println(this)
}
