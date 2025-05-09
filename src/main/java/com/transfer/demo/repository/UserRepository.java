package com.transfer.demo.repository;


import com.transfer.demo.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmails_email(@NotBlank String email);

    Boolean existsByEmails_email(String email);

    Boolean existsByEmails_emailAndId(String email, Long id);

    Boolean existsByPhones_phone(Long phone);

    Boolean existsByPhones_phoneAndId(Long phone, Long id);

    Optional<User> findByPhones_phone(@NotBlank Long phone);
}
