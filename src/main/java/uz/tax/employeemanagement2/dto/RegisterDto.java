package uz.tax.employeemanagement2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "^998(9[012345789]|6[125679]|7[01234569])[0-9]{7}$", message = "user.phone.invalid")
    private String phoneNumber;

    @NotNull
    private String password;

    @NotNull
    private String perPassword;

}
