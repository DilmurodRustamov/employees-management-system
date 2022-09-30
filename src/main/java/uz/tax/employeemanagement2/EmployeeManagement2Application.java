package uz.tax.employeemanagement2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "uz.tax.employeemanagement2.repository",
        repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class EmployeeManagement2Application {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagement2Application.class, args);
    }

}
