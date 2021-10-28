package be.govenmunno.myrecipe.recipelist;

import java.util.UUID;

public interface IChangeFragment {
    void onSelectedRecipe(UUID recipeId);
    void showHomeFragment();
    void newRecipeAsked();
    void showAllRecipe();
    void modifyRecipe(UUID recipeId);
}
