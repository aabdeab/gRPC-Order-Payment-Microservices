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
        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(System.currentTimeMillis())
                .setStatus("PENDING")
                .setTotalPrice(request.getQuantity())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
