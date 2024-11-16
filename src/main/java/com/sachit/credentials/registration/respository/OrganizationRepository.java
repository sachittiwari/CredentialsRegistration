package com.sachit.credentials.registration.respository;

import com.sachit.credentials.registration.entity.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    List<Organization> findAll();

}
