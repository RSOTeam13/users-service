package si.fri.rso.albify.usersservice.models.converters;

import si.fri.rso.albify.usersservice.lib.User;
import si.fri.rso.albify.usersservice.models.entities.UserEntity;

public class UserConverter {

    public static User toDto(UserEntity entity) {

        User dto = new User();
        dto.setId(entity.getId().toString());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setImageUrl(entity.getImageUrl());

        return dto;
    }
}
