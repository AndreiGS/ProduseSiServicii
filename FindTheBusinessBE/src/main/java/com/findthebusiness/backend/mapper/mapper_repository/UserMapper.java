package com.findthebusiness.backend.mapper.mapper_repository;

import com.findthebusiness.backend.dto.authentication.RegisterRequestDto;
import com.findthebusiness.backend.dto.users.ProfileInfoDto;
import com.findthebusiness.backend.entity.Users;

public interface UserMapper {

    Users convertRegisterRequestDtoToUser(RegisterRequestDto registerRequestDto);

}
