package com.SpringMVC2.DAOI;

import java.util.List;
import java.util.Set;

import entity.Category;
import entity.Charity;

public interface CategoryDAOI {

	boolean newCategory(Category category);

	List<Category> getAllCategories();

	Category findCategoryById(int id);

	boolean editCategory(Category category);

	Set<Charity> getCharitiesByCat(int catId);

	Category findCategoryByName(String catName);


}
