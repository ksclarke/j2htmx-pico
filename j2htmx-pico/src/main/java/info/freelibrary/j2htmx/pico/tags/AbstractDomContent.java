package info.freelibrary.j2htmx.pico.tags;

/**
 * Base abstract class representing any node or text content that can be placed in an HTML DOM hierarchy.
 */
public abstract class AbstractDomContent implements Renderable {

    @Override
    public String toString() {
        return render();
    }

}
