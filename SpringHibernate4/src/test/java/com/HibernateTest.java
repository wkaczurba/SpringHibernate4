package com;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.db.CustomerRepository;
import com.domain.Customer;

public class HibernateTest {
	
	@Autowired
	CustomerRepository repo; 
		
	public void hibernateTest() {
		Customer customer = new Customer("Jason", "Millon", "jmillo", "zzz", "jason.millon@gmail.com");
		
		repo.save(customer);
		List<Customer> customers = repo.findAll();
		for (Customer c : customers)
			System.out.format("** READ: %s %s%n", c.getFirstname(), c.getLastname() );
	}
}
