package com.ProductOrder;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) {
        // Création du canal gRPC vers le serveur
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        // Créer un stub pour appeler le service
        OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);

        // Construire la requête
        OrderRequest request = OrderRequest.newBuilder()
                .setProductId(1)
                .setQuantity(3)
                .build();

        // Envoyer la requête et récupérer la réponse
        OrderResponse response = stub.createOrder(request);

        // Afficher la réponse
        System.out.println("Commande créée : ID = " + response.getOrderId() +
                ", Statut = " + response.getStatus() +
                ", Prix total = " + response.getTotalPrice());

        // Fermer le canal
        channel.shutdown();
    }
}
