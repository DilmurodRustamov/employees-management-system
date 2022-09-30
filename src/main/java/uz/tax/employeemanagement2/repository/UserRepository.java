package uz.tax.employeemanagement2.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.tax.employeemanagement2.entity.User;
import uz.tax.employeemanagement2.ref.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, DataTablesRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findAllByUserRole(UserRole userRole);

}
