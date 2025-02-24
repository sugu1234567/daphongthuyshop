package vn.sugu.daphongthuyshop.mapper;

import org.springframework.stereotype.Component;

import vn.sugu.daphongthuyshop.dto.response.userResponse.UserResponse;
import vn.sugu.daphongthuyshop.entity.User;

@Component
public class UserMapper {

    public UserResponse mapToUserResponse(User user) {

        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .dob(user.getDob())
                .gender(user.getGender())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }
}
