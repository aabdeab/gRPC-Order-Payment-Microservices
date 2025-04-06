package com.ProductOrder.server;

import com.ProductOrder.ProductRequest;
import com.ProductOrder.ProductResponse;
import com.ProductOrder.Repository.ProductRepository;
import com.ProductOrder.Product;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GrpcProductServiceTest {

    @Mock
    private ProductRepository productRepository;  // Mock database interaction

    @InjectMocks
    private GrpcProductService grpcProductService; // Service to be tested

    private ProductRequest request;
    private StreamObserver<ProductResponse> responseObserver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        request = ProductRequest.newBuilder().setProductId(1L).build();
        responseObserver = mock(StreamObserver.class);  // Mock StreamObserver
    }

    @Test
    void testGetProductById_ProductFound() {
        // Arrange: Mock ProductRepository behavior (no DB call needed)
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .stock(100)
                .price(10.0)
                .build();

        // Mock the findById method to return the product
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));

        // Act: Simulate the getProductById method
        grpcProductService.getProductById(request, responseObserver);

        // Assert: Verify that the mocked method was called
        verify(productRepository).findById(1L);

        // Build the expected response (using real ProductResponse)
        ProductResponse expectedResponse = ProductResponse.newBuilder()
                .setProductId(1L)
                .setName("Product 1")
                .setStock(100)
                .setPrice(10.0)
                .build();

        // Capture the argument passed to the onNext method using ArgumentCaptor
        ArgumentCaptor<ProductResponse> captor = ArgumentCaptor.forClass(ProductResponse.class);
        verify(responseObserver).onNext(captor.capture());

        // Assert the captured response matches the expected response
        ProductResponse capturedResponse = captor.getValue();
        assertEquals(expectedResponse.getProductId(), capturedResponse.getProductId());
        assertEquals(expectedResponse.getName(), capturedResponse.getName());
        assertEquals(expectedResponse.getStock(), capturedResponse.getStock());
        assertEquals(expectedResponse.getPrice(), capturedResponse.getPrice());

        // Ensure the onCompleted method is called
        verify(responseObserver).onCompleted();
    }

    @Test
    void testGetProductById_ProductNotFound() {
        // Arrange: Mock ProductRepository behavior for non-existent product
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act: Simulate the getProductById method
        grpcProductService.getProductById(request, responseObserver);

        // Assert: Verify that the mocked method was called
        verify(productRepository).findById(1L);

        // Build the expected response for a "Product not found" scenario
        ProductResponse expectedResponse = ProductResponse.newBuilder()
                .setProductId(1L)
                .setName("Product not found")
                .setStock(0)
                .setPrice(0.0)
                .build();

        // Capture the argument passed to the onNext method using ArgumentCaptor
        ArgumentCaptor<ProductResponse> captor = ArgumentCaptor.forClass(ProductResponse.class);
        verify(responseObserver).onNext(captor.capture());

        // Assert the captured response matches the expected response
        ProductResponse capturedResponse = captor.getValue();
        assertEquals(expectedResponse.getProductId(), capturedResponse.getProductId());
        assertEquals(expectedResponse.getName(), capturedResponse.getName());
        assertEquals(expectedResponse.getStock(), capturedResponse.getStock());
        assertEquals(expectedResponse.getPrice(), capturedResponse.getPrice());

        // Ensure the onCompleted method is called
        verify(responseObserver).onCompleted();
    }
}
