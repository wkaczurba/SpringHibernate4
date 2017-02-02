package com.db;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
	//long count();
	//Customer getByUsername(String username);*/
	Customer getById(Long id); // This will be automatically generated.
	//Customer save(Customer customer);
	//List<Customer> findAll();
}
