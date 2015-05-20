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
import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.model.Field;
import com.agro.gusutri.agroconsult.model.ProblemEvent;
import com.agro.gusutri.agroconsult.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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
    private LinearLayout linearFieldsButtons, linearProblemButtons;
    private Marker problemMarkerLocation = null;
    private Circle problemCircleLocation = null;
    private GoogleMap.OnMapClickListener myMapClickListener = null;


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

                    ArrayList<com.agro.gusutri.agroconsult.model.Location> locations = new ArrayList<com.agro.gusutri.agroconsult.model.Location>();
                    //create polygon to be added to map
                    PolygonOptions polygonOptions = new PolygonOptions().strokeColor(Color.BLUE).fillColor(Color.argb(50, 162, 23, 23));
                    for (int i = 0; i < points.size(); i++) {
                        Marker m = points.get(i);
                        LatLng position = m.getPosition();
                        polygonOptions.add(position);

                        locations.add(new com.agro.gusutri.agroconsult.model.Location(position.latitude, position.longitude, "x:" + i));
                    }
                    fields.add(polygonOptions);
                    resetMap();

                    Intent intent = new Intent(getActivity(), RegisterFieldActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Dao.LOCATIONS, locations);
                    bundle.putParcelable(Dao.USER, MainActivity.user);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);

                } else {
                    Toast.makeText(MyMapFragment.this.getActivity(), getString(R.string.map_error_few_points), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Problem sending code
        linearFieldsButtons = (LinearLayout) view.findViewById(R.id.map_linear_fields_buttons);
        linearProblemButtons = (LinearLayout) view.findViewById(R.id.map_linear_problems_buttons);
        Button btnReportProblem = (Button) view.findViewById(R.id.map_button_report_problem);
        Button btnIncreaseRadius = (Button) view.findViewById(R.id.map_button_increase_radius);
        Button btnDecreaseRadius = (Button) view.findViewById(R.id.map_button_decrease_radius);

        btnReportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequireImageDialog(savedInstanceState);
            }
        });

        btnIncreaseRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (problemCircleLocation != null) {
                    double radius = problemCircleLocation.getRadius();
                    problemCircleLocation.setRadius(radius + 10);
                } else
                    Toast.makeText(getActivity(), getString(R.string.map_error_no_problem_location), Toast.LENGTH_LONG).show();
            }
        });
        btnDecreaseRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (problemCircleLocation != null) {
                    double radius = problemCircleLocation.getRadius();
                    if (radius > 10) {

                        problemCircleLocation.setRadius(radius - 10);
                    }
                } else
                    Toast.makeText(getActivity(), getString(R.string.map_error_no_problem_location), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void resetMap() {
        mMap.clear();
        if (mapOption == true) {
            //reset markers
            points = new ArrayList<Marker>();
            //add existing fields

        } else {
            problemMarkerLocation = null;
            problemCircleLocation = null;

        }
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
        //fill map with fields
        GetFieldsAsyncTask task = new GetFieldsAsyncTask();
        task.execute(MainActivity.user);
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
        resetMap();
        //map clickListener
        if (myMapClickListener == null)
            myMapClickListener = new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Field field = service.getFieldOfLocation(fieldArrayList, latLng);
                    if (field != null) {
                        if (problemMarkerLocation == null)
                            problemMarkerLocation = mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(getString(R.string.map_new_problem_marker)));
                        else
                            problemMarkerLocation.setPosition(latLng);

                        if (problemCircleLocation == null)
                            problemCircleLocation = mMap.addCircle(new CircleOptions()
                                    .center(latLng)
                                    .radius(10));
                        else
                            problemCircleLocation.setCenter(latLng);
                    } else
                        Toast.makeText(getActivity(), "Put marker on a field", Toast.LENGTH_LONG).show();
                }
            };

        if (mapOption == false) {
            //change visibility settings
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_add_field));
            item.setTitle(getString(R.string.action_map_settings_field));

            linearProblemButtons.setVisibility(View.VISIBLE);
            linearFieldsButtons.setVisibility(View.GONE);
            //change onMapClickListener
            mMap.setOnMapClickListener(myMapClickListener);
        } else {
            //change visibility settings
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_send_problem));
            item.setTitle(getString(R.string.action_map_settings_problem));

            linearProblemButtons.setVisibility(View.GONE);
            linearFieldsButtons.setVisibility(View.VISIBLE);
            //change onMapClickListener
            mMap.setOnMapClickListener(null);
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
                String categoryName = mCategoryProblem;
                LatLng location = problemCircleLocation.getCenter();
                Double radius = problemCircleLocation.getRadius();

                Field field = service.getFieldOfLocation(fieldArrayList, location);//new Field(fieldID, MainActivity.user, 0, 0, "sirupcode", null, null);


                ProblemEvent problemEvent = new ProblemEvent(imageBitmap, date, details, field, categoryName, location, radius);

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
                mDetails = txtDetails.getText().toString();
                mCategoryProblem = txtCategory.getText().toString();
                if (!(mDetails.equals("") || mCategoryProblem.equals(""))) {

                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, MainActivity.REQUEST_CODE_IMAGE_CAPTURE);
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            Date date = new Date();
                            String details = mDetails;
                            String categoryName = mCategoryProblem;
                            LatLng location = problemCircleLocation.getCenter();
                            Double radius = problemCircleLocation.getRadius();

                            Field field = service.getFieldOfLocation(fieldArrayList, location);//new Field(fieldID, MainActivity.user, 0, 0, "sirupcode", null, null);

                            ProblemEvent problemEvent = new ProblemEvent(null, date, details, field, categoryName, location, radius);

                            ReportProblemAsyncTask r2 = new ReportProblemAsyncTask();
                            r2.execute(problemEvent);
                            break;
                    }
                } else
                    Toast.makeText(getActivity(), getString(R.string.alert_error), Toast.LENGTH_LONG).show();
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

    private ArrayList<Field> fieldArrayList;

    private class GetFieldsAsyncTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User[] params) {
            fieldArrayList = Dao.getInstance().getFields(params[0].getId());
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (Field f : fieldArrayList) {
                PolygonOptions polygonOptions = new PolygonOptions().strokeColor(Color.BLUE).fillColor(Color.argb(50, 162, 23, 23));
                for (com.agro.gusutri.agroconsult.model.Location l : f.getLocations()) {
                    polygonOptions.add(new LatLng(l.getLatitude(), l.getLongitude()));
                }
                fields.add(polygonOptions);
            }
            resetMap();
        }
    }
}
