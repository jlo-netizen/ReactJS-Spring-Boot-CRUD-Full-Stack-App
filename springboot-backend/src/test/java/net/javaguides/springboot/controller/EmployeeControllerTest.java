package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("John", "Doe", "john.doe@example.com");
        employee.setId(1L);
    }

    // ========== GET /api/v1/employees ==========

    @Test
    void testGetAllEmployees_returnsList() throws Exception {
        Employee employee2 = new Employee("Jane", "Smith", "jane@example.com");
        employee2.setId(2L);

        given(employeeRepository.findAll()).willReturn(Arrays.asList(employee, employee2));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    @Test
    void testGetAllEmployees_emptyList() throws Exception {
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ========== GET /api/v1/employees/{id} ==========

    @Test
    void testGetEmployeeById_success() throws Exception {
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.emailId", is("john.doe@example.com")));
    }

    @Test
    void testGetEmployeeById_notFound() throws Exception {
        given(employeeRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/employees/999"))
                .andExpect(status().isNotFound());
    }

    // ========== POST /api/v1/employees ==========

    @Test
    void testCreateEmployee_success() throws Exception {
        Employee inputEmployee = new Employee("John", "Doe", "john.doe@example.com");
        given(employeeRepository.save(any(Employee.class))).willReturn(employee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.emailId", is("john.doe@example.com")));

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // ========== PUT /api/v1/employees/{id} ==========

    @Test
    void testUpdateEmployee_success() throws Exception {
        Employee updatedDetails = new Employee("Jane", "Smith", "jane.smith@example.com");
        Employee updatedEmployee = new Employee("Jane", "Smith", "jane.smith@example.com");
        updatedEmployee.setId(1L);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        given(employeeRepository.save(any(Employee.class))).willReturn(updatedEmployee);

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.emailId", is("jane.smith@example.com")));
    }

    @Test
    void testUpdateEmployee_notFound() throws Exception {
        Employee updatedDetails = new Employee("Jane", "Smith", "jane@example.com");
        given(employeeRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/employees/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());
    }

    // ========== DELETE /api/v1/employees/{id} ==========

    @Test
    void testDeleteEmployee_success() throws Exception {
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted", is(true)));

        verify(employeeRepository, times(1)).delete(any(Employee.class));
    }

    @Test
    void testDeleteEmployee_notFound() throws Exception {
        given(employeeRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/employees/999"))
                .andExpect(status().isNotFound());
    }
}
