package in.codepredators.vedanta.home.fragments;


import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.SharedPrefManager;
import in.codepredators.vedanta.home.RecyclerAdapter;
import in.codepredators.vedanta.models.Appointment;
import in.codepredators.vedanta.models.Prescription;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Appointments extends Fragment {

    RecyclerView appointmentRecycler;
    RecyclerView.LayoutManager linearLayoutManager;
    ImageView searchImage;
    EditText searchQuery;
    public boolean isSearched = false;

    public Appointments() {
        // Required empty public constructor
    }

    void Assign(){
        appointmentRecycler = getView().findViewById(R.id.recyclerView);
        searchImage = getView().findViewById(R.id.search_icon);
        searchQuery = getView().findViewById(R.id.search_here);
    }

    public void setUp(){
        final String clinicId = SharedPrefManager.getId(getContext());
        Log.i("idofClinic",clinicId);
        if(!clinicId.equals("NONE")){
            FirebaseDatabase.getInstance().getReference().child("clinics").child(clinicId).child("doctors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Appointment> appointments = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        appointments.add(ds.getValue(Appointment.class));
                    }
                        linearLayoutManager = new LinearLayoutManager(getActivity());
                        appointmentRecycler.setAdapter(new RecyclerAdapter(appointments,getContext(),2));
                        appointmentRecycler.setLayoutManager(linearLayoutManager);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                isSearched = true;
                final String query = searchQuery.getText().toString();
                if(!clinicId.equals("NONE")&&!query.equals("")){
                    FirebaseDatabase.getInstance().getReference().child("clinics").child(clinicId).child("doctors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Appointment> appointments = new ArrayList<>();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Appointment temp =  ds.getValue(Appointment.class);
                                if(temp.getPatientName().toLowerCase().equals(query.toLowerCase())||temp.getPatientName().toLowerCase().startsWith(query.toLowerCase())||temp.getPatientName().toLowerCase().endsWith(query.toLowerCase())){
                                    appointments.add(temp);
                                }

                            }
                            if(appointments.size()!=0){
                                RecyclerAdapter adapter = (RecyclerAdapter)appointmentRecycler.getAdapter();
                                adapter.appointmentList.clear();
                                adapter.appointmentList.addAll(appointments);
                                appointmentRecycler.getAdapter().notifyDataSetChanged();
                            }else {
                                RecyclerAdapter adapter = (RecyclerAdapter)appointmentRecycler.getAdapter();
                                adapter.appointmentList.clear();
                                appointmentRecycler.getAdapter().notifyDataSetChanged();
                                //todo handle no results
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        searchQuery.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                {
                    searchImage.callOnClick();
                }
                return false;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Assign();
        setUp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }

}
