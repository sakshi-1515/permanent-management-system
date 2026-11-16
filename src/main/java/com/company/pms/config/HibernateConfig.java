
package com.company.pms.config;

import java.util.Properties;

import javax.sql.DataSource;

import com.company.pms.entity.ContractorEmployee;
import com.company.pms.entity.PermanentEmployee;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.hibernate.HibernateTransactionManager;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.h2.tools.Server;
import java.sql.SQLException;

@Configuration
public class HibernateConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.postgres")
    public DataSourceProperties postgresDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "postgresDataSource")
    @Primary
    public DataSource postgresDataSource(
            @Qualifier("postgresDataSourceProperties")
            DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer(
                "-tcp",
                "-tcpPort", "9092"
        );
    }
    @Bean
    @ConfigurationProperties("app.datasource.h2")

    public DataSourceProperties h2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "h2DataSource")
    public DataSource h2DataSource(
            @Qualifier("h2DataSourceProperties")
            DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    private Properties hibernateProperties(Environment environment) {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto",
                environment.getProperty("app.hibernate.ddl-auto", "update"));
        properties.put("hibernate.show_sql",
                environment.getProperty("app.hibernate.show-sql", "true"));
        properties.put("hibernate.format_sql", "true");
        return properties;
    }

    @Bean(name = "postgresSessionFactory")
    @Primary
    public LocalSessionFactoryBean postgresSessionFactory(
            @Qualifier("postgresDataSource") DataSource dataSource,
            Environment environment) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setAnnotatedClasses(PermanentEmployee.class);
        factory.setHibernateProperties(hibernateProperties(environment));
        return factory;
    }

    @Bean(name = "h2SessionFactory")
    public LocalSessionFactoryBean h2SessionFactory(
            @Qualifier("h2DataSource") DataSource dataSource,
            Environment environment) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setAnnotatedClasses(ContractorEmployee.class);
        factory.setHibernateProperties(hibernateProperties(environment));
        return factory;
    }

    @Bean(name = "postgresTransactionManager")
    @Primary
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresSessionFactory") SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager h2TransactionManager(
            @Qualifier("h2SessionFactory") SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
