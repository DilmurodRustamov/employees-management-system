package uz.tax.employeemanagement2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tax.employeemanagement2.apiResponseMessages.ApiResponse;
import uz.tax.employeemanagement2.dto.LoginDto;
import uz.tax.employeemanagement2.dto.RegisterDto;
import uz.tax.employeemanagement2.service.AuthService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse = authService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginDto") LoginDto loginDto) {
        String token = authService.loginUser(loginDto);
        return "pages/dashboard";
    }

}
