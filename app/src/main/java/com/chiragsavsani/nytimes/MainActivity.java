package com.chiragsavsani.nytimes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chiragsavsani.nytimes.adapters.FeedAdapter;
import com.chiragsavsani.nytimes.entities.Feeds;
import com.chiragsavsani.nytimes.entities.ImageData;
import com.chiragsavsani.nytimes.utils.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FeedAdapter mAdapter;
    ArrayList<Feeds> items = new ArrayList<>();
    ProgressDialog progressDialog;
    LinearLayout linearNoItemFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearNoItemFound = findViewById(R.id.linearNoItemFound);

        initComponent();

        if (isConnectingToInternet(MainActivity.this)) {
            getFeeds("1");
        } else {
            changeVisibility(true);
        }
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.listViewFeeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new FeedAdapter(this, items);
        recyclerView.setAdapter(mAdapter);

    }

    private void getFeeds(String days) {

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        final String uri = "https://api.nytimes.com/svc/mostpopular/v2/viewed/" + days + ".json?api-key=" + BuildConfig.API_KEY;
        StringRequest request = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            if (response != null && !response.trim().isEmpty()) {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                if (status.equals("OK")) {

                                    if (items != null) {
                                        items.clear();
                                    }

                                    /*Gson gson = new Gson();
                                    Type listType = new TypeToken<List<Feeds>>() {
                                    }.getType();
                                    items = gson.fromJson(object.getString("results"), listType);*/

                                    JSONArray resultArray = object.getJSONArray("results");

                                    for (int i = 0; i < resultArray.length(); i++) {
                                        JSONObject innerObj = resultArray.getJSONObject(i);
                                        ArrayList<ImageData> imageData = new ArrayList<>();

                                        String section = innerObj.getString("section");
                                        String byLine = innerObj.getString("byline");
                                        String type = innerObj.getString("type");
                                        String title = innerObj.getString("title");
                                        String feedAbstract = innerObj.getString("abstract");
                                        String published_date = innerObj.getString("published_date");
                                        String source = innerObj.getString("source");
                                        double id = innerObj.getDouble("id");

                                        String imageType = "";
                                        String subtype = "";
                                        String caption = "";
                                        String copyright = "";

                                        JSONArray imageArray = innerObj.getJSONArray("media");
                                        if (imageArray != null && imageArray.length() > 0) {
                                            JSONObject innerImageObj = imageArray.getJSONObject(0);

                                            imageType = innerImageObj.getString("type");
                                            subtype = innerImageObj.getString("subtype");
                                            caption = innerImageObj.getString("caption");
                                            copyright = innerImageObj.getString("copyright");

                                            JSONArray mediaMetadata = innerImageObj.getJSONArray("media-metadata");
                                            for (int j = 0; j < mediaMetadata.length(); j++) {
                                                JSONObject imgObj = mediaMetadata.getJSONObject(j);
                                                String url = imgObj.getString("url");
                                                String format = imgObj.getString("format");
                                                int height = imgObj.getInt("height");
                                                int width = imgObj.getInt("width");

                                                imageData.add(new ImageData(url, format, height, width));
                                            }
                                        }

                                        items.add(new Feeds(id, title, type, byLine, feedAbstract, published_date, source, section, caption, imageType, subtype, copyright, imageData));
                                    }

                                    if (items != null && items.size() > 0) {
                                        changeVisibility(false);
                                        mAdapter = new FeedAdapter(MainActivity.this, items);
                                        recyclerView.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();

                                        // on item list clicked
                                        mAdapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, Feeds obj, int position) {

                                                ArrayList<Feeds> singleFeedList = new ArrayList<>();
                                                singleFeedList.add(items.get(position));
                                                Intent goToDetails = new Intent(MainActivity.this, DetailsActivity.class);
                                                goToDetails.putParcelableArrayListExtra("DETAILS", singleFeedList);
                                                startActivity(goToDetails);
                                            }
                                        });

                                    } else {
                                        changeVisibility(true);
                                    }
                                } else {
                                    changeVisibility(true);
                                }
                            } else {
                                changeVisibility(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            changeVisibility(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            changeVisibility(true);
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void changeVisibility(boolean isVisible) {
        if (isVisible) {
            linearNoItemFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            linearNoItemFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_1) {
            if (isConnectingToInternet(MainActivity.this)) {
                getFeeds("1");
            } else {
                changeVisibility(true);
            }
            return true;
        } else if (id == R.id.action_7) {
            if (isConnectingToInternet(MainActivity.this)) {
                getFeeds("7");
            } else {
                changeVisibility(true);
            }
            return true;
        } else if (id == R.id.action_30) {
            if (isConnectingToInternet(MainActivity.this)) {
                getFeeds("30");
            } else {
                changeVisibility(true);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
