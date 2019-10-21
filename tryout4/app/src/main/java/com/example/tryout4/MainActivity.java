package com.example.tryout4;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tryout4.adapters.ExampleAdapter;
import com.example.tryout4.databasees.DBHelper;
import com.example.tryout4.model.ListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private int CAMERA_PERMISSION_CODE = 1;
    public static TextView scanned_code;
    private Button btn_scan,btn_scan_more,btn_show,btn_show2;
    public static ExampleAdapter mAdapter;
    private SharedPreferences sharedPref;



    @Override
    protected void onCreate(Bundle savedIntancesState) {
        super.onCreate(savedIntancesState);
        setContentView(R.layout.activity_main);

        btn_scan =findViewById(R.id.btn_scan);
        btn_scan_more =findViewById(R.id.btn_scan_more);
        btn_show=findViewById(R.id.Show_Scanned_codes);
        btn_show2=findViewById(R.id.Show_Scanned_codes2);






        Spinner spinner =findViewById(R.id.spinner1);
        scanned_code =findViewById(R.id.scanned_code);
                final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner1, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(this);
                btn_scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){ }
                        else{
                            requestCameraPermission();
                        }
                       startActivity(new Intent(getApplicationContext(),CamActivity.class));
                    }
                });
                btn_scan_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){ }
                        else{
                            requestCameraPermission();
                        }

                        startActivity(new Intent(getApplicationContext(),ContinuousCaptureActivity.class));
                    }
                });
                btn_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                        startActivity(intent);

                    }
                });
        btn_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Result2Activity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("type",text);
        editor.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void requestCameraPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This Permission Is needed to Use Your Camera")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE)
            if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();

    }

    }