package com.example.phonecall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;

    private static final String LOG_TAG = "Android Phone Call";
    private EditText editText;
    private Button callButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editText = (EditText)findViewById(R.id.editText_PhoneNumber);
        this.callButton = (Button)findViewById(R.id.button_call);
    }

    private void askPermissionAndCall(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int sendPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if(sendPermission != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_REQUEST_CODE_CALL_PHONE);
                return;
            }
        }
        this.callNow();
    }

    private void callNow(){
        String phoneNumber = this.editText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: "+phoneNumber));
        try{
            this.startActivity(intent);
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Call Failed", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    public void onRequestPermissionResult(int requestCode, String permission[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permission, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(LOG_TAG, "Permission Granted");
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    this.callNow();
                }else{
                    Log.i(LOG_TAG, "Permission Denied");
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_CALL_PHONE){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();

            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Action Cancelled", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}