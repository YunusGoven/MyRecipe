package be.govenmunno.myrecipe.recipedetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import be.govenmunno.myrecipe.R;
import be.govenmunno.myrecipe.recipeeditor.RecipePresenter;
import be.govenmunno.myrecipe.recipeeditor.RecipeViewModel;
import be.govenmunno.myrecipe.recipelist.IChangeFragment;



public class RecipeDetailFragment extends Fragment implements IRecipeScreen  {

    private RecipePresenter recipePresenter;
    private UUID recipeId;

    private IChangeFragment callback;

    private ImageView likeRecipe, recipeImage, deleteButton,modifyButton,shareButton;
    private TextView title, fullTime, diffiulty, categori,cuissonTime, preparationTime;
    private LinearLayout ingredientLayout, preparationLayout;

    private ShareDialog shareDialog;

    public static RecipeDetailFragment newInstance(UUID recipeId){
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.recipeId = recipeId;
        return recipeDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_detail_fragment,container,false);
        likeRecipe = view.findViewById(R.id.like_recipe);
        recipeImage= view.findViewById(R.id.detail_item_image);
        shareButton= view.findViewById(R.id.share_btn);
        deleteButton= view.findViewById(R.id.drop_recipe);
        title= view.findViewById(R.id.detail_name_subtitle);
        fullTime= view.findViewById(R.id.detail_full_time_subtitle);
        diffiulty= view.findViewById(R.id.detail_difficulty_subtitle);
        categori= view.findViewById(R.id.detail_cat_subtitle);
        cuissonTime= view.findViewById(R.id.duree_cuisson);
        preparationTime= view.findViewById(R.id.duree_preration);
        ingredientLayout= view.findViewById(R.id.ingredient);
        preparationLayout= view.findViewById(R.id.preparation);
        modifyButton = view.findViewById(R.id.modify_recipe);
        //facebook
        shareDialog = new ShareDialog(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecipeViewModel viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        viewModel.setRecipeId(recipeId);

        recipePresenter = new RecipePresenter(this);
        recipePresenter.loadRecipe(recipeId);

    }

    @Override
    public void onStart() {
        super.onStart();
        likeRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipePresenter.likeRecipe(recipeId);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForConfirmation();
                RecipeDetailFragment.super.onDetach();
            }
        });
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.modifyRecipe(recipeId);
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipePresenter.shareAsked();
            }
        });

    }
    private void showDialogForConfirmation(){
        final CharSequence[]item = {"Oui", "Non"};
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(getContext());
        alertdialogbuilder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(item[0].equals(item[which])){
                    recipePresenter.delete(recipeId);
                    getFragmentManager().popBackStack();
                }
                dialog.dismiss();
            }
        });
        alertdialogbuilder.show();
    }

    @Override
    public void showRecipe(boolean isLiked,List<String> ingredient, Map<Integer,String>etape,String... informations) {
        isLiked(isLiked);
        title.setText(informations[0]);
        showImage(informations[1]);
        categori.setText(informations[2]);
        diffiulty.setText(informations[3]);
        String prep = String.format(" %s min",informations[4]);
        String cuisson = String.format(" %s min",informations[5]);
        preparationTime.setText(prep);
        cuissonTime.setText(cuisson);
        fullTime.setText(Integer.parseInt(informations[4])+Integer.parseInt(informations[5]) + " min");
        addInLayout(ingredientLayout, ingredient);
        addForEtape(preparationLayout,etape);

    }

    @Override
    public void showMessage(String msg, boolean err) {

    }

    private void showImage(String pathImage) {
        File file = new File(pathImage);
        if(file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            recipeImage.setImageBitmap(bitmap);
        }
    }

    public void isLiked(boolean isLiked){
        if(isLiked){
            likeRecipe.setImageResource(R.drawable.ic_like);
        }else{
            likeRecipe.setImageResource(R.drawable.ic_unlike);
        }
    }


    @Override
    public void share(String textRecipe) {
        Bitmap image = ((BitmapDrawable) recipeImage.getDrawable()).getBitmap();
        SharePhoto photo = new SharePhoto
                .Builder()
                .setBitmap(image)
                .setCaption("Recipe capttion")
                .build();
        SharePhotoContent content = new SharePhotoContent
                .Builder()
                .addPhoto(photo)
                .setShareHashtag(new ShareHashtag.Builder()
                .setHashtag(textRecipe).build())
                .build();
        shareDialog.show(content);
    }

    private void addInLayout(LinearLayout layout, List<String>value){
        layout.removeAllViews();
        for(String val:value){
            TextView textView = new TextView(layout.getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(val);
            layout.addView(textView);
        }
    }
    private void addForEtape(LinearLayout layout, Map<Integer, String>values){
        layout.removeAllViews();
        Set<Integer> keys = values.keySet();
        for(int key:keys){
            TextView textView = new TextView(layout.getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            String text = String.format("%d. %s", key, values.get(key));
            textView.setText(text);
            layout.addView(textView);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (IChangeFragment) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
