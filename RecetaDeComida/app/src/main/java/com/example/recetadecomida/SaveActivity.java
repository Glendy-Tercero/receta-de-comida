package com.example.recetadecomida;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveActivity extends AppCompatActivity {
    private RecyclerView recyclerSave;
    private List<SaveRecipe> mealSave = new ArrayList<>();
    private SaveAdapter adapter;
    private Button btnDelete;
    private DataBase dataBase;
    private int positionSelect = -1;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_save);

        recyclerSave = findViewById(R.id.RecyclerRecipe);
        recyclerSave.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SaveAdapter(mealSave);
        recyclerSave.setAdapter(adapter);

        dataBase = new DataBase(this);

        btnDelete = findViewById(R.id.BtnDelete);
        btnDelete.setVisibility(View.GONE);

        loadSavedRecipes();

        adapter.setOnItemClickListener(new SaveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectRecipe(position);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idMeal = mealSave.get(position).getIdMeal();
                deleteRecipe(idMeal);
                mealSave.remove(position);
                btnDelete.setVisibility(View.GONE);
            }
        });
    }

    private void loadSavedRecipes() {
        mealSave.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);
        if (userEmail != null) {
            SQLiteDatabase db = dataBase.getReadableDatabase();
            String[] projection = {
                    "idMeal",
                    "title",
                    "area",
                    "category",
                    "instructions",
                    "ingredients",
                    "imageUrl"
            };
            String selection = "email = ?";
            String[] selectionArgs = {userEmail};
            Cursor cursor = db.query("meal", projection, selection, selectionArgs, null, null, null);
            while (cursor.moveToNext()) {
                int idMeal = cursor.getInt(cursor.getColumnIndexOrThrow("idMeal"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String area = cursor.getString(cursor.getColumnIndexOrThrow("area"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                String ingredientsStr = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));

                List<String> ingredients = new ArrayList<>(Arrays.asList(ingredientsStr.split("\n")));

                SaveRecipe recipe = new SaveRecipe(idMeal, userEmail, title, area, category, instructions, ingredients, imageUrl);
                mealSave.add(recipe);
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectRecipe(int position) {
        if (positionSelect == position) {
            mealSave.get(position).setSelected(false);
            positionSelect = -1;
            btnDelete.setVisibility(View.GONE);
        } else {
            if(position >= 0) {
                mealSave.get(position).setSelected(false);
            }
            mealSave.get(position).setSelected(true);
            positionSelect = position;
            btnDelete.setVisibility(View.VISIBLE);
        }
        adapter.notifyItemChanged(position);
    }

    private void deleteRecipe(int idMeal) {
        SQLiteDatabase db = dataBase.getWritableDatabase();
        db.delete("meal", "idMeal = ?", new String[]{String.valueOf(idMeal)});
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show();
    }
}

