package com.gustavomartini.recipes.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.gustavomartini.recipes.entity.Recipe;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

    void deleteById(Integer id);
    Optional<Recipe> findById(Integer id);
    Optional<List<Recipe>> findAllByNameContainingIgnoreCaseOrderByDateDesc(String name);
    Optional<List<Recipe>> findAllRecipesByCategoryIgnoreCaseOrderByDateDesc(String category);

}
