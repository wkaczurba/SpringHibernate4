package com.db;

import java.util.List;

import com.domain.Customer;

public interface CustomerRepository {
	long count();
	Customer getByUsername(String username);
	Customer getById(Long id);
	Customer save(Customer customer);
	List<Customer> findAll();
}
