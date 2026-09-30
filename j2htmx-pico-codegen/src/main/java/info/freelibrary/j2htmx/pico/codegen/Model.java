package info.freelibrary.j2htmx.pico.codegen;

import static info.freelibrary.j2htmx.pico.codegen.Model.Metadata.ON_OFF;
import static info.freelibrary.j2htmx.pico.codegen.Model.Metadata.SELF_CLOSING;
import static info.freelibrary.j2htmx.pico.codegen.Model.Node.Type.BOOLEAN;
import static info.freelibrary.j2htmx.pico.codegen.Model.Node.Type.ELEMENT;
import static info.freelibrary.j2htmx.pico.codegen.Model.Node.Type.STRING;

import info.freelibrary.util.warnings.PMD;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the parsed HTML/MathML tag and attribute definition model.
 */
@SuppressWarnings(PMD.TOO_MANY_STATIC_IMPORTS)
public class Model implements Parser.Listener {

    /** Map of element names to their node representations. */
    private final Map<String, Node> myElements;

    /** Map of attribute names to their node representations. */
    private final Map<String, Node> myAttributes;

    /**
     * Constructs a new empty Model.
     */
    public Model() {
        myElements = new LinkedHashMap<>();
        myAttributes = new LinkedHashMap<>();
    }

    /**
     * Gets all element nodes in this model.
     *
     * @return Collection of element nodes
     */
    public Collection<Node> elements() {
        return myElements.values();
    }

    /**
     * Gets all attribute nodes in this model.
     *
     * @return Collection of attribute nodes
     */
    public Collection<Node> attributes() {
        return myAttributes.values();
    }

    /**
     * Adds an element node with the specified name.
     *
     * @param name The element name
     * @return The created element node
     */
    public Node addElement(final String name) {
        return add(ELEMENT, name, myElements);
    }

    /**
     * Adds a boolean attribute node with the specified name.
     *
     * @param name The attribute name
     * @return The created attribute node
     */
    public Node addBooleanAttribute(final String name) {
        return add(BOOLEAN, name, myAttributes);
    }

    /**
     * Adds a string attribute node with the specified name.
     *
     * @param name The attribute name
     * @return The created attribute node
     */
    public Node addStringAttribute(final String name) {
        return add(STRING, name, myAttributes);
    }

    /**
     * Retrieves an element node by name.
     *
     * @param name The element name
     * @return The element node
     */
    public Node element(final String name) {
        if (!myElements.containsKey(name)) {
            throw new NodeDoesNotExist(name);
        }
        return myElements.get(name);
    }

    /**
     * Retrieves an attribute node by name.
     *
     * @param name The attribute name
     * @return The attribute node
     */
    public Node attribute(final String name) {
        if (!myAttributes.containsKey(name)) {
            throw new NodeDoesNotExist(name);
        }
        return myAttributes.get(name);
    }

    private Node add(final Node.Type type, final String name, final Map<String, Node> nodes) {
        if (nodes.containsKey(name)) {
            throw new NodeAlreadyExists(name);
        }

        final Node node = new Node(type, name);
        nodes.put(name, node);
        return node;
    }

    @Override
    public void lineCommented(final int line, final String txt) {
        // Ignore.
    }

    @Override
    public void elementDefined(final int line, final String name) {
        attempt(() -> addElement(name), line);
    }

    @Override
    public void emptyElementDefined(final int line, final String name) {
        attempt(() -> addElement(name).annotate(SELF_CLOSING), line);
    }

    @Override
    public void booleanDefined(final int line, final String name) {
        attempt(() -> addBooleanAttribute(name), line);
    }

    @Override
    public void onOffDefined(final int line, final String name) {
        attempt(() -> addBooleanAttribute(name).annotate(ON_OFF), line);
    }

    @Override
    public void stringDefined(final int line, final String name) {
        attempt(() -> addStringAttribute(name), line);
    }

    @Override
    public void attributeDefined(final int line, final String element, final String name) {
        attempt(() -> element(element).addChild(attribute(name)), line);
    }

    @Override
    @SuppressWarnings(PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES)
    public void invalidLine(final int line, final String txt) {
        throw new RuntimeException("Invalid line [" + line + "]: " + txt);
    }

    private void attempt(final Unsafe operation, final int line) {
        try {
            operation.call();
        } catch (@SuppressWarnings(PMD.AVOID_CATCHING_GENERIC_EXCEPTION) RuntimeException details) {
            throw new InvalidModel(details, line);
        }
    }

    /**
     * Metadata tags applied to model myElements and myAttributes.
     */
    public enum Metadata {
        /** Indicates an element has self-closing/void. */
        SELF_CLOSING,
        /** Indicates a boolean attribute uses "on"/"off" syntax. */
        ON_OFF,
        /** Indicates an element or attribute has obsolete. */
        OBSOLETE
    }

    /**
     * Functional interface representing an operation that may throw a RuntimeException.
     */
    @FunctionalInterface
    private interface Unsafe {

        /**
         * Executes the operation.
         *
         * @throws RuntimeException If an error occurs during execution
         */
        @SuppressWarnings({PMD.AVOID_UNCHECKED_EXCEPTIONS_IN_SIGNATURES})
        void call() throws RuntimeException;
    }

    /**
     * Represents an element or attribute node within the model.
     */
    public static final class Node {

        /** The type of this node. */
        public final Type type;

        /** The name of this node. */
        public final String name;

        /** Metadata annotations associated with this node. */
        public final List<Metadata> metadata;

        /** Child nodes under this node. */
        public final List<Node> children;

        private Node(final Type type, final String name) {
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
        public void annotate(final Metadata meta) {
            metadata.add(meta);
        }

        /**
         * Adds a child node to this node.
         *
         * @param node The child node to add
         */
        public void addChild(final Node node) {
            children.add(node);
        }

        /**
         * Checks whether this node has the specified metadata aAnnotation.
         *
         * @param anAnnotation The metadata aAnnotation to test
         * @return True if present, false otherwise
         */
        public boolean has(final Metadata anAnnotation) {
            return metadata.contains(anAnnotation);
        }

        /**
         * The type of model node.
         */
        enum Type {
            /** Element node type. */
            ELEMENT,

            /** Boolean attribute node type. */
            BOOLEAN,

            /** String attribute node type. */
            STRING
        }
    }

    /**
     * Exception thrown when the model definition contains a syntax or structural error.
     */
    public static class InvalidModel extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 8750656387999734873L;

        /**
         * Constructs an InvalidModel exception with cause and line number.
         *
         * @param cause The underlying cause
         * @param line The line number where error occurred
         */
        public InvalidModel(final Exception cause, final int line) {
            super(cause.getMessage() + ".  At line " + line, cause);
        }
    }

    /**
     * Exception thrown when attempting to add a node that already exists.
     */
    public static class NodeAlreadyExists extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 3378083241703976208L;

        /**
         * Constructs a NodeAlreadyExists exception for the given node name.
         *
         * @param name The duplicate node name
         */
        public NodeAlreadyExists(final String name) {
            super("Node already exists: " + name);
        }
    }

    /**
     * Exception thrown when referencing a node that does not exist.
     */
    public static class NodeDoesNotExist extends RuntimeException {

        @Serial
        private static final long serialVersionUID = 1363926375151005695L;

        /**
         * Constructs a NodeDoesNotExist exception for the given node name.
         *
         * @param name The missing node name
         */
        public NodeDoesNotExist(final String name) {
            super("Node does not exist: " + name);
        }
    }
}
