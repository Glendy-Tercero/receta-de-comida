package com.example.recetadecomida;

import android.text.TextUtils;

import java.util.List;

public class SaveRecipe {
    private int idMeal;
    private String email;
    private String title;
    private String area;
    private String category;
    private String instructions;
    private List<String> ingredients;
    private String imageUrl;
    private boolean select;

    public SaveRecipe(int idMeal, String email, String title, String area, String category, String instructions, List<String> ingredients, String imageUrl) {
        this.idMeal = idMeal;
        this.email = email;
        this.title = title;
        this.area = area;
        this.category = category;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }

    public SaveRecipe(boolean select){
        this.select = select;
    }
    public int getIdMeal() {
        return idMeal;
    }
    public String getEmail() {
        return email;
    }
    public String getTitle() {
        return title;
    }
    public String getArea() {
        return area;
    }
    public String getCategory() {
        return category;
    }
    public String getInstructions() {
        return instructions;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public String getIngredientsAsString() {
        return TextUtils.join("\n", ingredients);
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public boolean getSelect() {
        return select;
    }
    public void setSelected(boolean selected) {
        select = selected;
    }
}
