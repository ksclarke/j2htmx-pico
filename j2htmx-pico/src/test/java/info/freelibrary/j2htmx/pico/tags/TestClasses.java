/*
 * This code was derived from j2html, which is distributed under the Apache License, Version 2.0.
 * See LICENSE-ASL-2.0.txt in the parent project's root directory for more information.
 *
 * This file was modified for j2htmx-pico by Kevin S. Clarke (my.j2htmx-pico@kevinclarke.info).
 *
 * The modifications are licensed under the Mozilla Public License, Version 2.0.
 * See LICENSE-MPL-2.0.txt in the parent project's root directory for more information.
 */

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
