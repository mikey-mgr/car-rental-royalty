package com.Mike.Proj.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Mike.Proj.common.APIResponse;
import com.Mike.Proj.dto.ProductDto;
import com.Mike.Proj.model.Category;
import com.Mike.Proj.service.CategoryService;
import com.Mike.Proj.service.ProductService;


@RestController
@RequestMapping("/category")
//for configuring access to the backend controller from
//a different server/origin ie. vue app
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;
	
	//create a new category
	@PostMapping("/create")
	public ResponseEntity<APIResponse> createCategory(@Valid @RequestBody Category category) {
		categoryService.createCategory(category);
		return new ResponseEntity<APIResponse>(new APIResponse(true, "New category has been created"), HttpStatus.CREATED);
		
	}
	//List of all products
	@GetMapping("/list")
	public List<Category> listCategory() {
		return categoryService.listCategory();
	}

	//List of all products in category
	@GetMapping("/show/{categoryId}")
	public List<ProductDto> categoryProducts(@PathVariable("categoryId") Integer categoryId){
			return categoryService.categoryProducts(categoryId);
	}

	//update a category's details
	@PostMapping("/update/{categoryId}")
	public ResponseEntity<APIResponse> editCategory(@PathVariable("categoryId")Integer categoryId, @Valid @RequestBody Category updatedCategory){
		if(!categoryService.findById(categoryId)){
			return new ResponseEntity<APIResponse>(new APIResponse(false, "Category not found"), HttpStatus.NOT_FOUND);
		}

		categoryService.editCategory(categoryId, updatedCategory);
		return new ResponseEntity<APIResponse>(new APIResponse(true, "Category updated successfully"), HttpStatus.OK);
	}

	//delete a category by id
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<APIResponse> deleteCategory(@PathVariable("categoryId") Integer categoryId){

		categoryService.deleteCategory(categoryId);

		return new ResponseEntity<>(new APIResponse(true, "Category has been deleted"), HttpStatus.OK);
	}
}
