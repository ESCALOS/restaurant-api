package com.nanoka.restaurant_api.receipt.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.receiptDetail.model.ReceiptDetail;
import com.nanoka.restaurant_api.receipt.adapters.input.rest.model.response.*;
import com.nanoka.restaurant_api.receipt.model.Receipt;
import com.nanoka.restaurant_api.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ReceiptRestMapper {

    @Mapping(target = "user", source = "user", qualifiedByName = "mapToUserResponse")
    public abstract ReceiptWithoutDetailResponse toReceiptResponseWithoutDetails(Receipt receipt);

    @Mapping(target = "user", source = "user", qualifiedByName = "mapToUserResponse")
    @Mapping(target = "details", source = "detailList", qualifiedByName = "mapReceiptDetails")
    public abstract ReceiptResponse toReceiptResponse(Receipt receipt);

    @Mapping(target = "user", source = "user", qualifiedByName = "mapToUserResponse")
    public abstract List<ReceiptWithoutDetailResponse> toReceiptResponseListWithoutDetails(List<Receipt> receiptList);

    @Named("mapToUserResponse")
    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    @Named("mapReceiptDetails")
    public List<ReceiptDetailResponse> mapReceiptDetails(List<ReceiptDetail> details) {
        return details.stream()
                .map(this::toReceiptDetailResponse)
                .toList();
    }

    @Mapping(target = "orderDetail", source = "orderDetail", qualifiedByName = "mapToOrderDetailResponse")
    public abstract ReceiptDetailResponse toReceiptDetailResponse(ReceiptDetail detail);

    @Named("mapToOrderDetailResponse")
    public OrderDetailResponse mapOrderDetailResponse(OrderDetail orderDetail) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(orderDetail.getProduct().getId())
                .name(orderDetail.getProduct().getName())
                .build();

        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .product(productResponse)
                .unitPrice(orderDetail.getUnitPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
