package be.govenmunno.myrecipe.recipelist;

import java.util.UUID;


interface IRecipeItemScreen{
    void showRecipe(UUID id, String name, String categorie, int dureePreparation, int dureeCuisson, String pathImage);
}