package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.config.AppConfig;

@ActiveProfiles({"prod"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class HibernateProdProfile extends HibernateTest {

	@Test
	@Override
	public void hibernateTest() {
		super.hibernateTest();
	}
}
