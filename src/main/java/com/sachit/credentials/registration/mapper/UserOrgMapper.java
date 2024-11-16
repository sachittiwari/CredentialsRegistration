package com.sachit.credentials.registration.mapper;

import com.sachit.credentials.registration.entity.UserOrganizationMapping;
import com.sachit.credentials.registration.model.UserOrgMappingRequestDTO;
import com.sachit.credentials.registration.model.UserOrgMappingResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserOrgMapper {

    UserOrganizationMapping toUserOrganizationMapping(UserOrgMappingRequestDTO request);

    UserOrgMappingResponseDTO toUserOrgResponseDTO(UserOrganizationMapping entity);
}
