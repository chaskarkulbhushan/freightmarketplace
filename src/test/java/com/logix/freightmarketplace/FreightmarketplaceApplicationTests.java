package com.logix.freightmarketplace;

import org.junit.jupiter.api.Test;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FreightmarketplaceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Flyway flyway;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void postgresConfigurationLoadsAndV1MigrationRunsOnlyOnce() throws Exception {
		try (var connection = dataSource.getConnection()) {
			assertEquals("PostgreSQL", connection.getMetaData().getDatabaseProductName());
			assertTrue(connection.getMetaData().getURL().startsWith("jdbc:postgresql:"));
		}

		assertEquals(1, successfulV1MigrationCount());
		flyway.migrate();
		assertEquals(1, successfulV1MigrationCount());
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

		mockMvc.perform(get("/v3/api-docs/swagger-config"))
				.andExpect(status().isOk());
	}

	@Test
	void openApiDocumentContainsProjectMetadataWithoutSecuritySchemes() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.info.title").value("Freight Marketplace API"))
				.andExpect(jsonPath("$.info.description").value("REST API for the Freight Capacity Marketplace"))
				.andExpect(jsonPath("$.info.version").value("1.0.0"))
				.andExpect(jsonPath("$.components.securitySchemes").doesNotExist())
				.andExpect(content().string(not(containsStringIgnoringCase("bearer"))))
				.andExpect(content().string(not(containsStringIgnoringCase("jwt"))));
	}

	@Test
	void swaggerUiIsPublic() throws Exception {
		mockMvc.perform(get("/swagger-ui/index.html"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/swagger-ui/swagger-ui.css"))
				.andExpect(status().isOk());
	}

	private int successfulV1MigrationCount() {
		return jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM freightmarketplace.flyway_schema_history "
						+ "WHERE version = '1' AND success = TRUE",
				Integer.class
		);
	}
}
