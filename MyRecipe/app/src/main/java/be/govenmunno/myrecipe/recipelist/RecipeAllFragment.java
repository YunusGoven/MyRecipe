package be.govenmunno.myrecipe.recipelist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import be.govenmunno.myrecipe.R;

public class RecipeAllFragment extends Fragment implements IRecipeListScreen {
    private RecyclerView recyclerView;
    private IChangeFragment callback;
    private RecipeListPresenter recipeHomePresenter;
    private Spinner spinner;
    private RadioGroup radioGroup;

    public static RecipeAllFragment newInstance(){
        RecipeAllFragment fragment = new RecipeAllFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_recipe_fragment,container,false);
        recyclerView = view.findViewById(R.id.recipe_list);
        recyclerView.setAdapter(new RecipeAdapter(null, R.layout.item_layout,callback));
        spinner = view.findViewById(R.id.filter_categorie);
        radioGroup = view.findViewById(R.id.all_recipe_filter_like_choose);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.filter_all){
                    loadPresenter("all");
                }else if(checkedId == R.id.filter_like){
                    loadPresenter("liked");
                }else if(checkedId == R.id.filter_unlike){
                    loadPresenter("unlike");
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = (String)spinner.getSelectedItem();
                loadPresenter(select);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadPresenter(String fragmentName){
        recipeHomePresenter = new RecipeListPresenter(this, fragmentName);
        recipeHomePresenter.loadRecipes();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadPresenter("all");
    }

    @Override
    public void loadView() {
        recyclerView.setAdapter(new RecipeAdapter(recipeHomePresenter,R.layout.item_layout,callback));
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
