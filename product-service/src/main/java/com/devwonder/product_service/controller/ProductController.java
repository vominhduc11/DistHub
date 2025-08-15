package com.devwonder.product_service.controller;

import com.devwonder.product_service.dto.*;
import com.devwonder.product_service.model.Product;
import com.devwonder.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDetailDTO> createProduct(@Valid @RequestBody ProductCreateDTO createDTO) {
        try {
            Product createdProduct = productService.createProduct(createDTO);
            ProductDetailDTO responseDTO = convertToDetailDTO(createdProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDetailDTO>> getAllProducts() {
        List<ProductDetailDTO> products = productService.getAllProducts()
                .stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductById(@PathVariable String id) {
        return productService.getProductById(id)
                .map(this::convertToDetailDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDetailDTO> getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku)
                .map(this::convertToDetailDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDetailDTO>> getProductsByCategory(@PathVariable String categoryId) {
        List<ProductDetailDTO> products = productService.getProductsByCategory(categoryId)
                .stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDetailDTO>> searchProducts(@RequestParam String keyword) {
        List<ProductDetailDTO> products = productService.searchProducts(keyword)
                .stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/published")
    public ResponseEntity<List<ProductDetailDTO>> getPublishedProducts() {
        List<ProductDetailDTO> products = productService.getPublishedProducts()
                .stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/rating/{minRating}")
    public ResponseEntity<List<ProductDetailDTO>> getProductsByRating(@PathVariable Double minRating) {
        List<ProductDetailDTO> products = productService.getProductsByRating(minRating)
                .stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    private ProductDetailDTO convertToDetailDTO(Product product) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSubtitle(product.getSubtitle());
        dto.setDescription(product.getDescription());
        dto.setLongDescription(product.getLongDescription());
        
        if (product.getCategory() != null) {
            ProductCategoryDTO categoryDTO = new ProductCategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            categoryDTO.setDescription(product.getCategory().getDescription());
            categoryDTO.setSlug(product.getCategory().getSlug());
            dto.setCategory(categoryDTO);
        }
        
        dto.setSpecifications(product.getSpecifications());
        dto.setFeatures(product.getFeatures());
        dto.setAvailability(product.getAvailability());
        dto.setWarranty(product.getWarranty());
        dto.setHighlights(product.getHighlights());
        dto.setTargetAudience(product.getTargetAudience());
        dto.setUseCases(product.getUseCases());
        dto.setTags(product.getTags());
        dto.setRelatedProductIds(product.getRelatedProductIds());
        dto.setAccessories(product.getAccessories());
        dto.setPopularity(product.getPopularity());
        dto.setRating(product.getRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setSku(product.getSku());
        dto.setSeoTitle(product.getSeoTitle());
        dto.setSeoDescription(product.getSeoDescription());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setPublishedAt(product.getPublishedAt());

        if (product.getImages() != null) {
            List<ProductImageDTO> imageDTOs = product.getImages().stream()
                    .map(image -> {
                        ProductImageDTO imageDTO = new ProductImageDTO();
                        imageDTO.setId(image.getId());
                        imageDTO.setImageId(image.getImageId());
                        imageDTO.setUrl(image.getUrl());
                        imageDTO.setAltText(image.getAltText());
                        imageDTO.setType(image.getType());
                        imageDTO.setDisplayOrder(image.getDisplayOrder());
                        return imageDTO;
                    })
                    .collect(Collectors.toList());
            dto.setImages(imageDTOs);
        }

        if (product.getVideos() != null) {
            List<ProductVideoDTO> videoDTOs = product.getVideos().stream()
                    .map(video -> {
                        ProductVideoDTO videoDTO = new ProductVideoDTO();
                        videoDTO.setId(video.getId());
                        videoDTO.setVideoId(video.getVideoId());
                        videoDTO.setTitle(video.getTitle());
                        videoDTO.setDescription(video.getDescription());
                        videoDTO.setUrl(video.getUrl());
                        videoDTO.setThumbnail(video.getThumbnail());
                        videoDTO.setDuration(video.getDuration());
                        videoDTO.setType(video.getType());
                        return videoDTO;
                    })
                    .collect(Collectors.toList());
            dto.setVideos(videoDTOs);
        }

        return dto;
    }
}