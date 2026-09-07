package info.freelibrary.j2htmx.pico.codegen.wattsi;

public interface AttributeDefinition {

    String name();

    boolean appliesTo(ElementDefinition element);

    boolean isObsolete();
}
