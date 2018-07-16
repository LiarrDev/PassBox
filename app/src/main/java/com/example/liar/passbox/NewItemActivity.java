package com.example.liar.passbox;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.content.DialogInterface.OnClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewItemActivity extends AppCompatActivity {

	private SQLiteDatabase db;
	private ListView lv;
	EditText AppNameedit;
	EditText Useredit;
	EditText passwordedit;
	ImageButton donebtn;
	EditText WebSiteedit;
	EditText PSedit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_item);

		// 生成随机密码的按钮
		Button randPassbtn = (Button) findViewById(R.id.RandPass);
		randPassbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showRandPassDialog();
			}
		});

		// 确认按钮
		donebtn = (ImageButton) findViewById(R.id.Done);
		donebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AppNameedit = findViewById(R.id.AppName);
				String AppName = AppNameedit.getText().toString();
				Useredit = findViewById(R.id.UserName);
				String UserName = Useredit.getText().toString();
				passwordedit = findViewById(R.id.PassWord);
				String PassWord = passwordedit.getText().toString();
				WebSiteedit = findViewById(R.id.WebSite);
				String WebSite = WebSiteedit.getText().toString();
				PSedit = findViewById(R.id.PSEdit);
				String PS = PSedit.getText().toString();


				Context context = NewItemActivity.this;
				db = openOrCreateDatabase(DBinfo._DBNAME, Context.MODE_PRIVATE, null);

				if (AppName.length() == 0 || UserName.length() == 0 || PassWord.length() == 0) {
					Toast.makeText(context, "前三项不能为空", Toast.LENGTH_LONG).show();
				} else {
					// 查询 AppName 是否存在
					Boolean isExist = false;
					String selsql = "SELECT * FROM " + DBinfo._TABLENAME
							+ " WHERE " + DBinfo._APPNAME + "=?";
					Cursor cursor = db.rawQuery(selsql, new String[]{AppName});
					while (cursor.moveToNext()) {
						db.close();
						Toast.makeText(context, "该项目名称已存在", Toast.LENGTH_LONG).show();
						isExist = true;
					}

					if (isExist == false) {

						String sql = "INSERT INTO " + DBinfo._TABLENAME + "("
								+ DBinfo._APPNAME + ","
								+ DBinfo._USERNAME + ","
								+ DBinfo._PASSWORD + ","
								+ DBinfo._WEBSITE + ","
								+ DBinfo._PS + ") values (?, ?, ?, ?, ?)";
						db.execSQL(sql, new Object[]{AppName, UserName, PassWord, WebSite, PS});

						Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
						finish();
					}
				}
			}
		});
	}

	private void showRandPassDialog() {
		final String[] items = new String[] {"6位数字密码", "12位组合密码"};
		passwordedit = findViewById(R.id.PassWord);
		final StringBuffer sb = new StringBuffer();
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("选择密码类型")
				.setSingleChoiceItems(items, -1, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				if (which == 0) {
					String randbase = "0123456789";
					int len = randbase.length();
					for (int i = 0; i < 6; i++) {
						sb.append(randbase.charAt((int) Math.round(Math.random() * (len - 1))));
					}
					passwordedit.setText(sb.toString());
					dialogInterface.dismiss();
				} else if (which == 1){
					String randbase = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
					int len = randbase.length();
					for (int i = 0; i < 12; i++) {
						sb.append(randbase.charAt((int) Math.round(Math.random() * (len - 1))));
					}
					passwordedit.setText(sb.toString());
					dialogInterface.dismiss();
				} else {
					dialogInterface.dismiss();
				}
			}
		}).create();
		dialog.show();
	}

}
