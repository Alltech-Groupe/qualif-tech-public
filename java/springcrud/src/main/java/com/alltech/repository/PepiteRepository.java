package com.alltech.repository;

import com.alltech.pepites.Pepite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PepiteRepository extends CrudRepository<Pepite, Long> {


}
