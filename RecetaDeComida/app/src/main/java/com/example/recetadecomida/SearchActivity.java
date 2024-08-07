package com.example.recetadecomida;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerSearch;
    private List<SearchRecipe> meals = new ArrayList<>();
    private SearchAdapter adapter;
    private Retrofit retrofit;
    private TheMealDB theMealDB;
    private Button btnSave;
    private DataBase dataBase;
    private int positionSelect = -1;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        recyclerSearch = findViewById(R.id.RecyclerSearch);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(meals);
        recyclerSearch.setAdapter(adapter);

        dataBase = new DataBase(this);

        TextView edTxtSearch = findViewById(R.id.EdTxtSearch);
        ImageButton btnMyRecipes = findViewById(R.id.BtnMyRecipes);

        btnSave = findViewById(R.id.BtnSave);
        btnSave.setVisibility(View.GONE);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMealDB = retrofit.create(TheMealDB.class);

        edTxtSearch.setOnEditorActionListener((v, action, event) -> {
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                String strMeal = edTxtSearch.getText().toString().trim();
                searchRecipes(strMeal);
                if (positionSelect == position) {
                    meals.get(position).setSelected(false);
                    positionSelect = -1;
                    btnSave.setVisibility(View.GONE);
                }
                return true;
            }
            return false;
        });

        btnMyRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SaveActivity.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectRecipe(position);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe(positionSelect);
                btnSave.setVisibility(View.GONE);
                if (positionSelect == position) {
                    meals.get(position).setSelected(false);
                    positionSelect = -1;
                }
            }
        });
    }

    private void searchRecipes(String strMeal) {
        Call<SearchListRecipe> call = theMealDB.searchMeals(strMeal);
        call.enqueue(new Callback<SearchListRecipe>() {
            @Override
            public void onResponse(Call<SearchListRecipe> call, Response<SearchListRecipe> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SearchActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                SearchListRecipe listRecipe = response.body();
                if (listRecipe != null && listRecipe.getMeals() != null) {
                    meals.clear();
                    meals.addAll(listRecipe.getMeals());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SearchListRecipe> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectRecipe(int position) {
        if (positionSelect == position) {
            meals.get(position).setSelected(false);
            positionSelect = -1;
            btnSave.setVisibility(View.GONE);
        } else {
            if(position >= 0) {
                meals.get(position).setSelected(false);
            }
            meals.get(position).setSelected(true);
            positionSelect = position;
            btnSave.setVisibility(View.VISIBLE);
        }
        adapter.notifyItemChanged(position);
    }

    private void addIngredient(List<String> ingredients, String ingredient) {
        if (ingredient != null && !ingredient.trim().isEmpty()) {
            ingredients.add(ingredient);
        }
    }

    public void saveRecipe(int position) {
        SearchRecipe recipe = adapter.getRecipePosition(position);
        String title = recipe.getStrMeal();
        String area = recipe.getStrArea();
        String category = recipe.getStrCategory();
        String instructions = recipe.getStrInstructions();
        String imageUrl = recipe.getStrMealThumb();

            List<String> ingredients = new ArrayList<>();
            addIngredient(ingredients, recipe.getStrIngredient1());
            addIngredient(ingredients, recipe.getStrIngredient2());
            addIngredient(ingredients, recipe.getStrIngredient3());
            addIngredient(ingredients, recipe.getStrIngredient4());
            addIngredient(ingredients, recipe.getStrIngredient5());
            addIngredient(ingredients, recipe.getStrIngredient6());
            addIngredient(ingredients, recipe.getStrIngredient7());
            addIngredient(ingredients, recipe.getStrIngredient8());
            addIngredient(ingredients, recipe.getStrIngredient9());
            addIngredient(ingredients, recipe.getStrIngredient10());
            addIngredient(ingredients, recipe.getStrIngredient11());
            addIngredient(ingredients, recipe.getStrIngredient12());
            addIngredient(ingredients, recipe.getStrIngredient13());
            addIngredient(ingredients, recipe.getStrIngredient14());
            addIngredient(ingredients, recipe.getStrIngredient15());
            addIngredient(ingredients, recipe.getStrIngredient16());
            addIngredient(ingredients, recipe.getStrIngredient17());
            addIngredient(ingredients, recipe.getStrIngredient18());
            addIngredient(ingredients, recipe.getStrIngredient19());
            addIngredient(ingredients, recipe.getStrIngredient20());

        String ingredientsStr = TextUtils.join("\n", ingredients);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

            if (userEmail != null) {
                if (recipeAlreadySaved(userEmail, title)) {
                    Toast.makeText(this, "Recipe already saved", Toast.LENGTH_SHORT).show();
                } else {
                    insert(userEmail, title, area, category, instructions, ingredientsStr, imageUrl);
                    Toast.makeText(this, "Recipe saved", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error: Could not save", Toast.LENGTH_SHORT).show();
            }
    }

    private void insert(String email, String title, String area, String category, String instructions, String ingredientsStr, String imageUrl) {
        SQLiteDatabase db = dataBase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("title", title);
        values.put("area", area);
        values.put("category", category);
        values.put("instructions", instructions);
        values.put("ingredients", ingredientsStr);
        values.put("imageUrl", imageUrl);

        db.insert("meal", null, values);
        Toast.makeText(this, "Recipe saved", Toast.LENGTH_SHORT).show();
    }

    private boolean recipeAlreadySaved(String userEmail, String title) {
        SQLiteDatabase db = dataBase.getReadableDatabase();
        String[] projection = {"idMeal"};
        String selection = "email = ? AND title = ?";
        String[] selectionArgs = {userEmail, title};
        Cursor cursor = db.query("meal", projection, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}

