package com.example.liar.passbox;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

	private SQLiteDatabase db;

	TextView appshow;
	TextView usershow;
	TextView passshow;
	TextView webshow;
	TextView psshow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		ActivityManagerApplication.addDestoryActivity(this, "DetailActivity");

		ImageButton editBTN = findViewById(R.id.EditBtn);
		ImageButton delBTN = findViewById(R.id.DelBtn);

		appshow = findViewById(R.id.AppShow);
		usershow = findViewById(R.id.UserShow);
		passshow = findViewById(R.id.PassShow);
		webshow = findViewById(R.id.WebShow);
		psshow = findViewById(R.id.PSShow);

		Intent intent = getIntent();
		final String targetapp = intent.getStringExtra("targetapp");
		final String targetuser = intent.getStringExtra("targetuser");
		final String targetpass = intent.getStringExtra("targetpass");
		final String targetweb = intent.getStringExtra("targetweb");
		final String targetps = intent.getStringExtra("targetps");

		appshow.setText(targetapp);
		usershow.setText(targetuser);
		passshow.setText(targetpass);
		webshow.setText(targetweb);
		psshow.setText(targetps);

		// 删除提醒对话框
		delBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog dialog = new AlertDialog.Builder(DetailActivity.this).setTitle("删除")
						.setIcon(R.drawable.delete_alertdialog).setNegativeButton("取消", null)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								db = openOrCreateDatabase(DBinfo._DBNAME, Context.MODE_PRIVATE, null);
								db.delete(DBinfo._TABLENAME, DBinfo._APPNAME + "=?", new String[]{targetapp});
								dialogInterface.dismiss();
								Toast.makeText(DetailActivity.this, "删除成功", Toast.LENGTH_LONG).show();
								finish();
							}
						}).setMessage("确认删除？").create();
				dialog.show();
			}
		});

		// 修改信息 BUTTON
		editBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent editintent = new Intent(DetailActivity.this, EditItemActivity.class);
				editintent.putExtra("editapp", targetapp);
				editintent.putExtra("edituser", targetuser);
				editintent.putExtra("editpass", targetpass);
				editintent.putExtra("editweb", targetweb);
				editintent.putExtra("editps", targetps);
				startActivity(editintent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

}
