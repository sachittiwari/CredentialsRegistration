package com.sachit.credentials.registration.mapper;


import com.sachit.credentials.registration.entity.User;
import com.sachit.credentials.registration.model.UserRequestDTO;
import com.sachit.credentials.registration.model.UserResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toUserResponseDTO(User user);
    User toUser(UserRequestDTO userRequestDTO);
    List<UserResponseDTO> toUserResponseDTOList(List<User> users);
}