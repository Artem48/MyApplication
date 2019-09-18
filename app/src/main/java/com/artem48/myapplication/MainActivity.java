package com.artem48.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1;
        Button btn2;

        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn1.setOnClickListener(oclBtn1);
        btn2.setOnClickListener(oclBtn2);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native byte[] pictureFromJNI(AssetManager mng);
    public native byte[] pictureFromJNIlibzip(String PathToAPK);

    View.OnClickListener oclBtn1 = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            ImageView imageView = (ImageView) findViewById(R.id.image) ;
            String filename = "Canada_Mountains_Scenery_488936.jpg";
            InputStream inputStream = null;
            try{
                byte[] s=pictureFromJNI(getResources().getAssets());
                InputStream stream = new ByteArrayInputStream(s);
                inputStream = stream;
                Drawable d = Drawable.createFromStream(inputStream, null);
                imageView.setImageDrawable(d);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            finally {
                try{
                    if(inputStream!=null)
                        inputStream.close();
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    };
    View.OnClickListener oclBtn2 = new View.OnClickListener() {
        public void onClick(View v) {
            String PathToAPK;
            ApplicationInfo appInfo = null;
            PackageManager packMgmr = getPackageManager();
            try {
                appInfo = packMgmr.getApplicationInfo("com.artem48.myapplication", 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to locate APK...");
            }

            PathToAPK = appInfo.sourceDir;

            ImageView imageView = (ImageView) findViewById(R.id.image) ;
            String filename = "Canada_Mountains_Scenery_488936.jpg";
            InputStream inputStream = null;
            try{
                //byte[] s=pictureFromJNIlibzip(PathToAPK);
                byte[] s=pictureFromJNIlibzip(PathToAPK.toString());

                InputStream stream = new ByteArrayInputStream(s);
                inputStream = stream;
                Drawable d = Drawable.createFromStream(inputStream, null);
                imageView.setImageDrawable(d);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            finally {
                try{
                    if(inputStream!=null)
                        inputStream.close();
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    };
}
