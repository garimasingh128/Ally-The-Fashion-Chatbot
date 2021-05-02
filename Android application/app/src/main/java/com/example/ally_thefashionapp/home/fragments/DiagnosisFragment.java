package in.codepredators.vedanta.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import in.codepredators.vedanta.R;

public class DiagnosisFragment extends Fragment {
public EditText diagnosis_text;

    @Override
    public void onStart() {
        super.onStart();
        diagnosis_text=getView().findViewById(R.id.edit_text_diagnosis);

    }
    public DiagnosisDetails sendData(){
        DiagnosisDetails diagnosisDetails=new DiagnosisDetails();
        diagnosisDetails.setDiagnosis("Norovirus Infection");
        return diagnosisDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_diagnosis, container, false);
        return view;
    }
    public class DiagnosisDetails{
        String diagnosis;

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }
    }
}
