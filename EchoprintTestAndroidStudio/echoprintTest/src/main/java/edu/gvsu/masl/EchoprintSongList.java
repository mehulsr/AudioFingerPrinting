package edu.gvsu.masl;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehulsmritiraje on 12/12/15.
 */
public class EchoprintSongList extends Activity {

    Button btn_done;
    ArrayList<Song> songs;
    ListView song_list_view;
    ArrayAdapter<Song> song_list_adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        btn_done=(Button)findViewById(R.id.okbutton);
        btn_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setContentView(R.layout.main);
                //send it back to the previous activity
                //1. Generate and store fingerprints for the songs selected
                //2. Create a new DB for the same - done
                //3. While loading, only show the songs that have not been fingerprinted - check against app DB for the same - done
                //4. finish() activity

                try
                {
                    AddFingerprints();

                }
                catch (Exception e) {

                }
                finally {
                    finish();
                }
            }
        });

        song_list_view=(ListView)findViewById(R.id.songlist);
        LoadSongList();

    }

    public void AddFingerprints()
    {
        final DatabaseHandler printHandler=new DatabaseHandler(this);
        Log.d("ACOnn1:","connection made?");
        for(int i=0;i<songs.size();i++)
        {
            if(songs.get(i).isChecked())
            {
                songs.get(i).setFingerprint("print "+i);
                Log.d("Print: ", "print " + i);
                printHandler.addFingerprint(songs.get(i));
            }
        }
        printHandler.close();
    }

    /*
@uthor: Mehul
This function fetches list of songs that are not generated.
 */
    public void LoadSongList()
    {
        //Fetching song list that has already been fingerprinted
        List<String> existingIDs=null;
        try {
            DatabaseHandler printHandler = new DatabaseHandler(this);
            existingIDs = printHandler.getAllFingerprintedIDs();
            printHandler.close();
            Log.d("size:",""+existingIDs.size());
            for(int i=0;i<existingIDs.size();i++)
            {
                Log.d("items:",existingIDs.get(i));
            }
        }
        catch (Exception e)
        {
            existingIDs=null;
        }

        //Fetching song list on the device
        ContentResolver cr = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, projection, selection, null, sortOrder);

        int count = 0;
        songs = new ArrayList<Song>();
        //song_paths = new ArrayList<String>();

        if(cur != null)
        {
            count = cur.getCount();
            if(count > 0)
            {
                while(cur.moveToNext())
                {
                    String internal_id=cur.getString(cur.getColumnIndex(MediaStore.Audio.Media._ID));
                    String title = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    // Add code to get more column here

                    /*
                    @uthor: Mehul
                    Step 2: Create new ListView row object and save data to it
                    Step 3: Saving model object to the ArrayList<Song> if it has not already been fingerprinted
                     */
                    //Integer id_to_int=Integer.parseInt(internal_id);
                    Log.d("song-message:",""+internal_id);
                    if(existingIDs!=null && existingIDs.contains(title))
                        continue;
                    else {
                        Song new_song;
                        new_song = new Song(internal_id, title, data);
                        songs.add(new_song);
                    }
                }

            }
        }
        cur.close();

        song_list_adapter=new SongAdapter(this,songs);
        song_list_view.setAdapter(song_list_adapter);
    }

//
//    /**@uthor: Mehul
//     *  Holds songs data.
//     *  Step 1: Model for each ListView row
//     * */
//    private static class Song
//    {
//        private String internal_id = "" ;
//        private String name = "" ;
//        private String title = "" ;
//        private String path = "" ;
//        private boolean checked = false ;
//        public Song() {}
//        public Song( String id,String title, String path )
//        {
//            this.internal_id=id;
//            this.title = title;
//            this.path = path;
//        }
//        public Song( String id,String title,String path, boolean checked )
//        {
//            this.internal_id=id;
//            this.title=title;
//            this.path=path;
//            this.checked = checked ;
//        }
//        public String getID()
//        {
//            return internal_id;
//        }
//        public String getTitle()
//        {
//            return title;
//        }
//        public void setTitle(String title)
//        {
//            this.title = title;
//        }
//        public boolean isChecked() {
//            return checked;
//        }
//        public void setChecked(boolean checked) {
//            this.checked = checked;
//        }
//        public String toString() {
//            return name ;
//        }
//        public void toggleChecked() {
//            checked = !checked ;
//        }
//    }

    /**
     * @uthor: Mehul
     * Child view for each row of the SongList
     * */

    private static class SongViewHolder
    {
        private CheckBox checkBox ;
        private TextView textView ;
        public SongViewHolder() {}
        public SongViewHolder( TextView textView, CheckBox checkBox )
        {
            this.checkBox = checkBox ;
            this.textView = textView ;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }


    /**
     * @uthor: Mehul
     * Step 4: Create Custom Adapter for the List
     */
    private static class SongAdapter extends ArrayAdapter<Song>
    {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        private LayoutInflater inflater;
        public SongAdapter( Context context, ArrayList<Song> song_list )
        {
            super( context, R.layout.simplerow, R.id.row_text, song_list );
            inflater = LayoutInflater.from(context) ;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // Song to display
            Song song_item = (Song) this.getItem(position);

            // The child views in each row.
            CheckBox checkBox ;
            TextView textView ;

            // Create a new row view
            if ( convertView == null )
            {
                convertView = inflater.inflate(R.layout.simplerow, null);
                // Find the child views.
                textView = (TextView) convertView.findViewById( R.id.row_text );
                checkBox = (CheckBox) convertView.findViewById( R.id.row_checkbox );

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag( new SongViewHolder(textView,checkBox) );

                // If CheckBox is toggled, update the planet it is tagged with.
                checkBox.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        CheckBox cb = (CheckBox) v ;
                        Song song = (Song) cb.getTag();
                        song.setChecked(cb.isChecked());
                    }
                });
            }
            // Reuse existing row view
            else
            {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                SongViewHolder viewHolder = (SongViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                textView = viewHolder.getTextView() ;
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBox.setTag( song_item );

            // Display song data
            checkBox.setChecked(song_item.isChecked());
            textView.setText(song_item.getTitle());
            return convertView;
        }
    }

    public Object onRetainNonConfigurationInstance()
    {
        return songs;
    }

}


/**@uthor: Mehul
 *  Holds songs data.
 *  Step 1: Model for each ListView row
 * */
class Song
{
    private String internal_id = "" ;
    private String name = "" ;
    private String title = "" ;
    private String path = "" ;
    private boolean checked = false ;
    private String fingerprint="";

    public Song() {}
    public Song( String id,String title, String path )
    {
        this.internal_id=id;
        this.title = title;
        this.path = path;
    }
    public Song( String id,String title,String path, boolean checked )
    {
        this.internal_id=id;
        this.title=title;
        this.path=path;
        this.checked = checked ;
    }
    public String getID()
    {
        return internal_id;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public String toString() {
        return name ;
    }
    public void toggleChecked() {
        checked = !checked ;
    }
    public void setFingerprint(String fingerprint)
    {
        this.fingerprint=fingerprint;
    }
    public String getFingerprint()
    {
        return fingerprint;
    }
}