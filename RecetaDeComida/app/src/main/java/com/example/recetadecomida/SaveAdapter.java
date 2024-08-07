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

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.SaveViewHolder> {
    private List<SaveRecipe> mealSave;
    private OnItemClickListener onItemClickListener;
    public static class SaveViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMeal, txtArea, txtCategory, txtInstructions;
        public LinearLayout ingredientsLayout;
        public ImageView imageMeal;
        public CardView cardView;

        public SaveViewHolder(View itemView) {
            super(itemView);
            txtMeal = itemView.findViewById(R.id.TxtMeal);
            txtArea = itemView.findViewById(R.id.TxtArea);
            txtCategory = itemView.findViewById(R.id.TxtCategory);
            txtInstructions = itemView.findViewById(R.id.TxtInstructions);
            ingredientsLayout = itemView.findViewById(R.id.IngredientsLayout);
            imageMeal = itemView.findViewById(R.id.ImageMeal);
            cardView = itemView.findViewById(R.id.CardView);
        }

        public void Click(final SaveRecipe saveRecipe, final OnItemClickListener click){
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

    public void setOnItemClickListener(SaveAdapter.OnItemClickListener click){
        this.onItemClickListener = click;
    }
    public SaveAdapter (List<SaveRecipe> saveRecipes) {
        this.mealSave = saveRecipes;
    }
    @NonNull
    @Override
    public SaveAdapter.SaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes, parent, false);
        return new SaveAdapter.SaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveAdapter.SaveViewHolder holder, int position) {
        SaveRecipe saveRecipe = mealSave.get(position);
        holder.txtMeal.setText(saveRecipe.getTitle());
        holder.txtArea.setText("• AREA: " + saveRecipe.getArea());
        holder.txtCategory.setText("• CATEGORY: " + saveRecipe.getCategory());
        holder.txtInstructions.setText("• INSTRUCTIONS:" + "\n" + saveRecipe.getInstructions());
        holder.ingredientsLayout.removeAllViews();
        addIngredientView(holder.ingredientsLayout, "• INGREDIENTS:");
        for (String ingredient : saveRecipe.getIngredients()) {
            addIngredientView(holder.ingredientsLayout, ingredient);
        }
        Glide.with(holder.itemView.getContext())
                .load(saveRecipe.getImageUrl())
                .into(holder.imageMeal);
        holder.Click(saveRecipe, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mealSave.size();
    }

    public SaveRecipe getRecipePosition(int position) {
        return mealSave.get(position);
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
