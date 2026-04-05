package com.sk.syncboard.repository;

import com.sk.syncboard.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByOrganizationId(Long orgId);


    List<Task> findAllByOrganizationId(Long orgId);
}
