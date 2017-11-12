package com.example.serhii.githubio;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static boolean sessionStarted = false;
    private RequestQueue requestQueue;
    private String dataFromCache;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<RepositoryItemInfo> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBarCyclic);

        recyclerView = (RecyclerView) findViewById(R.id.repositoriesInfoRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = null;
        File cacheFile = new File(getApplicationContext().getCacheDir(), Constants.CACHE_FILE_NAME);

        if(NetworkHelper.IsOnline(getApplicationContext()) && !sessionStarted) {
            LoadDataFromServer();
        } else if(cacheFile.exists()) {
            LoadDataFromCache();
        } else {
            Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    private void LoadDataFromCache()
    {
        progressBar.setVisibility(View.VISIBLE);
        dataFromCache = CacheManager.ReadFromCacheFile(Constants.CACHE_FILE_NAME, getApplicationContext());
        JSONArray cachedJsonData = null;

        try{
            cachedJsonData = new JSONArray(dataFromCache);
            listItems = GetRepositoryItems(cachedJsonData);
            InitRecycleViewWithAdapterData();
        }
        catch (JSONException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        progressBar.setVisibility(View.GONE);
    }

    private void LoadDataFromServer()
    {
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest request = new JsonArrayRequest(Constants.URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        listItems = GetRepositoryItems(jsonArray);
                        InitRecycleViewWithAdapterData();
                        sessionStarted = true;

                        CacheManager.WriteToCacheFile(Constants.CACHE_FILE_NAME, jsonArray.toString(), getApplicationContext());
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        request.setTag(Constants.LOADING_REPOSITORIES_TAG);
        request.setShouldCache(true);

        requestQueue = NetworkSingleton.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.start();
        requestQueue.add(request);
    }

    private List<RepositoryItemInfo> GetRepositoryItems(JSONArray jsonArray)
    {
        List<RepositoryItemInfo> ls = new ArrayList<RepositoryItemInfo>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);


                //This is because bug in google Json library, it parse null values as string 'null'
                //https://stackoverflow.com/questions/18226288/json-jsonobject-optstring-returns-string-null
                String name = obj.getString(Constants.JSON_NAME);
                if(obj.isNull(Constants.JSON_NAME))
                {
                    name = null;
                }

                String desc = obj.getString(Constants.JSON_DESCRIPTION);
                if(obj.isNull(Constants.JSON_DESCRIPTION))
                {
                    desc = null;
                }

                String lang = obj.getString(Constants.JSON_LANGUAGE);
                if(obj.isNull(Constants.JSON_LANGUAGE))
                {
                    lang = null;
                }

                RepositoryItemInfo item = new RepositoryItemInfo(
                        name,
                        desc,
                        lang,
                        obj.getInt(Constants.JSON_STARS_COUNT),
                        obj.getInt(Constants.JSON_FORKS_COUNT),
                        obj.getInt(Constants.JSON_ISSUES_COUNT),
                        obj.getInt(Constants.JSON_WATCHERS_COUNT),
                        obj.getString(Constants.JSON_UPDATED_AT));

                ls.add(item);
            }
        }
        catch (JSONException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return ls;
    }

    private void InitRecycleViewWithAdapterData()
    {
        adapter = new RepositoryAdapter(listItems, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue != null)
        {
            requestQueue.cancelAll(Constants.LOADING_REPOSITORIES_TAG);
        }
    }
}