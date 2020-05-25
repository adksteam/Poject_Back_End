package com.example.Customer.ControllerTest;

//Unit Testing of Controller Module

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.Customer.Controller.CustomerController;
import com.example.Customer.ErrorHandler.ErrorResponse;
import com.example.Customer.Exception.ResourceNotFoundException;
import com.example.Customer.Model.Customer;
import com.example.Customer.Service.CustomerServiceInterface;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_ = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype());

	@MockBean
	private CustomerServiceInterface customerServiceInterface;

	@MockBean
	private ErrorResponse errorResponse;

	@Autowired
	private WebApplicationContext webApplicationContext;

	// Convert object to json
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

	@Before
	public void setUp() {

		Mockito.reset(customerServiceInterface);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	// Test to check getAllCustomers
	@Test
	public void getAllCustomersTest() throws Exception {
		Customer testCustomer = new Customer(1L, "Kushagra Tiwari", "kushagra@gmail.com", "Itarsi, MP",
				"Savings Account", "DB1234567890", 100.00);

		Mockito.when(customerServiceInterface.getAllCustomers()).thenReturn(Arrays.asList(testCustomer));

		mockMvc.perform(get("/Customers")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_)).andExpect(jsonPath("$[*]", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].name", is("Kushagra Tiwari")))
				.andExpect(jsonPath("$[0].email", is("kushagra@gmail.com")))
				.andExpect(jsonPath("$[0].address", is("Itarsi, MP")))
				.andExpect(jsonPath("$[0].accountType", is("Savings Account")))
				.andExpect(jsonPath("$[0].accountNo", is("DB1234567890")))
				.andExpect(jsonPath("$[0].accountBalance", is(100.00)));
		verify(customerServiceInterface, times(1)).getAllCustomers();
		verifyNoMoreInteractions(customerServiceInterface);

	}

	// Test to check getCustomerById
	@Test
	public void getCustomerByIdTest() throws Exception {
		Customer testCustomer = new Customer(1L, "Kushagra Tiwari", "kushagra@gmail.com", "Itarsi, MP",
				"Savings Account", "DB1234567890", 100.00);

		Mockito.<Optional<Customer>>when(customerServiceInterface.getCustomerById(Mockito.anyLong()))
				.thenReturn(Optional.of(testCustomer));

		mockMvc.perform(get("/Customers/{id}", Mockito.anyLong())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_)).andExpect(jsonPath("$.*", hasSize(7)))
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Kushagra Tiwari")))
				.andExpect(jsonPath("$.email", is("kushagra@gmail.com")))
				.andExpect(jsonPath("$.address", is("Itarsi, MP")))
				.andExpect(jsonPath("$.accountType", is("Savings Account")))
				.andExpect(jsonPath("$.accountNo", is("DB1234567890")))
				.andExpect(jsonPath("$.accountBalance", is(100.00)));
		verify(customerServiceInterface, times(1)).getCustomerById(Mockito.anyLong());
		verifyNoMoreInteractions(customerServiceInterface);

	}

	// Test for createCustomer()
	@Test
	public void createCustomerTest() throws Exception {

		Customer customerRequest = new Customer();
		customerRequest.setName("Kushagra Tiwari");
		customerRequest.setAddress("Itarsi, MP");
		customerRequest.setEmail("kushagra@gmail.com");
		customerRequest.setAccountNo("DB1234567890");
		customerRequest.setAccountType("Savings Account");
		customerRequest.setAccountBalance(100.00);

		Customer customerResponse = new Customer(1L, "Kushagra Tiwari", "kushagra@gmail.com", "Itarsi, MP",
				"Savings Account", "DB1234567890", 100.00);

		Mockito.when(customerServiceInterface.createNewCustomer(Mockito.any(Customer.class)))
				.thenReturn(customerResponse);

		mockMvc.perform(
				post("/Customers").contentType(APPLICATION_JSON_).content(convertObjectToJsonBytes(customerRequest)))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_))
				.andExpect(jsonPath("$.*", hasSize(7))).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Kushagra Tiwari")))
				.andExpect(jsonPath("$.email", is("kushagra@gmail.com")))
				.andExpect(jsonPath("$.address", is("Itarsi, MP")))
				.andExpect(jsonPath("$.accountType", is("Savings Account")))
				.andExpect(jsonPath("$.accountNo", is("DB1234567890")))
				.andExpect(jsonPath("$.accountBalance", is(100.00)));

		ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
		verify(customerServiceInterface, times(1)).createNewCustomer(customerCaptor.capture());
		verifyNoMoreInteractions(customerServiceInterface);

		Customer customer = customerCaptor.getValue();

		assertThat(customer.getId(), is(customerRequest.getId()));
		assertThat(customer.getName(), is(customerRequest.getName()));
		assertThat(customer.getEmail(), is(customerRequest.getEmail()));
		assertThat(customer.getAddress(), is(customerRequest.getAddress()));
		assertThat(customer.getAccountType(), is(customerRequest.getAccountType()));
		assertThat(customer.getAccountNo(), is(customerRequest.getAccountNo()));
		assertThat(customer.getAccountBalance(), is(customerRequest.getAccountBalance()));

	}

	// Test for updateCustomerById()
	@Test
	public void updateCustomerByIdTest() throws Exception {

		Customer customerRequest = new Customer();
		customerRequest.setName("Kushagra Tiwari");
		customerRequest.setAddress("Itarsi, MP");
		customerRequest.setEmail("kushagra@gmail.com");
		customerRequest.setAccountNo("DB1234567890");
		customerRequest.setAccountType("Savings Account");
		customerRequest.setAccountBalance(100.00);

		Customer customerResponse = new Customer(1L, "Kushagra Tiwari", "kushagra@gmail.com", "Bhopal, MP",
				"Current Account", "DB1234567890", 1000.00);
		Mockito.<Optional<Customer>>when(
				customerServiceInterface.updateCustomerById(Mockito.anyLong(), Mockito.any(Customer.class)))
				.thenReturn(Optional.of(customerResponse));

		mockMvc.perform(put("/Customers/{id}", 1L).contentType(APPLICATION_JSON_)
				.content(convertObjectToJsonBytes(customerRequest))).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_)).andExpect(jsonPath("$.*", hasSize(7)))
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Kushagra Tiwari")))
				.andExpect(jsonPath("$.email", is("kushagra@gmail.com")))
				.andExpect(jsonPath("$.address", is("Bhopal, MP")))
				.andExpect(jsonPath("$.accountType", is("Current Account")))
				.andExpect(jsonPath("$.accountNo", is("DB1234567890")))
				.andExpect(jsonPath("$.accountBalance", is(1000.00)));

		ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
		verify(customerServiceInterface, times(1)).updateCustomerById(Mockito.anyLong(), customerCaptor.capture());
		verifyNoMoreInteractions(customerServiceInterface);

		Customer customer = customerCaptor.getValue();

		assertThat(customer.getId(), is(customerRequest.getId()));
		assertThat(customer.getName(), is(customerRequest.getName()));
		assertThat(customer.getEmail(), is(customerRequest.getEmail()));
		assertThat(customer.getAddress(), is(customerRequest.getAddress()));
		assertThat(customer.getAccountType(), is(customerRequest.getAccountType()));
		assertThat(customer.getAccountNo(), is(customerRequest.getAccountNo()));
		assertThat(customer.getAccountBalance(), is(customerRequest.getAccountBalance()));

	}

	// Test for delteCustomerById
	@Test
	public void deleteCustomerByIdTest() throws Exception {

		Mockito.<Optional<Customer>>when(customerServiceInterface.deleteCustomerById(Mockito.anyLong()))
				.thenReturn(Optional.of(new Customer()));

		mockMvc.perform(delete("/Customers/{id}", Mockito.anyLong())).andExpect(status().isOk());

		verify(customerServiceInterface, times(1)).deleteCustomerById(Mockito.anyLong());
		verifyNoMoreInteractions(customerServiceInterface);

	}

	// Testing ExceptionHandlers

	// Test for Find All
	@Test
	public void geAlltCustomersExceptionTest() throws Exception {

		Mockito.when(customerServiceInterface.getAllCustomers()).thenThrow(new ResourceNotFoundException(""));

		mockMvc.perform(get("/Customers")).andExpect(status().isNotFound());

		verify(customerServiceInterface, times(1)).getAllCustomers();
		verifyNoMoreInteractions(customerServiceInterface);

	}

	// Test for findById
	@Test
	public void getCustomerByIdExceptionTest() throws Exception {

		Mockito.when(customerServiceInterface.getCustomerById(Mockito.anyLong()))
				.thenThrow(new ResourceNotFoundException(""));

		mockMvc.perform(get("/Customers/{id}", 1L)).andExpect(status().isNotFound());

		verify(customerServiceInterface, times(1)).getCustomerById(Mockito.anyLong());
		verifyNoMoreInteractions(customerServiceInterface);

	}

	// Test for deleteById
	@Test
	public void deleteCustomerByIdExceptionTest() throws Exception {

		Mockito.when(customerServiceInterface.deleteCustomerById(Mockito.anyLong()))
				.thenThrow(new ResourceNotFoundException(""));

		mockMvc.perform(delete("/Customers/{id}", 1L)).andExpect(status().isNotFound());

		verify(customerServiceInterface, times(1)).deleteCustomerById(Mockito.anyLong());
		verifyNoMoreInteractions(customerServiceInterface);

	}

	// Test for updateById
	@Test
	public void updateCustomerByIdException1Test() throws Exception {

		Customer customerRequest = new Customer();
		customerRequest.setName("Kushagra Tiwari");
		customerRequest.setAddress("Itarsi, MP");
		customerRequest.setEmail("kushagra@gmail.com");
		customerRequest.setAccountNo("DB1234567890");
		customerRequest.setAccountType("Savings Account");
		customerRequest.setAccountBalance(100.00);

		Mockito.when(customerServiceInterface.updateCustomerById(Mockito.anyLong(), Mockito.any(Customer.class)))
				.thenThrow(new ResourceNotFoundException(""));

		mockMvc.perform(put("/Customers/{id}", 1L).contentType(APPLICATION_JSON_)
				.content(convertObjectToJsonBytes(customerRequest))).andExpect(status().isNotFound());

		ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
		verify(customerServiceInterface, times(1)).updateCustomerById(Mockito.anyLong(), customerCaptor.capture());
		verifyNoMoreInteractions(customerServiceInterface);

		Customer customer = customerCaptor.getValue();

		assertThat(customer.getId(), is(customerRequest.getId()));
		assertThat(customer.getName(), is(customerRequest.getName()));
		assertThat(customer.getEmail(), is(customerRequest.getEmail()));
		assertThat(customer.getAddress(), is(customerRequest.getAddress()));
		assertThat(customer.getAccountType(), is(customerRequest.getAccountType()));
		assertThat(customer.getAccountNo(), is(customerRequest.getAccountNo()));
		assertThat(customer.getAccountBalance(), is(customerRequest.getAccountBalance()));

	}

	// Test for update By Id InvalidMethodArgumentException
	@Test
	public void updateCustomerByIdException2Test() throws Exception {

		Customer customerRequest = new Customer();
		customerRequest.setName("Kushagra Tiwari");
		customerRequest.setAddress("Itarsi, MP");
		customerRequest.setEmail("kushagra@gmail.com");
		customerRequest.setAccountNo("DB1234567890!@#~$");
		customerRequest.setAccountType("Savings Account");
		customerRequest.setAccountBalance(100.00);

		mockMvc.perform(put("/Customers/{id}", 1L).contentType(APPLICATION_JSON_)
				.content(convertObjectToJsonBytes(customerRequest))).andExpect(status().isBadRequest());

		verifyNoMoreInteractions(customerServiceInterface);
	}

	// Test for update By Id InvalidMethodArgumentException
	@Test
	public void createCustomerByIdExceptionTest() throws Exception {

		Customer customerRequest = new Customer();
		customerRequest.setName("Kushagra Tiwari");
		customerRequest.setAddress("Itarsi, MP");
		customerRequest.setEmail("kushagra@gmail.com");
		customerRequest.setAccountNo("DB1234567890!@#~$");
		customerRequest.setAccountType("Savings Account");
		customerRequest.setAccountBalance(100.00);

		mockMvc.perform(
				post("/Customers").contentType(APPLICATION_JSON_).content(convertObjectToJsonBytes(customerRequest)))
				.andExpect(status().isBadRequest());

		verifyNoMoreInteractions(customerServiceInterface);
	}

}
