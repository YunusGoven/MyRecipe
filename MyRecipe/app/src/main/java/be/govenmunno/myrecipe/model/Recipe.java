package be.govenmunno.myrecipe.model;

import android.net.Uri;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
public class Recipe {

    @PrimaryKey
    @NonNull
    private UUID id;
    private String name;
    private List<String> ingredients;
    private Map<Integer, String> etapes;
    private int dureePreparation;
    private int dureeCuisson;
    private String categorie;
    private int difficulte;
    private String pathImage;
    private boolean liked = false;

    /**
     * Constructeur
     */
    public Recipe() {
        id = UUID.randomUUID();
        name = "";
        ingredients = new ArrayList<>();
        etapes = new HashMap<>();
        dureePreparation = 5;
        dureeCuisson =0;
        categorie = "";
        difficulte=0;
        pathImage= "";

    }

    /**
     * Methode qui retourne l'id
     * @return identifiant de la recette
     */
    public UUID getId() {
        return id;
    }

    /**
     * Methode qui permet de modifier l'id
     * @param id id de la recette
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Methode qui retourne le nom de la recette
     * @return nom de la recette
     */
    public String getName() {
        return name;
    }

    /**
     * Methode qui permet de modifier le nom d'une recette
     * @param name nouveau nom
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Methode qui retourne la liste des ingredient
     * @return liste de chaine d'ingredient
     */
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * Methode qui modifier la liste des ingredients
     * @param ingredients liste des ingredient
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<Integer, String> getEtapes() {
        return etapes;
    }

    public void setEtapes(Map<Integer, String> etapes) {
        this.etapes = etapes;
    }

    public int getDureePreparation() {
        return dureePreparation;
    }

    public void setDureePreparation(int dureePreparation) {
        this.dureePreparation = dureePreparation<5? 5 :  dureePreparation;
    }

    public int getDureeCuisson() {
        return dureeCuisson;
    }

    public void setDureeCuisson(int dureeCuisson) {
        this.dureeCuisson = dureeCuisson;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }


    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        String msg = "Recette: "+name+"\nRecettes:\n";
        msg+="Categorie : "+categorie+"\n";
        msg+="Difficulté : "+difficulte+"\n";
        for (String in:ingredients) {
            msg += "- "+in+"\n";
        }
        msg += "Preparation: \n";
        for (int et:etapes.keySet()) {
            msg+= et +". "+ etapes.get(et)+"\n";
        }
        msg += "Durrée Preparation : "+dureePreparation+"\n";
        msg +="Durrée Cuisson : "+dureeCuisson+"\n";

        return msg;
    }
}
