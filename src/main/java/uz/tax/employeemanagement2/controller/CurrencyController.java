package uz.tax.employeemanagement2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uz.tax.employeemanagement2.entity.Currency;
import uz.tax.employeemanagement2.service.CurrencyService;

import java.text.ParseException;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    @GetMapping
    public String getCurrencyDateRate() throws ParseException {
        return "/pages/currency";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    @GetMapping("data")
    @ResponseBody
    public DataTablesOutput<Currency> getLeadsData(DataTablesInput input) throws ParseException {
        return currencyService.getAll(input);
    }

}
