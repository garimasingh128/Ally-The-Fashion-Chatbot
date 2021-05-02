package in.codepredators.vedanta.home.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import java.util.List;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.Vedanta;
import in.codepredators.vedanta.models.Doctor;
import in.codepredators.vedanta.room.VedantaDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends Fragment {



    public Account() {
        // Required empty public constructor
    }
    VedantaDB vedantaDB;

    TextView account;
    ImageView profilePic;
    TextView doctorName;
    TextView doctorJob;
    TextView doctorDegree;
    ImageView editSpecializations;
    EditText specializationsText;
    boolean isEditing = false;
    boolean isEdited = false;
    Doctor doctor;


    void Assign(){
        profilePic = getView().findViewById(R.id.image_profile_pic);
        doctorName = getView().findViewById(R.id.name);
        doctorJob = getView().findViewById(R.id.doctor_job);
        doctorDegree = getView().findViewById(R.id.doctor_degree);
        editSpecializations = getView().findViewById(R.id.edit_specializations_image);
        specializationsText = getView().findViewById(R.id.specialisations_text);
    }

    void setUp(){

        String doctorID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<Doctor> doctors = Vedanta.getDatabase(getContext()).VedantaDAO().getDoctors();
        doctor = new Doctor();
        for(int i=0;i<doctors.size();i++){
            if(doctors.get(i).getId().equals(doctorID)){
                doctor = doctors.get(i);
            }
        }

        doctorName.setText(doctor.getName());
        doctorJob.setText(doctor.getJob());
        doctorDegree.setText(doctor.getDegree());
        specializationsText.setText(doctor.getSpecialization());
        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profilePhotos/"+"fa2"+".jpg");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getContext()).load(imageURL).apply(new RequestOptions().circleCrop()).into(profilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });

        editSpecializations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdited = true;
                if (!isEditing) {
                    isEditing = true;
                    specializationsText.setEnabled(true);
                    editSpecializations.setImageResource(R.drawable.double_tic);
                }else {
                    isEditing = false;
                    specializationsText.setEnabled(false);
                    editSpecializations.setImageResource(R.drawable.edit);
                    doctor.setSpecialization(specializationsText.getText().toString());
                    Vedanta.getDatabase(getContext()).VedantaDAO().insertSpecialisation(specializationsText.getText().toString(),doctor.getId());
                }

            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();

        ImageView image_profile_pic=getView().findViewById(R.id.image_profile_pic);
        ImageView edit_hours=getView().findViewById(R.id.edit_hours);
        TextView monday= getView().findViewById(R.id.time_monday);
        TextView tuesday= getView().findViewById(R.id.time_tuesday);
        TextView wednesday= getView().findViewById(R.id.time_wednesday);
        TextView thursday= getView().findViewById(R.id.time_thursday);
        TextView friday= getView().findViewById(R.id.time_friday);
        TextView saturday= getView().findViewById(R.id.time_saturday);
        TextView sunday= getView().findViewById(R.id.time_sunday);
        String s= "10:00 am to 12:00 am and 16:30 pm to 20:20 pm";
        SpannableString ss1=  new SpannableString(s);
        vedantaDB= Room.databaseBuilder(getContext(), VedantaDB.class,"patient,medicine,doctor,prescription")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        List<Doctor> doctors=vedantaDB.VedantaDAO().getDoctors();
        Doctor doctor=doctors.get(0);
        dctorName.setText(doctor.getName());
        doctordegree.setText(doctor.getDegree());

        ss1.setSpan(new RelativeSizeSpan(1.2f), 0,8, 0); // set size

        ss1.setSpan(new RelativeSizeSpan(1.2f),12 ,20, 0); // set size

        ss1.setSpan(new RelativeSizeSpan(1.2f), 25,33, 0); // set size

        ss1.setSpan(new RelativeSizeSpan(1.2f), 36,45, 0); // set size

        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 8, 0);// set color

        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 12, 20, 0);// set color

        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 25, 33, 0);// set color

        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 36, 45, 0);// set color

        Glide.with(Account.this).load(R.drawable.ic_profilepic).apply(new RequestOptions().circleCrop()).into(image_profile_pic);


        monday.setText(ss1);

        thursday.setText(ss1);

        tuesday.setText(ss1);

        wednesday.setText(ss1);

        friday.setText(ss1);

        saturday.setText(ss1);

        sunday.setText(ss1);

        edit_hours.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner spin;
                spin= getView().findViewById(R.id.day_spinner);
                Dialog dialog = new Dialog(getActivity().getApplicationContext());
                dialog.setContentView(R.layout.edit_working_hours_pop_up);
                dialog.show();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                        R.array.days_arrays, R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);


            }

        });
        Assign();
        setUp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

}
