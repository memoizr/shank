import kotlin.reflect.KClass

infix fun <T : Any> T?.shouldBeInstanceOf(other: KClass<T>) = asserts(other.isInstance(this)) {
    """Expected:
    ${this!!::class}
    To be instance of:
    $other"""
}

infix fun <T : Any> T?.shouldNotBeSameReference(other: T) = asserts(this !== other) {
    """Values should not be same reference but are;"""
}

infix fun <T : Any> T?.shouldBeSameReference(other: T) = asserts(this === other) {
    """Values should be same reference but aren't;
    Expected:
    $other
    Actual:
    $this"""
}

infix fun <T : Any> T?.shouldBeEqualTo(other: T) = asserts(this == other) {
    """Values should be equal but aren't;
    Expected:
    $other
    Actual:
    $this"""
}

infix fun <T : Any> T?.shouldNotBeEqualTo(other: T) = asserts(this != other) {
    """Values should not be equal but are;
    Value:
    $this"""
}

public inline fun asserts(value: Boolean, lazyMessage: () -> Any) {
    if (!value) {
        val message = lazyMessage()
        throw AssertionError(message)
    }
}
