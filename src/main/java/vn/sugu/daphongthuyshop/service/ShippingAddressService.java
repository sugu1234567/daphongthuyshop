package vn.sugu.daphongthuyshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.sugu.daphongthuyshop.dto.request.addressRequest.CreateShippingAddress;
import vn.sugu.daphongthuyshop.dto.response.addressResponse.ShippingAddressResponse;
import vn.sugu.daphongthuyshop.entity.ShippingAddress;
import vn.sugu.daphongthuyshop.entity.User;
import vn.sugu.daphongthuyshop.exception.AppException;
import vn.sugu.daphongthuyshop.exception.ErrorCode;
import vn.sugu.daphongthuyshop.repository.ShippingAddressRepository;
import vn.sugu.daphongthuyshop.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingAddressService {

    ShippingAddressRepository shippingAddressRepository;
    UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private ShippingAddressResponse toShippingAddressResponse(ShippingAddress shippingAddress) {
        ShippingAddressResponse response = new ShippingAddressResponse();
        response.setUserId(shippingAddress.getUser().getUserId().toString());
        response.setFullName(shippingAddress.getFullName());
        response.setPhoneNumber(shippingAddress.getPhoneNumber());
        response.setAddress(shippingAddress.getAddress());
        response.setDefault(shippingAddress.isDefault());
        return response;
    }

    public ShippingAddressResponse createShippingAddress(CreateShippingAddress request) {
        User user = getCurrentUser();

        if (request.isDefault()) {
            ShippingAddress existingDefault = shippingAddressRepository.findByUserAndIsDefault(user, true)
                    .orElse(null);
            if (existingDefault != null) {
                existingDefault.setDefault(false);
                shippingAddressRepository.save(existingDefault);
            }
        }

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setUser(user);
        shippingAddress.setFullName(request.getFullName());
        shippingAddress.setPhoneNumber(request.getPhone());
        shippingAddress.setAddress(request.getFullAddress());
        shippingAddress.setDefault(request.isDefault());

        ShippingAddress savedAddress = shippingAddressRepository.save(shippingAddress);
        return toShippingAddressResponse(savedAddress);
    }

    public ShippingAddressResponse updateShippingAddress(Long id, CreateShippingAddress request) {
        User user = getCurrentUser();
        ShippingAddress existingAddress = shippingAddressRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.SHIPPING_ADDRESS_NOT_FOUND));

        if (request.isDefault() && !existingAddress.isDefault()) {
            ShippingAddress existingDefault = shippingAddressRepository.findByUserAndIsDefault(user, true)
                    .orElse(null);
            if (existingDefault != null) {
                existingDefault.setDefault(false);
                shippingAddressRepository.save(existingDefault);
            }
        }

        existingAddress.setFullName(request.getFullName());
        existingAddress.setPhoneNumber(request.getPhone());
        existingAddress.setAddress(request.getFullAddress()); // Dùng getFullAddress()
        existingAddress.setDefault(request.isDefault());

        ShippingAddress updatedAddress = shippingAddressRepository.save(existingAddress);
        return toShippingAddressResponse(updatedAddress);
    }

    public void deleteShippingAddress(Long id) {
        User user = getCurrentUser();
        ShippingAddress existingAddress = shippingAddressRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.SHIPPING_ADDRESS_NOT_FOUND));

        List<ShippingAddress> addresses = shippingAddressRepository.findByUser(user);
        if (existingAddress.isDefault() && addresses.size() <= 1) {
            throw new AppException(ErrorCode.CANNOT_DELETE_DEFAULT_ADDRESS);
        }

        shippingAddressRepository.delete(existingAddress);
    }

    public List<ShippingAddressResponse> getAllShippingAddresses() {
        User user = getCurrentUser();
        List<ShippingAddress> addresses = shippingAddressRepository.findByUser(user);
        return addresses.stream()
                .map(this::toShippingAddressResponse)
                .collect(Collectors.toList());
    }
}