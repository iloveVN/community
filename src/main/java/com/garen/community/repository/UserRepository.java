package com.garen.community.repository;

import com.garen.community.domain.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<JpaUser, Integer> {
}
