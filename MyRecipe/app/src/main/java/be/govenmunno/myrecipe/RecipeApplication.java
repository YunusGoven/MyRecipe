package be.govenmunno.myrecipe;

import android.app.Application;

import be.govenmunno.myrecipe.database.RecipeDatabase;

public class RecipeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RecipeDatabase.initDatabase(getBaseContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RecipeDatabase.disconnectDatabase();
    }
}
