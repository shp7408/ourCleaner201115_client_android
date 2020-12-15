package com.example.ourcleaner_201008_java.View.Manager;


import androidx.fragment.app.FragmentActivity;

import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.Manager.demos.CameraDemoActivity;
import com.example.ourcleaner_201008_java.View.Manager.demos.EventsDemoActivity;
import com.example.ourcleaner_201008_java.View.Manager.demos.LocationDemoActivity;
import com.example.ourcleaner_201008_java.View.Manager.demos.MapViewDemoActivity;
import com.example.ourcleaner_201008_java.View.Manager.demos.MarkerDemoActivity;
import com.example.ourcleaner_201008_java.View.Manager.demos.PolygonDemoActivity;

public class DemoListItem {
	public final int titleId;
	public final int descriptionId;
	public final Class<? extends FragmentActivity> activity;

	public DemoListItem(int titleId, int descriptionId,
			Class<? extends FragmentActivity> activity) {
		this.titleId = titleId;
		this.descriptionId = descriptionId;
		this.activity = activity;
	}

	public static final DemoListItem[] DEMO_LIST_ITEMS = {
			new DemoListItem(R.string.map_view_demo_title,
					R.string.map_view_demo_desc, MapViewDemoActivity.class),
			new DemoListItem(R.string.marker_demo_title,
					R.string.marker_demo_desc, MarkerDemoActivity.class),
			new DemoListItem(R.string.polygon_demo_title,
					R.string.polygon_demo_desc, PolygonDemoActivity.class),
			new DemoListItem(R.string.location_demo_title,
					R.string.location_demo_desc, LocationDemoActivity.class),
			new DemoListItem(R.string.camera_demo_title,
					R.string.camera_demo_desc, CameraDemoActivity.class),
			new DemoListItem(R.string.events_demo_title,
					R.string.events_demo_desc, EventsDemoActivity.class)
	};
}
