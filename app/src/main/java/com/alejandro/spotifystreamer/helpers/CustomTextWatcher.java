package com.alejandro.spotifystreamer.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import com.alejandro.spotifystreamer.activities.MainActivityFragment;
import com.alejandro.spotifystreamer.tasks.FindArtistTask;

/**
 * Created by agermenos on 7/1/15.
 */
public class CustomTextWatcher implements TextWatcher {
    private MainActivityFragment myFragment;

    public CustomTextWatcher(MainActivityFragment maf){
        super();
        myFragment = maf;
    }

    public void beforeTextChanged(CharSequence s, int start,
                                  int count, int after) {

    }
    public void afterTextChanged(Editable s) {
        if (s.length()>=2) {
            FindArtistTask fat = new FindArtistTask(myFragment);
            fat.execute(s.toString());
        }
    }

    public void onTextChanged(CharSequence s, int start,
                              int count, int after){

    }

}
