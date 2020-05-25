package com.example.Customer.Service;

import java.util.List;
import java.util.Optional;
import com.example.Customer.Model.Customer;

public interface CustomerServiceInterface {

	public List<Customer> getAllCustomers();

	public Optional<Customer> getCustomerById(Long customerId);

	public Customer createNewCustomer(Customer customerRequest);

	public Optional<Customer> updateCustomerById(Long customerId, Customer customerRequest);

	public Optional<Customer> deleteCustomerById(Long id);
}
