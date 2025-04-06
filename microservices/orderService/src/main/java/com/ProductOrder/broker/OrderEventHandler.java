package com.ProductOrder.broker;

import com.ProductOrder.DTOs.OrderDTO;
import com.ProductOrder.Events.InventoryResponseEvent;
import com.ProductOrder.Repository.OrderRepository;
import com.ProductOrder.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class OrderEventHandler {
    @Autowired
    private OrderRepository repository;

    @KafkaListener(topics = "Inventory-Response-topic", groupId = "OrderGroup")
    public OrderDTO inventoryResponseConsumer(InventoryResponseEvent e) {
        log.info("Received inventory response for order: {}", e.getOrderId());

        if (e.getStatus().equals("FAILURE")) {
            log.error("Updating Inventory Failed: {}", e.getFailureReason());
            Order order = Order.builder()
                    .orderId(e.getOrderId().toString())
                    .status("FAILED")
                    .totalAmount(0.0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .transactionId("TXN_" + System.currentTimeMillis())
                    .build();

            Order savedOrder = repository.save(order);
            return mapToOrderDTO(savedOrder);
        } else {
            log.info("Update Inventory with success for order with id: {}", e.getOrderId());
            Optional<Order> orderOptional = repository.findById(e.getOrderId().toString());

            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                order.setStatus("PROCESSING");
                order.setTotalAmount(e.getPrice());
                order.setUpdatedAt(LocalDateTime.now());
                Order savedOrder = repository.save(order);
                return mapToOrderDTO(savedOrder);
            } else {
                log.error("Order not found for id: {}", e.getOrderId());
                Order newOrder = Order.builder()
                        .orderId(e.getOrderId().toString())
                        .status("PROCESSING")
                        .totalAmount(e.getPrice())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .transactionId("TXN_" + System.currentTimeMillis())
                        .build();
                Order savedOrder = repository.save(newOrder);
                return mapToOrderDTO(savedOrder);
            }
        }
    }

    private OrderDTO mapToOrderDTO(Order order) {
        return OrderDTO.builder()
                .orderId(Long.parseLong(order.getOrderId()))
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .transactionId(order.getTransactionId())
                .build();
    }
}