package com.devwonder.product_service.service;

import com.devwonder.product_service.dto.ProductCreateDTO;
import com.devwonder.product_service.dto.ProductImageCreateDTO;
import com.devwonder.product_service.dto.ProductVideoCreateDTO;
import com.devwonder.product_service.model.Product;
import com.devwonder.product_service.model.ProductImage;
import com.devwonder.product_service.model.ProductVideo;
import com.devwonder.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public List<Product> getProductsByCategory(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameOrDescriptionContainingIgnoreCase(keyword);
    }

    public List<Product> getPublishedProducts() {
        return productRepository.findPublishedProducts();
    }

    public List<Product> getProductsByRating(Double minRating) {
        return productRepository.findByRatingGreaterThanEqual(minRating);
    }

    @Transactional
    public Product createProduct(ProductCreateDTO createDTO) {
        Product product = new Product();
        
        // Generate ID if not provided
        if (createDTO.getId() == null || createDTO.getId().trim().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        } else {
            product.setId(createDTO.getId());
        }
        
        product.setName(createDTO.getName());
        product.setSubtitle(createDTO.getSubtitle());
        product.setDescription(createDTO.getDescription());
        product.setLongDescription(createDTO.getLongDescription());
        product.setCategoryId(createDTO.getCategoryId());
        product.setSpecifications(createDTO.getSpecifications());
        product.setFeatures(createDTO.getFeatures());
        product.setAvailability(createDTO.getAvailability());
        product.setWarranty(createDTO.getWarranty());
        product.setHighlights(createDTO.getHighlights());
        product.setTargetAudience(createDTO.getTargetAudience());
        product.setUseCases(createDTO.getUseCases());
        product.setTags(createDTO.getTags());
        product.setRelatedProductIds(createDTO.getRelatedProductIds());
        product.setAccessories(createDTO.getAccessories());
        product.setPopularity(createDTO.getPopularity());
        product.setRating(createDTO.getRating());
        product.setReviewCount(createDTO.getReviewCount());
        product.setSku(createDTO.getSku());
        product.setSeoTitle(createDTO.getSeoTitle());
        product.setSeoDescription(createDTO.getSeoDescription());
        
        // Save product first to get the ID
        Product savedProduct = productRepository.save(product);
        
        // Handle images
        if (createDTO.getImages() != null && !createDTO.getImages().isEmpty()) {
            List<ProductImage> images = createDTO.getImages().stream()
                    .map(imageDTO -> {
                        ProductImage image = new ProductImage();
                        image.setImageId(imageDTO.getImageId());
                        image.setUrl(imageDTO.getUrl());
                        image.setAltText(imageDTO.getAltText());
                        image.setType(imageDTO.getType());
                        image.setDisplayOrder(imageDTO.getDisplayOrder());
                        image.setProductId(savedProduct.getId());
                        return image;
                    })
                    .collect(Collectors.toList());
            savedProduct.setImages(images);
        }
        
        // Handle videos
        if (createDTO.getVideos() != null && !createDTO.getVideos().isEmpty()) {
            List<ProductVideo> videos = createDTO.getVideos().stream()
                    .map(videoDTO -> {
                        ProductVideo video = new ProductVideo();
                        video.setVideoId(videoDTO.getVideoId());
                        video.setTitle(videoDTO.getTitle());
                        video.setDescription(videoDTO.getDescription());
                        video.setUrl(videoDTO.getUrl());
                        video.setThumbnail(videoDTO.getThumbnail());
                        video.setDuration(videoDTO.getDuration());
                        video.setType(videoDTO.getType());
                        video.setProductId(savedProduct.getId());
                        return video;
                    })
                    .collect(Collectors.toList());
            savedProduct.setVideos(videos);
        }
        
        return productRepository.save(savedProduct);
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}