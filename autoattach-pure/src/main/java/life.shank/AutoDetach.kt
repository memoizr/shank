package life.shank

interface Attachable

interface AttachListener<A : Attachable> {
    fun onAttach(a: A)
    fun onDetach(a: A)
    fun onDispose()
}
