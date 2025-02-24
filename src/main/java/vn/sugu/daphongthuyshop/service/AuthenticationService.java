package vn.sugu.daphongthuyshop.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import vn.sugu.daphongthuyshop.dto.request.authRequest.ChangePasswordRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.ForgotPasswordRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.IntrospectRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.LoginRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.LogoutRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.RefreshRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.RegisterRequest;
import vn.sugu.daphongthuyshop.dto.request.authRequest.ResetPasswordRequest;
import vn.sugu.daphongthuyshop.dto.response.authResponse.AuthenticationResponse;
import vn.sugu.daphongthuyshop.dto.response.authResponse.IntrospectResponse;
import vn.sugu.daphongthuyshop.dto.response.authResponse.IsAuthenticatedResponse;
import vn.sugu.daphongthuyshop.dto.response.authResponse.RegisterResponse;
import vn.sugu.daphongthuyshop.entity.User;
import vn.sugu.daphongthuyshop.enums.Role;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    JavaMailSender mailSender;

    @NonFinal
    @Value("${jwt.signer.key}")
    protected String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected Long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected Long REFRESHABLE_DURATION;

    public IntrospectResponse verifyResetToken(String token) throws ParseException, JOSEException {
        boolean isValid = true;

        try {
            var signedJWT = verifyToken(token, false);
            String email = signedJWT.getJWTClaimsSet().getSubject();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            if (user.getResetPasswordToken() == null || !user.getResetPasswordToken().equals(token)) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }
        } catch (AppException exception) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.LOGIN_INVALID);

        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();
    }

    private String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("vn.daphongthuyshop")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("vn.daphongthuyshop")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Tạo user mới
        User newUser = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dob(request.getDob())
                .phone(request.getPhone())
                .role(Role.CUSTOMER) // Mặc định role là CUSTOMER
                .build();

        // Lưu vào database
        userRepository.save(newUser);
        return RegisterResponse.builder()
                .fullName(newUser.getFullName())
                .email(newUser.getEmail())
                .phone(newUser.getPhone())
                .dob(newUser.getDob())
                .role(newUser.getRole())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {

        var signedJWT = verifyToken(request.getRefreshToken(), true);
        String email = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!user.getRefreshToken().equals(request.getRefreshToken())) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);

        user.setAccessToken(newAccessToken);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .access_token(newAccessToken)
                .refresh_token(newRefreshToken)
                .build();
    }

    public IsAuthenticatedResponse isAuthenticated() throws ParseException, JOSEException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return IsAuthenticatedResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getAccessToken(), false);
        String email = signedJWT.getJWTClaimsSet().getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Xóa refreshToken khỏi database
        user.setRefreshToken(null);
        userRepository.save(user);

        log.info("User {} logged out successfully", email);
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        String email = signedJWT.getJWTClaimsSet().getSubject();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!signedJWT.verify(verifier)) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        if (expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRATION);
        }

        if (isRefresh) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            if (user.getRefreshToken() == null || !user.getRefreshToken().equals(token)) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }
        }

        return signedJWT;
    }

    public void changePassword(ChangePasswordRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {

            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Buộc người dùng phải đăng nhập lại
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    public void sendResetPasswordEmail(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String token = generateAccessToken(user);
        user.setResetPasswordToken(token);
        userRepository.save(user);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Reset Your Password");
        message.setText("Click vào link để đặt lại mật khẩu: " + resetLink);
        mailSender.send(message);
    }

    public void resetPassword(ResetPasswordRequest request) throws ParseException, JOSEException {
        String email = verifyToken(request.getResetToken(), false).getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!user.getResetPasswordToken().equals(request.getResetToken())) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
        // Lưu password mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Vô hiệu hóa token sau khi sử dụng
        user.setResetPasswordToken(null);

        userRepository.save(user);
    }

    private String buildScope(User user) {

        StringJoiner stringJoiner = new StringJoiner(" ");
        stringJoiner.add("ROLE_" + user.getRole().name());
        return stringJoiner.toString();
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;
        try {
            verifyToken(token, false);

        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }
}
