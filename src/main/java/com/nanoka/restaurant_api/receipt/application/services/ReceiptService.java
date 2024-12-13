package com.nanoka.restaurant_api.receipt.application.services;

import java.util.ArrayList;

import com.nanoka.restaurant_api.receipt.application.ports.input.ReceiptServicePort;
import com.nanoka.restaurant_api.receipt.application.ports.output.ReceiptPersistencePort;
import com.nanoka.restaurant_api.receipt.domain.model.Receipt;
import com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.model.request.ReceiptCreateRequest;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import com.nanoka.restaurant_api.client.application.ports.input.ClientServicePort;
import com.nanoka.restaurant_api.serie.application.ports.input.SerieServicePort;
import com.nanoka.restaurant_api.serie.domain.model.Serie;
import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.domain.model.User;

import com.nanoka.restaurant_api.client.domain.model.Client;
import com.nanoka.restaurant_api.order.application.ports.input.OrderServicePort;
import com.nanoka.restaurant_api.order.domain.model.Order;
import com.nanoka.restaurant_api.orderDetail.aplication.ports.input.OrderDetailServicePort;
import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.productMovement.application.ports.input.ProductMovementServicePort;
import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;
import com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.model.request.ReceiptDetailCreateRequest;
import com.nanoka.restaurant_api.receiptDetail.model.ReceiptDetail;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptService implements ReceiptServicePort {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
    private final ReceiptPersistencePort persistencePort;
    private final UserServicePort userServicePort;
    private final SerieServicePort SerieServicePort;
    private final ClientServicePort clientServicePort;
    private final OrderServicePort orderServicePort;
    private final ProductServicePort productServicePort;
    private final OrderDetailServicePort orderDetailServicePort;

    @Override
    public Receipt findById(Long id) {
        logger.info("Buscando recibo con id: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.RECEIPT_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Receipt> findAll() {
        logger.info("Buscando todos los recibos");
        return persistencePort.findAll();
    }

    @Override
    @Transactional
    public Receipt save(ReceiptCreateRequest request) {
        logger.info("Guardando recibo con id de orden: {}", request.getOrderId());
        // Obtener el usuario desde el contexto de seguridad
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userServicePort.findByUsername(username);

        // Obtenemos la serie desde el request
        Serie serie = SerieServicePort.findById(request.getSerieId());

        // Obtenemos el cliente desde el request
        Client client = clientServicePort.findById(request.getClientId());

        //Obtenemos la orden desde el request
        Order order = orderServicePort.findById(request.getOrderId());

        // Validar que la lista de detalles no esté vacía
        if (request.getDetails() == null || request.getDetails().isEmpty()) {
            logger.warn("La lista de detalles del recibo está vacía");
            throw new IllegalArgumentException("La lista de detalles del recibo no puede estar vacía");
        }

        // Crea el recibo base
        Receipt receipt = new Receipt();
        receipt.setUser(user);
        receipt.setSerie(serie);
        receipt.setCorrelative(serie.getCorrelative());
        receipt.setClient(client);
        receipt.setOrder(order);
        receipt.setTotalAmount(request.getTotalAmount());
        receipt.setPaymentMethod(request.getPaymentMethod());
        receipt.setObservation(request.getObservation());

        // Guarda el recibo
        receipt = persistencePort.save(receipt);

        // Actualiza la serie
        SerieServicePort.updateCorrelative(serie.getId());

        // Lista para almacenar los detalles del recibo
        List<ReceiptDetail> detailList = new ArrayList<>();

        // Manejo de los detalles del recibo
        for (ReceiptDetailCreateRequest detailRequest : request.getDetails()) {
            OrderDetail orderDetail = orderDetailServicePort.findById(detailRequest.getOrderDetailId()); 

            // Crea el detalle del recibo
            ReceiptDetail detail = ReceiptDetail.builder()
                    .orderDetail(orderDetail)
                    .quantity(detailRequest.getQuantity())
                    .build();
            detailList.add(detail);

            // Actualiza la cantidad pagada en el detalle de la orden
            orderDetailServicePort.updateQuantityPaid(orderDetail.getId(), orderDetail.getQuantityPaid() + detailRequest.getQuantity());
        }

        // Verificamos si la orden fue pagada en su totalidad, se verifica si en todos los detalles de la orden la cantidad pagada es igual a la cantidad solicitada
        order.setPaid(order.getDetailList().stream().allMatch(od -> od.getQuantityPaid() == od.getQuantity()));
        // Actualizamos la orden
        orderServicePort.update(order.getId(), order);

        // Guardamos los detalles del recibo
        receipt.setDetailList(detailList);
        
        //guardamos el recibo actualizado
        receipt = persistencePort.save(receipt);

        return receipt;

    }
}
