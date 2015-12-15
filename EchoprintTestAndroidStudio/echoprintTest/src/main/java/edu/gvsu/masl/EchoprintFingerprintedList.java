package edu.gvsu.masl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehulsmritiraje on 12/12/15.
 */
public class EchoprintFingerprintedList extends Activity {

    Button btn_seen;
    ListView song_list_view;
    ArrayAdapter<String> fingerprinted_adapter;
    ArrayList<Song> send_back_list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_list);
        song_list_view = (ListView) findViewById(R.id.printedlist);
        LoadFingerprintedList();

        btn_seen=(Button)findViewById(R.id.seenbutton);
        btn_seen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    finish();
                }
        });
    }

    /*
@uthor: Mehul
This function fetches list of songs that generated.
 */
    public void LoadFingerprintedList() {
        //Fetching song list that has already been fingerprinted
        List<String> existingIDs;
        try
        {
            DatabaseHandler printHandler = new DatabaseHandler(this);
            existingIDs = printHandler.getAllFingerprintedIDs();
            printHandler.close();
        }
        catch (Exception e)
        {
            existingIDs=null;
        }

        fingerprinted_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, existingIDs);
        song_list_view.setAdapter(fingerprinted_adapter);
    }
}
