package com.logix.freightmarketplace.common.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler("Asia/Kolkata"))
                .build();
    }

    @Test
    void validationErrorsUseStandardBadRequestResponse() throws Exception {
        mockMvc.perform(post("/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value("/validation"))
                .andExpect(jsonPath("$.timestamp").value(org.hamcrest.Matchers.endsWith("+05:30")));
    }

    @Test
    void notBlankViolationIsHandledByGlobalExceptionHandler() throws Exception {
        assertValidationFailure("{\"name\":\" \",\"value\":\"present\",\"code\":\"valid\"}");
    }

    @Test
    void notNullViolationIsHandledByGlobalExceptionHandler() throws Exception {
        assertValidationFailure("{\"name\":\"valid\",\"value\":null,\"code\":\"valid\"}");
    }

    @Test
    void sizeViolationIsHandledByGlobalExceptionHandler() throws Exception {
        assertValidationFailure("{\"name\":\"valid\",\"value\":\"present\",\"code\":\"x\"}");
    }

    private void assertValidationFailure(String requestBody) throws Exception {
        mockMvc.perform(post("/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void constraintViolationsUseStandardBadRequestResponse() throws Exception {
        mockMvc.perform(post("/constraint-violation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void malformedRequestBodyUsesStandardBadRequestResponse() throws Exception {
        mockMvc.perform(post("/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Malformed request body"));
    }

    @Test
    void unexpectedExceptionsDoNotExposeInternalDetails() throws Exception {
        mockMvc.perform(post("/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.message").value(not(containsString("database password"))));
    }

    @RestController
    static class TestController {
        @PostMapping("/validation")
        void validation(@Valid @RequestBody TestRequest request) {
        }

        @PostMapping("/constraint-violation")
        void constraintViolation() {
            throw new ConstraintViolationException("database password", java.util.Set.of());
        }

        @PostMapping("/body")
        void body(@RequestBody Map<String, String> body) {
        }

        @PostMapping("/unexpected")
        void unexpected() {
            throw new IllegalStateException("database password");
        }
    }

    record TestRequest(
            @NotBlank String name,
            @NotNull String value,
            @Size(min = 2, max = 5) String code
    ) {
    }
}
