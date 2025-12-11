package com.example.wholesalehub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wholesalehub.Entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {

}
