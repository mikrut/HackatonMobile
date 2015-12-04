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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        priceInput = (EditText) findViewById(R.id.maxPriceInput);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

}
