package vn.sugu.daphongthuyshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.sugu.daphongthuyshop.configuration.SecurityUtils;
import vn.sugu.daphongthuyshop.dto.request.userRequest.CreateUserRequest;
import vn.sugu.daphongthuyshop.dto.request.userRequest.UpdateProfileRequest;
import vn.sugu.daphongthuyshop.dto.request.userRequest.UpdateUserRequest;
import vn.sugu.daphongthuyshop.dto.response.userResponse.UpdateProfileResponse;
import vn.sugu.daphongthuyshop.dto.response.userResponse.UpdateUserResponse;
import vn.sugu.daphongthuyshop.dto.response.userResponse.UserResponse;
import vn.sugu.daphongthuyshop.entity.User;
import vn.sugu.daphongthuyshop.enums.Role;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.mapper.UserMapper;
import vn.sugu.daphongthuyshop.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dob(request.getDob())
                .gender(request.getGender())
                .phone(request.getPhone())
                .address(request.getFullAddress())
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(user);

        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gender(user.getGender())
                .phone(user.getPhone())
                .dob(user.getDob())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UpdateUserResponse updateUser(String email, UpdateUserRequest request) {
        if (!SecurityUtils.isAdmin()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setFullName(request.getFullName());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setAddress(request.getFullAddress());
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);
        return UpdateUserResponse.builder()
                .fullName(updatedUser.getFullName())
                .dob(updatedUser.getDob())
                .gender(updatedUser.getGender())
                .phone(updatedUser.getPhone())
                .address(updatedUser.getAddress())
                .role(updatedUser.getRole())
                .build();
    }

    public UserResponse showProfileData() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gender(user.getGender())
                .phone(user.getPhone())
                .dob(user.getDob())
                .role(user.getRole())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> searchUser(String keyword, Pageable pageable) {

        if (!SecurityUtils.isAdmin()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Page<User> user = userRepository.findByFullNameContainingOrEmailContainingOrPhoneContaining(
                keyword, keyword, keyword, pageable);

        return user.map(userMapper::mapToUserResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getAllEmployees(Pageable pageable) {
        if (!SecurityUtils.isAdmin()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Page<User> user = userRepository.findAll(pageable);
        return user.map(userMapper::mapToUserResponse);
    }

    public UpdateProfileResponse updateProfile(UpdateProfileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setFullName(request.getFullName());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setAddress(request.getFullAddress());

        userRepository.save(user);

        return UpdateProfileResponse.builder()
                .fullName(user.getFullName())
                .gender(user.getGender())
                .phone(user.getPhone())
                .dob(user.getDob())
                .address(user.getAddress())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String email) {
        if (!SecurityUtils.isAdmin()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userRepository.delete(user);
    }

}
