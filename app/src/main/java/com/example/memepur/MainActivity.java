package com.example.memepur;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView ;
    ProgressBar progressBar;
    String imageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressbar);
        loadMeme();
    }
    private void loadMeme() {
//        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
//                getRequestQueue();
//        ProgressBar pb = progressBar.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        imageURL = "https://meme-api.herokuapp.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, imageURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        textView.setText("Response: " + response.toString());

                        try {
                            imageURL = response.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Glide.with(MainActivity.this).load(imageURL).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;

                            }
                        }).into(imageView);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();


                    }
                });

// Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);

    }



    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT,"Hey Check this out!! : "+imageURL);
        //chooser menu
        Intent chooser = Intent.createChooser(intent,"Share this meme using....");
        startActivity(chooser);
    }

    public void nextMeme(View view) {
        loadMeme();
    }
}
//    public static class MySingleton {
//            private static MySingleton instance;
//            private RequestQueue requestQueue;
//            private ImageLoader imageLoader;
//            private static Context ctx;
//
//            private MySingleton(Context context) {
//                ctx = context;
//                requestQueue = getRequestQueue();
//
//                imageLoader = new ImageLoader(requestQueue,
//                        new ImageLoader.ImageCache() {
//                            private final LruCache<String, Bitmap>
//                                    cache = new LruCache<String, Bitmap>(20);
//
//                            @Override
//                            public Bitmap getBitmap(String url) {
//                                return cache.get(url);
//                            }
//
//                            @Override
//                            public void putBitmap(String url, Bitmap bitmap) {
//                                cache.put(url, bitmap);
//                            }
//                        });
//            }
//
//            public static synchronized MySingleton getInstance(Context context) {
//                if (instance == null) {
//                    instance = new MySingleton(context);
//                }
//                return instance;
//            }
//
//            public RequestQueue getRequestQueue() {
//                if (requestQueue == null) {
//                    // getApplicationContext() is key, it keeps you from leaking the
//                    // Activity or BroadcastReceiver if someone passes one in.
//                    requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
//                }
//                return requestQueue;
//            }
//
//            public <T> void addToRequestQueue(Request<T> req) {
//                getRequestQueue().add(req);
//            }
//
//            public ImageLoader getImageLoader() {
//                return imageLoader;
//            }
//        }
//    }
