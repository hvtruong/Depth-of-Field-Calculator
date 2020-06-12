package com.example.depthoffieldcalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Objects;

public class CalculateDoF extends AppCompatActivity {

    Lens chosenLens;
    int lensID;
    EditText edit4, edit5, edit6;
    TextView textView14, textView15, textView16, textView17;
    LensManager lensManager = LensManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_do_f);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extractDataFromIntent();

        TextView textView10 = findViewById(R.id.textView10);
        textView10.setText(chosenLens.toString());

        edit4 = (EditText) findViewById(R.id.editText4);
        edit5 = (EditText) findViewById(R.id.editText5);
        edit6 = (EditText) findViewById(R.id.editText6);

        textView14 = findViewById(R.id.textView14);
        textView15 = findViewById(R.id.textView15);
        textView16 = findViewById(R.id.textView16);
        textView17 = findViewById(R.id.textView17);
    }

    public void calculateDoF(View v){

        String cOf, dist, aper;

        cOf = edit4.getText().toString();
        dist = edit5.getText().toString();
        aper = edit6.getText().toString();

        if(cOf.isEmpty() || dist.isEmpty() || aper.isEmpty()){
            Toast.makeText(CalculateDoF.this, "Oops!Invalid input, please try again!", Toast.LENGTH_LONG).show();
            return;
        }

        double circleOfConfusion = Double.parseDouble(cOf);
        double aperture = Double.parseDouble(aper);
        double distance = Double.parseDouble(dist);

        if(circleOfConfusion <= 0 || distance <= 0 || aperture < 1.4){
            Toast.makeText(CalculateDoF.this, "Oops!Invalid input, please try again!", Toast.LENGTH_LONG).show();
            return;
        }

        if(circleOfConfusion <= 0) {
            textView14.setText("NaN");
            textView15.setText("NaN");
            textView16.setText("NaN");
            textView17.setText("NaN");
        }
        else if(aperture < chosenLens.getMaximumAperture()){
            textView14.setText("Invalid aperture");
            textView15.setText("Invalid aperture");
            textView16.setText("Invalid aperture");
            textView17.setText("Invalid aperture");
        }
        else{
            DepthOfFieldCalculator DoF = new DepthOfFieldCalculator(chosenLens, distance,aperture,circleOfConfusion);
            textView14.setText("" + formatM(DoF.nearFocalPoint()/1000) + "m");
            textView15.setText("" + formatM(DoF.farFocalPoint()/1000) + "m");
            textView16.setText("" + formatM(DoF.depthOfField()/1000) + "m");
            textView17.setText("" + formatM(DoF.hyperfocalDistance()/1000) + "m");
        }
    }

    public void editLens(View v){
        Intent i = LensDetail.makeEditIntent(CalculateDoF.this, lensID);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String lensName = data.getStringExtra("make");
                int focalLength = Integer.parseInt(data.getStringExtra("focalLength"));
                double aperture = Double.parseDouble(Objects.requireNonNull(data.getStringExtra("aperture")));

                lensManager.getLens(lensID).setMake(lensName);
                lensManager.getLens(lensID).setMaximumAperture(aperture);
                lensManager.getLens(lensID).setFocalLength(focalLength);

                Intent backIntent = new Intent(CalculateDoF.this,MainActivity.class);
                setResult(Activity.RESULT_CANCELED,backIntent);
                Lens subLens = lensManager.getLens(lensID);

                TextView textView10 = findViewById(R.id.textView10);
                textView10.setText(subLens.toString());
                this.finish();
            }
        }
    }

    public static Intent makeIntent(Context context, Lens chosenLens, int lensID){
        Intent newIntent = new Intent(context, CalculateDoF.class);
        newIntent.putExtra("make", chosenLens.getMake());
        newIntent.putExtra("maxAperture",chosenLens.getMaximumAperture());
        newIntent.putExtra("focalLength",chosenLens.getFocalLength());
        newIntent.putExtra("lensID", lensID);
        return newIntent;
    }

    private void extractDataFromIntent(){
        Intent newIntent = getIntent();
        String make = newIntent.getStringExtra("make");
        double maxAperture = newIntent.getDoubleExtra("maxAperture",0);
        int focalLength = newIntent.getIntExtra("focalLength",0);
        lensID = newIntent.getIntExtra("lensID",-1);
        chosenLens = new Lens(make,maxAperture,focalLength);
    }

    public void deleteLens(View v){
        lensManager.remove(lensManager.getLens(lensID));
        Intent backIntent = new Intent(CalculateDoF.this,MainActivity.class);
        setResult(Activity.RESULT_CANCELED,backIntent);
        this.finish();
    }

    private String formatM(double distanceInM) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distanceInM);
    }
}
