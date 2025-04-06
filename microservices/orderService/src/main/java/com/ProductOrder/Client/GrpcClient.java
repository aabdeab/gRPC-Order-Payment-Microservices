package com.ProductOrder.Client;

import com.ProductOrder.OrderRequest;
import com.ProductOrder.OrderResponse;
import com.ProductOrder.OrderServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
        OrderRequest request = OrderRequest.newBuilder()
                .setProductId(1)
                .setQuantity(3)
                .build();
        OrderResponse response = stub.createOrder(request);
        System.out.println("Commande créée : ID = " + response.getOrderId() +
                ", Statut = " + response.getStatus() +
                ", Prix total = " + response.getTotalPrice());

        channel.shutdown();
    }
}
