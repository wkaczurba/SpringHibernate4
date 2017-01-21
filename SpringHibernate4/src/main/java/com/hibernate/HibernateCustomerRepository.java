package com.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import com.db.CustomerRepository;
import com.domain.Customer;

@Repository
public class HibernateCustomerRepository implements CustomerRepository {
	
	@Autowired
	private SessionFactory sessionFactory; 
	
	public HibernateCustomerRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public long count() {
		// TODO: code it.
		return 0;
	}

	@Override
	public Customer getByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional	
	public Customer save(Customer customer) {
		Serializable id = currentSession().save(customer);
		return new Customer((Long) id,
				customer.getUsername(),
				customer.getPassword(),
				customer.getFirstname(),
				customer.getLastname(),
				customer.getEmail());
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Customer> findAll() {
		List<Customer> list = currentSession().createQuery("from Customer").getResultList();
		return list;

	}

}
