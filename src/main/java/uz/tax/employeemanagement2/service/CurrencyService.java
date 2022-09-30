package uz.tax.employeemanagement2.service;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import uz.tax.employeemanagement2.entity.Currency;

import java.text.ParseException;

public interface CurrencyService {

    DataTablesOutput<Currency> getAll(DataTablesInput input) throws ParseException;

    void saveCurrencyByDate() throws ParseException;
}
