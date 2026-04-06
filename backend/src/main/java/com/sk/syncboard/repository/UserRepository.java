package com.sk.syncboard.repository;

import com.sk.syncboard.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

   Optional<User> findByEmail(String email);

    List<User> findByOrganizationId(Long id);
}
