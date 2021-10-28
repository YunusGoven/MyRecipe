package be.govenmunno.myrecipe.recipeeditor;

import androidx.lifecycle.ViewModel;

import java.util.UUID;

public class RecipeViewModel extends ViewModel {

    private UUID recipeId;
    public void setRecipeId(UUID recipeId){this.recipeId =recipeId;}
    public UUID getRecipeId(){return recipeId;}
}
