package uz.tax.employeemanagement2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull(message = "username must not be empty")
    private String phoneNumber;

    @NotNull(message = "password must not be empty")
    private String password;

}
