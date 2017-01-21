package com;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.config.AppConfig;
import com.db.CustomerRepository;
import com.domain.Customer;

public class HibernateTest {
	
	@Autowired
	CustomerRepository repo; 
	
	public void hibernateTest() {
		Customer customer = new Customer();
		customer.setFirstname("Jason");
		customer.setLastname("Millon");
		customer.setUsername("jmillo");
		customer.setPassword("zzz");
		customer.setEmail("jason.millon@gmail.com");		
		
		repo.save(customer);
		List<Customer> customers = repo.findAll();
		for (Customer c : customers)
			System.out.format("** READ: %s %s%n", c.getFirstname(), c.getLastname() );
	}
}
