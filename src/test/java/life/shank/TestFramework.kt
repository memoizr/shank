import kotlin.reflect.KClass

infix fun <T: Any> T?.shouldBeInstanceOf(other: KClass<T>) = assert(other.isInstance(this)) {
    """Expected:
    ${this!!::class}
    To be instance of:
    $other"""
}

infix fun <T: Any> T?.shouldNotBeSameReference(other: T) = assert(this !== other) {
    """Values should not be same reference but are;"""
}

infix fun <T: Any> T?.shouldBeSameReference(other: T) = assert(this === other) {
    """Values should be same reference but aren't;
    Expected:
    $other
    Actual:
    $this"""
}

infix fun <T: Any> T?.shouldBeEqualTo(other: T) = assert(this == other) {
    """Values should be equal but aren't;
    Expected:
    $other
    Actual:
    $this"""
}

infix fun <T: Any> T?.shouldNotBeEqualTo(other: T) = assert(this != other) {
    """Values should not be equal but aren't;
    Value:
    $this"""
}
