package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "orderIds", expression = "java(mapOrdersToIds(product.getOrders()))")
    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productsToProductDTOs(List<Product> products);

    ProductOrderDTO orderToProductOrderDTO(Order order);

    default List<Long> mapOrdersToIds(List<Order> orders) {
        if (orders == null) {
            return List.of();
        }
        return orders.stream().map(Order::getId).collect(Collectors.toList());
    }
}
