package com.gustavomartini.recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.gustavomartini.recipes.entity.Recipe;
import com.gustavomartini.recipes.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe save(Recipe toSave) {
        toSave.setDate(LocalDateTime.now());
        return this.recipeRepository.save(toSave);
    }

    public Recipe findById(Integer id) {
        Optional<Recipe> recipe0 = this.recipeRepository.findById(id);
        return recipe0.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Recipe> searchRecipe(String category, String name) {
        if (category != null && name != null
                || category == null && name == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Optional<List<Recipe>> foundRecipes;
        if (category != null) {
            foundRecipes = this.recipeRepository.findAllRecipesByCategoryIgnoreCaseOrderByDateDesc(category);
        } else {
            foundRecipes = this.recipeRepository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name);
        }

        return foundRecipes.orElse(Collections.emptyList());
    }

    public void deleteById(Integer id) {
        this.recipeRepository.deleteById(id);
    }

    public void updateRecipe(Integer id, Recipe toUpdate) {
        Optional<Recipe> recipe0 = this.recipeRepository.findById(id);
        Recipe updatedRecipe = recipe0.get();
        updatedRecipe.setName(toUpdate.getName());
        updatedRecipe.setDescription(toUpdate.getDescription());
        updatedRecipe.setCategory(toUpdate.getCategory());
        updatedRecipe.setDate(LocalDateTime.now());
        updatedRecipe.setIngredients(toUpdate.getIngredients());
        updatedRecipe.setDirections(toUpdate.getDirections());

        this.save(updatedRecipe);
    }
}

