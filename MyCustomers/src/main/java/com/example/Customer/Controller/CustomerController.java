package com.example.Customer.Controller;

//Module for controlling HttpRequests and Sending HttpResposes

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Customer.Exception.ResourceNotFoundException;
import com.example.Customer.Model.Customer;
import com.example.Customer.Service.CustomerServiceInterface;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "Customer Management System")
@CrossOrigin(origins="http://localhost:3000")
public class CustomerController {

	@Autowired
	private CustomerServiceInterface customerServiceInterface;

	// Get all Customers
	// If No Customers then raise Exception

    @ApiOperation(value = "View a list of available Customers", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not present.")
    })
	@GetMapping("/Customers")
	public List<Customer> getAllCustomers() {
		List<Customer> Customers = customerServiceInterface.getAllCustomers();
		if (Customers.isEmpty()) {
			throw new ResourceNotFoundException("No Customer Found.");
		}
		return Customers;
	}

	// Get Customers by Id
	// If Customer with required Id is not found then raise
	// ResourceNotFoundException
	
    @ApiOperation(value = "Get a Customer by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Customer"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not present."),
            @ApiResponse(code = 400, message = "Bad Request for retrieving Customer.")
        })
    @GetMapping("/Customers/{id}")
	public Customer getCustomerById(
			@ApiParam(value = "Customer id from which Customer object will retrieve", required = true)
			@PathVariable("id") Long id) {
		
		return customerServiceInterface.getCustomerById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer Not found with id:" + id));
		
	}

	// Create a new Customer
	// If Validation Constraints of any field is broken then raise
	// MethodArgumentInvalidException
    @ApiOperation(value = "Create a new Customer by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Customer"),
            @ApiResponse(code = 400, message = "Bad Request for creating Customer.")
        })
	@PostMapping("/Customers")
	public Customer createCustomer(
			@ApiParam(value = "Customer Object from to store in Database.", required = true)
			@Valid @RequestBody Customer customerRequest) {
		return customerServiceInterface.createNewCustomer(customerRequest);

	}

	// Update a new customer by Id
	// If Customer with required Id is not found then ResourceNotFoundException
	// If Validation Constraints of any field is broken then raise
	// MethodArgumentInvalidException
	@PutMapping("/Customers/{id}")
	@ApiOperation(value = "Update an existing Customer by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Customer"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not present."),
            @ApiResponse(code = 400, message = "Bad Request for updating Customer.")
        })
	public Customer updateCustomerById(@PathVariable("id") Long id, @Valid @RequestBody Customer customerRequest) {
		return customerServiceInterface.updateCustomerById(id, customerRequest)
				.orElseThrow(() -> new ResourceNotFoundException("Customer Not found with id:" + id));
	}

	// Delete a customer by id
	// If Customer with required Id is not found then ResourceNotFoundException
	@ApiOperation(value = "Delete an existing Customer by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Customer"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not present.")
        })
	@DeleteMapping("/Customers/{id}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable("id") Long id) {

			customerServiceInterface.deleteCustomerById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Customer Not found with id:" + id));
			
			return ResponseEntity.ok().build();

	}
}
