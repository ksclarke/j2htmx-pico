package info.freelibrary.j2htmx.pico.tags;

public interface Instance<T> {

    //to get the actual instance
    // (every implementing class would have to implement: { return this; }
    // public T self();

    // this method shows up in autocomplete.
    // this is really undesireable as it does not do anything.
    @SuppressWarnings("unchecked")
    default T self() {
        //we know that the implementing class will supply
        //its own type as the type argument.
        //therefore every instance of Instance can assume it
        //is also of type T

        return (T) this;
    }
}
