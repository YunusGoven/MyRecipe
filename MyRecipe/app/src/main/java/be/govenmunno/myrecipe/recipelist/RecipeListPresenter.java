package be.govenmunno.myrecipe.recipelist;

import androidx.lifecycle.Observer;

import java.util.List;
import be.govenmunno.myrecipe.database.repository.RecipeRepesitory;
import be.govenmunno.myrecipe.model.Recipe;

public class RecipeListPresenter {
    private List<Recipe> recipes;
    private final IRecipeListScreen screen;
    private final String fragmentName;

    public RecipeListPresenter(IRecipeListScreen screen, String fragmentName){
        this.screen = screen;
        this.fragmentName = fragmentName;
    }
    public void loadRecipes(){
        RecipeRepesitory.getInstance().getRecipes(fragmentName).observeForever(new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                RecipeListPresenter.this.recipes = recipes;
                screen.loadView();
            }
        });
    }

    public int getItemCount(){
        if(recipes == null){
            return 0;
        }
        return recipes.size();
    }
    public void showRecipe(IRecipeItemScreen holder, int position){
        Recipe recipe = recipes.get(position);
        holder.showRecipe(recipe.getId(),recipe.getName(),recipe.getCategorie(),recipe.getDureePreparation(),recipe.getDureeCuisson(), recipe.getPathImage());
    }

}
