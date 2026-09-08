package info.freelibrary.j2htmx.pico.tags;

/**
 * Interface that provides self-referential fluent typing for builder methods.
 *
 * @param <T> The concrete tag type implementing this interface
 */
public interface Instance<T> {

    //to get the actual instance
    // (every implementing class would have to implement: { return this; }
    // public T self();

    // this method shows up in autocomplete.
    // this is really undesireable as it does not do anything.
    /**
     * Returns this instance cast to type {@code T}.
     *
     * @return This instance as type {@code T}
     */
    @SuppressWarnings("unchecked")
    default T self() {
        //we know that the implementing class will supply
        //its own type as the type argument.
        //therefore every instance of Instance can assume it
        //is also of type T

        return (T) this;
    }
}
