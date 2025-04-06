package com.ProductOrder.server;

import com.ProductOrder.OrderServiceGrpc;
import com.ProductOrder.OrderRequest;
import com.ProductOrder.OrderResponse;
import com.ProductOrder.Events.OrderEvent;
import com.ProductOrder.Repository.OrderRepository;
import com.ProductOrder.broker.OrderEventProducer;
import com.ProductOrder.model.Order;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;

@GrpcService
@RequiredArgsConstructor
public class GrpcOrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Override
    public void createOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        long orderId = System.currentTimeMillis();

        Order order = Order.builder()
                .orderId(Long.toString(orderId))
                .productId(request.getProductId())
                .status("PENDING")
                .totalAmount(0.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .transactionId("TXN_" + orderId)
                .build();

        orderRepository.save(order);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(orderId);
        orderEvent.setProductId(request.getProductId());
        orderEvent.setQuantity(request.getQuantity());
        orderEventProducer.sendOrderEvent(orderEvent);

        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(orderId)
                .setStatus("PENDING")
                .setTotalPrice(0.0)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
