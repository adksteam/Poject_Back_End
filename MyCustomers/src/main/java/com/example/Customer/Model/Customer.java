package com.example.Customer.Model;

//Module for storing data in fields

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "Customers")
public class Customer {

	// Primary Key
	// Auto Increment
	@Id
	@GeneratedValue
    @ApiModelProperty(notes = "The database generated Customer ID")
	@Column(name = "Customer_Id")
	private Long id;

	@NotNull(message = "Name must not be empty")
	@ApiModelProperty(notes = "Provided Customer Name")
	@Column(name = "Name")
	@Pattern(regexp = "^[A-Za-z_ ]+", message = "Please enter Name in correct format.")
	private String name;

	@NotNull(message = "Email should not be missing")
	@Email(message = "Email format should be like customer@db.com")
	@ApiModelProperty(notes = "Provided Customer Email")
	@Column(name = "Email")
	private String email;

	@NotNull(message = "Address can not be empty.")
	@ApiModelProperty(notes = "Provided Customer Address")
	@Column(name = "Address")
	@Pattern(regexp = "^[A-Za-z0-9_ ,]+", message = "Please enter Address in correct format.")
	private String address;

	@NotNull(message = "Account Type can not be empty.")
	@ApiModelProperty(notes = "Provided Customer Account Type")
	@Column(name = "Account_Type")
	@Pattern(regexp = "^[A-Za-z_ ]+", message = "Please enter Account Type in correct format.")
	private String accountType;

	@NotNull(message = "Account Number can not be empty.")
	@Column(name = "Account_No")
	@ApiModelProperty(notes = "Provided Customer Account Number")
	@Pattern(regexp = "^[A-Z0-9]+", message = "Please enter Account No in correct format.")
	private String accountNo;

	@Column(name = "Account_Balance", columnDefinition = "Decimal(10,2) default '00.00'")
	@ApiModelProperty(notes = "Provided Customer Account Balance")
	private double accountBalance;

	public Customer(Long id,
			@NotNull(message = "Name must not be empty") @Pattern(regexp = "^[A-Za-z_ ]+", message = "Please enter Name in correct format.") String name,
			@NotNull(message = "Email should not be missing") @Email(message = "Email format should be like customer@db.com") String email,
			@NotNull(message = "Address can not be empty.") @Pattern(regexp = "^[A-Za-z0-9_ ,]+", message = "Please enter Address in correct format.") String address,
			@NotNull(message = "Account Type can not be empty.") @Pattern(regexp = "^[A-Za-z_ ]+", message = "Please enter Account Type in correct format.") String accountType,
			@NotNull(message = "Account Number can not be empty.") @Pattern(regexp = "^[A-Z0-9]+", message = "Please enter Account Number in correct format.") String accountNo,
			double accountBalance) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.accountType = accountType;
		this.accountNo = accountNo;
		this.accountBalance = accountBalance;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Customer() {
		super();
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", accountType="
				+ accountType + ", accountNo=" + accountNo + "]";
	}

}
