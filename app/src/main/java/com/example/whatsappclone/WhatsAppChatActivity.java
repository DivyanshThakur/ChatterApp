package com.example.whatsappclone;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class WhatsAppChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter arrayAdapter;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_chat);

        selectedUser = getIntent().getStringExtra("selectedUser");
        Toasty.info(WhatsAppChatActivity.this,"Chat with " + selectedUser + " Now!",Toasty.LENGTH_SHORT,true).show();
        setTitle(selectedUser);

        chatListView = findViewById(R.id.chatListView);

        findViewById(R.id.btnSend).setOnClickListener(this);

        chatsList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(WhatsAppChatActivity.this,android.R.layout.simple_list_item_1,chatsList);

        chatListView.setAdapter(arrayAdapter);


        try {

            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());


            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");


            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObjects : objects) {
                            String waMessage = chatObjects.get("waMessage") + "";
                            if (chatObjects.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }
                            if (chatObjects.get("waSender").equals(selectedUser)) {
                                waMessage = selectedUser + ": " + waMessage;
                            }
                            chatsList.add(waMessage);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        final EditText edtSend = findViewById(R.id.edtSend);

        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient", selectedUser);
        chat.put("waMessage", edtSend.getText().toString());

        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " + edtSend.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    edtSend.setText("");
                }
            }
        });

    }
}
