package com.example.tourmate;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.tourmate.helper.EventUtils;
import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.viewmodels.EventViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFrag extends Fragment {

    private Button createEvnBtn, updateEventBtn;
    private TextInputEditText eventNameET, startLocationET, departureDateET, destinationET, budgetET;
    private String departureDate = "";
    private EventViewModel eventViewModel;
    public static String eventID =" " ;
    private String updateEventID ;



    public AddEventFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

            if (eventID.isEmpty()) {

            }
            else
            {
                eventViewModel.getEventDetails(eventID);
                eventViewModel.eventDetailsLD.observe(getActivity(), new Observer<TourMateEventPojo>() {
                    @Override
                    public void onChanged(TourMateEventPojo eventPojo) {

                        try {
                            eventNameET.setText(eventPojo.getEventName());
                            startLocationET.setText(eventPojo.getDeparture());
                            destinationET.setText(eventPojo.getDestination());
                            departureDateET.setText(eventPojo.getDepartureDate());
                            budgetET.setText(String.valueOf(eventPojo.getInitialBudget()));
                            createEvnBtn.setVisibility(View.GONE);
                            updateEventBtn.setVisibility(View.VISIBLE);

                            departureDate = eventPojo.getDepartureDate();


                            updateEventID = eventID;
                            eventID = "";

                        }
                        catch (Exception e)
                        {

                        }

                    }
                });
            }





        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventNameET = view.findViewById(R.id.eventNameET);
        startLocationET = view.findViewById(R.id.startLocationET);
        destinationET = view.findViewById(R.id.destinationET);
        budgetET = view.findViewById(R.id.budgetET);
        createEvnBtn = view.findViewById(R.id.createEventbtn);
        departureDateET = view.findViewById(R.id.departureDateET);
        updateEventBtn = view.findViewById(R.id.upateEventBtn);


        createEvnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameET.getText().toString();
                String startLocation = startLocationET.getText().toString();
                String destination = destinationET.getText().toString();
                String budget = budgetET.getText().toString();

                if (eventName.isEmpty()) {
                    eventNameET.setError("Provide event name!");
                } else if (startLocation.isEmpty()) {
                    startLocationET.setError("Provide start location!");
                } else if (destination.isEmpty()) {
                    destinationET.setError("Provide destination location!");
                } else if (budget.isEmpty()) {
                    budgetET.setError("Provide budget amount!");
                } else if (departureDate.isEmpty()) {
                    departureDateET.setError("Select Date!");
                } else {
                    TourMateEventPojo tourMateEventPojo = new TourMateEventPojo(null, eventName, startLocation, destination, Integer.parseInt(budget), departureDate, EventUtils.getDateWithTime());
                    eventViewModel.SaveEvent(tourMateEventPojo);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragmnet).navigate(R.id.eventListFragment);

                }


            }
        });

        departureDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateaDilogPicker();
            }
        });


        updateEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameET.getText().toString();
                String startLocation = startLocationET.getText().toString();
                String destination = destinationET.getText().toString();
                String budget = budgetET.getText().toString();
                TourMateEventPojo tourMateEventPojo = new TourMateEventPojo(updateEventID, eventName, startLocation, destination, Integer.parseInt(budget), departureDate, EventUtils.getDateWithTime());
                eventViewModel.updateEvent(tourMateEventPojo);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragmnet).navigate(R.id.eventListFragment);

            }
        });


    }

    private void showDateaDilogPicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                dateSetListener, year, month, day);
        dpd.show();


    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            departureDate = new SimpleDateFormat("dd.MM.yyyy EEE")
                    .format(calendar.getTime());
            departureDateET.setText(departureDate);


        }
    };


}
