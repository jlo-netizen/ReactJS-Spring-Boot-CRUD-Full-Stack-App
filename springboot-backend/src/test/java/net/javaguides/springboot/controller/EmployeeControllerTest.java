package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // --- GET /api/v1/employees ---

    @Test
    void getAllEmployees_returnsListOfEmployees() throws Exception {
        Employee emp1 = new Employee("John", "Doe", "john@example.com");
        emp1.setId(1L);
        Employee emp2 = new Employee("Jane", "Smith", "jane@example.com");
        emp2.setId(2L);

        given(employeeRepository.findAll()).willReturn(Arrays.asList(emp1, emp2));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    @Test
    void getAllEmployees_returnsEmptyList() throws Exception {
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // --- POST /api/v1/employees ---

    @Test
    void createEmployee_returnsCreatedEmployee() throws Exception {
        Employee employee = new Employee("John", "Doe", "john@example.com");
        employee.setId(1L);

        given(employeeRepository.save(any(Employee.class))).willReturn(employee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.emailId", is("john@example.com")));
    }

    // --- GET /api/v1/employees/{id} ---

    @Test
    void getEmployeeById_returnsEmployee() throws Exception {
        Employee employee = new Employee("John", "Doe", "john@example.com");
        employee.setId(1L);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.emailId", is("john@example.com")));
    }

    @Test
    void getEmployeeById_notFound() throws Exception {
        given(employeeRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/employees/999"))
                .andExpect(status().isNotFound());
    }

    // --- PUT /api/v1/employees/{id} ---

    @Test
    void updateEmployee_returnsUpdatedEmployee() throws Exception {
        Employee existing = new Employee("John", "Doe", "john@example.com");
        existing.setId(1L);

        Employee updatedDetails = new Employee("Jane", "Smith", "jane@example.com");

        Employee savedEmployee = new Employee("Jane", "Smith", "jane@example.com");
        savedEmployee.setId(1L);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(existing));
        given(employeeRepository.save(any(Employee.class))).willReturn(savedEmployee);

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.emailId", is("jane@example.com")));
    }

    @Test
    void updateEmployee_notFound() throws Exception {
        Employee updatedDetails = new Employee("Jane", "Smith", "jane@example.com");

        given(employeeRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/employees/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());
    }

    // --- DELETE /api/v1/employees/{id} ---

    @Test
    void deleteEmployee_returnsDeletedTrue() throws Exception {
        Employee employee = new Employee("John", "Doe", "john@example.com");
        employee.setId(1L);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted", is(true)));
    }

    @Test
    void deleteEmployee_notFound() throws Exception {
        given(employeeRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/employees/999"))
                .andExpect(status().isNotFound());
    }
}
