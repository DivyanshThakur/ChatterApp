package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class WhatsAppClone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_clone);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeContainer);
        final ListView listView = findViewById(R.id.listView);
        final ArrayList<String> waUsers = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(WhatsAppClone.this,android.R.layout.simple_list_item_1,waUsers);

        try {
            // adding users in listview from parse server
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseUser user : objects) {
                            waUsers.add(user.getUsername());
                        }
                        listView.setAdapter(adapter);
                    } else {
                        Toasty.error(WhatsAppClone.this, e.getMessage(), Toasty.LENGTH_LONG, true).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // refreshing the listview
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {

                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", waUsers);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() >0) {
                                if (e == null) {
                                    for (ParseUser user : objects) {
                                        waUsers.add(user.getUsername());
                                    }
                                    adapter.notifyDataSetChanged();
                                    if (swipeRefreshLayout.isRefreshing()) {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }

                            } else {
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutMenuItem) {
            Toasty.info(WhatsAppClone.this,ParseUser.getCurrentUser().getUsername() + "is logged out",Toasty.LENGTH_SHORT,true).show();
            ParseUser.getCurrentUser().logOut();
            finish();

            Intent intent = new Intent(WhatsAppClone.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
