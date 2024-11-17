package com.sachit.credentials.registration.service;

import com.sachit.credentials.registration.constants.UserOrgMappingTestConstants;
import com.sachit.credentials.registration.constants.UserTestConstants;
import com.sachit.credentials.registration.entity.User;
import com.sachit.credentials.registration.entity.UserOrganizationMapping;
import com.sachit.credentials.registration.exception.UserNotFoundException;
import com.sachit.credentials.registration.mapper.UserMapper;
import com.sachit.credentials.registration.model.UserResponseDTO;
import com.sachit.credentials.registration.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserOrganizationMappingService userOrganizationMappingService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetAllUsers_returnResponse() {
        //Arrange data
        List<User> userList = List.of(UserTestConstants.ENTITY_1,UserTestConstants.ENTITY_2);

        //mock data
        when(userRepository.findAll()).thenReturn(userList);
        when(userOrganizationMappingService.getOrganizationIdByUserId(1L)).thenReturn(List.of(1L,2L));
        when(userOrganizationMappingService.getOrganizationIdByUserId(2L)).thenReturn(List.of(2L));

        //Act
        List<UserResponseDTO> result = userService.fetchAllUsers();

        //Assert
        assertEquals(result.size(),2);
        assertEquals(result.get(0).getOrganizationIds(),List.of(1L,2L));
        assertEquals(result.get(1).getOrganizationIds(),List.of(2L));
        assertEquals(result.get(0).getName(),"Sachit");
        assertEquals(result.get(1).getName(),"Sachin");
        verify(userRepository).findAll();
        verify(userOrganizationMappingService).getOrganizationIdByUserId(1L);
        verify(userOrganizationMappingService).getOrganizationIdByUserId(2L);
    }

    @Test
    public void testGetUserById_throwException(){
        //Arrange data
        Optional<User> user = Optional.empty();

        //mock data
        when(userRepository.findById(1L)).thenReturn(user);

        //Act and Assert
        assertThrows(UserNotFoundException.class,()->userService.getUserById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    public void testGetUserById_returnResponse() throws UserNotFoundException {
        //Arrange data
        Optional<User> user = Optional.of(UserTestConstants.ENTITY_1);

        //mock data
        when(userRepository.findById(1L)).thenReturn(user);
        when(userOrganizationMappingService.getOrganizationIdByUserId(1L)).thenReturn(List.of(1L,2L));

        //Act
        UserResponseDTO result = userService.getUserById(1L);

        //Assert
        assertEquals(result.getId(),1L);
        assertEquals(result.getName(),"Sachit");
        assertEquals(result.getFirstName(),"Sachit");
        assertEquals(result.getLastName(),"Tiwari");
        assertEquals(result.getSubjectId(),"ST");
        assertEquals(result.getOrganizationIds(),List.of(1L,2L));

        verify(userRepository).findById(1L);
        verify(userOrganizationMappingService).getOrganizationIdByUserId(1L);
    }


    @Test
    public void testCreateUser_returnResponse() {
        //Arrange
        UserOrganizationMapping userOrganizationMapping1 = UserOrganizationMapping.builder().userId(1L).organizationId(1L).build();
        UserOrganizationMapping userOrganizationMapping2 = UserOrganizationMapping.builder().userId(1L).organizationId(2L).build();

        //mock data
        when(userRepository.save(userMapper.toUser(UserTestConstants.REQUEST))).thenReturn(UserTestConstants.ENTITY_1);
        when(userOrganizationMappingService.createUserOrgMapping(userOrganizationMapping1)).thenReturn(UserOrgMappingTestConstants.ENTITY_1);
        when(userOrganizationMappingService.createUserOrgMapping(userOrganizationMapping2)).thenReturn(UserOrgMappingTestConstants.ENTITY_2);
        when(userOrganizationMappingService.getOrganizationIdByUserId(1L)).thenReturn(List.of(1L,2L));

        //Act
        UserResponseDTO result = userService.createUser(UserTestConstants.REQUEST);

        //Assert
        assertEquals(result.getId(),1L);
        assertEquals(result.getName(),"Sachit");
        assertEquals(result.getFirstName(),"Sachit");
        assertEquals(result.getLastName(),"Tiwari");
        assertEquals(result.getSubjectId(),"ST");
        assertEquals(result.getOrganizationIds(),List.of(1L,2L));
        verify(userRepository).save(userMapper.toUser(UserTestConstants.REQUEST));
        verify(userOrganizationMappingService).createUserOrgMapping(userOrganizationMapping2);
        verify(userOrganizationMappingService).createUserOrgMapping(userOrganizationMapping1);
    }

    @Test
    public void testCreateUser_throwException() {
        //mock data
        when(userRepository.save(userMapper.toUser(UserTestConstants.REQUEST))).thenThrow(new RuntimeException("Error during create"));

        //Act and Assert
        assertThrows(RuntimeException.class,()->userService.createUser(UserTestConstants.REQUEST));
    }


    @Test
    public void testUpdateUser_returnResponse() throws UserNotFoundException {
        //Arrange data
        Long id = 1L;
        User user = userMapper.toUser(UserTestConstants.REQUEST);
        user.setId(id);

        //mock data
        when(userRepository.existsById(id)).thenReturn(true);
        when(userRepository.save(user)).thenReturn(UserTestConstants.ENTITY_1);
        when(userOrganizationMappingService.getOrganizationIdByUserId(1L)).thenReturn(List.of(1L,2L));

        //Act
        UserResponseDTO result = userService.updateUserById(id,UserTestConstants.REQUEST);

        //Assert
        assertEquals(result.getId(),1L);
        assertEquals(result.getName(),"Sachit");
        assertEquals(result.getFirstName(),"Sachit");
        assertEquals(result.getLastName(),"Tiwari");
        assertEquals(result.getSubjectId(),"ST");
        assertEquals(result.getOrganizationIds(),List.of(1L,2L));
        verify(userRepository).existsById(id);
        verify(userRepository).save(user);
        verify(userOrganizationMappingService).getOrganizationIdByUserId(1L);
    }

    @Test
    public void testUpdateUser_throwException(){
        //mock data
        when(userRepository.existsById(1L)).thenReturn(false);

        //Act and Assert
        assertThrows(UserNotFoundException.class,()->userService.updateUserById(1L,UserTestConstants.REQUEST));
        verify(userRepository).existsById(1L);
    }


    @Test
    public void testDeleteUser_success() throws UserNotFoundException {
        //Arrange data
        Long id = 1L;

        //mock data
        when(userRepository.existsById(id)).thenReturn(true);
        doNothing().when(userRepository).deleteById(id);
        doNothing().when(userOrganizationMappingService).deleteUserOrganizationMappingByUserId(id);


        //Act
        userService.deleteUserById(id);

        //Assert
        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
        verify(userOrganizationMappingService).deleteUserOrganizationMappingByUserId(id);
    }

    @Test
    public void testDeleteUser_NotFound(){
        //mock data
        when(userRepository.existsById(1L)).thenReturn(false);

        //Act and Assert
        assertThrows(UserNotFoundException.class,()->userService.deleteUserById(1L));
        verify(userRepository).existsById(1L);
    }
}