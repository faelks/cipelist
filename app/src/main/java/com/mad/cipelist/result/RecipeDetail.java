package com.mad.cipelist.result;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.cipelist.R;
import com.mad.cipelist.common.BaseActivity;
import com.mad.cipelist.services.yummly.model.LocalRecipe;

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
    @BindView(R.id.recipe_cooking_time)
    TextView cookingTimeTv;
    @BindView(R.id.recipe_prep_time)
    TextView prepTimeTv;
    @BindView(R.id.recipe_detail_image)
    ImageView recipeIv;
    @BindView(R.id.recipe_ingredient_lines)
    RecyclerView ingredientLinesRv;
    @BindView(R.id.recipe_launch_source_btn)
    Button launchSourceBtn;
    private LocalRecipe mRecipe;

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
        String recipeId = getIntent().getStringExtra("recipeId");
        List<LocalRecipe> recipe = LocalRecipe.find(LocalRecipe.class, "m_id = ?", recipeId);
        mRecipe = recipe.get(0);

        // Format the url and inject a resultin image into the image view
        String url = mRecipe.getImageUrl();
        if (url.substring(url.length() - 4, url.length()).equals("=s90")) {
            url = url.substring(0, url.length() - 4);
        }
        Glide.with(getApplicationContext()).load(url).into(recipeIv);

        // Inject the other views
        titleTv.setText(mRecipe.getRecipeName());
        cookingTimeTv.setText(mRecipe.getCookingTime());
        prepTimeTv.setText("PrepTime = " + mRecipe.getPrepTime());
        /*ListAdapter adapter = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int i) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                return null;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
        mIngredientLinesLv.setAdapter(adapter); */

    }
}
