package com.ProductOrder;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcProductService {
    @Override
    public void getProduct(ProductRequest p, StreamObserver<ProductResponse> responseObserver){
        ProductResponse.newBuilder().setProductId(p.getProductId())
                .setName(repository.getProduct(p.getProductId()).getName())
                .setPrice()
                .setStock()
                .build();
        return ProductResponse;
        responseObserver.onNext(responseObserver);
        responseObserver.onCompleted();

    }
}
