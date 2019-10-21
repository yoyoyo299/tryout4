package com.example.tryout4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tryout4.adapters.MyAdapter;
import com.example.tryout4.databasees.DBHelper;
import com.example.tryout4.model.ListItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Result2Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ListItem> arrayList;
    DBHelper helper;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Button show,clear,share;
    private EditText result_message;
    MyAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper = new DBHelper(this);
        Cursor cursor = helper.alldata();
        if (cursor.getCount()==0){
            Toast.makeText(getApplicationContext(),"NO DATA",Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext());

        }

        //fetch data from database... if it is available, show it using recycler view adapter

        arrayList = helper.getallinformation();
        if (arrayList.size()>0){
            myAdapter = new MyAdapter(arrayList,this);
            recyclerView.setAdapter(myAdapter);
            //data is available, set to adapter
        }
        else {
            Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_SHORT).show();
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                ListItem listItem = arrayList.get(position);
                //remove data
                helper.deleterow(listItem.getId());
                arrayList.remove(position);
                myAdapter.notifyItemRemoved(position);
                myAdapter.notifyItemRangeChanged(position,arrayList.size());


            }
        }).attachToRecyclerView(recyclerView);

        final IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setCameraId(0);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentIntegrator.initiateScan();
            }
        });




        show=findViewById(R.id.btn_show_list);
        clear=findViewById(R.id.btn_clear);
        share=findViewById(R.id.btn_share);
        result_message=findViewById(R.id.message);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                myAdapter.notifyDataSetChanged();

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMail();
            }
        });


    }

    private void SendMail() {

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        final String type = sharedPref.getString("type","");
        final String UserId = sharedPref.getString("firebasekey", "");



            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String username = "yahiatestdummyone@gmail.com";
                        final String password = "yahiatest";
                        Properties props = new Properties();
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.port", "587");

                        Session session = Session.getInstance(props,
                                new Authenticator() {

                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication(username, password);
                                    }
                                });
                        try {
                            javax.mail.Message message = new MimeMessage(session);
                            message.setFrom(new InternetAddress("yahiatestdummyone@gmail.com"));
                            message.setRecipients(javax.mail.Message.RecipientType.TO,
                                    InternetAddress.parse("narutoyoyo10@gmail.com"));
                            message.setSubject("scanned codes from "+"\n"+UserId);
                            message.setText("model "+type+"\n"+result_message.getText().toString());
                            Transport.send(message);

                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!=null){
            if (result.getContents() == null){

                Toast.makeText(getApplicationContext(),"no results foound",Toast.LENGTH_SHORT).show();
            }else{
                boolean isinserted = helper.insertdata(result.getContents(), result.getFormatName());
                if (isinserted)
                {
                    arrayList.clear();
                    arrayList = helper.getallinformation();
                    myAdapter = new MyAdapter(arrayList,this);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }else
        {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    };

