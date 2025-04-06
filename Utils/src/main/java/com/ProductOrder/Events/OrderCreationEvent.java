package com.ProductOrder.Events;

import com.ProductOrder.DTOs.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreationEvent {
    private Long OrderId;
    private List<ProductDTO> p;
    private Double TotalAmount;
    private Double Quantity;
    private Date CreatedAt;

}
