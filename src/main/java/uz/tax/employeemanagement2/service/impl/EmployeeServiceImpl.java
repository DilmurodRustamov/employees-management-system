package uz.tax.employeemanagement2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.tax.employeemanagement2.apiResponseMessages.ApiResponse;
import uz.tax.employeemanagement2.entity.User;
import uz.tax.employeemanagement2.repository.UserRepository;
import uz.tax.employeemanagement2.service.EmployeeService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static uz.tax.employeemanagement2.apiResponseMessages.ResponseMessageKeys.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public DataTablesOutput<User> getAll(DataTablesInput input) {
        return userRepository.findAll(input);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public ApiResponse createUser(User userDto) {
        return createEmployee(userDto);
    }

    @Override
    public User get(Long id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.orElseThrow(() -> null);
    }

    @Override
    public ApiResponse delete(Long id) {
        userRepository.deleteById(id);
        return new ApiResponse(USER_DELETED, true);
    }

    public ApiResponse createEmployee(User user) {
        if (userRepository.existsByPhoneNumberAndIdNot(user.getPhoneNumber(), user.getId()))
            return new ApiResponse(USER_ALREADY_EXISTS, false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ApiResponse(USER_SAVED, true);
    }

    @Override
    public ApiResponse updateEmployee(Long id, User userDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return new ApiResponse(USER_DOES_NOT_EXIST, false);
        User user = optionalUser.get();
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setUserRole(userDto.getUserRole());
        user.setPassword(userDto.getPassword());
        user.setPosition(userDto.getPosition());
        user.setSalary(userDto.getSalary());
        userRepository.save(user);
        return new ApiResponse(USER_SAVED, true);
    }
}
