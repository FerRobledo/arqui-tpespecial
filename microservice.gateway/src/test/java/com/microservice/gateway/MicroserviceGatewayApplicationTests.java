package com.microservice.gateway;

import com.microservice.gateway.service.dto.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class MicroserviceGatewayApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testSaveUserSuccess() throws Exception {
		Set<String> autoridades = new HashSet<>();
		autoridades.add("USUARIO");
		UserDTO userDTO = new UserDTO("John", "Doe", autoridades);

		// Simula una solicitud POST al endpoint /api/auth/usuarios
		mockMvc.perform(post("/api/auth/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"role\":\"USUARIO\"}"))
				.andExpect(MockMvcResultMatchers.status().isCreated()) // Verifica el código de estado
				.andExpect(MockMvcResultMatchers.content().string("1")); // Verifica el cuerpo de la respuesta, que debería ser el ID del usuario
	}

	@Test
	void testSaveUserBadRequest() throws Exception {
		// Test con datos inválidos que deberían provocar un error
		mockMvc.perform(post("/api/auth/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"\", \"lastName\":\"\", \"role\":\"\"}"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()) // Espera un Bad Request
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").exists()); // Verifica que se devuelve un mensaje de error
	}
}
