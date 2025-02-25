package vn.sugu.daphongthuyshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.sugu.daphongthuyshop.dto.request.userRequest.CreateUserRequest;
import vn.sugu.daphongthuyshop.dto.request.userRequest.UpdateProfileRequest;
import vn.sugu.daphongthuyshop.dto.request.userRequest.UpdateUserRequest;
import vn.sugu.daphongthuyshop.dto.response.userResponse.UpdateProfileResponse;
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
    CloudinaryService cloudinaryService; // Thêm CloudinaryService

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(CreateUserRequest request, MultipartFile avatarFile) throws Exception {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setAddress(request.getFullAddress());

        // Upload avatar nếu có file
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = cloudinaryService.uploadFile(avatarFile);
            user.setAvatarUrl(avatarUrl);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String email, UpdateUserRequest request, MultipartFile avatarFile) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setAddress(request.getFullAddress());

        // Upload avatar nếu có file
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = cloudinaryService.uploadFile(avatarFile);
            user.setAvatarUrl(avatarUrl);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    public UpdateProfileResponse updateProfile(UpdateProfileRequest request, MultipartFile avatarFile)
            throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setFullName(request.getFullName());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setAddress(request.getFullAddress());

        // Upload avatar nếu có file
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = cloudinaryService.uploadFile(avatarFile);
            user.setAvatarUrl(avatarUrl);
        }

        userRepository.save(user);

        return UpdateProfileResponse.builder()
                .fullName(user.getFullName())
                .gender(user.getGender())
                .phone(user.getPhone())
                .dob(user.getDob())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepository.delete(user);
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
                .avatarUrl(user.getAvatarUrl())
                .address(user.getAddress())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> searchUser(String keyword, Pageable pageable) {
        Page<User> users = userRepository.findByFullNameContainingOrEmailContainingOrPhoneContaining(
                keyword, keyword, keyword, pageable);
        return users.map(user -> userMapper.toUserResponse(user));
    }
}