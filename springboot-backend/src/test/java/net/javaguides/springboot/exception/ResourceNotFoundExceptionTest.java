package net.javaguides.springboot.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Employee not exist with id :1";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("test");
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testResponseStatusAnnotation() {
        ResponseStatus annotation = ResourceNotFoundException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }

    @Test
    void testExceptionWithDifferentMessages() {
        ResourceNotFoundException ex1 = new ResourceNotFoundException("Resource A not found");
        ResourceNotFoundException ex2 = new ResourceNotFoundException("Resource B not found");
        assertEquals("Resource A not found", ex1.getMessage());
        assertEquals("Resource B not found", ex2.getMessage());
        assertNotEquals(ex1.getMessage(), ex2.getMessage());
    }

    @Test
    void testSerialVersionUID() throws NoSuchFieldException {
        var field = ResourceNotFoundException.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        assertNotNull(field);
    }
}
