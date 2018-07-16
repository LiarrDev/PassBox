package com.example.liar.passbox;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	private SQLiteDatabase db;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
				startActivity(intent);
			}
		});

		db = openOrCreateDatabase(DBinfo._DBNAME, Context.MODE_PRIVATE, null);
		int version = db.getVersion();

		// 创建数据库
		if (version < 1) {
			db.execSQL("CREATE TABLE " + DBinfo._TABLENAME + "("
					+ DBinfo._APPNAME + " text PRIMARY KEY, "
					+ DBinfo._USERNAME + " text NOT NULL, "
					+ DBinfo._PASSWORD + " text NOT NULL, "
					+ DBinfo._WEBSITE + " text, "
					+ DBinfo._PS + " text)");
			db.setVersion(1);
		}

		lv = findViewById(R.id.listview);
		initview();

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Map<String, String> map = (Map<String, String>) adapterView.getItemAtPosition(i);

				String sqlquery = "SELECT * FROM " + DBinfo._TABLENAME
						+ " WHERE " + DBinfo._APPNAME + "=?";
				Cursor cursor = db.rawQuery(sqlquery, new String[]{map.get("appname")});

				if (cursor.getCount() > 0) {
					cursor.moveToNext();
					String targetuser = cursor.getString(cursor.getColumnIndex(DBinfo._USERNAME));
					String targetpass = cursor.getString(cursor.getColumnIndex(DBinfo._PASSWORD));
					String targetweb = cursor.getString(cursor.getColumnIndex(DBinfo._WEBSITE));
					String targetps = cursor.getString(cursor.getColumnIndex(DBinfo._PS));

					Intent intent = new Intent(MainActivity.this, DetailActivity.class);
					intent.putExtra("targetapp", map.get("appname"));
					intent.putExtra("targetuser", targetuser);
					intent.putExtra("targetpass", targetpass);
					intent.putExtra("targetweb", targetweb);
					intent.putExtra("targetps", targetps);
					startActivity(intent);

				}
			}
		});
	}

	private void initview() {
		List<Map<String, String>> dt = new ArrayList<Map<String, String>>();
		try {
			// 查询表中所有数据
			Cursor cursor = db.query(DBinfo._TABLENAME, null, null, null, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("appname", cursor.getString(cursor.getColumnIndex(DBinfo._APPNAME)));
					map.put("username", cursor.getString(cursor.getColumnIndex(DBinfo._USERNAME)));
					dt.add(map);
				}
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("appname", "没有记录");
				dt.add(map);
			}
		} catch (Exception e) {
			Log.e("db-error", e.getMessage());
		}

		// 给 ListView 设置适配器，显示表中所有数据
		lv.setAdapter(new SimpleAdapter(this, dt, R.layout.listitem, new String[] {"appname", "username"}, new int[] {R.id.itemapp, R.id.itemuser}));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_about) {
			Intent intent = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		lv = findViewById(R.id.listview);
		initview();
	}
}
