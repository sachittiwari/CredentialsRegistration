package com.sachit.credentials.registration.service;

import com.sachit.credentials.registration.entity.UserOrganizationMapping;
import com.sachit.credentials.registration.exception.OrganizationNotFoundException;
import com.sachit.credentials.registration.model.OrganizationResponseDTO;
import com.sachit.credentials.registration.model.UserOrgMappingRequestDTO;
import com.sachit.credentials.registration.model.UserOrgMappingResponseDTO;
import com.sachit.credentials.registration.respository.UserOrganizationMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserOrganizationMappingService {

    private final UserOrganizationMappingRepository userOrganizationMappingRepository;

    private final OrganizationService organizationService;

    public List<Long> getOrganizationIdByUserId(Long userId) {
        log.debug("Inside getOrganizationIdByUserId method: ", userId);
        return userOrganizationMappingRepository.findOrganizationIdByUserId(userId);
    }

    public List<OrganizationResponseDTO> getMappingByUserId(Long userId){
        log.debug("Inside getMappingByUserId method: ", userId);
        List<OrganizationResponseDTO> organizations = new ArrayList<>();
        userOrganizationMappingRepository.findByUserId(userId).forEach(mapping -> {
            try {
                organizations.add(organizationService.getOrganizationById(mapping.getOrganizationId()));
            } catch (OrganizationNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return organizations;
    }


    public void deleteUserOrganizationMappingByUserId(Long userId) {
        log.debug("Inside deleteUserOrganizationMappingByUserId method: ", userId);
        userOrganizationMappingRepository.deleteAllByUserId(userId);
    }

    public UserOrganizationMapping createUserOrgMapping(UserOrganizationMapping mapping) {
        log.debug("Inside createUserOrgMapping method: ", mapping);
        return userOrganizationMappingRepository.save(mapping);
    }

    public UserOrgMappingResponseDTO createUserOrgMappingByRequest(UserOrgMappingRequestDTO request) {
        log.debug("Inside createUserOrgMappingByRequest method: ", request);
        request.getOrganizationIds().forEach(organizationId -> userOrganizationMappingRepository
                            .save(UserOrganizationMapping.builder()
                                    .organizationId(organizationId)
                                    .userId(request.getUserId())
                                    .build()));
        return UserOrgMappingResponseDTO.builder()
                .userId(request.getUserId())
                .organizationIds(request.getOrganizationIds())
                .build();
    }
}
