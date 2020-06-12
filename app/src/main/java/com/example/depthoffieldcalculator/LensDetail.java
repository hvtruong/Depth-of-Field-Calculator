package com.example.depthoffieldcalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class LensDetail extends AppCompatActivity {

    static boolean editLens;
    static int lensID;
    LensManager lensManager = LensManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lens_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


   public void newLensReturn(View v){
        EditText edit1 = (EditText) findViewById(R.id.editText);
        EditText edit2 = (EditText) findViewById(R.id.editText2);
        EditText edit3 = (EditText) findViewById(R.id.editText3);

        String lensName, focalLength, aperture;

        lensName = edit1.getText().toString();
        focalLength = edit2.getText().toString();
        aperture = edit3.getText().toString();
        if(lensName.isEmpty() || focalLength.isEmpty() || aperture.isEmpty() || Double.parseDouble(aperture) < 1.4 || Integer.parseInt(focalLength) == 0){
            Toast.makeText(LensDetail.this, "Oops!Invalid input, please try again!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent returnIntent = new Intent(LensDetail.this, MainActivity.class);

        returnIntent.putExtra("make",lensName);
        returnIntent.putExtra("focalLength",focalLength);
        returnIntent.putExtra("aperture",aperture);

        if(editLens != true) {
            setResult(Activity.RESULT_OK, returnIntent);
            lensManager.add(new Lens(lensName, Double.parseDouble(aperture), Integer.parseInt(focalLength)));
        }
        else{
            lensManager.getLens(lensID).setMake(lensName);
            lensManager.getLens(lensID).setMaximumAperture(Double.parseDouble(aperture));
            lensManager.getLens(lensID).setFocalLength(Integer.parseInt(focalLength));

            setResult(Activity.RESULT_CANCELED,returnIntent);
        }
        this.finish();
    }

    public static Intent makeLensDetailIntent(Context context){
        Intent newIntent = new Intent(context, LensDetail.class);
        return newIntent;
    }

    public static Intent makeEditIntent(Context context, int ID){
        Intent intent = new Intent(context, LensDetail.class);
        editLens = true;
        lensID = ID;
        return intent;
    }

    public void lensCancel(View v){
        Intent backIntent = new Intent(LensDetail.this,MainActivity.class);
        setResult(Activity.RESULT_CANCELED,backIntent);
        this.finish();
    }
}
