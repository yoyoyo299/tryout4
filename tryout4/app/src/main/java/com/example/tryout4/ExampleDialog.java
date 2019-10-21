package com.example.tryout4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import static com.example.tryout4.MainActivity.scanned_code;


public class ExampleDialog extends AppCompatDialogFragment {

    public static TextView scan;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder Builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.leyout_poststcan,null);

        Builder.setView(view)
                .setTitle("Scan Complete")
                .setMessage("" + scanned_code.getText().toString() + "")

                .setNegativeButton("Change model", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Enter the command u want when pressing change model
                        Intent myIntent = new Intent(getContext(),MainActivity.class);
                        startActivity(myIntent);
                    }
                })
                .setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Enter the command u want when pressing scan again

                    }
                });
        scan = view.findViewById(R.id.scan);
        return Builder.create();
    }




}
