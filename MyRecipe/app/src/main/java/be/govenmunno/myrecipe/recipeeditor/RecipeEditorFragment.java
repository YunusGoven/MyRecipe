package be.govenmunno.myrecipe.recipeeditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import be.govenmunno.myrecipe.MainActivity;
import be.govenmunno.myrecipe.R;
import be.govenmunno.myrecipe.recipedetails.IRecipeScreen;


public class RecipeEditorFragment extends Fragment implements IRecipeScreen {

    private RecipePresenter recipePresenter;

    private Button addImageButton,validationButton;
    private EditText nameEditText;
    private Spinner categorieSpinner, difficultySpinner;
    private NumberPicker preparationDurationPicker,cuissonDurationPicker;
    private LinearLayout ingredientLayout, preparationLayout;
    private ImageView addIngredient, addPreparation,recipeImageView;
    private TextView informationMessage;

    private UUID recipeId;

    public static RecipeEditorFragment newInstance(UUID recipeId){
        RecipeEditorFragment recipeFragment = new RecipeEditorFragment() ;
        recipeFragment.recipeId = recipeId;
        return recipeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_recipe_fragment, container,false);

        addImageButton = view.findViewById(R.id.add_image_button);
        nameEditText=view.findViewById(R.id.imput_name);
        recipeImageView =view.findViewById(R.id.detail_item_image);
        categorieSpinner=view.findViewById(R.id.categorie_choose);
        difficultySpinner=view.findViewById(R.id.difficulty_choose);
        preparationDurationPicker=view.findViewById(R.id.preparation_duration);
        preparationDurationPicker.setMinValue(5);
        preparationDurationPicker.setMaxValue(1000);
        cuissonDurationPicker=view.findViewById(R.id.cuisson_duration);
        cuissonDurationPicker.setMinValue(0);
        cuissonDurationPicker.setMaxValue(1000);
        validationButton=view.findViewById(R.id.validation_button);
        informationMessage = view.findViewById(R.id.information_message);
        ingredientLayout = view.findViewById(R.id.ingredient_layout);
        addIngredient = view.findViewById(R.id.add_ingredient);
        addPreparation = view.findViewById(R.id.add_preparation);
        preparationLayout = view.findViewById(R.id.preparation_layout);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipeViewModel viewModel = new ViewModelProvider(this ).get(RecipeViewModel.class);
        if(viewModel.getRecipeId() == null){
            viewModel.setRecipeId(recipeId);
        }else{
            recipeId = viewModel.getRecipeId();
        }
        recipePresenter = new RecipePresenter(this);
        if(recipeId!=null)
            recipePresenter.loadRecipe(recipeId);

    }

    @Override
    public void onStart() {
        super.onStart();

        addEditTextForLayout(addIngredient,ingredientLayout);
        addEditTextForLayout(addPreparation,preparationLayout);

        addImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openSelectImageDialog();
            }
        });
        validationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getRecipeValues();
            }
        });

    }
    private void openSelectImageDialog(){
        final CharSequence[]item = {"Ouvrir la camera", "Choisir depuis la gallerie","Annuler"};
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(getContext());
        alertdialogbuilder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(item[0].equals(item[which])){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, MainActivity.REQUEST_CODE_CAMERA);
                }else if(item[1].equals(item[which])){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, MainActivity.REQUEST_CODE_GALLERY);
                }else{
                    dialog.dismiss();
                }
            }
        });
        alertdialogbuilder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode== Activity.RESULT_OK) {
            if (requestCode == MainActivity.REQUEST_CODE_CAMERA) {
                Bundle bundle = data.getExtras();
                Bitmap image = (Bitmap) bundle.get("data");
                recipeImageView.setImageBitmap(image);
            }else if(requestCode == MainActivity.REQUEST_CODE_GALLERY){
                Uri selectedImage = data.getData();
                recipeImageView.setImageURI(selectedImage);
            }
        }

    }
    private void addEditTextForLayout(ImageView image, final LinearLayout layout){
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditTextForLayout(layout,null);
            }
        });
    }

    private void createEditTextForLayout(LinearLayout layout, String text){
        EditText editText = new EditText(layout.getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setId(layout.getId()+layout.getChildCount()+1);
        if(text!=null)editText.setText(text);
        layout.addView(editText);
    }

    private void getRecipeValues(){
        String name = nameEditText.getText().toString();
        String categorie = categorieSpinner.getSelectedItem().toString();
        String difficulte = difficultySpinner.getSelectedItem().toString();
        int dureePrep = preparationDurationPicker.getValue();
        int dureeCuis = cuissonDurationPicker.getValue();
        List<String> ingredients = addIngredientInRecipe();
        Map<Integer, String> etapes = addStepsInRecipe();
        BitmapDrawable drawable = (BitmapDrawable) recipeImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        recipePresenter.checkValues(recipeId,name, categorie, difficulte, dureeCuis, dureePrep, ingredients, etapes,bitmap);

    }
    @Override
    public void showMessage(String msg, boolean isNotAdded) {
        informationMessage.setText(msg);
        if(!isNotAdded){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void share(String textRecipe) { }

    private List<String> addIngredientInRecipe(){
        int nbChild = ingredientLayout.getChildCount();
        List<String> ingredient = new LinkedList<>();
        for(int i=0;i<nbChild;i++){
            EditText view = (EditText)ingredientLayout.getChildAt(i);
            String val = view.getText().toString();
            if(!val.isEmpty())
                ingredient.add(val);
        }
        return ingredient;
    }

    private Map<Integer, String> addStepsInRecipe() {
        int nbChild = preparationLayout.getChildCount();
        Map<Integer, String> steps = new HashMap<>();
        for(int i = 0; i < nbChild; i++){
            EditText view = (EditText)preparationLayout.getChildAt(i);

            String val = view.getText().toString();
            if(!val.isEmpty())
                steps.put(i+1, val);

        }
        return steps;
    }

    @Override
    public void showRecipe(boolean isLiked, List<String> ingredient, Map<Integer,String> etape, String... informations) {

        nameEditText.setText(informations[0]);
        recipeImageView.setImageURI(Uri.parse(informations[1]));
        categorieSpinner.setSelection(getPosition(categorieSpinner,informations[2]));
        difficultySpinner.setSelection(getPosition(difficultySpinner, informations[3]));
        preparationDurationPicker.setValue(Integer.parseInt(informations[4]));
        cuissonDurationPicker.setValue(Integer.parseInt(informations[5]));
        showIgredient(ingredient);
        showEtape(etape);
    }

    private void showEtape(Map<Integer, String> etape) {
        Iterator<String> it = etape.values().iterator();
        while (it.hasNext()){
            createEditTextForLayout(preparationLayout,it.next());
        }
    }

    private void showIgredient(List<String> ingredient) {
        for(String ing : ingredient){
            createEditTextForLayout(ingredientLayout, ing);
        }
    }

    private int getPosition(Spinner spinner, String selected) {
        for(int i =0;i<spinner.getCount();++i){
            String item = (String)spinner.getItemAtPosition(i);
            if(selected.equals(item)){
                return i;
            }
        }
        return 0;
    }



}
