package com.agro.gusutri.agroconsult;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.agro.gusutri.agroconsult.Service.SQSProducer;
import com.agro.gusutri.agroconsult.Service.Service;
import com.agro.gusutri.agroconsult.model.Field;
import com.agro.gusutri.agroconsult.model.ProblemEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dragos on 4/20/15.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private GoogleMap mMap;
    private FragmentManager fragmentManager;
    private LinearLayout linearButtons;
    private Button btnReportProblem;

    private Service service = Service.getInstance();

    private boolean didMyLocationAppear = false;
    private boolean mapOption = true;
    private ArrayList<Marker> points = new ArrayList<>();
    private ArrayList<PolygonOptions> fields = new ArrayList<>();
    private Double latitude, longitude;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        fragmentManager = getChildFragmentManager();
        if (container == null) {
            return null;
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.map_fragment, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        //set actionbar
        setHasOptionsMenu(true);

        latitude = 0.0;
        longitude = 0.0;

        // Try to instantiate a map and obtain it from the SupportMapFragment.
        if (mMap == null) {
            ((SupportMapFragment) fragmentManager.findFragmentById(R.id.location_map)).getMapAsync(this);
        }

        final Button btnAddLocation = (Button) view.findViewById(R.id.map_button_add_marker);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddLocation.setText(latitude + "-" + longitude);
                LatLng newPoint = new LatLng(latitude, longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(newPoint).title(points.size() + ""));
                marker.setDraggable(true);
                points.add(marker);
            }
        });

        Button btnSendField = (Button) view.findViewById(R.id.map_button_send_data);
        btnSendField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points.size() > 2) {
                    PolygonOptions polygonOptions = new PolygonOptions().strokeColor(Color.BLUE).fillColor(Color.argb(50, 162, 23, 23));
                    for (Marker m : points) {
                        polygonOptions.add(m.getPosition());
                    }
                    fields.add(polygonOptions);
                    Polygon fieldMap = mMap.addPolygon(polygonOptions);

                    float area = service.calculateArea(fieldMap.getPoints());
                    Toast.makeText(MyMapFragment.this.getActivity(), area + " mp?", Toast.LENGTH_LONG).show();

                    resetFieldsOnMap();

                } else {
                    Toast.makeText(MyMapFragment.this.getActivity(), getString(R.string.map_error_few_points), Toast.LENGTH_LONG).show();
                }
            }
        });

        linearButtons = (LinearLayout) view.findViewById(R.id.map_linear_buttons);
        btnReportProblem = (Button) view.findViewById(R.id.map_button_report_problem);

        btnReportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequireImageDialog(savedInstanceState);
            }
        });

        return view;
    }

    private void resetFieldsOnMap() {
        //reset markers
        points = new ArrayList<Marker>();
        mMap.clear();
        //add existing fields
        for (PolygonOptions x : fields) {
            mMap.addPolygon(x);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //set location values
        GoogleMap.OnMyLocationChangeListener locationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if (didMyLocationAppear == false) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                            longitude), 18.0f));
                    didMyLocationAppear = true;
                }
            }
        };
        mMap.setOnMyLocationChangeListener(locationChangeListener);

        // For showing a move to my location button
        mMap.setMyLocationEnabled(true);

        //center on Romania
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.91,
                25.80), 5.0f));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        didMyLocationAppear = false;
    }


    /*
    * Action bar modify map settings
    * */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater in = getActivity().getMenuInflater();
        in.inflate(R.menu.action_bar_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map_settings:
                changeMapSettings(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void changeMapSettings(MenuItem item) {
        mapOption = !mapOption;

        if (mapOption == false) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_add_field));
            item.setTitle(getString(R.string.action_map_settings_field));

            btnReportProblem.setVisibility(View.VISIBLE);
            linearButtons.setVisibility(View.GONE);
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_send_problem));
            item.setTitle(getString(R.string.action_map_settings_problem));

            btnReportProblem.setVisibility(View.GONE);
            linearButtons.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MainActivity.REQUEST_CODE_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                Date date = new Date();
                String details = mDetails;
                Field field = new Field(23, "fieldd", 234, 23);
                String categoryName = mCategoryProblem;
                LatLng location = new LatLng(4, 5);

                ProblemEvent problemEvent = new ProblemEvent(imageBitmap, date, details, field, categoryName, location);

                ReportProblemAsyncTask r = new ReportProblemAsyncTask();
                r.execute(problemEvent);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private String mDetails = "", mCategoryProblem = "";

    private void showRequireImageDialog(Bundle savedInstance) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater(savedInstance);
        View dialogView = inflater.inflate(R.layout.alert_send_problem, null);
        dialogBuilder.setView(dialogView);

        final EditText txtDetails = (EditText) dialogView.findViewById(R.id.alert_send_details);
        final EditText txtCategory = (EditText) dialogView.findViewById(R.id.alert_send_category_problem);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mDetails = txtDetails.getText().toString();
                        mCategoryProblem = txtCategory.getText().toString();
                        if (!(mDetails.equals("") || mCategoryProblem.equals(""))) {
                            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, MainActivity.REQUEST_CODE_IMAGE_CAPTURE);
                            }
                        } else
                            Toast.makeText(getActivity(), getString(R.string.alert_error), Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        ReportProblemAsyncTask r2 = new ReportProblemAsyncTask();
                        r2.execute();
                        break;
                }
            }
        };
        dialogBuilder.setMessage(R.string.action_confirm_image_required).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    private class ReportProblemAsyncTask extends AsyncTask<ProblemEvent, Void, Boolean> {

        @Override
        protected Boolean doInBackground(ProblemEvent... params) {

            ProblemEvent problemEvent = params[0];
            SQSProducer sqsProducer = SQSProducer.getInstance();
            return sqsProducer.sendProblemEventMessage(problemEvent);

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success)
                Toast.makeText(getActivity(), "Problem reported", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "ERROR: Problem NOT reported", Toast.LENGTH_LONG).show();
        }

    }
}
