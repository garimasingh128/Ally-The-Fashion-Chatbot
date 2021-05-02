package in.codepredators.vedanta.home.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.Vedanta;
import in.codepredators.vedanta.models.Prescription;
import in.codepredators.vedanta.new_prescription.SignPrescriptionActivity;


public class PrescriptionSigned extends Fragment {

CardView signprescription;
Prescription prescription=new Prescription();
String sign="";
Intent i;
String doctorName,clinicAddress,date,patientName,address,DOB,symptoms,weight,medication;
    @Override
    public void onStart() {
        super.onStart();
        signprescription=getView().findViewById(R.id.sign_prescription);
        signprescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_prescription_signed, container, false);
        i=new Intent(getContext(), SignPrescriptionActivity.class);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
    public void getDetails(String id,BasicDetailsFragment.BasicDetails basicDetails, AdviceFragment.AdviceDetails adviceDetails, DiagnosisFragment.DiagnosisDetails diagnosisDetails, SymptomsFragment.SymptomsDetails symptomsDetails, String sign,Context context){

        Log.i("hey","success 1");
        prescription.advice=new ArrayList<>();
        prescription.advice.add(adviceDetails.getAdvicetext());
        prescription.diagnosis=new ArrayList<>();
        prescription.diagnosis.add(diagnosisDetails.getDiagnosis());
        prescription.setId(id);
        prescription.medications=new ArrayList<>();
        prescription.medications.add(adviceDetails.getPrescriptiontext());
        //  prescription.setDate(Integer.parseInt("12-12-2020"));
        prescription.setDoctorId("DOC123");
        prescription.setName(basicDetails.getName());
        i.putExtra("ADVICE",adviceDetails.getAdvicetext());
        i.putExtra("ID",id);
        i.putExtra("DOCTOR_ID","DOC123");
        i.putExtra("DIAGNOSIS",diagnosisDetails.getDiagnosis());
        i.putExtra("MEDICATIONS",diagnosisDetails.getDiagnosis());
        i.putExtra("NAME",basicDetails.getName());
        i.putExtra("ADDRESS",basicDetails.getAddress());
        i.putExtra("AGE",basicDetails.getAge());
        i.putExtra("SEX",basicDetails.getSex());
        doctorName="Dr.bailey";
        clinicAddress="1000 University drive,san fransico";
        date="12122020";
        symptoms=symptomsDetails.getSymptoms();
        patientName="Kumar Harsh";
        address=basicDetails.getAddress();
        DOB="12-12-2000";
        weight="60lbs";
        medication=adviceDetails.getPrescriptiontext();
        Log.i("hey","success");

    }
    public String getSign(){
        return sign;
    }
}
