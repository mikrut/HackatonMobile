package ru.bmstu.iu6.hackatonmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import ru.bmstu.iu6.hackatonmobile.database.RequirementHelper;
import ru.bmstu.iu6.hackatonmobile.models.RequirementModel;

public class RequirementEditActivity extends AppCompatActivity {
    private Integer food_id = null;
    private int type = RequirementModel.TYPE_FOOD;

    private EditText priceInput;
    private EditText categoryInput;
    private CheckBox[] daysBoxes;

    private TextView header, categoryHelper, priceHelper;

    private EditText minH, minM, maxH, maxM;

    public final static String PARAM_FOOD_ID = "RequirementEditActivity.PARAM_FOOD_ID";
    public final static String PARAM_TYPE = "RequirementEditActivity.PARAM_TYPE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        priceInput = (EditText) findViewById(R.id.maxPriceInput);
        categoryInput = (EditText) findViewById(R.id.foodCategroyInput);

        header = (TextView) findViewById(R.id.textView);
        categoryHelper = (TextView) findViewById(R.id.foodTypeText);
        priceHelper = (TextView) findViewById(R.id.maxPriceHelper);

        minH = (EditText) findViewById(R.id.minH);
        minM = (EditText) findViewById(R.id.minM);
        maxH = (EditText) findViewById(R.id.maxH);
        maxM = (EditText) findViewById(R.id.maxM);

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
        if (intent.hasExtra(PARAM_TYPE))
            type = intent.getIntExtra(PARAM_TYPE, RequirementModel.TYPE_FOOD);

        String[] headers = getResources().getStringArray(R.array.types_array);
        String[] chooses = getResources().getStringArray(R.array.choose_array);

        header.setText(headers[type]);
        categoryHelper.setText(chooses[type]);

        if (type != RequirementModel.TYPE_STORE && type != RequirementModel.TYPE_FOOD) {
            priceHelper.setVisibility(View.GONE);
            priceInput.setVisibility(View.GONE);
        }

        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        FloatingActionButton fab_del = (FloatingActionButton) findViewById(R.id.fab_del);
        fab_save.setOnClickListener(new SaveAction());
        fab_del.setOnClickListener(new DeleteAction());
        if (food_id == null) {
            fab_del.setVisibility(View.GONE);
        }

        assert getSupportActionBar() != null;
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
            RequirementModel food = new RequirementModel();
            RequirementHelper requirementHelper = new RequirementHelper(RequirementEditActivity.this);
            food.setMaxPrice(Short.valueOf(priceInput.getText().toString()));

            food.setMinH(Integer.valueOf(minH.getText().toString()));
            food.setMinM(Integer.valueOf(minM.getText().toString()));

            food.setMaxH(Integer.valueOf(maxH.getText().toString()));
            food.setMaxM(Integer.valueOf(maxM.getText().toString()));

            food.setDaysMask(getDaysMask());

            food.setType(type);
            food.setCategory(categoryInput.getText().toString());

            if (food_id == null) {
                requirementHelper.saveModel(food);
            } else {
                food.setId(food_id);
                requirementHelper.updateModel(food);
            }

            Intent intent = new Intent(RequirementEditActivity.this, CurrentListActivity.class);
            RequirementEditActivity.this.startActivity(intent);
        }
    }

    class DeleteAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            RequirementHelper requirementHelper = new RequirementHelper(RequirementEditActivity.this);

            if (food_id != null) {
                requirementHelper.deleteModel(food_id);
            }

            Intent intent = new Intent(RequirementEditActivity.this, CurrentListActivity.class);
            RequirementEditActivity.this.startActivity(intent);
        }
    }

}
