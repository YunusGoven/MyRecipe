package be.govenmunno.myrecipe;

import android.os.Bundle;
import androidx.fragment.app.testing.*;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import be.govenmunno.myrecipe.recipeeditor.RecipeEditorFragment;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewRecipeAddTest {
    private String recipeName;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void initString(){
        recipeName = "Une recette";
    }
    @Test
    public void change(){
        onView(withId(R.id.new_recipe)).perform(click());
        FragmentScenario<RecipeEditorFragment> f = FragmentScenario.launchInContainer(RecipeEditorFragment.class);
        onView(withId(R.id.imput_name)).perform(typeText(recipeName),closeSoftKeyboard());
        onView(withId(R.id.categorie_choose)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Plats"))).perform(click());
        onView(withId(R.id.difficulty_choose)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2"))).perform(click());
        onView(withId(R.id.add_ingredient)).perform(click());

        onView(withId(R.id.ingredient_layout+1)).perform(typeText("ingre"),closeSoftKeyboard());
        onView(withId(R.id.add_preparation)).perform(click());
        onView(withId(R.id.preparation_layout+1)).perform(typeText("etap"),closeSoftKeyboard());




        onView(withId(R.id.validation_button)).perform(click());

        onView(withId(R.id.information_message))
                .check(matches(withText("La recette a été ajoutée !")));

    }

}
