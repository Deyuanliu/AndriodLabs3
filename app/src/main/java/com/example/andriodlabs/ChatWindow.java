package com.example.andriodlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andriodlabs.Message;
import com.example.andriodlabs.R;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {


    protected EditText chatEditText;
    private ArrayList<Message> chatMessage = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "ChatWindow";
    protected static final int SEND_MESSAGE = 1;
    protected static final int RECEIVE_MESSAGE = 2;

    // SQLite database
    protected static ChatDatabase chatData;
    protected SQLiteDatabase db;
    Cursor results;
    ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatData = new ChatDatabase(this);
        db = chatData.getWritableDatabase();

        results = db.query(false, chatData.TABLE_NAME,
                new String[]{chatData.KEY_ID, chatData.KEY_MESSAGE, chatData.KEY_TYPE}, chatData.KEY_ID +
                        " not null", null, null, null, null, null, null);
        int rows = results.getCount();
        printCursor(results);
        results.moveToFirst();
        while (!results.isAfterLast()) {
            // get message from database and add message to chatMessage
            chatMessage.add(new Message(results.getString(results.getColumnIndex(ChatDatabase.KEY_MESSAGE)),
                    results.getInt(results.getColumnIndex(ChatDatabase.KEY_TYPE)),
                    results.getLong(results.getColumnIndex(ChatDatabase.KEY_ID))

            ));
            //Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString( results.getColumnIndex( ChatDatabase.KEY_MESSAGE)));
            results.moveToNext();
        }


        ListView listView = (ListView) findViewById(R.id.listView);
        chatEditText = (EditText) findViewById(R.id.editChat);

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        Button btSend = (Button) findViewById(R.id.buttonSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                chatMessage.add(new Message(chatEditText.getText().toString(), SEND_MESSAGE));
//                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
//
//                ContentValues newValues = new ContentValues();
//
//                chatEditText.setText("");

                String text = chatEditText.getText().toString();
                if (!text.isEmpty()) {
                    addChatData(text, SEND_MESSAGE);
                    chatEditText.setText("");
                    listView.setSelection(messageAdapter.getCount() - 1);
                }


            }
        });

        Button btReceive = (Button) findViewById(R.id.buttonRecieve);
        btReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                chatMessage.add(new Message(chatEditText.getText().toString(), RECEIVE_MESSAGE));
//                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
//
//                ContentValues newValues = new ContentValues();
//
//                chatEditText.setText("");
                String text = chatEditText.getText().toString();
                if (!text.isEmpty()) {
                    addChatData(text, RECEIVE_MESSAGE);
                    chatEditText.setText("");
                    listView.setSelection(messageAdapter.getCount() - 1);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Context context = view.getContext();

                TextView textViewItem = ((TextView) view.findViewById(R.id.message_text));

                // get the clicked item name
                String listItemText = textViewItem.getText().toString();

                // just toast it
                Toast.makeText(context, "Item: " + listItemText, Toast.LENGTH_SHORT).show();

                Log.i("chatListView", "onItemClick: " + i + " " + l);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    //inner class ChatAdapter
    public class ChatAdapter extends ArrayAdapter<Message> {

        public ChatAdapter(Context ctx) {

            super(ctx, 0);
        }

        @Override
        public int getCount() {

            return chatMessage.size();
        }

        @Override
        public long getItemId(int position) {
            return position;

        }

        @Override
        public Message getItem(int position) {

            return chatMessage.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if (getItem(position).getMessage_type() == RECEIVE_MESSAGE) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_sending, null);
            }

            TextView message = (TextView) result.findViewById(R.id.message_text);

            message.setText(getItem(position).getMessage());

            return result;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 5) && (resultCode == Activity.RESULT_OK)) {
            Log.i(ACTIVITY_NAME, "Returned to ChatWindow.onActivityResult");
        }
    }

    private void printCursor(Cursor c) {
        //•	The database version number
        Log.i(ACTIVITY_NAME, "SQL MESSAGE: Databse version " + String.valueOf(db.getVersion()));
        //•	The number of columns in the cursor.
        Log.i(ACTIVITY_NAME, "SQL MESSAGE: Column Count " + String.valueOf(c.getColumnCount()));
        //•	The name of the columns in the cursor.
        for (String s : c.getColumnNames()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: Column Name " + s);
        }
        //•	The name of the columns in the cursor.
        for (int i = 0; i < c.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: Column Name " + c.getColumnName(i));
        }
        //•	The number of results in the cursor
        Log.i(ACTIVITY_NAME, "SQL MESSAGE: rows returned " + String.valueOf(c.getCount()));
        //•	Each row of results in the cursor. 
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + c.getString(c.getColumnIndex(ChatDatabase.KEY_MESSAGE)));
            results.moveToNext();
        }
    }

    public void deleteListMessage(Long id) {
        final SQLiteDatabase db = chatData.getWritableDatabase();

        db.delete(ChatDatabase.TABLE_NAME, "_id = " + id, null);
        chatMessage.clear();
        results = db.query(false, chatData.TABLE_NAME,
                new String[]{chatData.KEY_ID, chatData.KEY_MESSAGE, chatData.KEY_TYPE},
                chatData.KEY_ID + " not null", null, null, null, null, null, null);

        int rows = results.getCount();
        results.moveToFirst();
        while (!results.isAfterLast()) {
            // get message from database and add message to chatMessage
            chatMessage.add(new Message(results.getString(results.getColumnIndex(ChatDatabase.KEY_MESSAGE)),
                    results.getInt(results.getColumnIndex(ChatDatabase.KEY_TYPE)),
                    results.getLong(results.getColumnIndex(ChatDatabase.KEY_ID))
            ));
            //Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString( results.getColumnIndex( ChatDatabase.KEY_MESSAGE)));
            results.moveToNext();
        }

        messageAdapter.notifyDataSetChanged();
    }

    private void addChatData(final String str, final int type) {

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string name in the NAME column:
        newRowValues.put(ChatDatabase.KEY_MESSAGE, str);
        //put string email in the EMAIL column:
        newRowValues.put(ChatDatabase.KEY_TYPE, type);
        //insert in the database:
        long newId = db.insert(ChatDatabase.TABLE_NAME, null, newRowValues);

        Log.i("ChatRoomActivity:","addChatData done, msgId=" + newId);
        Message item = new Message(str, type, newId);

        chatMessage.add(item);
        messageAdapter.notifyDataSetChanged();
    }

}






