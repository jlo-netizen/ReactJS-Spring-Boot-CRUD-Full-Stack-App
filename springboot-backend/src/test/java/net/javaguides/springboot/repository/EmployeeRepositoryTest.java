package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        employee = new Employee("John", "Doe", "john.doe@example.com");
    }

    @Test
    void testSaveEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee);
        assertTrue(savedEmployee.getId() > 0);
        assertEquals("John", savedEmployee.getFirstName());
        assertEquals("Doe", savedEmployee.getLastName());
        assertEquals("john.doe@example.com", savedEmployee.getEmailId());
    }

    @Test
    void testFindAllEmployees() {
        employeeRepository.save(employee);
        Employee employee2 = new Employee("Jane", "Smith", "jane.smith@example.com");
        employeeRepository.save(employee2);

        List<Employee> employees = employeeRepository.findAll();
        assertEquals(2, employees.size());
    }

    @Test
    void testFindEmployeeById() {
        Employee savedEmployee = employeeRepository.save(employee);
        Optional<Employee> found = employeeRepository.findById(savedEmployee.getId());

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    void testFindEmployeeByIdNotFound() {
        Optional<Employee> found = employeeRepository.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        savedEmployee.setFirstName("Updated");
        savedEmployee.setLastName("Name");
        savedEmployee.setEmailId("updated@example.com");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        assertEquals("Updated", updatedEmployee.getFirstName());
        assertEquals("Name", updatedEmployee.getLastName());
        assertEquals("updated@example.com", updatedEmployee.getEmailId());
    }

    @Test
    void testDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        employeeRepository.delete(savedEmployee);

        Optional<Employee> found = employeeRepository.findById(savedEmployee.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById() {
        Employee savedEmployee = employeeRepository.save(employee);
        employeeRepository.deleteById(savedEmployee.getId());

        Optional<Employee> found = employeeRepository.findById(savedEmployee.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAllEmpty() {
        List<Employee> employees = employeeRepository.findAll();
        assertTrue(employees.isEmpty());
    }

    @Test
    void testCount() {
        employeeRepository.save(employee);
        Employee employee2 = new Employee("Jane", "Smith", "jane@example.com");
        employeeRepository.save(employee2);

        assertEquals(2, employeeRepository.count());
    }
}
