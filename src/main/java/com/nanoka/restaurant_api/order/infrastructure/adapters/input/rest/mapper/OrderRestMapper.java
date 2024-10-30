package com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.response.*;
import com.nanoka.restaurant_api.order.domain.model.Order;
import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class OrderRestMapper {

    @Mapping(target = "user", source = "user", qualifiedByName = "mapToUserResponse")
    public abstract OrderWithoutDetailResponse toOrderResponseWithoutDetails(Order order);

    @Mapping(target = "user", source = "user", qualifiedByName = "mapToUserResponse")
    @Mapping(target = "details", source = "detailList", qualifiedByName = "mapOrderDetails")
    public abstract OrderResponse toOrderResponse(Order order);

    @Mapping(target = "user", source = "user", qualifiedByName = "mapToUserResponse")
    public abstract List<OrderWithoutDetailResponse> toOrderResponseListWithoutDetails(List<Order> orderList);

    @Named("mapToUserResponse")
    public UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName());
    }

    @Named("mapOrderDetails")
    public List<OrderDetailResponse> mapOrderDetails(List<OrderDetail> details) {
        return details.stream()
                .map(this::toOrderDetailResponse)
                .toList();
    }

    @Mapping(target = "product", source = "product", qualifiedByName = "mapProductResponse")
    public abstract OrderDetailResponse toOrderDetailResponse(OrderDetail detail);

    @Named("mapProductResponse")
    public ProductResponse mapProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName());
    }
}
