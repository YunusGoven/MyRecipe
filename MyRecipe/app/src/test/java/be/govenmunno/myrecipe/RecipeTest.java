package be.govenmunno.myrecipe;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import be.govenmunno.myrecipe.model.Recipe;

import static org.junit.Assert.*;

public class RecipeTest {

    @Test
    public void IdTest(){
        Recipe recipe = new Recipe();
        UUID id = UUID.randomUUID();
        recipe.setId(id);

        assertEquals(id, recipe.getId());
    }

    @Test
    public void NameTest(){
        Recipe recipe = new Recipe();
        String name = "nametest";
        recipe.setName(name);

        assertEquals(name, recipe.getName());
    }

    @Test
    public void ingredientsTest(){
        Recipe recipe = new Recipe();
        List<String> ingredients = new ArrayList<>();
        ingredients.add("ingredientstest");
        recipe.setIngredients(ingredients);

        assertEquals(ingredients, recipe.getIngredients());
    }

    @Test
    public void etapesTest(){
        Recipe recipe = new Recipe();
        Map<Integer, String> etapes = new HashMap<>();
        etapes.put(1, "etapestest");
        recipe.setEtapes(etapes);

        assertEquals(etapes, recipe.getEtapes());
    }

    @Test
    public void cuissonTest(){
        Recipe recipe = new Recipe();
        int cuisson = 55;
        recipe.setDureeCuisson(cuisson);

        assertEquals(cuisson, recipe.getDureeCuisson());
    }

    @Test
    public void preparationTest(){
        Recipe recipe = new Recipe();
        int prep = 55;
        recipe.setDureePreparation(prep);

        assertEquals(prep, recipe.getDureePreparation());
    }

    @Test
    public void categorieTest(){
        Recipe recipe = new Recipe();
        String cat = "test";
        recipe.setCategorie(cat);

        assertEquals(cat, recipe.getCategorie());
    }

    @Test
    public void difficultyTest(){
        Recipe recipe = new Recipe();
        int diff = 55;
        recipe.setDifficulte(diff);

        assertEquals(diff, recipe.getDifficulte());
    }

    @Test
    public void pathTest(){
        Recipe recipe = new Recipe();
        String path = "/path/test";
        recipe.setPathImage(path);

        assertEquals(path, recipe.getPathImage());
    }
    @Test
    public void likeTest(){
        Recipe recipe = new Recipe();
        assertFalse(recipe.isLiked());
        recipe.setLiked(true);
        assertTrue(recipe.isLiked());
    }
}
