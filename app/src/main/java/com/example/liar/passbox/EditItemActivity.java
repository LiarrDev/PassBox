package com.example.liar.passbox;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EditItemActivity extends AppCompatActivity {

	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		Intent intent = getIntent();
		final String editapp = intent.getStringExtra("editapp");
		final String edituser = intent.getStringExtra("edituser");
		final String editpass = intent.getStringExtra("editpass");
		final String editweb = intent.getStringExtra("editweb");
		final String editps = intent.getStringExtra("editps");

		EditText appET = (EditText) findViewById(R.id.AppName);
		final EditText userET = (EditText) findViewById(R.id.UserName);
		final EditText passET = (EditText) findViewById(R.id.PassWord);
		final EditText webET = (EditText) findViewById(R.id.WebSite);
		final EditText psET = (EditText) findViewById(R.id.PSEdit);

		appET.setText(editapp);
		userET.setText(edituser);
		passET.setText(editpass);
		webET.setText(editweb);
		psET.setText(editps);

		appET.setEnabled(false);

		ImageButton edDoneBTN = (ImageButton) findViewById(R.id.Done);
		db = openOrCreateDatabase(DBinfo._DBNAME, Context.MODE_PRIVATE, null);

		edDoneBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (userET.length() == 0 || passET.length() == 0) {
					Toast.makeText(EditItemActivity.this, "账号和密码不能为空", Toast.LENGTH_LONG).show();
				} else if (userET.getText().toString().equals(edituser) && passET.getText().toString().equals(editpass) && webET.getText().toString().equals(editweb) && psET.getText().toString().equals(editps)){
					Toast.makeText(EditItemActivity.this, "未作任何修改", Toast.LENGTH_LONG).show();
				} else {
					String whereClause = DBinfo._APPNAME + "=?";
					ContentValues values = new ContentValues();
					values.put(DBinfo._USERNAME, userET.getText().toString());
					values.put(DBinfo._PASSWORD, passET.getText().toString());
					values.put(DBinfo._WEBSITE, webET.getText().toString());
					values.put(DBinfo._PS, psET.getText().toString());
					db.update(DBinfo._TABLENAME, values, DBinfo._APPNAME + "=?", new String[]{editapp});

					db.close();

					ActivityManagerApplication.destoryActivity("DetailActivity");

					Toast.makeText(EditItemActivity.this, "修改成功", Toast.LENGTH_LONG).show();

					finish();
				}
			}
		});


		Button randBTN = (Button) findViewById(R.id.RandPass);
		randBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final String[] items = new String[] {"6位数字密码", "12位组合密码"};
				final StringBuffer sb = new StringBuffer();
				AlertDialog dialog = new AlertDialog.Builder(EditItemActivity.this).setTitle("选择密码类型")
						.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								if (which == 0) {
									String randbase = "0123456789";
									int len = randbase.length();
									for (int i = 0; i < 6; i++) {
										sb.append(randbase.charAt((int) Math.round(Math.random() * (len - 1))));
									}
									passET.setText(sb.toString());
									dialogInterface.dismiss();
								} else if (which == 1){
									String randbase = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
									int len = randbase.length();
									for (int i = 0; i < 12; i++) {
										sb.append(randbase.charAt((int) Math.round(Math.random() * (len - 1))));
									}
									passET.setText(sb.toString());
									dialogInterface.dismiss();
								} else {
									dialogInterface.dismiss();
								}
							}
						}).create();
				dialog.show();
			}
		});

	}
}
