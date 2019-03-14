package com.xungerrrr.storage2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    private myDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String name = bundle.getString("name");
        db = new myDB(getApplicationContext());

        ListView comments = findViewById(R.id.comments);
        final EditText input = findViewById(R.id.comment_input);
        Button send = findViewById(R.id.send);

        final List<Map<String, Object>> data = db.getComments(name);
        final MyAdapter myAdapter = new MyAdapter(this, data, name);
        comments.setAdapter(myAdapter);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input.getText().toString())) {
                    // 评论为空
                    Toast.makeText(getApplicationContext(), R.string.empty_comment, Toast.LENGTH_SHORT).show();
                }
                else {
                    // 发送评论
                    Date date = Calendar.getInstance().getTime();
                    android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
                    String time = dateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString();
                    Map<String, Object> comment = new LinkedHashMap<>();
                    comment.put("name", name);
                    comment.put("time", time);
                    comment.put("content", input.getText().toString());
                    comment.put("_id", db.addComment(comment));
                    comment.put("avatar", db.getAvatar(name));
                    comment.put("number", 0);
                    comment.put("tag", 0);
                    data.add(comment);
                    myAdapter.notifyDataSetChanged();
                    input.setText("");
                }
            }
        });
        comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = data.get(position).get("name").toString();
                // 查询电话号码
                Cursor cursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = \"" + name + "\"",
                        null, null);
                String number;
                if(cursor.moveToFirst()) {
                    // 至少有一个号码
                    number = "\nPhone: ";
                    do {
                        number += cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "         ";
                    } while (cursor.moveToNext());
                }
                else {
                    // 没有号码
                    number = "\nPhone number not exist.";
                }
                alertDialog.setTitle("INFO").setPositiveButton("OK", null).setMessage(
                        "Username: " + name + number).create().show();
            }
        });
        comments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView comment_name = view.findViewById(R.id.comment_name);
                if (TextUtils.equals(comment_name.getText().toString(), name)) {
                    // 如果是自己发的评论，则实现删除
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int _id = (int)data.get(position).get("_id");
                            db.deleteComment(_id);
                            data.remove(position);
                            myAdapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("NO", null).setTitle(
                            "Delete or not?").setMessage("").create().show();
                }
                else {
                    // 如果是别人发的评论，则实现举报
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), R.string.report_msg, Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("NO", null).setTitle(
                            "Report or not?").setMessage("").create().show();
                }
                return true;
            }
        });
    }
}
