package com.example.talleraplicada;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * @author paulburke (ipaulpro)
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    // code

    private SendImage sendImage;

    private EditText editTextIpAddress;

    private String ipAddress;

    private RadioGroup radioGroup;

    private String option = "1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sendImage = new SendImage();

        setContentView(R.layout.activity_main_interface);

        editTextIpAddress = (EditText)findViewById(R.id.editText_ip_address);

        radioGroup = findViewById(R.id.radio_group_exported);

        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(radioGroup.getCheckedRadioButtonId()==R.id.object_and_scene_detention){
                    option = "1";
                }else{
                    option = "0";
                }

            }
        });

    }


    public void showChooser(View v) {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);

                            ipAddress = editTextIpAddress.getText().toString();

                            boolean succesfulConnection = sendImage.sendImage(path, ipAddress, option);

                            if(!succesfulConnection){
                                Toast.makeText(MainActivity.this,
                                        "Hubo un problema al conectar con el servidor", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
