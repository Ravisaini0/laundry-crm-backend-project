package com.laundry.testing;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.laundry.controller.OrderController;
import com.laundry.repository.UserRepository;
import com.laundry.service.OrderService;
import com.laundry.service.PaymentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {
	

	    @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private OrderService orderService;

	    @MockBean
	    private UserRepository userRepository;

	    @MockBean
	    private PaymentService paymentService;

	    @Test
	    void shouldReturnOkWhenFetchingOrders() throws Exception {
	        mockMvc.perform(get("/api/orders"))
	                .andExpect(status().isOk());
	    
	}
}