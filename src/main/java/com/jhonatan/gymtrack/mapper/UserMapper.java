package com.jhonatan.gymtrack.mapper;

import com.jhonatan.gymtrack.dto.UserRequestDTO;
import com.jhonatan.gymtrack.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDTOToUser(UserRequestDTO userRequestDTO);
}
