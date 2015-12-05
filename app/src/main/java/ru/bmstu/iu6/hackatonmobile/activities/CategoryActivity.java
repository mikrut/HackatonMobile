package ru.bmstu.iu6.hackatonmobile.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ru.bmstu.iu6.hackatonmobile.R;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        final ListView listView = (ListView) findViewById(R.id.listViewCategory);

        String[] values = new String[] {
                "Рестораны", "Магазины", "Потерянные люди"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {

                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString(); // получаем текст нажатого элемента

                switch (strText) {
                    case "Рестораны" : {
                        startActivity(new Intent(listView.getContext(), MainActivity.class));
                        break;
                    }
                    case "Магазины" : {
                        startActivity(new Intent(listView.getContext(), MainActivity.class));
                        break;
                    }
                    case "Потерянные люди" : {
                        startActivity(new Intent(listView.getContext(), MainActivity.class));
                        break;
                    }
                    default : {
                        break;
                    }
                }
            }
        });

    }


}
