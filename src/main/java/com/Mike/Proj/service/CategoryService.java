package com.Mike.Proj.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import com.Mike.Proj.dto.ProductDto;
import com.Mike.Proj.exceptions.CustomException;
import com.Mike.Proj.model.Category;
import com.Mike.Proj.model.Product;
import com.Mike.Proj.repository.CategoryRepo;
import com.Mike.Proj.repository.ProductRepo;

@Service
public class CategoryService {

	@Autowired
	CategoryRepo categoryRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	ProductService productService;

	public void createCategory(@NonNull Category category) {
		categoryRepo.save(category);
		
	}

	//list products in category
	public List<ProductDto> categoryProducts(Integer id) {
		List<ProductDto> categoryProducts = new ArrayList<>();
		for(ProductDto product: productService.getAllProducts()){
			if(product.getCategoryId().equals(id)){
				categoryProducts.add(product);	
			}
		}
		return categoryProducts;
	}
	
	public List<Category> listCategory() {
        return categoryRepo.findAll();
    }

	public void editCategory(int categoryId, Category updatedCategory) {
		@SuppressWarnings("deprecation")
		Category category = categoryRepo.getById(categoryId);
		category.setCategoryName(updatedCategory.getCategoryName());
		category.setDescription(updatedCategory.getDescription());
		category.setImageUrl(updatedCategory.getImageUrl());
		categoryRepo.save(category);
	}

	public boolean findById(int categoryId) {
		return categoryRepo.findById(categoryId).isPresent();
	}

    @SuppressWarnings("null")
	public void deleteCategory(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);
        
        // throw an exception if category doesn't exist
        if(optionalCategory.isEmpty()){
            throw new CustomException("Category isn't present");
        }
        
		Category category = optionalCategory.get();
		Optional<List<Product>> optionalProducts = productRepo.findAllByCategoryId(category.getId());

		if(!optionalProducts.isEmpty() && !optionalProducts.get().isEmpty()){
			throw new CustomException("Cannot delete category, products exist in it");
		}

		categoryRepo.delete(category);
    }
}