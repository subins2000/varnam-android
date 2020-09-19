package com.subinsb.varnam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("varnam-middle");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appDataFolder = getFilesDir().getPath();
        String varnamFolder = appDataFolder;
        copyAsset("ml.vst",  varnamFolder + "/ml.vst");
        copyAsset("ml.vst.learnings",  varnamFolder + "/ml.vst.learnings");

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(TextUtils.join(",", stringFromJNI(varnamFolder)));
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String[] stringFromJNI(String varnam_folder);

    private void copyAsset(String asset, String destination) {
        AssetManager assetManager = getAssets();

        try {
            InputStream in = assetManager.open(asset);

            File outFile = new File(destination);
            OutputStream out = new FileOutputStream(outFile);

            copyFile(in, out);

            in.close();
            out.close();
        } catch (Exception e) {
            Log.d("varnam", e.toString());
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
