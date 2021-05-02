package in.codepredators.vedanta.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.room.Database;
import androidx.room.Room;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import com.scwang.wave.MultiWaveHeader;
//import com.scwang.wave.MultiWaveHeader;
import in.codepredators.vedanta.Constants;
import in.codepredators.vedanta.R;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


import in.codepredators.vedanta.R;
import in.codepredators.vedanta.converters.CSVReader;
import in.codepredators.vedanta.converters.CSVWriter;
import in.codepredators.vedanta.login.LoginActivity;
import in.codepredators.vedanta.models.Medicine;
import in.codepredators.vedanta.models.Patient;
import in.codepredators.vedanta.models.Prescription;
import in.codepredators.vedanta.room.Encrypt;
import in.codepredators.vedanta.room.VedantaDB;
import java.util.ArrayList;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.converters.CSVWriter;
import in.codepredators.vedanta.details.PrescriptionDetails;
import in.codepredators.vedanta.home.fragments.Account;
import in.codepredators.vedanta.home.fragments.Appointments;
import in.codepredators.vedanta.home.fragments.Home;
import in.codepredators.vedanta.home.fragments.Prescriptions;
import in.codepredators.vedanta.room.Encrypt;
import in.codepredators.vedanta.room.VedantaDB;
import in.codepredators.vedanta.voice.VoiceActivity;

//import com.scwang.wave.MultiWaveHeader;

public class HomeActivity extends AppCompatActivity {


    VedantaDB vedantaDB;
    Account accountFragment;
    Appointments appointmentsFragment;
    Home homeFragment;
    Prescriptions prescriptionsFragment;
    ViewPager viewPager;
    Switch mainSwitch;
    ImageView mic;
    private int[] navImageId = {
            R.id.home_icon,
            R.id.prescription_icon,
            R.id.appointments_icon,
            R.id.account_icon
    };
    private int[] navTextId = {
            R.id.home_text,
            R.id.prescription_text,
            R.id.appointments_text,
            R.id.account_text
    };
    LinearLayout homeLayout, prescriptionLayout, appointmentsLayout, accountLayout;

    private int[] navIcons = {
            R.drawable.ic_home,
            R.drawable.ic_treatment,
            R.drawable.ic_appointment,
            R.drawable.ic_account,
    };
    private String[] navText = {
            "Home",
            "Prescriptions",
            "Appointments",
            "Account"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);

        setContentView(R.layout.activity_main);

//        startActivity(new Intent(this, PrescriptionDetails.class));
//        finish();

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        accountFragment = new Account();
        mic = findViewById(R.id.mic_icon);
        appointmentsFragment = new Appointments();
        homeFragment = new Home();
        prescriptionsFragment = new Prescriptions();
        homeLayout = findViewById(R.id.home_layout);
        accountLayout = findViewById(R.id.account_layout);
        appointmentsLayout = findViewById(R.id.appointments_layout);
        prescriptionLayout = findViewById(R.id.prescription_layout);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragsPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        mainSwitch = findViewById(R.id.switchWidget);
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPager(0);
            }
        });
        appointmentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPager(2);
            }
        });
        prescriptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPager(1);
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPager(3);
            }
        });
        setViewPager(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    mainSwitch.animate().alpha(0);
                    mic.animate().alpha(0);
                } else {
                    mainSwitch.animate().alpha(1);
                    mic.animate().alpha(1);
                }
                setViewPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startActivity(new Intent(HomeActivity.this, VoiceActivity.class));
                    mainSwitch.setChecked(false);
                }
            }
        });

        File file = new File(exportDir, "vedanta.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = vedantaDB.query("SELECT * FROM " + "patient", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i = 0; i < curCSV.getColumnCount() - 1; i++) {
                    arrStr[i] = curCSV.getString(i);
                    arrStr[i] = Encrypt.encrypt(arrStr[i].toString());
                    Log.i("enc", arrStr[i].toString() + "  " + i);
                }
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }



    int current = 0;


    public void setViewPager(int position) {
        ImageView im = findViewById(navImageId[current]);
        im.getDrawable().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
        TextView tv = findViewById(navTextId[current]);
        tv.setTextColor(Color.parseColor("#000000"));
        im = findViewById(navImageId[position]);
        im.getDrawable().setColorFilter(0xff0B71E3, PorterDuff.Mode.MULTIPLY);
        tv = findViewById(navTextId[position]);
        tv.setTextColor(Color.parseColor("#0B71E3"));
        viewPager.setCurrentItem(position);
        current = position;
    }

    class MainFragsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments = new ArrayList<>();

        public MainFragsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(homeFragment);
            fragments.add(prescriptionsFragment);
            fragments.add(appointmentsFragment);
            fragments.add(accountFragment);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


}