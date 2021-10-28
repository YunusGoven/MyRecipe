package be.govenmunno.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import be.govenmunno.myrecipe.model.Recipe;
import be.govenmunno.myrecipe.recipedetails.RecipeDetailFragment;
import be.govenmunno.myrecipe.recipeeditor.RecipeEditorFragment;
import be.govenmunno.myrecipe.recipelist.IChangeFragment;
import be.govenmunno.myrecipe.recipelist.RecipeAllFragment;
import be.govenmunno.myrecipe.recipelist.RecipeHomeFragment;

public class MainActivity extends AppCompatActivity implements IChangeFragment {

    public static final int REQUEST_CODE_CAMERA = 10;
    public static final int REQUEST_CODE_GALLERY = 11;
    private final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };
    private final int PERMISSION_CODE = 3;

    private boolean permissionIsAlreadyAccept(Context context, String ...permission){
        if(context!=null && permission!=null){
            for(String perm : permission){
                if(ActivityCompat.checkSelfPermission(context,perm)!=PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!permissionIsAlreadyAccept(this, PERMISSIONS))
            ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSION_CODE);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(currentFragment == null) {
           showHomeFragment();
        }

    }
    @Override
    public void showHomeFragment(){
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, RecipeHomeFragment.newInstance()).commit();
    }

    @Override
    public void onSelectedRecipe(UUID recipeId) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RecipeDetailFragment.newInstance(recipeId)).addToBackStack(null).commit();

    }

    @Override
    public void newRecipeAsked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RecipeEditorFragment.newInstance(null)).addToBackStack(null).commit();
    }

    @Override
    public void showAllRecipe() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RecipeAllFragment.newInstance()).addToBackStack(null).commit();

    }

    @Override
    public void modifyRecipe(UUID recipeId) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RecipeEditorFragment.newInstance(recipeId)).addToBackStack(null).commit();

    }


}