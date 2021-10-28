package be.govenmunno.myrecipe.database.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.govenmunno.myrecipe.database.RecipeDatabase;
import be.govenmunno.myrecipe.database.dao.RecipeDao;
import be.govenmunno.myrecipe.model.Recipe;

public class RecipeRepesitory {
    private static RecipeRepesitory instance;

    private final RecipeDao recipeDao = RecipeDatabase.getInstance().recipeDao();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private RecipeRepesitory() {}

    /**
     * Methide qui renvoie la liste des recettes par rapport au fragment
     * @param fragmentName nom du fragment
     * @return une liste de recette
     */
    public LiveData<List<Recipe>> getRecipes(String fragmentName) {
        if("liked".equals(fragmentName)){
            return recipeDao.getLikedRecipe();
        }else if("last".equals(fragmentName)){
            return  recipeDao.getLatestRecipe(10);
        }else if("unlike".equals(fragmentName)){
            return  recipeDao.getUnlikeRecipes();
        }else if("all".equals(fragmentName)||"Choissisez une catégorie".equals(fragmentName) ){
            return recipeDao.getAllRecipe();
        }

        return recipeDao.getThisList(fragmentName);
    }

    /**
     * Methode qui retourne une recette par rapport a son identifiant
     * @param uuid id de la recette
     * @return une recette
     */
    public LiveData<Recipe> getRecipe(UUID uuid) {
        return recipeDao.getRecipe(uuid);
    }

    /**
     * Methode qui permet d'ajouter une recette dans la bd
     * @param recipe la recete a ajouté
     */
    public void insertRecipe(final Recipe recipe) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.insert(recipe);
            }
        });
    }

    /**
     * Methode qui permet de mettre a jour une recette
     * @param recipe la recette a modifier dans la bd
     */
    public void updateRecipe(final Recipe recipe) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.update(recipe);
            }
        });
    }

    /**
     * Fonction qui permet d'instancier un RecipeRepesitory
     * @return l'instance
     */
    public static RecipeRepesitory getInstance() {
        if(instance == null)
            instance = new RecipeRepesitory();
        return instance;
    }

    /**
     * Methode qui permet de changer le like d'une recette dans la bd
     * @param recipe la recette modifié
     */
   public void changeLike(final Recipe recipe) {
       executor.execute(new Runnable() {
           @Override
           public void run() {
               recipeDao.update(recipe);
           }
       });
   }


    /**
     * Methode qui permet de supprimer une recette
     * @param recipeId identifiant de la recette
     */
    public void delete(final UUID recipeId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.delete(recipeId);
            }
        });
    }
}
