package com.logix.freightmarketplace;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FreightmarketplaceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void actuatorHealthIsPublicAndReportsApplicationStatus() throws Exception {
		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	void apiHealthIsPublicAndReturnsJsonStatus() throws Exception {
		mockMvc.perform(get("/api/v1/health"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	void swaggerApiDocsRemainPublic() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk());
	}

	@Test
	void openApiDocumentContainsProjectMetadataWithoutSecuritySchemes() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.info.title").value("Freight Marketplace API"))
				.andExpect(jsonPath("$.info.description").value("REST API for the Freight Capacity Marketplace"))
				.andExpect(jsonPath("$.info.version").value("1.0.0"))
				.andExpect(jsonPath("$.components.securitySchemes").doesNotExist());
	}

	@Test
	void swaggerUiIsPublic() throws Exception {
		mockMvc.perform(get("/swagger-ui/index.html"))
				.andExpect(status().isOk());
	}
}
