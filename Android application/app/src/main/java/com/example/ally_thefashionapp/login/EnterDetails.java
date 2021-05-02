package in.codepredators.vedanta.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import in.codepredators.vedanta.R;
import in.codepredators.vedanta.SharedPrefManager;
import in.codepredators.vedanta.Vedanta;
import in.codepredators.vedanta.home.HomeActivity;
import in.codepredators.vedanta.models.Doctor;

public class EnterDetails extends AppCompatActivity {

    EditText fullName, specialization, degree, regNumber, email, dateOfBirth, city, clinicId;
    Spinner gender;
    TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_enter_details);
        assignVariables();
        setup();
    }

    private void assignVariables() {
        gender = findViewById(R.id.gender_spinner);
        fullName = findViewById(R.id.full_name);
        specialization = findViewById(R.id.specialisation);
        degree = findViewById(R.id.degree);
        regNumber = findViewById(R.id.reg_no);
        email = findViewById(R.id.email_address);
        dateOfBirth = findViewById(R.id.dob);
        city = findViewById(R.id.city);
        clinicId = findViewById(R.id.clinic_id);
        createAccount = findViewById(R.id.create_account);
    }

    private void setup() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item_gender, new String[]{"Gender", "Male", "Female"});
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_item_gender);

        gender.setAdapter(dataAdapter);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(fullName.getText().toString().equals("") ||
                        specialization.getText().toString().equals("") ||
                        degree.getText().toString().equals("") ||
                        regNumber.getText().toString().equals("") ||
                        email.getText().toString().equals("") ||
                        dateOfBirth.getText().toString().equals("") ||
                        city.getText().toString().equals("")) && FirebaseAuth.getInstance().getCurrentUser() != null
                ) {
                    createAccount();
                } else
                    Toast.makeText(EnterDetails.this, "All fields are compulsory", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount() {
        Doctor doctor = new Doctor();
        doctor.setName(fullName.getText().toString());
        doctor.setSpecialization(specialization.getText().toString());
        doctor.setDegree(degree.getText().toString());
        doctor.setRegNumber(regNumber.getText().toString());
        doctor.setEmail(email.getText().toString());
        doctor.setDateOfBirth(dateOfBirth.getText().toString());
        doctor.setCity(city.getText().toString());
        doctor.setSex(gender.getSelectedItem().toString());
        doctor.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        SharedPrefManager.setId(this,clinicId.getText().toString());

        Vedanta.getDatabase(this).VedantaDAO().insertDoctorInfo(doctor);

        if (!clinicId.getText().toString().equals("")) {
            FirebaseDatabase.getInstance().getReference().child("clinics").child(clinicId.getText().toString()).child("doctors").child(doctor.getId())

                    .setValue(doctor)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            moveToNextActivity();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EnterDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            FirebaseDatabase.getInstance().getReference().child("independent_docs").child(doctor.getId())
                    .setValue(doctor)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            moveToNextActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EnterDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void moveToNextActivity() {
        startActivity(new Intent(EnterDetails.this, HomeActivity.class));
        finish();
    }
}
