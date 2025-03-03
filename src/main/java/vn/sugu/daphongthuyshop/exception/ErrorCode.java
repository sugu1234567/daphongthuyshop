package vn.sugu.daphongthuyshop.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Internal error", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_EXISTED(400, "User already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(400, "Category already exists", HttpStatus.BAD_REQUEST),

    INVALID_KEY(400, "Invalid key", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(400, "Invalid email format ", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(400, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(400, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400, "Old password is incorrect", HttpStatus.BAD_REQUEST),
    LOGIN_INVALID(400, "Username or Password is incorrect", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(401, "No authorization token was found", HttpStatus.UNAUTHORIZED),

    CATEGORY_NAME_CHANGE_NOT_ALLOWED(400, "Category name change not allowed ", HttpStatus.BAD_REQUEST),

    LOGIN_BLANK(400, "Username or Password cannot be blank", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(404, "User not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_EXISTED(404, "Category not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(404, "Role not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_EXISTED(404, "Product not found", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
    ACCESS_DENIED(403, "Access denied", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRATION(401, "Token is expired", HttpStatus.UNAUTHORIZED),

    NAME_IS_REQUIRED(400, "Name is required", HttpStatus.BAD_REQUEST),
    IS_REQUIRED(400, "Field is required", HttpStatus.BAD_REQUEST),
    PRICE_IS_REQUIRED(400, "Price is required", HttpStatus.BAD_REQUEST),
    STOCK_IS_REQUIRED(400, "Stock is required", HttpStatus.BAD_REQUEST),
    CATEGORY_IS_REQUIRED(400, "Category is required", HttpStatus.BAD_REQUEST),

    PRICE_MUST_BE_POSITIVE(400, "Price must be  positive", HttpStatus.BAD_REQUEST),
    STOCK_MUST_BE_NON_NEGATIVE(400, "Stock must be non negative", HttpStatus.BAD_REQUEST),

    MISSING_COOKIE(400, "Required cookie is missing", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
