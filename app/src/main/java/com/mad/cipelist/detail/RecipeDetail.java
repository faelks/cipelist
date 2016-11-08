package com.mad.cipelist.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.common.Utils;
import com.mad.cipelist.services.model.LocalRecipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dsiplays the specific details for a recipe including image, rating and source.
 * TODO: Needs a bit more formatting to be acceptable
 */

public class RecipeDetail extends BaseActivity {

    @BindView(R.id.recipe_title)
    TextView titleTv;
    @BindView(R.id.recipe_detail_rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.recipe_cooking_time)
    TextView cookingTimeTv;
    @BindView(R.id.recipe_prep_time)
    TextView prepTimeTv;
    @BindView(R.id.recipe_number_of_servings)
    TextView numberOfServings;
    @BindView(R.id.recipe_source_name)
    TextView sourceName;
    @BindView(R.id.recipe_detail_image)
    ImageView recipeIv;
    @BindView(R.id.recipe_launch_source_btn)
    Button launchSourceBtn;
    private LocalRecipe mRecipe;


    /**
     * Sends out a request to view an uri link to the specified recipe
     */
    @OnClick(R.id.recipe_launch_source_btn)
    public void launchSource() {
        String uri = mRecipe.getRecipeUrl();
        if (!uri.startsWith("http://") && !uri.startsWith("https://"))
            uri = "http://" + uri;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.content_recipe_detail, contentFrameLayout);
        ButterKnife.bind(this);

        // Get the recipe that is to be injected
        String recipeId = getIntent().getStringExtra(getString(R.string.recipe_id));
        List<LocalRecipe> recipe = LocalRecipe.find(LocalRecipe.class, getString(R.string.recipe_id_query), recipeId);
        mRecipe = recipe.get(0);

        // Format the url and inject a resulting image into the image view
        String url = Utils.removeUrlImageSize(mRecipe.getImageUrl());
        Glide.with(getApplicationContext()).load(url).into(recipeIv);

        // Inject the other views
        titleTv.setText(mRecipe.getRecipeName());
        int rating = Integer.parseInt(mRecipe.getRating());
        ratingBar.setRating(rating);
        String cookString = getString(R.string.cook_time) + mRecipe.getCookingTime();
        cookingTimeTv.setText(cookString);
        String prepString = getString(R.string.prep_time) + mRecipe.getPreparationTime();
        prepTimeTv.setText(prepString);
        String numberOfServingsString = getString(R.string.number_of_servings) + mRecipe.getNumberOfServings();
        numberOfServings.setText(numberOfServingsString);
        String sourceNameString = getString(R.string.recipe_source) + mRecipe.getSourceDisplayName();
        sourceName.setText(sourceNameString);


    }
}
