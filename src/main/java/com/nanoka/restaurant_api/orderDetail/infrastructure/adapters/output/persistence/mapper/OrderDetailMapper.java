package com.nanoka.restaurant_api.orderDetail.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.orderDetail.persistence.entity.OrderDetailEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailEntity toOrderDetailEntity(OrderDetail orderDetail);
    OrderDetail toOrderDetail(OrderDetailEntity entity);
}
