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
     * @param aName The element name
     * @return The created element node
     */
    public Node addElement(final String aName) {
        return add(ELEMENT, aName, myElements);
    }

    /**
     * Adds a boolean attribute node with the specified name.
     *
     * @param aName The attribute name
     * @return The created attribute node
     */
    public Node addBooleanAttribute(final String aName) {
        return add(BOOLEAN, aName, myAttributes);
    }

    /**
     * Adds a string attribute node with the specified name.
     *
     * @param aName The attribute name
     * @return The created attribute node
     */
    public Node addStringAttribute(final String aName) {
        return add(STRING, aName, myAttributes);
    }

    /**
     * Retrieves an element node by name.
     *
     * @param aName The element name
     * @return The element node
     * @throws NodeDoesNotExist If the element node does not exist
     */
    public Node element(final String aName) {
        if (!myElements.containsKey(aName)) {
            throw new NodeDoesNotExist(aName);
        }
        return myElements.get(aName);
    }

    /**
     * Retrieves an attribute node by name.
     *
     * @param aName The attribute name
     * @return The attribute node
     * @throws NodeDoesNotExist If the attribute node does not exist
     */
    public Node attribute(final String aName) {
        if (!myAttributes.containsKey(aName)) {
            throw new NodeDoesNotExist(aName);
        }
        return myAttributes.get(aName);
    }

    /**
     * Adds a node of the given type and name to the map.
     *
     * @param aType The node type
     * @param aName The node name
     * @param aNodes The target nodes map
     * @return The created node
     * @throws NodeAlreadyExists If the node already exists in the map
     */
    private Node add(final Node.Type aType, final String aName, final Map<String, Node> aNodes) {
        if (aNodes.containsKey(aName)) {
            throw new NodeAlreadyExists(aName);
        }

        final Node node = new Node(aType, aName);
        aNodes.put(aName, node);
        return node;
    }

    @Override
    public void lineCommented(final int aLine, final String aTxt) {
        // Ignore.
    }

    @Override
    public void elementDefined(final int aLine, final String aName) {
        attempt(() -> addElement(aName), aLine);
    }

    @Override
    public void emptyElementDefined(final int aLine, final String aName) {
        attempt(() -> addElement(aName).annotate(SELF_CLOSING), aLine);
    }

    @Override
    public void booleanDefined(final int aLine, final String aName) {
        attempt(() -> addBooleanAttribute(aName), aLine);
    }

    @Override
    public void onOffDefined(final int aLine, final String aName) {
        attempt(() -> addBooleanAttribute(aName).annotate(ON_OFF), aLine);
    }

    @Override
    public void stringDefined(final int aLine, final String aName) {
        attempt(() -> addStringAttribute(aName), aLine);
    }

    @Override
    public void attributeDefined(final int aLine, final String anElement, final String aName) {
        attempt(() -> element(anElement).addChild(attribute(aName)), aLine);
    }

    @Override
    @SuppressWarnings(PMD.AVOID_THROWING_RAW_EXCEPTION_TYPES)
    public void invalidLine(final int aLine, final String aTxt) {
        throw new RuntimeException("Invalid line [" + aLine + "]: " + aTxt);
    }

    /**
     * Executes the given operation, wrapping any runtime exception in an InvalidModel exception.
     *
     * @param anOperation The operation to execute
     * @param aLine The current line number
     * @throws InvalidModel If the operation fails
     */
    private void attempt(final Unsafe anOperation, final int aLine) {
        try {
            anOperation.call();
        } catch (@SuppressWarnings(PMD.AVOID_CATCHING_GENERIC_EXCEPTION) RuntimeException details) {
            throw new InvalidModel(details, aLine);
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
        @SuppressWarnings({"PMD.AvoidUncheckedExceptionsInSignatures", "IllegalThrows"})
        void call() throws RuntimeException;
    }

    /**
     * Represents an element or attribute node within the model.
     */
    public static final class Node {

        /** The type of this node. */
        public final Type myType;

        /** The name of this node. */
        public final String myName;

        /** Metadata annotations associated with this node. */
        public final List<Metadata> myMetadata;

        /** Child nodes under this node. */
        public final List<Node> myChildren;

        /**
         * Constructs a new Node.
         *
         * @param aType The node type
         * @param aName The node name
         */
        private Node(final Type aType, final String aName) {
            this.myType = aType;
            this.myName = aName;
            this.myMetadata = new ArrayList<>();
            this.myChildren = new ArrayList<>();
        }

        /**
         * Adds a metadata annotation to this node.
         *
         * @param aMeta The metadata annotation to add
         */
        public void annotate(final Metadata aMeta) {
            myMetadata.add(aMeta);
        }

        /**
         * Adds a child node to this node.
         *
         * @param aNode The child node to add
         */
        public void addChild(final Node aNode) {
            myChildren.add(aNode);
        }

        /**
         * Checks whether this node has the specified metadata aAnnotation.
         *
         * @param anAnnotation The metadata aAnnotation to test
         * @return True if present, false otherwise
         */
        public boolean has(final Metadata anAnnotation) {
            return myMetadata.contains(anAnnotation);
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
         * @param aCause The underlying cause
         * @param aLine The line number where error occurred
         */
        public InvalidModel(final Exception aCause, final int aLine) {
            super(aCause.getMessage() + ".  At line " + aLine, aCause);
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
         * @param aName The duplicate node name
         */
        public NodeAlreadyExists(final String aName) {
            super("Node already exists: " + aName);
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
         * @param aName The missing node name
         */
        public NodeDoesNotExist(final String aName) {
            super("Node does not exist: " + aName);
        }
    }
}
