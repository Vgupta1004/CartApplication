package com.app.cartapp.testcontroller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.cartapp.controller.UserController;
import com.app.cartapp.dto.UserRequestDTO;
import com.app.cartapp.dto.UserResponseDTO;
import com.app.cartapp.exception.ResourceNotFoundException;
import com.app.cartapp.model.UserType;
import com.app.cartapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(UserController.class)
@Import(com.app.cartapp.exception.GlobalExceptionHandler.class)
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
  
	private final ObjectMapper objectMapper = new ObjectMapper();
  
	@MockitoBean
	private UserService userService;
  
	private UserRequestDTO createRequest() {
        UserRequestDTO req = new UserRequestDTO();
        req.setFirstName("Jane");
        req.setLastName("Smith");
        req.setEmail("jane.smith@g.c");
        req.setPhone("1234567890");
        req.setUserType(UserType.CUSTOMER); 
        return req;
    }
	
	private UserResponseDTO createResponse() {
        return new UserResponseDTO(
                1L, 
                "Jane", 
                "Smith", 
                "jane.smith@g.c", 
                "1234567890", 
                UserType.CUSTOMER
        );
    }
	
	@Test
	void testCreateUser_Success() throws Exception {
	    when(userService.createUser(any(UserRequestDTO.class)))
	            .thenReturn(createResponse());

	    mockMvc.perform(post("/api/users")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(createRequest())))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.userId").value(1)) 
	            .andExpect(jsonPath("$.firstName").value("Jane"))
	            .andExpect(jsonPath("$.email").value("jane.smith@g.c"));
	}
	
	@Test
	void testCreateUser_ValidationError() throws Exception {
	    UserRequestDTO invalidReq = new UserRequestDTO();
	    invalidReq.setFirstName("");
	    invalidReq.setLastName("Smith");
	    invalidReq.setEmail("invalid-email");
	    invalidReq.setPhone("1234567890");
	    invalidReq.setUserType(UserType.CUSTOMER);

	    mockMvc.perform(post("/api/users")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(invalidReq)))
	            .andExpect(status().isBadRequest());
	}

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Jane"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(createResponse());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userType").value("CUSTOMER"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        when(userService.updateUser(eq(1L), any(UserRequestDTO.class)))
                .thenReturn(createResponse());

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

	
}
