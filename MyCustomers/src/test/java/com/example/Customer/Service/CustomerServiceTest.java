package com.example.Customer.Service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.Customer.Model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import com.example.Customer.Repository.CustomerRepository;

@RunWith(SpringRunner.class)
public class CustomerServiceTest {


	@MockBean
	private CustomerRepository customerRepository;

	@InjectMocks
	CustomerService customerService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	// Test for getAllCustomers
	@Test
	public void getAllCustomerTest() throws Exception {

		List<Customer> customers = new ArrayList<>();
		Mockito.when(customerRepository.findAll()).thenReturn(customers);

		List<Customer> actualCustomers = customerService.getAllCustomers();
		verify(customerRepository, times(1)).findAll();
		verifyNoMoreInteractions(customerRepository);

		assertThat(actualCustomers, is(customers));
	}

	// Test for getCustomerById
	@Test
	public void getCustomerByIdTest() throws Exception {

		Customer customer = new Customer();
		Mockito.<Optional<Customer>>when(customerRepository.findById(Mockito.anyLong()))
				.thenReturn(Optional.of(customer));

		Optional<Customer> actualCustomer = customerService.getCustomerById(1L);
      
		verify(customerRepository, times(1)).findById(1L);
		verifyNoMoreInteractions(customerRepository);

		assertThat(actualCustomer, is(Optional.of(customer)));
	}

	// Test for createNewCustomer
	@Test
	public void createNewCustomerTest() throws Exception {

		Customer customerRequest = new Customer();

		Customer customerResponse = new Customer();

		Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customerResponse);

		Customer actualCustomer = customerService.createNewCustomer(customerRequest);

		ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);

		verify(customerRepository, times(1)).save(customerArgument.capture());
		verifyNoMoreInteractions(customerRepository);

		assertThat(actualCustomer, is(customerResponse));
	}

	// Test for updateCustomerbyId
	@Test
	public void updateCustomerByIdTest() throws Exception {

		Customer customerRequest = new Customer();

		Customer customerResponse = new Customer();

		Mockito.<Optional<Customer>>when(customerRepository.findById(1L))
		.thenReturn(Optional.of(customerResponse));

		Mockito.when(customerRepository.save(Mockito.any(Customer.class)))
		.thenReturn(customerResponse);
		
		Optional<Customer> actualCustomer = customerService.updateCustomerById(1L,customerRequest);

		ArgumentCaptor<Customer> customerArgument = ArgumentCaptor.forClass(Customer.class);
		verify(customerRepository, times(1)).findById(1L);
		verify(customerRepository,times(1)).save(customerArgument.capture());
		verifyNoMoreInteractions(customerRepository);

		assertThat(actualCustomer, is(Optional.of(customerResponse)));
			
	}
	
	//Test for deleteCustomerbyId
	@Test
	public void deleteCustomerByIdTest() throws Exception {
		
		Customer customer=new Customer();
		
		Mockito.<Optional<Customer>>when(customerRepository.findById(1L))
		.thenReturn(Optional.of(customer));
		
		//Mockito.when(customerRepository.delete(customer)).thenReturn();
		
		Object actualCustomer = customerService.deleteCustomerById(1L);

		verify(customerRepository, times(2)).findById(1L);
		verify(customerRepository, times(1)).delete(customer);
		verifyNoMoreInteractions(customerRepository);
		
		assertThat(actualCustomer, is(Optional.of(customer)));
	}
}
