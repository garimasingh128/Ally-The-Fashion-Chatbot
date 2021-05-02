package in.codepredators.vedanta.home.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.Vedanta;
import in.codepredators.vedanta.home.HomeActivity;
import in.codepredators.vedanta.home.RecyclerAdapter;
import in.codepredators.vedanta.models.Doctor;
import in.codepredators.vedanta.models.Prescription;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    public Home() {
        // Required empty public constructor
    }

    TextView headerText;
    TextView newPrescriptionText;
    TextView appointmentCountText;
    TextView viewAllPrescriptions;
    Doctor doctor;
    RecyclerView presciptionRecycler;
    RecyclerView.LayoutManager layoutManager;
    public boolean isViewAllOpen = false;


    void Assign(){
        headerText = getView().findViewById(R.id.header);
        newPrescriptionText = getView().findViewById(R.id.new_number);
        appointmentCountText = getView().findViewById(R.id.no_of_appointments);
        presciptionRecycler = getView().findViewById(R.id.recyclerView);
        viewAllPrescriptions = getView().findViewById(R.id.view_all_prescriptions);
    }

    public void setUp(){
        String doctorID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<Doctor> doctors = Vedanta.getDatabase(getContext()).VedantaDAO().getDoctors();
        doctor = new Doctor();
        for(int i=0;i<doctors.size();i++){
            if(doctors.get(i).getId().equals(doctorID)){
                doctor = doctors.get(i);
            }
        }
        headerText.setText("Hello, "+doctor.getName());
        final List<Prescription> prescriptions = Vedanta.getDatabase(getContext()).VedantaDAO().getPrescription();
        if(prescriptions.size()<=3){
            layoutManager = new LinearLayoutManager(getActivity());
            presciptionRecycler.setAdapter(new RecyclerAdapter(getContext(),prescriptions,0));
            presciptionRecycler.setLayoutManager(layoutManager);
            newPrescriptionText.setText(prescriptions.size()+" NEW");
        }
        else {
            List<Prescription> prescriptions1 = new ArrayList<>();
            prescriptions.add(prescriptions.get(0));
            prescriptions.add(prescriptions.get(1));
            prescriptions.add(prescriptions.get(2));
            layoutManager = new LinearLayoutManager(getActivity());
            presciptionRecycler.setAdapter(new RecyclerAdapter(getContext(),prescriptions,0));
            presciptionRecycler.setLayoutManager(layoutManager);
            newPrescriptionText.setText("3 NEW");
        }
        viewAllPrescriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isViewAllOpen = true;
                RecyclerAdapter prescriptionRecycler = (RecyclerAdapter) presciptionRecycler.getAdapter();
                prescriptionRecycler.prescriptionList.clear();
                prescriptionRecycler.prescriptionList.addAll(prescriptions);
                prescriptionRecycler.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onStart(){
        super.onStart();
        Assign();
        setUp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.prescriptionListRecyclerView);
        context=container.getContext();
        prescriptions=new ArrayList<>();
        vedantaDB= Room.databaseBuilder(context, VedantaDB.class,"patient,medicine,doctor,prescription")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        numberofAppointements=view.findViewById(R.id.no_of_appointments);


        recyclerView.setAdapter(mAdapter);
        return view;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

}
