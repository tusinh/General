package test.sinh.test.testdowloadfile;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import test.sinh.test.R;

public class Main19Activity extends AppCompatActivity {
    Button download, show;
    ProgressBar progressbar;

    String fileUrl = "http://divivuvn.com/wp-content/uploads/2017/12/thac-dambri.jpg";
    String fileUrl1 = "http://divivuvn.com/wp-content/uploads/2017/12/biet-thu-hang-nga-da-lat.jpg";
    String fileUrl2 = "http://divivuvn.com/wp-content/uploads/2017/12/secret-garden-da-lat.jpg";
    String fileUrl3 = "http://divivuvn.com/wp-content/uploads/2017/12/ho-than-tho.jpg";
    String fileUrl4 = "http://divivuvn.com/wp-content/uploads/2017/12/so-thu-zoodoo.jpg";

    String fileName = "img";
    String JPG = ".jpg";
    File rootDir = Environment.getExternalStorageDirectory();

    /*
    permission manifest:
     <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main19);
        download = findViewById(R.id.download);
        show = findViewById(R.id.show);
        progressbar = findViewById(R.id.progressbar);
        progressbar.setProgress(0);

        checkAndCreateDirectory("/test_async_task");

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownLoadFileAsync().execute(fileUrl,fileUrl1,fileUrl2,fileUrl3,fileUrl4);
                Log.e("tusinh", "kick download");
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tusinh", "kick show");
            }
        });

    }

    //function to verify if directory exists
    public void checkAndCreateDirectory(String dirName) {
        File new_dir = new File(rootDir + dirName);
        if (!new_dir.exists()) {
            new_dir.mkdirs();
        }
    }

    class DownLoadFileAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Main19Activity.this, "start download", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count = aurl.length;

            try {
                for(int i=0;i<count;i++){
                    URL u = new URL(aurl[i]);
                    HttpURLConnection c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(true);
                    c.connect();

                    int lengthOfFile = c.getContentLength();
                    FileOutputStream f = new FileOutputStream(new File(rootDir + "/test_async_task/", fileName+i+JPG));
                    InputStream in = c.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    long total = 0;
                    while ((len1 = in.read(buffer)) > 0) {
                        total += len1;
                        publishProgress("" + (int) ((total * 100) / lengthOfFile));
                        f.write(buffer, 0, len1);
                    }
                    f.close();
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("tusinh", "MalformedURLException: " + e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("tusinh", "IOException: " + e.toString());
            }
            return "download success";

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Log.e("tusinh", "gia tri: " + progress[0]);
            progressbar.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(Main19Activity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
