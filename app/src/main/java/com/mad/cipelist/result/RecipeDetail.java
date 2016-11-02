package com.mad.cipelist.result;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.services.yummly.model.LocalRecipe;

import java.util.List;

/**
 * Dsiplays the specific details for a recipe including image, rating and source.
 */

public class RecipeDetail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_recipe_detail, contentFrameLayout);

        //TextView idView = (TextView) findViewById(R.id.recipe_id);
        ImageView recipeImage = (ImageView) findViewById(R.id.recipe_detail_image);

        String recipeId = getIntent().getStringExtra("recipeId");

        List<LocalRecipe> recipe = LocalRecipe.find(LocalRecipe.class, "m_id = ?", recipeId);
        LocalRecipe mRecipe = recipe.get(0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRecipe.getRecipeName());
        }

        String url = mRecipe.getImageUrl();
        if (url.substring(url.length() - 4, url.length()).equals("=s90")) {
            url = url.substring(0, url.length() - 4);
        }
        Glide.with(getApplicationContext()).load(url).into(recipeImage);

    }
}
