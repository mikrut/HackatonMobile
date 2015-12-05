package ru.bmstu.iu6.hackatonmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import ru.bmstu.iu6.hackatonmobile.database.FoodHelper;
import ru.bmstu.iu6.hackatonmobile.models.FoodModel;

public class FoodEditActivity extends AppCompatActivity {
    private Integer food_id = null;

    private EditText priceInput;
    private TimePicker fromInput;
    private TimePicker toInput;
    private CheckBox[] daysBoxes;

    public final static String PARAM_FOOD_ID = "FoodEditActivity.PARAM_FOOD_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        priceInput = (EditText) findViewById(R.id.maxPriceInput);
        fromInput = (TimePicker) findViewById(R.id.timePickerFrom);
        toInput = (TimePicker) findViewById(R.id.timePickerTo);

        daysBoxes = new CheckBox[7];
        daysBoxes[0] = (CheckBox) findViewById(R.id.checkBoxMon);
        daysBoxes[1] = (CheckBox) findViewById(R.id.checkBoxTue);
        daysBoxes[2] = (CheckBox) findViewById(R.id.checkBoxWed);
        daysBoxes[3] = (CheckBox) findViewById(R.id.checkBoxThu);
        daysBoxes[4] = (CheckBox) findViewById(R.id.checkBoxFri);
        daysBoxes[5] = (CheckBox) findViewById(R.id.checkBoxSat);
        daysBoxes[6] = (CheckBox) findViewById(R.id.checkBoxSun);

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

    public byte getDaysMask() {
        byte accumulator = 0;
        for (int i = 0; i < daysBoxes.length; i++) {
            byte m = (byte) ((daysBoxes[i].isChecked() ? 1 : 0) << i);
            accumulator = (byte) (accumulator | m);
        }
        return accumulator;
    }

    class SaveAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FoodModel food = new FoodModel();
            FoodHelper foodHelper = new FoodHelper(FoodEditActivity.this);
            food.setMaxPrice(Short.valueOf(priceInput.getText().toString()));

            food.setMinH(fromInput.getCurrentHour());
            food.setMinM(fromInput.getCurrentMinute());

            food.setMaxH(toInput.getCurrentHour());
            food.setMaxM(toInput.getCurrentMinute());

            food.setDaysMask(getDaysMask());

            if (food_id == null) {
                foodHelper.saveModel(food);
            } else {
                food.setId(food_id);
                foodHelper.updateModel(food);
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
                foodHelper.deleteModel(food_id);
            }

            Intent intent = new Intent(FoodEditActivity.this, CurrentListActivity.class);
            FoodEditActivity.this.startActivity(intent);
        }
    }

}
