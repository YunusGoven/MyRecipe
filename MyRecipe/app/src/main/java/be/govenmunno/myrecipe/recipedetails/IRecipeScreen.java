package be.govenmunno.myrecipe.recipedetails;

import java.util.List;
import java.util.Map;

public interface IRecipeScreen{
    /**
     * Methode qui permet d'afficher une recette
     */
    void showRecipe(boolean isLiked, List<String> ingredient, Map<Integer,String> etape, String... informations);

    /**
     * Methode qui permet d'afficher un messgage poour l'ajout
     */
    void showMessage(String msg, boolean err);

    /**
     * Methode qui permet de partager une recette
     */
    void share(String textRecipe);
}
