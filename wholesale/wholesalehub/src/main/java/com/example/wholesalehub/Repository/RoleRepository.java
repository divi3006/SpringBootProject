package com.example.wholesalehub.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wholesalehub.Entity.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles,Integer> {
	
	Optional<Roles>findByName(String name);

}
//It acts as a bridge between your Java objects (entities) and the database tables.
//Instead of writing raw SQL queries, you just call methods from the repository.