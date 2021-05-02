package in.codepredators.vedanta.home.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import in.codepredators.vedanta.R;

public class BasicDetailsFragment extends Fragment {
    public EditText name,age,address;
    public Spinner malefemale;

    @Override
    public void onStart() {
        super.onStart();
        name=getView().findViewById(R.id.name);
        age=getView().findViewById(R.id.age);
        malefemale=getView().findViewById(R.id.select_sex);
        address=getView().findViewById(R.id.address);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public BasicDetails sendData(){
        BasicDetails basicDetails=new BasicDetails();
        basicDetails.setName("Kumar Harsh");
        basicDetails.setAge("19");
        basicDetails.setAddress("Ashok Rajpath,Patna-23");
        basicDetails.setSex("M");
        return basicDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_basic_details, container, false);
        return view;
    }
    public class BasicDetails{
        String name,age,sex,address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
