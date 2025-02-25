package vn.sugu.daphongthuyshop.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.dto.request.userRequest.CreateUserRequest;
import vn.sugu.daphongthuyshop.dto.request.userRequest.UpdateProfileRequest;
import vn.sugu.daphongthuyshop.dto.request.userRequest.UpdateUserRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.userResponse.UpdateProfileResponse;
import vn.sugu.daphongthuyshop.dto.response.userResponse.UserResponse;
import vn.sugu.daphongthuyshop.service.UserService;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UserController {

    UserService userService;

    @PostMapping(consumes = { "multipart/form-data" })
    APIResponse<UserResponse> createUser(
            @Valid @RequestPart("request") CreateUserRequest request,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile) throws Exception {
        return APIResponse.<UserResponse>builder()
                .data(userService.createUser(request, avatarFile))
                .build();
    }

    @GetMapping("/me")
    APIResponse<UserResponse> showProfileData() {
        return APIResponse.<UserResponse>builder()
                .data(userService.showProfileData())
                .build();
    }

    @GetMapping
    public APIResponse<Page<UserResponse>> searchEmployees(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 10, sort = "userId", direction = Direction.DESC) Pageable pageable) {
        return APIResponse.<Page<UserResponse>>builder()
                .data(userService.searchUser(keyword, pageable))
                .build();
    }

    @PatchMapping(value = "/me", consumes = { "multipart/form-data" })
    APIResponse<UpdateProfileResponse> updateProfile(
            @Valid @RequestPart("request") UpdateProfileRequest request,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile) throws Exception {
        return APIResponse.<UpdateProfileResponse>builder()
                .data(userService.updateProfile(request, avatarFile))
                .build();
    }

    @PatchMapping(value = "/{email}", consumes = { "multipart/form-data" })
    public APIResponse<UserResponse> updateEmployee(
            @PathVariable String email,
            @Valid @RequestPart("request") UpdateUserRequest request,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile) throws Exception {
        return APIResponse.<UserResponse>builder()
                .data(userService.updateUser(email, request, avatarFile))
                .build();
    }

    @DeleteMapping("/{email}")
    public APIResponse<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return APIResponse.<Void>builder().build();
    }
}