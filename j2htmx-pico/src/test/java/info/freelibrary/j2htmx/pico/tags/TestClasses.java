package info.freelibrary.j2htmx.pico.tags;

/**
 * Test model representing an employee.
 */
class Employee {

    /** The employee ID. */
    final int myId;

    /** The employee name. */
    final String myName;

    /** The employee title. */
    final String myTitle;

    /**
     * Constructs an Employee instance.
     *
     * @param anId The employee ID
     * @param aName The employee name
     * @param aTitle The employee title
     */
    Employee(final int anId, final String aName, final String aTitle) {
        this.myId = anId;
        this.myName = aName;
        this.myTitle = aTitle;
    }
}
