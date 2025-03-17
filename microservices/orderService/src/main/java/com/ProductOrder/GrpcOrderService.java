package com.ProductOrder;

import com.ProductOrder.OrderServiceGrpc;
import com.ProductOrder.OrderRequest;
import com.ProductOrder.OrderResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcOrderService extends OrderServiceGrpc.OrderServiceImplBase {

    @Override
    public void createOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        // Simulation d'une commande
        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(System.currentTimeMillis())  // Simule un ID unique
                .setStatus("CONFIRMED")
                .setTotalPrice(request.getQuantity() * 10.0)  // Exemple : 10.0€ par produit
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
