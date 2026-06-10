package com.Mike.Proj.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.Mike.Proj.common.APIResponse;
import com.Mike.Proj.dto.ProductDto;
import com.Mike.Proj.model.Category;
import com.Mike.Proj.model.Product;
import com.Mike.Proj.repository.CategoryRepo;
import com.Mike.Proj.repository.ProductRepo;
import com.Mike.Proj.service.ProductService;

@RestController
@RequestMapping("/product")
//for configuring access to the backend controller from
//a different server/origin ie. vue app
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductRepo productRepo;

    //add new products
    @PostMapping("/add")
    public ResponseEntity<APIResponse> createProduct(@Valid @RequestBody ProductDto productDto) {
    	
    	// changed OptionalInt to Optional<Integer> to match the type of Category id
    	Optional<Integer> categoryId = Optional.ofNullable(productDto.getCategoryId());
    	Optional<Category> optionalCategory = categoryId.flatMap(categoryRepo::findById);
    	
        // Optional<Category> optionalCategory = categoryRepo.findById(productDto.getCategoryId());
         
         if (!optionalCategory.isPresent()) {
             return new ResponseEntity<APIResponse>(new APIResponse(false, "category does not exist"), HttpStatus.BAD_REQUEST);
         }
         productService.createProduct(productDto, optionalCategory.get());
         return new ResponseEntity<APIResponse>(new APIResponse(true, "product has been added"), HttpStatus.CREATED);
    }

    //Get a list of all products
    @GetMapping("/list")
    public ResponseEntity<List<ProductDto>> getProduct(){
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    //Update a product's details
    @SuppressWarnings("null")
    @PostMapping("/update/{productId}")
    public ResponseEntity<APIResponse> updateProduct(@PathVariable("productId") Integer productId, @Valid @RequestBody ProductDto productDto) throws Exception {
    	
    	// changed OptionalInt to Optional<Integer> to match the type of Category id
    	Optional<Integer> categoryId = Optional.ofNullable(productDto.getCategoryId());
    	Optional<Category> optionalCategory = categoryId.flatMap(categoryRepo::findById);

        Optional<Product> optionalProduct = productRepo.findById(productId);
         
         if (!optionalCategory.isPresent()) {
             return new ResponseEntity<APIResponse>(new APIResponse(false, "Category does not exist"), HttpStatus.BAD_REQUEST);
         }else if(!optionalProduct.isPresent()){
            return new ResponseEntity<APIResponse>(new APIResponse(false, "Product does not exist"), HttpStatus.BAD_REQUEST);
         }

         productService.updateProduct(productDto, productId);
         return new ResponseEntity<APIResponse>(new APIResponse(true, "Product has been updated"), HttpStatus.OK);
    }

    //delete a product
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<APIResponse> deleteProduct(@PathVariable("productId") Integer productId){
        productService.deleteProduct(productId);

        return new ResponseEntity<>(new APIResponse(true, "Product deleted successfully"), HttpStatus.OK);
    }

    //find car by name
    @GetMapping("/find/")
    public ResponseEntity<List<ProductDto>> findProduct(@RequestParam("name") String name){
        List<ProductDto> products = productService.findProduct(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
