package info.freelibrary.j2htmx.pico.codegen;

import java.util.*;

import static info.freelibrary.j2htmx.pico.codegen.Model.Metadata.ON_OFF;
import static info.freelibrary.j2htmx.pico.codegen.Model.Metadata.SELF_CLOSING;
import static info.freelibrary.j2htmx.pico.codegen.Model.Node.Type.*;

/**
 * Represents the parsed HTML/MathML tag and attribute definition model.
 */
public class Model implements Parser.Listener {

    private Map<String, Node> elements;
    private Map<String, Node> attributes;

    /**
     * Constructs a new empty Model.
     */
    public Model() {
        elements = new LinkedHashMap<>();
        attributes = new LinkedHashMap<>();
    }

    /**
     * Gets all element nodes in this model.
     *
     * @return Collection of element nodes
     */
    public Collection<Node> elements(){
        return elements.values();
    }

    /**
     * Gets all attribute nodes in this model.
     *
     * @return Collection of attribute nodes
     */
    public Collection<Node> attributes(){
        return attributes.values();
    }

    /**
     * Adds an element node with the specified name.
     *
     * @param name The element name
     * @return The created element node
     */
    public Node addElement(String name) {
        return add(ELEMENT, name, elements);
    }

    /**
     * Adds a boolean attribute node with the specified name.
     *
     * @param name The attribute name
     * @return The created attribute node
     */
    public Node addBooleanAttribute(String name) {
        return add(BOOLEAN, name, attributes);
    }

    /**
     * Adds a string attribute node with the specified name.
     *
     * @param name The attribute name
     * @return The created attribute node
     */
    public Node addStringAttribute(String name) {
        return add(STRING, name, attributes);
    }

    /**
     * Retrieves an element node by name.
     *
     * @param name The element name
     * @return The element node
     */
    public Node element(String name) {
        if (!elements.containsKey(name)) {
            throw new NodeDoesNotExist(name);
        }
        return elements.get(name);
    }

    /**
     * Retrieves an attribute node by name.
     *
     * @param name The attribute name
     * @return The attribute node
     */
    public Node attribute(String name) {
        if (!attributes.containsKey(name)) {
            throw new NodeDoesNotExist(name);
        }
        return attributes.get(name);
    }

    private Node add(Node.Type type, String name, Map<String, Node> nodes) {
        if (nodes.containsKey(name)) {
            throw new NodeAlreadyExists(name);
        }

        Node node = new Node(type, name);
        nodes.put(name, node);
        return node;
    }

    @Override
    public void lineCommented(int line, String txt) {
        // Ignore.
    }

    @Override
    public void elementDefined(int line, String name) {
        attempt(() -> addElement(name), line);
    }

    @Override
    public void emptyElementDefined(int line, String name) {
        attempt(() -> addElement(name).annotate(SELF_CLOSING), line);
    }

    @Override
    public void booleanDefined(int line, String name) {
        attempt(() -> addBooleanAttribute(name), line);
    }

    @Override
    public void onOffDefined(int line, String name) {
        attempt(() -> addBooleanAttribute(name).annotate(ON_OFF), line);
    }

    @Override
    public void stringDefined(int line, String name) {
        attempt(() -> addStringAttribute(name), line);
    }

    @Override
    public void attributeDefined(int line, String element, String name) {
        attempt(() -> element(element).addChild(attribute(name)), line);
    }

    @Override
    public void invalidLine(int line, String txt) {
        throw new RuntimeException("Invalid line [" + line + "]: " + txt);
    }

    @FunctionalInterface
    private interface Unsafe {
        void call() throws RuntimeException;
    }

    private void attempt(Unsafe operation, int line) {
        try {
            operation.call();
        } catch (RuntimeException e) {
            throw new InvalidModel(e, line);
        }
    }

    /**
     * Represents an element or attribute node within the model.
     */
    public static class Node {
        enum Type {
            ELEMENT,
            BOOLEAN,
            STRING
        }

        /** The type of this node. */
        public final Type type;

        /** The name of this node. */
        public final String name;

        /** Metadata annotations associated with this node. */
        public final List<Metadata> metadata;

        /** Child nodes under this node. */
        public final List<Node> children;

        private Node(Type type, String name) {
            this.type = type;
            this.name = name;
            this.metadata = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        /**
         * Adds a metadata annotation to this node.
         *
         * @param meta The metadata annotation to add
         */
        public void annotate(Metadata meta) {
            metadata.add(meta);
        }

        /**
         * Adds a child node to this node.
         *
         * @param node The child node to add
         */
        public void addChild(Node node) {
            children.add(node);
        }

        /**
         * Checks whether this node has the specified metadata annotation.
         *
         * @param annotation The metadata annotation to test
         * @return True if present, false otherwise
         */
        public boolean is(Metadata annotation){
            return metadata.contains(annotation);
        }
    }

    /**
     * Metadata tags applied to model elements and attributes.
     */
    public enum Metadata {
        /** Indicates an element is self-closing/void. */
        SELF_CLOSING,
        /** Indicates a boolean attribute uses "on"/"off" syntax. */
        ON_OFF,
        /** Indicates an element or attribute is obsolete. */
        OBSOLETE
    }

    /**
     * Exception thrown when the model definition contains a syntax or structural error.
     */
    public static class InvalidModel extends RuntimeException {

        private static final long serialVersionUID = 8750656387999734873L;

        /**
         * Constructs an InvalidModel exception with cause and line number.
         *
         * @param cause The underlying cause
         * @param line The line number where error occurred
         */
        public InvalidModel(Exception cause, int line) {
            super(cause.getMessage() + ".  At line " + line, cause);
        }
    }

    /**
     * Exception thrown when attempting to add a node that already exists.
     */
    public static class NodeAlreadyExists extends RuntimeException {

        private static final long serialVersionUID = 3378083241703976208L;

        /**
         * Constructs a NodeAlreadyExists exception for the given node name.
         *
         * @param name The duplicate node name
         */
        public NodeAlreadyExists(String name) {
            super("Node already exists: " + name);
        }
    }

    /**
     * Exception thrown when referencing a node that does not exist.
     */
    public static class NodeDoesNotExist extends RuntimeException {

        private static final long serialVersionUID = 1363926375151005695L;

        /**
         * Constructs a NodeDoesNotExist exception for the given node name.
         *
         * @param name The missing node name
         */
        public NodeDoesNotExist(String name) {
            super("Node does not exist: " + name);
        }
    }
}
