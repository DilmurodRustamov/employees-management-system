package uz.tax.employeemanagement2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.tax.employeemanagement2.apiResponseMessages.ApiResponse;
import uz.tax.employeemanagement2.entity.User;
import uz.tax.employeemanagement2.ref.UserRole;
import uz.tax.employeemanagement2.service.EmployeeService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    @GetMapping
    public String getPage(Model model) {
        return "pages/dashboard";
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    @GetMapping("data")
    @ResponseBody
    public DataTablesOutput<User> getEmployeeData(DataTablesInput input) {
        return employeeService.getAll(input);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("all")
    @ResponseBody
    public List<User> getLeads() {
        return employeeService.getAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping("form")
    public String getLeadForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("roles", UserRole.values());
        User user = id != null ? employeeService.get(id) : new User();
        model.addAttribute("user", user);
        return "pages/add-employee";
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping
    public HttpEntity<?> createEmployee(@RequestParam(required = false) Long id, @Valid @ModelAttribute("employee") User userDto) {
        ApiResponse apiResponse = employeeService.createUser(userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("edit/{id}")
    public HttpEntity<?> updateEmployee(@PathVariable Long id, @RequestBody User user) {
        ApiResponse response = employeeService.updateEmployee(id, user);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        ApiResponse response = employeeService.delete(id);
        return "redirect:/employees";
    }

}
