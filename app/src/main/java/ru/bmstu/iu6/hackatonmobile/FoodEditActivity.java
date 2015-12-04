package ru.bmstu.iu6.hackatonmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import ru.bmstu.iu6.hackatonmobile.database.FoodHelper;
import ru.bmstu.iu6.hackatonmobile.models.FoodModel;

public class FoodEditActivity extends AppCompatActivity {
    private Integer food_id = null;
    private EditText priceInput;

    public final static String PARAM_FOOD_ID = "FoodEditActivity.PARAM_FOOD_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        priceInput = (EditText) findViewById(R.id.maxPriceInput);

        Intent intent = getIntent();
        if (intent.hasExtra(PARAM_FOOD_ID))
            food_id = intent.getIntExtra(PARAM_FOOD_ID, 0);

        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        FloatingActionButton fab_del = (FloatingActionButton) findViewById(R.id.fab_del);
        fab_save.setOnClickListener(new SaveAction());
        fab_del.setOnClickListener(new DeleteAction());
        if (food_id == null) {
            fab_del.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class SaveAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FoodModel food = new FoodModel();
            FoodHelper foodHelper = new FoodHelper(FoodEditActivity.this);
            food.setMaxPrice(Short.valueOf(priceInput.getText().toString()));

            if (food_id == null) {
                foodHelper.saveFoodRecord(food);
            } else {
                food.setId(food_id);
                foodHelper.updateFoodModel(food);
            }

            Intent intent = new Intent(FoodEditActivity.this, CurrentListActivity.class);
            FoodEditActivity.this.startActivity(intent);
        }
    }

    class DeleteAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FoodHelper foodHelper = new FoodHelper(FoodEditActivity.this);

            if (food_id != null) {
                foodHelper.deleteFood(food_id);
            }

            Intent intent = new Intent(FoodEditActivity.this, CurrentListActivity.class);
            FoodEditActivity.this.startActivity(intent);
        }
    }

}
