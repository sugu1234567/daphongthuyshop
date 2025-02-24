package vn.sugu.daphongthuyshop.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import vn.sugu.daphongthuyshop.dto.response.userResponse.UpdateUserResponse;
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

    @PostMapping
    APIResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return APIResponse.<UserResponse>builder()
                .data(userService.createUser(request))
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

    @PatchMapping("/me")
    APIResponse<UpdateProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return APIResponse.<UpdateProfileResponse>builder()
                .data(userService.updateProfile(request))
                .build();
    }

    @PatchMapping("/{email}")
    public APIResponse<UpdateUserResponse> updateEmployee(
            @PathVariable String email,
            @Valid @RequestBody UpdateUserRequest request) {
        return APIResponse.<UpdateUserResponse>builder()
                .data(userService.updateUser(email, request))
                .build();
    }

    @DeleteMapping("/{email}")
    public APIResponse<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return APIResponse.<Void>builder().build();
    }

}
