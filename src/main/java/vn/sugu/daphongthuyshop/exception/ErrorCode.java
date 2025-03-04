package vn.sugu.daphongthuyshop.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Lỗi nội bộ", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_EXISTED(400, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(400, "Danh mục đã tồn tại", HttpStatus.BAD_REQUEST),

    INVALID_KEY(400, "Key không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(400, "Định dạng email không hợp lệ", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(400, "Tên người dùng phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(400, "Mật khẩu phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400, "Mật khẩu cũ không chính xác", HttpStatus.BAD_REQUEST),
    LOGIN_INVALID(400, "Tên người dùng hoặc mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(401, "Không tìm thấy token xác thực", HttpStatus.UNAUTHORIZED),

    CATEGORY_NAME_CHANGE_NOT_ALLOWED(400, "Không được phép thay đổi tên danh mục", HttpStatus.BAD_REQUEST),

    LOGIN_BLANK(400, "Tên người dùng hoặc mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(404, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_EXISTED(404, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(404, "Không tìm thấy vai trò", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_EXISTED(404, "Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(404, "Không tìm thấy giỏ hàng", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(404, "Không tìm thấy sản phẩm trong giỏ hàng", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(401, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "Bạn không có quyền", HttpStatus.FORBIDDEN),
    ACCESS_DENIED(403, "Truy cập bị từ chối", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRATION(401, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),

    NAME_IS_REQUIRED(400, "Tên là bắt buộc", HttpStatus.BAD_REQUEST),
    IS_REQUIRED(400, "Trường này là bắt buộc", HttpStatus.BAD_REQUEST),
    PRICE_IS_REQUIRED(400, "Giá là bắt buộc", HttpStatus.BAD_REQUEST),
    STOCK_IS_REQUIRED(400, "Số lượng tồn kho là bắt buộc", HttpStatus.BAD_REQUEST),
    CATEGORY_IS_REQUIRED(400, "Danh mục là bắt buộc", HttpStatus.BAD_REQUEST),
    PRODUCT_ID_IS_REQUIRED(400, "Mã sản phẩm là bắt buộc", HttpStatus.BAD_REQUEST),

    INSUFFICIENT_STOCK(400, "Kho hàng không đủ", HttpStatus.BAD_REQUEST),

    PRICE_MUST_BE_POSITIVE(400, "Giá phải là số dương", HttpStatus.BAD_REQUEST),
    QUANTITY_MUST_BE_POSITIVE(400, "Số lượng phải là số dương", HttpStatus.BAD_REQUEST),
    STOCK_MUST_BE_NON_NEGATIVE(400, "Số lượng tồn kho không được âm", HttpStatus.BAD_REQUEST),

    MISSING_COOKIE(400, "Thiếu cookie bắt buộc", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
