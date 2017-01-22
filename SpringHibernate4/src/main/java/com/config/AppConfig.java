package com.config;

import java.util.Properties;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.env.Environment;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;

import java.io.IOException;
import java.util.Arrays;

@Configuration
// Component Scan less com.hibernate - as it would cause autowiring failure (no SessionFactory)  
@ComponentScan(basePackages="com",
  excludeFilters= @Filter(type=FilterType.REGEX, pattern="com\\.hibernate\\..*"))

// Rework for Hibernate+JPA:
// 1. No SessionFactory for Hibernate+JPA
// 2. EntityManagerFactory  (ie. emf bean -> LocalContainerEntityManagerFactoryBean set with JPAVendorAdapter bean)
// 3. JPA Vendor Adapter bean (jpaVendorAdapter) set with HibernateJpaVendorAdapter()
// 4. Different transaction management:  
//    a. PlatformTransactionManager declared in an internal static class, "TransactionConfig" 
public class AppConfig {
	// What is needed:?
	// 1. DataSource (either MySQL or H2)
	// 2. PlatformTransactionManager
	// 3. SessionFactoryBean (from Hibernate)
	// Because it is not JDBC - no need for JdbcTemplate
	
	@Autowired
	Environment environment; // This is to read active profile.
	
	@Bean
	@Profile("test")
	public DataSource embeddedDataSource() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			.addScripts("classpath:com/schema.sql")
			.build(); // Add SCHEMA SQL.
	}
	
	// TODO: DriverManagerDataSource MySQL dataSource @Profile("dev")
	@Bean
	@Profile("prod")
	public DataSource jdbcDataSource() throws Exception {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/hibernate?useSSL=true&requiresSSL=true");
		ds.setUsername("root");
		ds.setPassword("root");
		//ds.getConnection().createStatement().execute(sql)
		ScriptUtils.executeSqlScript(ds.getConnection(), new ClassPathResource("com/schema.sql"));
		
		return ds;
	}
	
	// This bean will also produce "EntityManagerFactory" bean
	@Bean
	public LocalContainerEntityManagerFactoryBean emf(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setPersistenceUnitName("salesPU");
		emf.setJpaVendorAdapter(jpaVendorAdapter);
		emf.setPackagesToScan("com.domain");
		return emf;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		// Here (at least) the following are possible:
		//  - EclipseLinkJpaVendorAdapter
		//  - HibernateJpaVendorAdapter
		//  - OpenJpaVendorAdapter
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		
		adapter.setDatabase(Database.H2); // FIXME: Or MYSQL. Depends on the Profile.
		
		if (environment.acceptsProfiles("prod", "!test")) {
			System.out.println("JPAVendorAdapter: MySQL");
			adapter.setDatabase(Database.MYSQL);
			adapter.setDatabasePlatform("org.hibernate.dialect.MySQL57InnoDBDialect"); 	
		} else if (environment.acceptsProfiles("test", "!prod")) {
			System.out.println("JPAVendorAdapter: H2");
			adapter.setDatabase(Database.H2);
			adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		} else {
			throw new RuntimeException("Incorrect profiles: " + Arrays.stream(environment.getActiveProfiles()).collect(Collectors.joining(",")) );
		}		
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		
		return adapter;
	}
	
	// Configuration in "Config" -> probably could unpack this to the outer class.
	@Configuration
	@EnableTransactionManagement
	public static class TransactionConfig implements TransactionManagementConfigurer {
		@Inject
		private EntityManagerFactory emf;

		@Override
		public PlatformTransactionManager annotationDrivenTransactionManager() {
			JpaTransactionManager transactionManager = new JpaTransactionManager();
			transactionManager.setEntityManagerFactory(emf);
			return transactionManager;
		}
	}
	
}
