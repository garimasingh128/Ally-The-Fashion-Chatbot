package in.codepredators.vedanta.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.home.fragments.Account;
import in.codepredators.vedanta.home.fragments.Appointments;
import in.codepredators.vedanta.home.fragments.Home;
import in.codepredators.vedanta.home.fragments.Prescriptions;

public class EnterDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_enter_details);


               Spinner spinner= findViewById(R.id.gender_spinner);
               spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {

                   }
               });

        // Spinner Drop down element

        List<String> categories = new ArrayList<String>();
        categories.add("   Gender");
        categories.add("Male");
        categories.add("Female");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item_gender, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_item_gender);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


    }
}
