package vn.sugu.daphongthuyshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.sugu.daphongthuyshop.dto.request.userRequest.CreateUserRequest;
import vn.sugu.daphongthuyshop.dto.request.userRequest.UpdateUserRequest;
import vn.sugu.daphongthuyshop.dto.response.userResponse.UserResponse;
import vn.sugu.daphongthuyshop.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UpdateUserRequest request);
}