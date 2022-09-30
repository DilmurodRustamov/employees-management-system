package uz.tax.employeemanagement2.service;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import uz.tax.employeemanagement2.apiResponseMessages.ApiResponse;
import uz.tax.employeemanagement2.entity.User;

import java.util.List;

public interface EmployeeService {

    DataTablesOutput<User> getAll(DataTablesInput input);

    List<User> getAll();

    ApiResponse createUser(User userDto);

    User get(Long id);

    ApiResponse updateEmployee(Long id, User userDto);

    ApiResponse delete(Long id);

}
