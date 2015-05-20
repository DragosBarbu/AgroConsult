package com.agro.gusutri.agroconsult;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.model.Field;

/**
 * Created by dragos on 5/13/15.
 */
public class FieldDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_details);

        TextView txtSirup=(TextView) findViewById(R.id.field_details_sirup_value);
        TextView txtPerimeter=(TextView) findViewById(R.id.field_details_perimeter_value);
        TextView txtArea=(TextView) findViewById(R.id.field_details_area_value);
        TextView txtCropType=(TextView) findViewById(R.id.field_details_crop_type_value);
        TextView txtCropStage=(TextView) findViewById(R.id.field_details_crop_stage_value);
        TextView txtCropPlanted=(TextView) findViewById(R.id.field_details_crop_date_planted_value);
        TextView txtCropHarvested=(TextView) findViewById(R.id.field_details_crop_date_harvested_value);
        TextView txtPreferred=(TextView) findViewById(R.id.field_details_crop_preferred_value);
        TextView txtNotIndicated=(TextView) findViewById(R.id.field_details_crop_not_indicated_value);

        Bundle bundle=getIntent().getExtras();
        Field field= bundle.getParcelable(Dao.FIELD);

        txtSirup.setText(field.getSirupCode());
        txtPerimeter.setText(field.getPerimeter()+"");
        txtArea.setText(field.getArea()+"");
        txtCropType.setText(field.getCrop().getCropType());
        txtCropStage.setText(field.getCrop().getCropStage());
        txtCropPlanted.setText(field.getCrop().getDatePlanted());
        txtCropHarvested.setText(field.getCrop().getDateHarvasted());
        txtPreferred.setText(field.getCrop().getPreviousCropPreferred());
        txtNotIndicated.setText(field.getCrop().getPreviousCropNotIndicated());
    }
}
