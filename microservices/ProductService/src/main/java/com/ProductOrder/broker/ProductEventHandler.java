package com.ProductOrder.broker;

import com.ProductOrder.Events.InventoryResponseEvent;
import com.ProductOrder.Events.OrderEvent;
import com.ProductOrder.Product;
import com.ProductOrder.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventHandler {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, InventoryResponseEvent> kafkaTemplate;

    @KafkaListener(topics = "order-event-topic", groupId = "productGroup")
    public void consumeOrderEvent(OrderEvent orderEvent) {
        log.info("Order event received in product service: {}", orderEvent.getOrderId());

        Long productId = orderEvent.getProductId();
        int quantity = orderEvent.getQuantity();

        InventoryResponseEvent responseEvent = new InventoryResponseEvent();
        responseEvent.setOrderId(orderEvent.getOrderId());
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            responseEvent.setStatus("FAILURE");
            responseEvent.setFailureReason("Product not found");
        } else if (product.getStock() < quantity) {
            responseEvent.setStatus("FAILURE");
            responseEvent.setFailureReason("Insufficient stock");
        } else {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            responseEvent.setStatus("AVAILABLE");
            responseEvent.setProductId(productId);
            responseEvent.setPrice(product.getPrice() * quantity);
        }
        kafkaTemplate.send("Inventory-Response-topic", responseEvent);
        log.info("Inventory response sent for order: {}", orderEvent.getOrderId());
    }
}