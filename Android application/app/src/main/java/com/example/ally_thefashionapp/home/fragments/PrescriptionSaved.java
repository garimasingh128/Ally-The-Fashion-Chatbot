package in.codepredators.vedanta.home.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;


import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.codepredators.vedanta.R;
import in.codepredators.vedanta.Vedanta;

import in.codepredators.vedanta.converters.Common;
import in.codepredators.vedanta.converters.PDFDocumentAdapter;
import in.codepredators.vedanta.models.Doctor;
import in.codepredators.vedanta.models.Prescription;

import static android.content.Context.MODE_PRIVATE;

public class PrescriptionSaved extends Fragment {
    CardView viewinapp,viewaspdf;
    String doctorName,doctorId,clinicAddress,date,patientName,address,DOB,advice,symptoms,weight,medication,signature,pres_id;
    Context context;
    Prescription prescription=new Prescription();

    public void getDetails(String id,BasicDetailsFragment.BasicDetails basicDetails, AdviceFragment.AdviceDetails adviceDetails, DiagnosisFragment.DiagnosisDetails diagnosisDetails, SymptomsFragment.SymptomsDetails symptomsDetails, String sign,Context context){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_prescription_saved, container, false);
        context=container.getContext();
        SharedPreferences sharedPreferences=context.getSharedPreferences("VEDANTA",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        viewaspdf=view.findViewById(R.id.cardView3);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                List<Doctor>  doctors=Vedanta.getDatabase(getContext()).VedantaDAO().getDoctors();
                Doctor doctor=doctors.get(0);
                doctorName=doctor.getName();
                doctorId=doctor.getId();
                clinicAddress=doctor.getClinicAddress();
            }
        });
        t.start();
        date=System.currentTimeMillis()+"";
        symptoms=sharedPreferences.getString("SYMPTOMS","Nausea");
        patientName=sharedPreferences.getString("NAME","Kuma Harsh");
        address=sharedPreferences.getString("ADDRESS","Ashok Rjapath,Patna -23");
        DOB=sharedPreferences.getString("DOB","12-12-2020");
        weight=sharedPreferences.getString("WEIGHT","60lbs");
        medication=sharedPreferences.getString("DIAGNOSIS","nausea");
        advice=sharedPreferences.getString("ADVICE","TAKE Care");
        signature=sharedPreferences.getString("SIGN","nausea");
        pres_id=sharedPreferences.getString("PRES_ID",System.currentTimeMillis()+"");
        prescription.setSignature(signature);
        prescription.setId(pres_id);
        prescription.setDoctorId(doctorid);
        prescription.setName(patientName);
        prescription.diagnosis=new ArrayList<>();
        prescription.diagnosis.add(medication);
        prescription.setDate(date);
        prescription.advice=new ArrayList<>();
        prescription.advice.add(advice);
        prescription.symptoms=new ArrayList<>();
        prescription.symptoms.add(symptoms);
        Thread tr=new Thread(new Runnable() {
            @Override
            public void run() {
                Vedanta.getDatabase(getContext()).VedantaDAO().insertpresciption(prescription);
            }
        });
        tr.start();
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        viewaspdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createFile(Common.getAppPath(getContext())+"veda.pdf");
                                Log.i("hey","sucess");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
        return view;
    }


    private void createFile(String path) {
        if(new File(path).exists())
            new File(path).delete();
        try{
            Document document=new Document();
            PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Vedanta");
            document.addCreator("Prince Sinha");
            BaseColor color=new BaseColor(0,153,204,255);
            float fontsize=20.0f;
            float valuefont=26.0f;
            BaseFont fontname= BaseFont.createFont("res/font/raleway_black.ttf","UTF-8",BaseFont.EMBEDDED);
            Font font=new Font(fontname,18.0f, Font.NORMAL,BaseColor.BLACK);
            addNewItem(document, Element.ALIGN_CENTER,font,doctorName);
            BaseFont fontname2=BaseFont.createFont("res/font/raleway_bold.ttf","UTF-8",BaseFont.EMBEDDED);
            Font font2=new Font(fontname2,18.0f, Font.NORMAL,BaseColor.BLACK);
            addNewItem(document, Element.ALIGN_CENTER,font2,clinicAddress);
            addlineSeparator(document);
            BaseFont fontname3=BaseFont.createFont("res/font/raleway_bold.ttf","UTF-8",BaseFont.EMBEDDED);
            Font font3=new Font(fontname3,18.0f, Font.NORMAL,BaseColor.BLACK);
            BaseFont fontname4=BaseFont.createFont("res/font/raleway_thin.ttf","UTF-8",BaseFont.EMBEDDED);
            Font font4=new Font(fontname4,18.0f, Font.NORMAL,BaseColor.BLACK);
            addNewItem(document, Element.ALIGN_CENTER,font2,clinicAddress);
            addNewItem(document, Element.ALIGN_CENTER,font2,"");
            addNewLeftAndRight(document, Element.ALIGN_LEFT,Element.ALIGN_CENTER,font3,font4,"Date : \t\t\t",date);
            addNewItem(document, Element.ALIGN_CENTER,font2,"");
            addNewLeftAndRight(document, Element.ALIGN_LEFT,Element.ALIGN_CENTER,font3,font4,"Patient's Name : \t\t\t",patientName);
            addNewItem(document, Element.ALIGN_CENTER,font2,"");
            addNewLeftAndRight(document, Element.ALIGN_LEFT,Element.ALIGN_CENTER,font3,font4,"Address : \t\t\t",address);
            addNewItem(document, Element.ALIGN_CENTER,font2,"");
            addNewLeftAndRight(document, Element.ALIGN_LEFT,Element.ALIGN_CENTER,font3,font4,"DOB : \t\t\t",DOB);
            addNewItem(document, Element.ALIGN_CENTER,font2,"");
            addNewLeftAndRight(document, Element.ALIGN_LEFT,Element.ALIGN_CENTER,font3,font4,"Allergies : \t\t\t",symptoms);
            addNewItem(document, Element.ALIGN_CENTER,font2,"");
            addNewLeftAndRight(document, Element.ALIGN_LEFT,Element.ALIGN_CENTER,font3,font4,"Weight : \t\t\t",weight);
            addNewItem(document, Element.ALIGN_CENTER,font2,"");
            addNewLeftAndRight(document, Element.ALIGN_LEFT,Element.ALIGN_CENTER,font3,font4,"Medications : \t\t\t",medication);
            //Log.i("hey",signature+" sign");
            //URL url = new URL(signature);
            //byte[] decodedString = Base64.decode(signature,Base64.DEFAULT);
            //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //decodedByte.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //Image image = Image.getInstance(stream.toByteArray());
            //image.scalePercent(30);
            //image.setAlignment(Element.ALIGN_LEFT);
            //document.add(image);
            document.close();
            Log.i("hey","success");
             printPDF();
        }catch (Exception e){
            Log.i("hey",e.getMessage().toString());
        }
    }

    private void addNewLeftAndRight(Document document, int alignLeft, int alignCenter, Font leftFont,Font rightFont, String textLeft, String TextRight) throws DocumentException {
        Chunk chunk=new Chunk(textLeft,leftFont);
        Chunk chunk1=new Chunk(TextRight,rightFont);
        Paragraph paragraph=new Paragraph(chunk);
        paragraph.add(new VerticalPositionMark());
        paragraph.add(chunk1);
        document.add(paragraph);
    }

    private void addNewItem(Document document, int align, Font font,String data)  throws DocumentException {
        Chunk chunk=new Chunk(data,font);
        Paragraph paragraph=new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
    public class PrescriptionThread implements Runnable{

        @Override
        public void run() {

        }
    }
    private void addlineSeparator(Document document) throws DocumentException{
        LineSeparator lineSeparator=new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }
    public void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void printPDF() {
        PrintManager printManager=(PrintManager)context.getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter=new PDFDocumentAdapter(getContext(),Common.getAppPath(getContext())+"veda.pdf");
            printManager.print("Documents",printDocumentAdapter,new PrintAttributes.Builder().build());
        }catch (Exception e){
            Log.i("hry",e.getMessage().toString());
        }
    }
}
