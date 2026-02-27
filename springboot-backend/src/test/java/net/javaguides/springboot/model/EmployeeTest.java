package net.javaguides.springboot.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testNoArgsConstructor() {
        Employee employee = new Employee();
        assertEquals(0, employee.getId());
        assertNull(employee.getFirstName());
        assertNull(employee.getLastName());
        assertNull(employee.getEmailId());
    }

    @Test
    void testParameterizedConstructor() {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com");
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("john.doe@example.com", employee.getEmailId());
    }

    @Test
    void testSetAndGetId() {
        Employee employee = new Employee();
        employee.setId(1L);
        assertEquals(1L, employee.getId());
    }

    @Test
    void testSetAndGetFirstName() {
        Employee employee = new Employee();
        employee.setFirstName("Jane");
        assertEquals("Jane", employee.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        Employee employee = new Employee();
        employee.setLastName("Smith");
        assertEquals("Smith", employee.getLastName());
    }

    @Test
    void testSetAndGetEmailId() {
        Employee employee = new Employee();
        employee.setEmailId("jane.smith@example.com");
        assertEquals("jane.smith@example.com", employee.getEmailId());
    }

    @Test
    void testAllFieldsSetViaSetters() {
        Employee employee = new Employee();
        employee.setId(5L);
        employee.setFirstName("Alice");
        employee.setLastName("Wonder");
        employee.setEmailId("alice@example.com");

        assertEquals(5L, employee.getId());
        assertEquals("Alice", employee.getFirstName());
        assertEquals("Wonder", employee.getLastName());
        assertEquals("alice@example.com", employee.getEmailId());
    }

    @Test
    void testConstructorDoesNotSetId() {
        Employee employee = new Employee("Bob", "Builder", "bob@example.com");
        assertEquals(0, employee.getId());
    }
}
