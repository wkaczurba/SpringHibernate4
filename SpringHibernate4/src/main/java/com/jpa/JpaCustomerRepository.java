package com.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.db.CustomerRepository;
import com.domain.Customer;

@Repository
public class JpaCustomerRepository implements CustomerRepository {
	
	@PersistenceContext // this is to create proxy for entityManager. 
	// Normally EntityManager is not save; the proxying of it ensures thread safety; 
	private EntityManager entityManager;

	@Override
	public long count() {
		return findAll().size(); // TODO: There is a better query I think.
	}

	@Override
	@Transactional
	public Customer getByUsername(String username) {
		return (Customer) entityManager.createQuery("select c from Customer s where s.username=?").setParameter(1, username).getSingleResult();
	}

	@Override
	@Transactional
	public Customer getById(Long id) {
		return (Customer) entityManager.find(Customer.class, id);
	}

	@Override
	@Transactional
	public Customer save(Customer customer) {
		entityManager.persist(customer);
		return customer;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Customer> findAll() {
		List<Customer> customers = entityManager.createQuery("select c from Customer c").getResultList();
		return customers;
	}
	
}
