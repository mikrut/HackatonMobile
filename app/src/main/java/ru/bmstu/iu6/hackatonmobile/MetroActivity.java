package ru.bmstu.iu6.hackatonmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;

import ru.bmstu.iu6.hackatonmobile.database.MetroHelper;
import ru.bmstu.iu6.hackatonmobile.models.MetroModel;

public class MetroActivity extends AppCompatActivity {
    private Spinner stationSpinner;
    private TimePicker fromInput;
    private TimePicker toInput;
    private Integer metroId;
    private CheckBox[] daysBoxes;

    public static final String PARAM_METRO_ID = "MetroActivity.PARAM_METRO_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        stationSpinner = (Spinner) findViewById(R.id.stationSpinner);
        fromInput = (TimePicker) findViewById(R.id.timePickerFrom);
        toInput = (TimePicker) findViewById(R.id.timePickerTo);
        String[] stationNames = getResources().getStringArray(R.array.stations_array);
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stationNames);
        stationSpinner.setAdapter(stationAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra(PARAM_METRO_ID))
            metroId = intent.getIntExtra(PARAM_METRO_ID, 0);

        daysBoxes = new CheckBox[7];
        daysBoxes[0] = (CheckBox) findViewById(R.id.checkBoxMon);
        daysBoxes[1] = (CheckBox) findViewById(R.id.checkBoxTue);
        daysBoxes[2] = (CheckBox) findViewById(R.id.checkBoxWed);
        daysBoxes[3] = (CheckBox) findViewById(R.id.checkBoxThu);
        daysBoxes[4] = (CheckBox) findViewById(R.id.checkBoxFri);
        daysBoxes[5] = (CheckBox) findViewById(R.id.checkBoxSat);
        daysBoxes[6] = (CheckBox) findViewById(R.id.checkBoxSun);

        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new SaveAction());

        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_del);
        fab_delete.setOnClickListener(new DeleteAction());

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
            MetroModel Metro = new MetroModel();
            MetroHelper MetroHelper = new MetroHelper(MetroActivity.this);
            Metro.setStationName(stationSpinner.getSelectedItem().toString());

            Metro.setMinH(fromInput.getCurrentHour());
            Metro.setMinM(fromInput.getCurrentMinute());

            Metro.setMaxH(toInput.getCurrentHour());
            Metro.setMaxM(toInput.getCurrentMinute());

            Metro.setDayMask(getDaysMask());

            if (metroId == null) {
                MetroHelper.saveModel(Metro);
            } else {
                Metro.setId(metroId);
                MetroHelper.updateModel(Metro);
            }

            Intent intent = new Intent(MetroActivity.this, CurrentListActivity.class);
            MetroActivity.this.startActivity(intent);
        }
    }

    class DeleteAction implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            MetroHelper MetroHelper = new MetroHelper(MetroActivity.this);

            if (metroId != null) {
                MetroHelper.deleteModel(metroId);
            }

            Intent intent = new Intent(MetroActivity.this, CurrentListActivity.class);
            MetroActivity.this.startActivity(intent);
        }
    }

}
