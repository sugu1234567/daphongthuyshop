package vn.sugu.daphongthuyshop.controller;

import java.text.ParseException;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.sugu.daphongthuyshop.dto.request.authRequest.ChangePasswordRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.ForgotPasswordRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.LoginRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.LogoutRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.RefreshRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.RegisterRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.ResetPasswordRequest;
import vn.sugu.daphongthuyshop.dto.response.APIResponse;
import vn.sugu.daphongthuyshop.dto.response.AuthenticationResponse;
import vn.sugu.daphongthuyshop.dto.response.IntrospectResponse;
import vn.sugu.daphongthuyshop.dto.response.IsAuthenticatedResponse;
import vn.sugu.daphongthuyshop.dto.response.RegisterResponse;
import vn.sugu.daphongthuyshop.service.AuthenticationService;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthenticationController {
        AuthenticationService authenticationService;

        @PostMapping("/auth/login")
        APIResponse<AuthenticationResponse> authenticate(@RequestBody LoginRequest request) {
                var result = authenticationService.authenticate(request);
                return APIResponse.<AuthenticationResponse>builder()
                                .data(result)
                                .build();
        }

        @GetMapping("/auth/is_authenticated")
        APIResponse<IsAuthenticatedResponse> isAuthenticated() throws ParseException, JOSEException {
                return APIResponse.<IsAuthenticatedResponse>builder()
                                .data(authenticationService.isAuthenticated())
                                .build();
        }

        @PostMapping("/auth/refresh")
        APIResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request)
                        throws ParseException, JOSEException {
                var result = authenticationService.refreshToken(request);
                return APIResponse.<AuthenticationResponse>builder()
                                .data(result)
                                .build();
        }

        @PostMapping("/auth/logout")
        APIResponse<Void> logout(@RequestBody LogoutRequest request)
                        throws ParseException, JOSEException {
                authenticationService.logout(request);
                return APIResponse.<Void>builder()
                                .build();
        }

        @PostMapping("/auth/register")
        APIResponse<RegisterResponse> register(@RequestBody RegisterRequest request)
                        throws ParseException, JOSEException {
                return APIResponse.<RegisterResponse>builder()
                                .data(authenticationService.register(request))
                                .build();
        }

        @PostMapping("/password/change_password")
        APIResponse<?> changePassword(@RequestBody ChangePasswordRequest request) {
                authenticationService.changePassword(request);
                return APIResponse.<Void>builder()
                                .message("Password changed successfully")
                                .build();
        }

        @PostMapping("/password/forgot_password")
        APIResponse<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
                authenticationService.sendResetPasswordEmail(request);
                return APIResponse.<Void>builder()
                                .message("Link reset password sent in e-mail")
                                .build();
        }

        @PostMapping("/password/reset_password")
        APIResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request)
                        throws ParseException, JOSEException {
                authenticationService.resetPassword(request);
                return APIResponse.<Void>builder()
                                .message("Password reset successfully")
                                .build();
        }

        @GetMapping("/password/verify_token/{reset_password_token}")
        APIResponse<IntrospectResponse> validateResetToken(@PathVariable String reset_password_token)
                        throws ParseException, JOSEException {
                var isValid = authenticationService.verifyResetToken(reset_password_token);
                return APIResponse.<IntrospectResponse>builder()
                                .data(isValid)
                                .build();
        }
}
