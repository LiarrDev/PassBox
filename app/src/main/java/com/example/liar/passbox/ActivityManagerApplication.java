package com.example.liar.passbox;

import android.app.Activity;
import android.app.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Liar on 2018/1/4.
 */

public class ActivityManagerApplication extends Application {
	private static Map<String, Activity> destoryMap = new HashMap<>();

	private ActivityManagerApplication() {}

	// 添加要销毁的 Activity 到队列
	public static void addDestoryActivity(Activity activity, String activityName) {
		destoryMap.put(activityName, activity);
	}

	// 销毁指定 Activity
	public static void destoryActivity(String activityName) {
		Set<String> keySet = destoryMap.keySet();
		for (String key:keySet){
			destoryMap.get(key).finish();
		}
	}
}
