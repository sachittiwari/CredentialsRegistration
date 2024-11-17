package com.sachit.credentials.registration.service;

import com.sachit.credentials.registration.constants.OrganizationTestConstants;
import com.sachit.credentials.registration.constants.UserOrgMappingTestConstants;
import com.sachit.credentials.registration.entity.UserOrganizationMapping;
import com.sachit.credentials.registration.exception.OrganizationNotFoundException;
import com.sachit.credentials.registration.model.OrganizationResponseDTO;
import com.sachit.credentials.registration.model.UserOrgMappingRequestDTO;
import com.sachit.credentials.registration.model.UserOrgMappingResponseDTO;
import com.sachit.credentials.registration.respository.UserOrganizationMappingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserOrganizationMappingServiceTest {

    @MockBean
    UserOrganizationMappingRepository userOrganizationMappingRepository;

    @MockBean
    OrganizationService organizationService;

    @Autowired
    UserOrganizationMappingService userOrganizationMappingService;

    @Test
    public void testGetOrganizationIdByUserId_Success(){
        //Arrange
        Long userId = 1L;
        List<Long> orgIds = List.of(1L,2L);

        //Mock
        when(userOrganizationMappingRepository.findOrganizationIdByUserId(userId)).thenReturn(orgIds);

        //Act
        List<Long> result = userOrganizationMappingService.getOrganizationIdByUserId(userId);

        //Assert
        assertEquals(result.size(),2);
        verify(userOrganizationMappingRepository).findOrganizationIdByUserId(userId);
    }

    @Test
    public void testDeleteUserOrganizationMappingByUserId_Success(){
        //Arrange
        Long userId = 1L;

        //Mock
        doNothing().when(userOrganizationMappingRepository).deleteAllByUserId(userId);

        //Act
        userOrganizationMappingService.deleteUserOrganizationMappingByUserId(userId);

        //Assert
        verify(userOrganizationMappingRepository).deleteAllByUserId(userId);
    }

    @Test
    public void testCreateUserOrganizationMapping_Success(){

        //Mock
        when(userOrganizationMappingRepository.save(UserOrgMappingTestConstants.ENTITY_1)).thenReturn(UserOrgMappingTestConstants.ENTITY_1);

        //Act
        UserOrganizationMapping result = userOrganizationMappingService.createUserOrgMapping(UserOrgMappingTestConstants.ENTITY_1);

        //Assert
        assertEquals(result.getUserId(),1L);
        assertEquals(result.getOrganizationId(),1L);
        verify(userOrganizationMappingRepository).save(UserOrgMappingTestConstants.ENTITY_1);
    }

    @Test
    public void testCreateUserOrganizationMapping_throwException(){

        //Mock
        when(userOrganizationMappingRepository.save(UserOrgMappingTestConstants.ENTITY_1)).thenThrow(new RuntimeException("Unable to create"));

        //Act and Assert
        assertThrows(RuntimeException.class,()->userOrganizationMappingService.createUserOrgMapping(UserOrgMappingTestConstants.ENTITY_1));
        verify(userOrganizationMappingRepository).save(UserOrgMappingTestConstants.ENTITY_1);
    }

    @Test
    public void testGetMappingByUserId_success() throws OrganizationNotFoundException {
        //Arrange
        Long userId = 1L;
        List<UserOrganizationMapping> userOrganizationMappingList = List.of(UserOrgMappingTestConstants.ENTITY_1,UserOrgMappingTestConstants.ENTITY_2);

        //Mock
        when(userOrganizationMappingRepository.findByUserId(userId)).thenReturn(userOrganizationMappingList);
        when(organizationService.getOrganizationById(1L)).thenReturn(OrganizationTestConstants.RESPONSE_1);
        when(organizationService.getOrganizationById(2L)).thenReturn(OrganizationTestConstants.RESPONSE_2);

        //Act
        List<OrganizationResponseDTO> result = userOrganizationMappingService.getMappingByUserId(userId);

        //Assert
        assertEquals(result.size(),2);
        assertEquals(result.get(0).getId(),1L);
        assertEquals(result.get(1).getId(),2L);
        verify(userOrganizationMappingRepository).findByUserId(userId);
        verify(organizationService).getOrganizationById(1L);
        verify(organizationService).getOrganizationById(2L);
    }

    @Test
    public void testCreateUserOrgMappingByRequest_success() {
        //Arrange
        UserOrgMappingRequestDTO requestDTO = UserOrgMappingRequestDTO.builder().userId(1L).organizationIds(List.of(1L,2L)).build();

        //Mock
        when(userOrganizationMappingRepository.save(any())).thenReturn(UserOrgMappingTestConstants.ENTITY_1);

        //Act
        UserOrgMappingResponseDTO result = userOrganizationMappingService.createUserOrgMappingByRequest(requestDTO);

        //Assert
        assertEquals(result.getUserId(),1L);
        assertEquals(result.getOrganizationIds().get(0),1L);
        assertEquals(result.getOrganizationIds().get(1),2L);
        verify(userOrganizationMappingRepository,times(2)).save(any());
    }
}
