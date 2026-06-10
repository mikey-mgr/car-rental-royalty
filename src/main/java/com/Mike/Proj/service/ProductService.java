package com.Mike.Proj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mike.Proj.dto.ProductDto;
import com.Mike.Proj.exceptions.CustomException;
import com.Mike.Proj.exceptions.ProductNotExistsException;
import com.Mike.Proj.model.Cart;
import com.Mike.Proj.model.Category;
import com.Mike.Proj.model.Product;
import com.Mike.Proj.model.Wishlist;
import com.Mike.Proj.repository.CartRepo;
import com.Mike.Proj.repository.ProductRepo;
import com.Mike.Proj.repository.WishlistRepo;

import jakarta.validation.constraints.NotNull;

@Service
public class ProductService {
    
    @Autowired
    ProductRepo productRepo;

    @Autowired
    WishlistRepo wishlistRepo;

    @Autowired
    CartRepo cartRepo;

    
    public void createProduct(ProductDto productDto, Category category) {
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(category);
        product.setBookingStatus(productDto.getBookingStatus());
        product.setFeatures(productDto.getFeatures());
        product.setCarousel_imgs(productDto.getCarousel_imgs());

        productRepo.save(product);
    }

    public ProductDto getProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setDescription(product.getDescription());
        productDto.setImageURL(product.getImageURL());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setId(product.getId());
        productDto.setBookingStatus(product.getBookingStatus());
        productDto.setFeatures(product.getFeatures());
        productDto.setCarousel_imgs(product.getCarousel_imgs());

        return productDto;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> allProducts = productRepo.findAll();

        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product: allProducts){
            productDtos.add(getProductDto(product));
        }
        return productDtos;
    }

    
    @SuppressWarnings("null")
    public void updateProduct(ProductDto productDto, Integer productId, Category category) throws Exception {
        Optional<Product> optionalProduct = productRepo.findById(productId);

        // throw an exception if product doesnt exist
        if(!optionalProduct.isPresent()){
            throw new Exception("Product isn't present");
        }
        Product product = optionalProduct.get();
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(category);
        product.setBookingStatus(productDto.getBookingStatus());
        product.setFeatures(productDto.getFeatures());
        product.setCarousel_imgs(productDto.getCarousel_imgs());
        
        productRepo.save(product);
    }

    @SuppressWarnings("null")
    public Product findById(@NotNull Integer productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if(optionalProduct.isEmpty()){
            throw new ProductNotExistsException("Product id is invalid: " + productId);
        } return optionalProduct.get();
    }

    //delete a product
    @SuppressWarnings("null")
    public void deleteProduct(Integer productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);

        Optional <List<Wishlist>> optionalWishlists = wishlistRepo.findAllByProductId(optionalProduct.get().getId());

        Optional <List<Cart>> optionalCarts = cartRepo.findAllByProductId(optionalProduct.get().getId());

        if(!optionalCarts.get().isEmpty()){
            // cartRepo.deleteAll(optionalCarts.get());
            throw new CustomException("Cannot delete product because there are carts containing product");
        }

        if(optionalWishlists.isPresent()){
            wishlistRepo.deleteAll(optionalWishlists.get());
        }

        if(optionalProduct.isEmpty()){
            throw new ProductNotExistsException("Product id is invalid: " + productId);
        } else { 
            Product product = optionalProduct.get();
            productRepo.delete(product);
        }
    }

    public List<ProductDto> findProduct(String name) {
        List<Product> allProducts = productRepo.findByNameContaining(name);

        List<ProductDto> productDtos = new ArrayList<>();
        if(allProducts.isEmpty()){
            throw new CustomException("Product with name '" + name + "' does not exist");
        }
        for(Product product: allProducts){
            productDtos.add(getProductDto(product));
        }
        return productDtos;
    }
}