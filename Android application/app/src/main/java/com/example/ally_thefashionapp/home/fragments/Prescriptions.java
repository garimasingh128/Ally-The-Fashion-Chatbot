package in.codepredators.vedanta.home.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.Vedanta;
import in.codepredators.vedanta.home.RecyclerAdapter;
import in.codepredators.vedanta.models.Prescription;

/**
 * A simple {@link Fragment} subclass.
 */
public class Prescriptions extends Fragment {


    public Prescriptions() {

    }

    RecyclerView prescriptionsRecycler;
    RecyclerView.LayoutManager layoutManager;

    void Assign(){
        prescriptionsRecycler = getView().findViewById(R.id.recyclerView);
    }

    public void setUp(){
        List<Prescription> prescriptions = Vedanta.getDatabase(getContext()).VedantaDAO().getPrescription();
        if(prescriptions.size()!=0){
            layoutManager = new LinearLayoutManager(getActivity());
            prescriptionsRecycler.setAdapter(new RecyclerAdapter(getContext(),prescriptions,1));
            prescriptionsRecycler.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Assign();
        setUp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_prescriptions, container, false);

        return view;
    }

}
