package com.agro.gusutri.agroconsult;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agro.gusutri.agroconsult.Service.Service;
import com.agro.gusutri.agroconsult.model.Crop;
import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.model.Field;
import com.agro.gusutri.agroconsult.model.Location;
import com.agro.gusutri.agroconsult.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dragos on 5/15/15.
 */
public class RegisterFieldActivity extends Activity {

    private Service service = Service.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_field);

        final EditText editSirup = (EditText) findViewById(R.id.register_field_edit_sirup);
        final EditText editCropType = (EditText) findViewById(R.id.register_field_edit_crop_type);
        TextView txtPerimeter = (TextView) findViewById(R.id.register_field_perimeter_value);
        TextView txtArea = (TextView) findViewById(R.id.register_field_area_value);
        final Spinner spinnerCropStage = (Spinner) findViewById(R.id.register_field_spinner);
        Button btnSend = (Button) findViewById(R.id.register_field_button_send);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_crop_stages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCropStage.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        final User user = bundle.getParcelable(Dao.USER);
        final ArrayList<Location> locations = bundle.getParcelableArrayList(Dao.LOCATIONS);
        final double perimeter = service.calculatePerimeter(locations);
        final double area = service.calculateArea(locations);
        txtPerimeter.setText(perimeter + "");
        txtArea.setText(area + "");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Crop crop = new Crop(spinnerCropStage.getSelectedItem().toString(), editCropType.getText().toString());
                Field field = new Field(-1, user, perimeter, area, editSirup.getText().toString(), crop,locations);

                if (spinnerCropStage.getSelectedItemPosition() > 0) {
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");

                    Date today = Calendar.getInstance().getTime();
                    String dateAsString = df.format(today);
                    crop.setDatePlanted(dateAsString);

                }
                RegisterFieldAsyncTask x = new RegisterFieldAsyncTask();
                x.execute(field);
            }
        });
    }

    private class RegisterFieldAsyncTask extends AsyncTask<Field, Void, Boolean> {

        String q;

        @Override
        protected Boolean doInBackground(Field... params) {

            Dao dao = Dao.getInstance();
            q = dao.registerField(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success)
                Toast.makeText(RegisterFieldActivity.this, q, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(RegisterFieldActivity.this, "ERROR", Toast.LENGTH_LONG).show();
        }

    }
}
