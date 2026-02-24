package net.javaguides.springboot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee1 = new Employee("John", "Doe", "john.doe@example.com");
        employee1.setId(1L);

        employee2 = new Employee("Jane", "Smith", "jane.smith@example.com");
        employee2.setId(2L);
    }

    // ==================== getAllEmployees ====================

    @Test
    void getAllEmployees_ReturnsListOfEmployees() {
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeController.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getAllEmployees_ReturnsEmptyList() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> result = employeeController.getAllEmployees();

        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    // ==================== createEmployee ====================

    @Test
    void createEmployee_SavesAndReturnsEmployee() {
        Employee newEmployee = new Employee("Bob", "Brown", "bob.brown@example.com");
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        Employee result = employeeController.createEmployee(newEmployee);

        assertNotNull(result);
        assertEquals("Bob", result.getFirstName());
        assertEquals("Brown", result.getLastName());
        assertEquals("bob.brown@example.com", result.getEmailId());
        verify(employeeRepository, times(1)).save(newEmployee);
    }

    // ==================== getEmployeeById ====================

    @Test
    void getEmployeeById_ExistingId_ReturnsEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        ResponseEntity<Employee> response = employeeController.getEmployeeById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_NonExistingId_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeController.getEmployeeById(99L);
        });
        verify(employeeRepository, times(1)).findById(99L);
    }

    // ==================== updateEmployee ====================

    @Test
    void updateEmployee_ExistingId_UpdatesAndReturnsEmployee() {
        Employee updatedDetails = new Employee("John", "Updated", "john.updated@example.com");
        Employee savedEmployee = new Employee("John", "Updated", "john.updated@example.com");
        savedEmployee.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        ResponseEntity<Employee> response = employeeController.updateEmployee(1L, updatedDetails);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Updated", response.getBody().getLastName());
        assertEquals("john.updated@example.com", response.getBody().getEmailId());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee_NonExistingId_ThrowsException() {
        Employee updatedDetails = new Employee("John", "Updated", "john.updated@example.com");
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeController.updateEmployee(99L, updatedDetails);
        });
        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // ==================== deleteEmployee ====================

    @Test
    void deleteEmployee_ExistingId_DeletesAndReturnsResponse() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        doNothing().when(employeeRepository).delete(employee1);

        ResponseEntity<Map<String, Boolean>> response = employeeController.deleteEmployee(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("deleted"));
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).delete(employee1);
    }

    @Test
    void deleteEmployee_NonExistingId_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeController.deleteEmployee(99L);
        });
        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
}
