package uz.tax.employeemanagement2.service;


import uz.tax.employeemanagement2.apiResponseMessages.ApiResponse;
import uz.tax.employeemanagement2.dto.LoginDto;
import uz.tax.employeemanagement2.dto.RegisterDto;

public interface AuthService {

    ApiResponse registerUser(RegisterDto registerDto);

    String loginUser(LoginDto loginDto);
}
