package info.freelibrary.j2htmx.pico.tags;

public abstract class DomContent implements Renderable {
    @Override
    public String toString() {
        return render();
    }
}
