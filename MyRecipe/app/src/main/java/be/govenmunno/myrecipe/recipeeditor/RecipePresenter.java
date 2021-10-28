package be.govenmunno.myrecipe.recipeeditor;

import android.graphics.Bitmap;
import android.os.Environment;

import androidx.lifecycle.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import be.govenmunno.myrecipe.database.repository.RecipeRepesitory;
import be.govenmunno.myrecipe.model.Recipe;
import be.govenmunno.myrecipe.recipedetails.IRecipeScreen;

public class RecipePresenter {
    private final IRecipeScreen screen;
    private Recipe recipe;

    public RecipePresenter(IRecipeScreen screen){this.screen = screen;}

    public void loadRecipe(UUID recipeId){
        RecipeRepesitory.getInstance().getRecipe(recipeId).observeForever(new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe!=null) {
                    RecipePresenter.this.recipe = recipe;
                    boolean isLiked = recipe.isLiked();
                    Map<Integer, String> etape = recipe.getEtapes();
                    List<String> ingredient = recipe.getIngredients();
                    String[] informartion = {
                            recipe.getName(),
                            recipe.getPathImage(),
                            recipe.getCategorie(),
                            String.valueOf(recipe.getDifficulte()),
                            String.valueOf(recipe.getDureePreparation()),
                            String.valueOf(recipe.getDureeCuisson())
                    };
                    screen.showRecipe(isLiked, ingredient, etape, informartion);
                }
            }
        });
    }
    public void saveRecipe(String name, String categorie, String difficulte, int dureeCuis, int dureePrep, List<String> ingredients, Map<Integer, String> etapes, Bitmap bitmap){

        updateRecipe(name,categorie,difficulte,dureeCuis,dureePrep,ingredients,etapes,bitmap);
        RecipeRepesitory.getInstance().updateRecipe(recipe);
    }
    private String saveImageToGallery(Bitmap bitmap) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        try {
            boolean isreated = directory.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageName = String.format("%s.png",recipe.getId().toString());
        File file = new File(directory,imageName);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.format("%s/%s",directory.getAbsolutePath(),imageName);

    }

    private void insertRecipe(String name, String categorie, String difficulte, int dureeCuis, int dureePrep,
                             List<String> ingredients, Map<Integer, String> etapes, Bitmap bitmap){
        this.recipe = new Recipe();
        updateRecipe(name,categorie,difficulte,dureeCuis,dureePrep,ingredients,etapes,bitmap);
        RecipeRepesitory.getInstance().insertRecipe(recipe);
    }
    private void updateRecipe(String name, String categorie, String difficulte, int dureeCuis, int dureePrep,
                            List<String> ingredients, Map<Integer, String> etapes, Bitmap bitmap){
        recipe.setName(name);
        recipe.setCategorie(categorie);
        recipe.setDifficulte(Integer.parseInt(difficulte));
        recipe.setDureeCuisson(dureeCuis);
        recipe.setDureePreparation(dureePrep);
        recipe.setIngredients(ingredients);
        recipe.setEtapes(etapes);

        String path = (bitmap != null)?saveImageToGallery(bitmap) : "";

        recipe.setPathImage(path);

    }
    public void likeRecipe(UUID recipeId) {
        boolean like = recipe.isLiked();
        recipe.setLiked(!like);
        RecipeRepesitory.getInstance().changeLike(recipe);
    }
    public void delete(UUID recipeId) {
        RecipeRepesitory.getInstance().delete(recipeId);
    }

    public void checkValues(UUID recipeId, String name, String categorie, String difficulte, int dureeCuis, int dureePrep,
                            List<String> ingredients, Map<Integer, String> etapes, Bitmap bitmap) {
        boolean err = false;
        String msg = "La recette a été ajoutée !" ;
        if(name.isEmpty()) {
            msg =  "Le nom de la recette ne peut pas être vide !";
            err = true;
        }
        if(ingredients.isEmpty()) {
            msg = "La recette doit contenir au moins 1 ingrédient !";
            err = true;
        }
        if(etapes.isEmpty()) {
            msg =  "La recette doit contenir au moins 1 étape !";
            err = true;
        }
        if("Choissisez une catégorie".equals(categorie)){
            msg =  "Vous devez choisir une catégorie !";
            err = true;
        }
        if(!err) {
            if(recipeId==null)
               insertRecipe(name, categorie, difficulte, dureeCuis, dureePrep, ingredients, etapes, bitmap);
            else if(recipeId !=null) {
                saveRecipe(name, categorie, difficulte, dureeCuis, dureePrep, ingredients, etapes, bitmap);
                msg = "la recette a bien été modifié";
            }

        }
        screen.showMessage(msg, err);
    }
    public void shareAsked() {
        if(recipe != null){
            String textRecipe = recipe.toString();
            screen.share(textRecipe);
        }
    }



}
