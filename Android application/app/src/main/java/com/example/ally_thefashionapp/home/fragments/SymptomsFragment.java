package in.codepredators.vedanta.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import in.codepredators.vedanta.R;

public class SymptomsFragment extends Fragment {
public EditText symptoms_text;

    @Override
    public void onStart() {
        super.onStart();

    }
    public SymptomsDetails sendData(){
        SymptomsDetails symptomsDetails=new SymptomsDetails();
        symptomsDetails.setSymptoms("Nausea");
        return symptomsDetails;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_symptoms, container, false);
        symptoms_text=view.findViewById(R.id.edit_text_diagnosis);
        return view;
    }
    public class SymptomsDetails{
        String symptoms;

        public String getSymptoms() {
            return symptoms;
        }

        public void setSymptoms(String symptoms) {
            this.symptoms = symptoms;
        }
    }
}
