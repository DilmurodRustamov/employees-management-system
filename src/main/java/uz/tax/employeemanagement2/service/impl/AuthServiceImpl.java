package uz.tax.employeemanagement2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.tax.employeemanagement2.apiResponseMessages.ApiResponse;
import uz.tax.employeemanagement2.dto.LoginDto;
import uz.tax.employeemanagement2.dto.RegisterDto;
import uz.tax.employeemanagement2.entity.User;
import uz.tax.employeemanagement2.ref.UserRole;
import uz.tax.employeemanagement2.repository.UserRepository;
import uz.tax.employeemanagement2.security.SecurityUser;
import uz.tax.employeemanagement2.service.AuthService;

import static uz.tax.employeemanagement2.apiResponseMessages.ResponseMessageKeys.*;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

//    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public ApiResponse registerUser(RegisterDto registerDto) {
        if (!registerDto.getPassword().equals(registerDto.getPerPassword()))
            return new ApiResponse(PASSWORD_NOT_EQUALS, false);
        boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(registerDto.getPhoneNumber());
        if (existsByPhoneNumber)
            return new ApiResponse(USER_ALREADY_EXISTS, false);
        User user = new User(
                registerDto.getName(),
                registerDto.getPhoneNumber(),
                passwordEncoder.encode(registerDto.getPassword()),
                UserRole.USER
        );
        userRepository.save(user);
        return new ApiResponse(USER_SAVED, true);
    }

    @Override
    public String loginUser(LoginDto loginDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getPhoneNumber(), loginDto.getPassword()));
        SecurityUser user = (SecurityUser) authenticate.getPrincipal();
        return user.getPassword();
//        return JwtProvider.generateToken(user.getPhoneNumber(), user.getUserRole());
    }
}
