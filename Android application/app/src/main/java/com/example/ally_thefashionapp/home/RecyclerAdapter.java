package in.codepredators.vedanta.home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.SharedPrefManager;
import in.codepredators.vedanta.models.Appointment;

import in.codepredators.vedanta.home.fragments.Home;

import in.codepredators.vedanta.models.Prescription;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    public List<Prescription> prescriptionList;
    public List<Appointment> appointmentList;
    int status;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView diseaseName;
        public ImageView patientPhoto;
        public TextView patientName;
        public TextView patientVisitedTime;
        public TextView patientDisease;
        public TextView patientStatus;
        public TextView medications;
        public TextView appointmentTime;
        public TextView appointmentDate;
        public TextView viewRecords;
        public TextView reschedule;
        public ImageView deletePrescription;



        public MyViewHolder(View v) {

            super(v);
            diseaseName = v.findViewById(R.id.disease_name);
            patientPhoto = v.findViewById(R.id.patient_photo);
            patientName = v.findViewById(R.id.patient_name);
            patientVisitedTime = v.findViewById(R.id.visited_time);
            patientDisease = v.findViewById(R.id.patients_disease);
            patientStatus = v.findViewById(R.id.patient_status);
            medications = v.findViewById(R.id.medication);
            appointmentTime = v.findViewById(R.id.appointment_time);
            appointmentDate = v.findViewById(R.id.appointment_date);
            viewRecords = v.findViewById(R.id.view_record);
            reschedule = v.findViewById(R.id.reschedule);
            deletePrescription = v.findViewById(R.id.delete_prescription);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(Context context,List<Prescription> prescriptionList,int status) {
        this.prescriptionList = prescriptionList;
        this.appointmentList = new ArrayList<>();
       this.context = context;
       this.status = status;
    }

    public RecyclerAdapter(List<Appointment> appointmentList,Context context,int status) {
        this.appointmentList = appointmentList;
        this.prescriptionList = new ArrayList<>();
        this.context = context;
        this.status = status;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType ) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_prescription,parent,false);


        switch (status){
            case 0:
                view.findViewById(R.id.patients_disease).setVisibility(View.GONE);
                view.findViewById(R.id.medication_view).setVisibility(View.GONE);
                view.findViewById(R.id.appointment_time_view).setVisibility(View.GONE);
                view.findViewById(R.id.reschedule).setVisibility(View.GONE);
                view.findViewById(R.id.delete_prescription).setVisibility(View.GONE);
                break;
            case 1:
                view.findViewById(R.id.new_or_urgent).setVisibility(View.GONE);
                view.findViewById(R.id.patients_disease).setVisibility(View.GONE);
                view.findViewById(R.id.appointment_time_view).setVisibility(View.GONE);
                view.findViewById(R.id.reschedule).setVisibility(View.GONE);
                break;
            case 2:
                view.findViewById(R.id.delete_prescription).setVisibility(View.GONE);
                view.findViewById(R.id.medication_view).setVisibility(View.GONE);
                view.findViewById(R.id.disease_name).setVisibility(View.GONE);
                view.findViewById(R.id.visited_time).setVisibility(View.GONE);
                break;
        }
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profilePhotos/"+"fa2"+".jpg");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(context).load(imageURL).apply(new RequestOptions().circleCrop()).into(holder.patientPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
        if(appointmentList.size()!=0){
            final Appointment appointment = appointmentList.get(position);
            holder.patientName.setText(appointment.getPatientName()+" | "+appointment.getPatientGender()+" | "+appointment.getPatientAge());
            holder.patientDisease.setText(appointment.getPatientDisease());
            holder.appointmentTime.setText(appointment.getTime());
            DateFormat dateFormat = new SimpleDateFormat("E, MMM d, yyyy");
            Date currentDate = new Date(appointment.getDate());
            String formattedDate = dateFormat.format(currentDate);
            holder.appointmentDate.setText(formattedDate);
            holder.viewRecords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo open appointmentDetails
                }
            });
            holder.reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar myCalendar = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String format = new SimpleDateFormat("E, MMM d, yyyy").format(myCalendar.getTime());
                            final Long databasetime = myCalendar.getTimeInMillis();
                            holder.appointmentDate.setText(format);
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    holder.appointmentTime.setText( selectedHour + ":" + selectedMinute);
                                    FirebaseDatabase.getInstance().getReference().child("clinics").child(SharedPrefManager.getId(context)).child("doctors").
                                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").child(appointment.getId()).
                                            child("time").setValue(holder.appointmentTime.getText().toString());
                                    FirebaseDatabase.getInstance().getReference().child("clinics").child(SharedPrefManager.getId(context)).child("doctors").
                                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").child(appointment.getId()).
                                            child("date").setValue(databasetime);
                                }
                            }, hour, minute, true);
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();
                        }

                    };

                    new DatePickerDialog(context, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }else if (prescriptionList.size()!=0){
            Prescription prescription = prescriptionList.get(position);
            if(prescription.getDiagnosis().size()!=0) {
                holder.diseaseName.setText(prescription.getDiagnosis().get(0));
            }
            holder.patientName.setText(prescription.getPatientName()+" | "+prescription.getPatientSex()+" | "+prescription.getPatientAge());
            DateFormat dateFormat = new SimpleDateFormat("E, MMM d, yyyy");
            Date currentDate = new Date(prescription.getDate());
            String formattedDate = dateFormat.format(currentDate);
            holder.patientVisitedTime.setText(formattedDate);
            if(status==1){
                String medications="";
                for(int i=0;i<prescription.getPrescription().size();i++){
                    if (i==prescription.getPrescription().size()-1) {
                        medications = medications + prescription.getPrescription().get(i);
                    }else {
                        medications = medications + prescription.getPrescription().get(i) + ",";
                    }
                }
                holder.medications.setText(medications);
            }
            holder.viewRecords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo open prescriptioDetails
                }
            });
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(status==0||status==1) {
            return prescriptionList.size();
        }
        else {
            return appointmentList.size();
        }
    }
}