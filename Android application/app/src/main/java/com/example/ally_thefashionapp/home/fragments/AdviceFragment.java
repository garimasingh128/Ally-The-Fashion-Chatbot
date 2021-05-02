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

public class AdviceFragment extends Fragment {
   public EditText prescription_text,advice_text;

    @Override
    public void onStart() {
        super.onStart();
        prescription_text=getView().findViewById(R.id.edit_text_prescription);
        advice_text=getView().findViewById(R.id.edit_text_advice);
    }
    public AdviceDetails sendData(){
        AdviceDetails adviceDetails=new AdviceDetails();
        adviceDetails.setAdvicetext("Take enough sleep");
        adviceDetails.setPrescriptiontext("Believe on others!!");
        return adviceDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_advice, container, false);
        return view;
    }
    public class AdviceDetails{
        String prescriptiontext,advicetext;

        public String getPrescriptiontext() {
            return prescriptiontext;
        }

        public void setPrescriptiontext(String prescriptiontext) {
            this.prescriptiontext = prescriptiontext;
        }

        public String getAdvicetext() {
            return advicetext;
        }

        public void setAdvicetext(String advicetext) {
            this.advicetext = advicetext;
        }
    }
}
