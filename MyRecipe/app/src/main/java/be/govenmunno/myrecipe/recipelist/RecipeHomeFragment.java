package be.govenmunno.myrecipe.recipelist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import be.govenmunno.myrecipe.R;

public class RecipeHomeFragment extends Fragment implements IRecipeListScreen {
    private RecipeListPresenter recipeHomePresenterLiked,recipeHomePresenterLast;
    private RecyclerView likedRecyclerView,secondRecyclerView;
    private IChangeFragment callback;
    private Button seeMore;

    public RecipeHomeFragment(){

    }
    public static RecipeHomeFragment newInstance(){
        RecipeHomeFragment fragment = new RecipeHomeFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        if(view instanceof LinearLayout){
            likedRecyclerView = view.findViewById(R.id.horizontal_recyclerview);
            likedRecyclerView.setAdapter(new RecipeAdapter(null, R.layout.liked_horizontal_view, callback));
            secondRecyclerView = view.findViewById(R.id.vertical_recyclerview);
            secondRecyclerView.setAdapter(new RecipeAdapter(null, R.layout.item_layout, callback));
            seeMore = view.findViewById(R.id.see_more_button);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.showAllRecipe();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipeHomePresenterLiked = new RecipeListPresenter(this,"liked");
        recipeHomePresenterLiked.loadRecipes();
        recipeHomePresenterLast = new RecipeListPresenter(this,"last");
        recipeHomePresenterLast.loadRecipes();
    }



    @Override
    public void loadView() {
        likedRecyclerView.setAdapter(new RecipeAdapter(recipeHomePresenterLiked,R.layout.liked_horizontal_view, callback));
        secondRecyclerView.setAdapter(new RecipeAdapter(recipeHomePresenterLast,R.layout.item_layout, callback));
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.new_recipe) {
            callback.newRecipeAsked();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
