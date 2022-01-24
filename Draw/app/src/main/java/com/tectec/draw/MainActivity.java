package com.tectec.draw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    int defaultColor,tempColor=0;
    SignatureView signatureView;
    ImageButton imgEraser,imgColor,imgSave,imgPen;
    SeekBar seekBar;
    TextView txtPenSize;


    private static String fileName;
    File path=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Draw");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signatureView=findViewById(R.id.signature_view);
        seekBar=findViewById(R.id.penSize);
        txtPenSize=findViewById(R.id.txtPenSize);
        imgColor=findViewById(R.id.btnColor);
        imgEraser=findViewById(R.id.btnEraser);
        imgSave=findViewById(R.id.btnSave);
        imgPen=findViewById(R.id.btnPen);

        askPermission();

        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String date=format.format(new Date());
        fileName=path+"/"+date+".png";
        if(!path.exists()){
            path.mkdir();
        }

        defaultColor= ContextCompat.getColor(MainActivity.this,R.color.black);
        imgPen.setColorFilter(defaultColor);


        imgColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                openColorPicker();
            }
        });

        imgEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signatureView.clearCanvas();
                tempColor=defaultColor;
                int newColor=ContextCompat.getColor(MainActivity.this,R.color.white);
                signatureView.setPenColor(newColor);

            }
        });
        imgPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tempColor==0){
                    tempColor=ContextCompat.getColor(MainActivity.this,R.color.black);
                }
                signatureView.setPenColor(tempColor);

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtPenSize.setText(i+"dp");
                signatureView.setPenSize(i);
                seekBar.setMax(50);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!signatureView.isBitmapEmpty()){
                    try {
                        saveImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Can't Save!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    private void askPermission(){
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void openColorPicker(){
        AmbilWarnaDialog ambilWarnaDialog=new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor=color;
                signatureView.setPenColor(color);
                imgPen.setColorFilter(color);

            }
        });
        ambilWarnaDialog.show();
    }

    private void saveImage() throws IOException {
        File file=new File(fileName);
        Bitmap bitmap=signatureView.getSignatureBitmap();
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,bos);
        byte[] bitmapData=bos.toByteArray();
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }
}