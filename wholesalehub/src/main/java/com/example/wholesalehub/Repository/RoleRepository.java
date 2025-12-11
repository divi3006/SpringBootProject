package com.example.wholesalehub.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wholesalehub.Entity.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles,Integer> {
	
	Optional<Roles>findByname(String name);

}
