package be.govenmunno.myrecipe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import be.govenmunno.myrecipe.database.dao.RecipeDao;
import be.govenmunno.myrecipe.model.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({RecipeTypeConverters.class})
public abstract class RecipeDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "recipe_database";
    private static RecipeDatabase instance;

    public abstract RecipeDao recipeDao();

    /**
     * Methode qui permet d'initialiser la database
     * @param context
     */
    public static void initDatabase(Context context) {
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class, DATABASE_NAME).build();
    }

    public static RecipeDatabase getInstance() {
        if(instance == null)
            throw new IllegalStateException("Recipe database must be initialized");
        return  instance;
    }

    /**
     * Methode qui permet de se deconecter de la bd
     */
    public static void disconnectDatabase() {
        instance = null;
    }
}
