package com.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
	// What is needed:?
	// 1. DataSource (either MySQL or H2)
	// 2. PlatformTransactionManager
	// 3. SessionFactoryBean (from Hibernate)
	// Because it is not JDBC - no need for JdbcTemplate
	
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
	
	// TODO: DBCP(MySQL) dataSource @Profile("prod")
	//public BasicDataSource dbcpDataSource() {
	//}
	
	@Bean
	public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
