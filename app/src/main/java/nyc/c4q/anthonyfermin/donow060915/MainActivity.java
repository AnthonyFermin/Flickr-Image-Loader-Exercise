package nyc.c4q.anthonyfermin.donow060915;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView images;
    Button reload;
    AsyncImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        images = (ListView) findViewById(R.id.imageViews);
        reload = (Button) findViewById(R.id.reloadButton);


    }

    @Override
    protected void onResume() {
        super.onResume();
        reload.callOnClick();
    }

    public void reload(View view){
        if(imageLoader != null){
            imageLoader.cancel(true);
        }
        imageLoader = new AsyncImageLoader();
        imageLoader.execute();
    }

    public class AsyncImageLoader extends AsyncTask<Void,Void,ArrayList<Bitmap>>{

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... voids) {
            Log.d("Background Process", "Started");

            ArrayList<String> imageUrls = new ArrayList<>();
            String url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";
            String flickrInfo = "";
            ArrayList<Bitmap> imageBitmaps = new ArrayList<>();
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                flickrInfo = EntityUtils.toString(httpEntity);

                JSONObject flickrJSON = new JSONObject(flickrInfo);
                JSONArray items = (JSONArray) flickrJSON.get("items");

                for(int i = 0; i < items.length(); i++){
                    JSONObject item = (JSONObject) items.get(i);
                    JSONObject mediaList = (JSONObject) item.get("media");
                    String imageUrl = (String) mediaList.get("m");
                    imageUrls.add(imageUrl);
                }

                for(int i = 0; i < imageUrls.size(); i++) {
                    Bitmap image = getBitmapFromURL(imageUrls.get(i));
                    imageBitmaps.add(image);
                }
            }catch(Exception e){
                e.printStackTrace();
                imageUrls.add(flickrInfo);
                return imageBitmaps;
            }

            return imageBitmaps;
        }

        public Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> imageList) {
            Log.d("PostExecute","Started");
            super.onPostExecute(imageList);

            if(imageList == null) {
                Toast message = Toast.makeText(MainActivity.this,"null info",Toast.LENGTH_LONG);
                message.show();
            }else {
                ImageAdapter imageAdapter = new ImageAdapter(MainActivity.this, imageList);
                images.setAdapter(imageAdapter);
            }
            Log.d("PostExecute","Finished");
        }

    }


}
