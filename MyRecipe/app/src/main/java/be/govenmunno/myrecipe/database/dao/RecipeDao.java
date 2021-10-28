package be.govenmunno.myrecipe.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

import be.govenmunno.myrecipe.model.Recipe;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe WHERE liked = 1")
    LiveData<List<Recipe>>getLikedRecipe();

    @Query("SELECT * FROM recipe LIMIT :max")
    LiveData<List<Recipe>>getLatestRecipe(int max);


    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAllRecipe();

    @Query("SELECT * FROM recipe where id = (:uuid)")
    LiveData<Recipe> getRecipe(UUID uuid);

    @Insert
    void insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Query("UPDATE recipe SET liked =:isLiked WHERE id =:recipeId")
    void updateLike(UUID recipeId, boolean isLiked);

    @Query("DELETE FROM recipe WHERE id = :recipeId")
    void delete(UUID recipeId);

    @Query("SELECT * FROM recipe WHERE liked = 0")
    LiveData<List<Recipe>> getUnlikeRecipes();

    @Query("SELECT * FROM recipe WHERE categorie = :categorie")
    LiveData<List<Recipe>> getThisList(String categorie);
}
