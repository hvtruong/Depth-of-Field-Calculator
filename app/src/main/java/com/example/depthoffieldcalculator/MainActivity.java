package com.example.depthoffieldcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    LensManager lensManager = LensManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        listView = (ListView)findViewById(R.id.listView);

        if(lensManager.isEmpty()) {
            lensManager.add(new Lens("Cannon", 1.8, 50));
            lensManager.add(new Lens("Tamron", 2.8, 90));
            lensManager.add(new Lens("Sigma", 2.8, 200));
            lensManager.add(new Lens("Nikon", 4, 200));
        }
        this.updateUI();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = LensDetail.makeLensDetailIntent(MainActivity.this);
            startActivityForResult(i,1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String lensName = data.getStringExtra("make");
                int focalLength = Integer.parseInt(data.getStringExtra("focalLength"));
                double aperture = Double.parseDouble(Objects.requireNonNull(data.getStringExtra("aperture")));
            }
            this.updateUI();
        }
    }

    private void updateUI(){
        ArrayList<String> arrayList = new ArrayList<>();
        TextView textView19 = findViewById(R.id.textView19);
        for(int i = 0; i < lensManager.size();i++){
            arrayList.add(lensManager.getLens(i).toString());
        }

        if(lensManager.size() != 0){
            textView.setText("Select a lens to use:");
            textView19.setText("");
        }
        else{
            textView.setText("");
            textView19.setText("OHH NO! Your lens list is empty now \n Please click the add button below to add a new lens!");
        }

        TextView textView18 = (TextView) findViewById(R.id.textView18);
        textView18.setText("Optional Features:\n1.App Bar Buttons Via Toolbar\n2.Edit and Delete Lens\n3.Error Checking Input\n7.Empty State");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClick, int position, long id) {
                Lens chosenLens = lensManager.getLens(position);
                Intent newIntent = CalculateDoF.makeIntent(MainActivity.this, chosenLens, position);
                startActivityForResult(newIntent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
