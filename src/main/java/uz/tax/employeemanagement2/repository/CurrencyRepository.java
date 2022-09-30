package uz.tax.employeemanagement2.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.tax.employeemanagement2.entity.Currency;
import uz.tax.employeemanagement2.entity.User;

import java.util.Date;

public interface CurrencyRepository extends JpaRepository<Currency,Integer>, DataTablesRepository<Currency, Integer> {

    boolean existsAllByRealTime(Date realTime);

}
