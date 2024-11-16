package com.sachit.credentials.registration.respository;

import com.sachit.credentials.registration.entity.Organization;
import com.sachit.credentials.registration.entity.UserOrganizationMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrganizationMappingRepository extends CrudRepository<UserOrganizationMapping, Long> {

    @Query("SELECT organizationId FROM UserOrganizationMapping where userId=:userId")
    List<Long> findOrganizationIdByUserId(Long userId);

    List<UserOrganizationMapping> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
