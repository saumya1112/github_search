package com.example.github_search;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.databinding.DataBindingUtil;
import com.example.github_search.adapters.GithubRepositoryAdapter;
import com.example.github_search.models.GithubRepository;
import com.example.github_search.network.NetworkUtil;
import com.example.github_search.databinding.ActivityMainBinding;
import java.io.IOException;
        import java.net.URL;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = binding.etSearchTerm.getText().toString();
                if (TextUtils.isEmpty(searchTerm)) return;
                URL searchUrl = NetworkUtil.buildRepoSearchUrl(searchTerm);
                new GithubQueryTask().execute(searchUrl);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class GithubQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtil.getResponseFromHttp(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                parseAndDisplayRepos(githubSearchResults);
            }
        }
    }

    /**
     * Parse the JSON Response from network and display Output
     *
     * @param json Github JSON Response form the network
     */
    private void parseAndDisplayRepos(String json) {
        // Parse List
        List<GithubRepository> repositories = NetworkUtil.parseGithubRepos(json);

        // Create List Adapter
        GithubRepositoryAdapter adapter = new GithubRepositoryAdapter(repositories);

        // Set Adapter and layout type to RecyclerView
        binding.rvResults.setLayoutManager(new LinearLayoutManager(this));
        binding.rvResults.setAdapter(adapter);
    }
}