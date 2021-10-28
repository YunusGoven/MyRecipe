package be.govenmunno.myrecipe.recipelist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

import be.govenmunno.myrecipe.R;
import be.govenmunno.myrecipe.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private final RecipeListPresenter presenter;
    private final int layoutId;
    private IChangeFragment callBacks;

    public RecipeAdapter(RecipeListPresenter presenter, int layoutId, IChangeFragment callBacks){
        this.presenter = presenter;
        this.layoutId = layoutId;
        this.callBacks = callBacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new ViewHolder(view, callBacks);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        presenter.showRecipe(holder,position);
    }

    @Override
    public int getItemCount() {
        if(presenter == null)
            return 0;
        return presenter.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, IRecipeItemScreen {
        private final View view;
        private IChangeFragment callBacks;
        private UUID id;
        private TextView recipeName,categorie, totalDuration;
        private ImageView recipeImage;


        public ViewHolder(@NonNull View itemView, IChangeFragment callBacks) {
            super(itemView);
            this.view = itemView;
            this.callBacks = callBacks;
            view.setOnClickListener(this);
            recipeName = view.findViewById(R.id.recipe_name);
            categorie = view.findViewById(R.id.recipe_categorie);
            totalDuration = view.findViewById(R.id.total_duration_item);
            recipeImage = view.findViewById(R.id.image_recipe);
        }

        @Override
        public String toString() {
            return id.toString();
        }

        @Override
        public void onClick(View view) {
            callBacks.onSelectedRecipe(id);
        }

        @Override
        public void  showRecipe(UUID id, String name, String cat, int dureePreparation, int dureeCuisson, String pathImage) {
            this.id =id;
            recipeName.setText(name);
            if(categorie!=null)categorie.setText(cat);
            int dureeTotal = dureeCuisson + dureePreparation;
            if(totalDuration!=null)totalDuration.setText(totalDuration.getText().toString()+": " +dureeTotal+"min");
            Bitmap imageBitMap = BitmapFactory.decodeFile(pathImage);
            if(imageBitMap!=null )recipeImage.setImageBitmap(imageBitMap);
        }
    }
}
