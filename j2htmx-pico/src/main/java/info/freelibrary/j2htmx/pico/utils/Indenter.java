package info.freelibrary.j2htmx.pico.utils;

@FunctionalInterface
public interface Indenter {
    String indent(int level, String text);
}
