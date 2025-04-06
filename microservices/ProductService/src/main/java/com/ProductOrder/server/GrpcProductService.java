package com.ProductOrder.server;

import com.ProductOrder.Product;
import com.ProductOrder.ProductRequest;
import com.ProductOrder.ProductResponse;
import com.ProductOrder.ProductServiceGrpc;
import com.ProductOrder.Repository.ProductRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GrpcProductService extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void getProductById(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        Product product = productRepository.findById(request.getProductId())
                .orElse(null);
        ProductResponse.Builder responseBuilder = ProductResponse.newBuilder();
        if (product != null) {
            responseBuilder
                    .setProductId(product.getProductId())
                    .setName(product.getName())
                    .setStock(product.getStock())
                    .setPrice(product.getPrice());
        } else {
            responseBuilder
                    .setProductId(request.getProductId())
                    .setName("Product not found")
                    .setStock(0)
                    .setPrice(0.0);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}