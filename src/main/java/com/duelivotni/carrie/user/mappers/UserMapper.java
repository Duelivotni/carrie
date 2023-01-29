package com.duelivotni.carrie.user.mappers;

import com.duelivotni.carrie.user.models.dtos.UserDto;
import com.duelivotni.carrie.user.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDto map(User source);

}
