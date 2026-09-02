package com.acciojobs.bms_august.repositories;

import com.acciojobs.bms_august.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    public Optional<User> findByEmail(String email);
}
