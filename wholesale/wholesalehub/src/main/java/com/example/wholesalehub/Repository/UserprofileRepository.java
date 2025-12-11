package com.example.wholesalehub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wholesalehub.Entity.Userprofile;

@Repository
public interface UserprofileRepository extends JpaRepository<Userprofile,Integer> {

}
