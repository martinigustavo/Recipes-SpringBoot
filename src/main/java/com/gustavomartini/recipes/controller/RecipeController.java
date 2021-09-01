package com.gustavomartini.recipes.controller;

import com.gustavomartini.recipes.entity.Recipe;
import com.gustavomartini.recipes.entity.User;
import com.gustavomartini.recipes.service.RecipeService;
import com.gustavomartini.recipes.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private final RecipeService recipeService;

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Integer id) {
        return new ResponseEntity<>(recipeService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Integer>> addRecipe(@Valid @RequestBody Recipe newRecipe, Principal principal) {
        Optional<User> user = userDetailsService.findUserByEmail(principal.getName());
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        newRecipe.setUser(user.get());
        Recipe savedRecipe = recipeService.save(newRecipe);

        return new ResponseEntity<>(
                Collections.singletonMap("id", savedRecipe.getId()),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable Integer id, @Valid @RequestBody Recipe toUpdate, Principal principal) {
        Recipe recipe0 = this.recipeService.findById(id);

        if (!recipe0.getUser().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        this.recipeService.updateRecipe(id, toUpdate);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Integer id, Principal principal) {
        Recipe recipe0 = this.recipeService.findById(id);

        if (!recipe0.getUser().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        }
        this.recipeService.deleteById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipe(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name
    ) {
        List<Recipe> recipes = this.recipeService.searchRecipe(category, name);
        return ResponseEntity.ok(recipes);
    }
}
