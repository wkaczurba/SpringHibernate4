package com.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.db.CustomerRepository;
import com.domain.Customer;

@Repository
public class HibernateCustomerRepository implements CustomerRepository {
	
	@Autowired
	private SessionFactory sessionFactory; 
	
	public HibernateCustomerRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
	public Customer save(Customer customer) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(customer);
		session.getTransaction().commit();
		session.close();
		return null;
	}

	@Override
	public List<Customer> findAll() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		@SuppressWarnings("unchecked") // Not best...
		List<Customer> list = session.createQuery("from Customer").getResultList();
		
		session.close();
		return list;

	}

}
