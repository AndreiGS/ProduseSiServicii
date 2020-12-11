package com.findthebusiness.backend.mapper.mapper_implementation;

import com.findthebusiness.backend.dto.authentication.RegisterRequestDto;
import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.mapper.mapper_repository.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Users convertRegisterRequestDtoToUser(RegisterRequestDto registerRequestDto) {
        return modelMapper.map(registerRequestDto, Users.class);
    }
}
