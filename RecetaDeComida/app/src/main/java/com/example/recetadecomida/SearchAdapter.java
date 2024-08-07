package com.example.recetadecomida;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<SearchRecipe> meals;
    private OnItemClickListener onItemClickListener;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtMeal, txtArea, txtCategory, txtInstructions;
        public LinearLayout ingredientsLayout;
        public ImageView imageMeal;
        public CardView cardView;

         public MyViewHolder(View itemView){
             super(itemView);
             txtMeal = itemView.findViewById(R.id.TxtMeal);
             txtArea = itemView.findViewById(R.id.TxtArea);
             txtCategory = itemView.findViewById(R.id.TxtCategory);
             txtInstructions = itemView.findViewById(R.id.TxtInstructions);
             ingredientsLayout = itemView.findViewById(R.id.IngredientsLayout);
             imageMeal = itemView.findViewById(R.id.ImageMeal);
             cardView = itemView.findViewById(R.id.CardView);
         }

         public void Click(final SearchRecipe searchRecipe, final OnItemClickListener click){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click != null) {
                        click.onItemClick(cardView, getAdapterPosition());
                    }
                }
            });
         }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener click){
        this.onItemClickListener = click;
    }
    public SearchAdapter(List<SearchRecipe> searchRecipes) {
        this.meals = searchRecipes;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
        SearchRecipe searchRecipe = meals.get(position);

        holder.txtMeal.setText(searchRecipe.getStrMeal());
        holder.txtArea.setText("• AREA: " + searchRecipe.getStrArea());
        holder.txtCategory.setText("• CATEGORY: " + searchRecipe.getStrCategory());
        holder.txtInstructions.setText("• INSTRUCTIONS:" + "\n" + searchRecipe.getStrInstructions());
        holder.ingredientsLayout.removeAllViews();
        addIngredientView(holder.ingredientsLayout, "• INGREDIENTS:" + "\n" + searchRecipe.getStrIngredient1());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient2());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient3());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient4());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient5());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient6());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient7());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient8());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient9());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient10());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient11());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient12());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient13());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient14());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient15());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient16());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient17());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient18());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient19());
        addIngredientView(holder.ingredientsLayout, searchRecipe.getStrIngredient20());
        Glide.with(holder.itemView.getContext())
                .load(searchRecipe.getStrMealThumb())
                .into(holder.imageMeal);
        holder.Click(searchRecipe, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public SearchRecipe getRecipePosition(int position) {
        return meals.get(position);
    }

    private void addIngredientView(LinearLayout layout, String ingredient) {
        if (ingredient != null && !ingredient.isEmpty()) {
            TextView textView = new TextView(layout.getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(ingredient);
            textView.setTextSize(18);
            textView.setTextColor(ContextCompat.getColor(layout.getContext(), R.color.white));
            textView.setTypeface(ResourcesCompat.getFont(layout.getContext(), R.font.ubuntu));
            layout.addView(textView);
        }
    }
}
