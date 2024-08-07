package com.example.recetadecomida;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheMealDB {
    @GET("search.php")
    Call<SearchListRecipe> searchMeals(@Query("s") String strMeal);
}
