package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
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

    @Test
    void testSaveEmployee() {
        Employee employee = new Employee("John", "Doe", "john@example.com");
        Employee saved = employeeRepository.save(employee);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals("John", saved.getFirstName());
        assertEquals("Doe", saved.getLastName());
        assertEquals("john@example.com", saved.getEmailId());
    }

    @Test
    void testFindAllEmployees() {
        employeeRepository.save(new Employee("John", "Doe", "john@example.com"));
        employeeRepository.save(new Employee("Jane", "Smith", "jane@example.com"));

        List<Employee> employees = employeeRepository.findAll();
        assertEquals(2, employees.size());
    }

    @Test
    void testFindEmployeeById() {
        Employee employee = new Employee("John", "Doe", "john@example.com");
        Employee saved = employeeRepository.save(employee);

        Optional<Employee> found = employeeRepository.findById(saved.getId());
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
        Employee employee = new Employee("John", "Doe", "john@example.com");
        Employee saved = employeeRepository.save(employee);

        saved.setFirstName("Updated");
        saved.setLastName("Name");
        saved.setEmailId("updated@example.com");
        Employee updated = employeeRepository.save(saved);

        assertEquals("Updated", updated.getFirstName());
        assertEquals("Name", updated.getLastName());
        assertEquals("updated@example.com", updated.getEmailId());
    }

    @Test
    void testDeleteEmployee() {
        Employee employee = new Employee("John", "Doe", "john@example.com");
        Employee saved = employeeRepository.save(employee);
        long id = saved.getId();

        employeeRepository.delete(saved);

        Optional<Employee> found = employeeRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById() {
        Employee employee = new Employee("John", "Doe", "john@example.com");
        Employee saved = employeeRepository.save(employee);
        long id = saved.getId();

        employeeRepository.deleteById(id);

        Optional<Employee> found = employeeRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testCount() {
        assertEquals(0, employeeRepository.count());

        employeeRepository.save(new Employee("John", "Doe", "john@example.com"));
        employeeRepository.save(new Employee("Jane", "Smith", "jane@example.com"));

        assertEquals(2, employeeRepository.count());
    }
}
