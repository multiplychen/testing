package idv.ron.mapdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// ListActivity內容即為ListView，點擊每個選項列會開啓一個範例
public class MainActivity extends ListActivity {
	// 儲存著多個新頁面資訊
	private List<DetailPage> detailPages;

	// 一個DetailPage物件代表一頁，也就是一個範例內容
	private class DetailPage {
		// 新頁面的標題id
		private int titleId;
		// 新頁面屬於哪種類別
		private Class<? extends FragmentActivity> detailActivity;

		public DetailPage(int titleId,
				Class<? extends FragmentActivity> detailActivity) {
			this.titleId = titleId;
			this.detailActivity = detailActivity;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 有多少個範例，就加上多少個DetailPage物件
		detailPages = new ArrayList<>();
		// 「基本Google地圖展示」範例
		detailPages.add(new DetailPage(R.string.title_BasicMap,
				BasicMapActivity.class));
		// 「地圖種類與操作界面設定」範例
		detailPages.add(new DetailPage(R.string.title_MapTypeUiSettings,
				MapTypeUiSettingsActivity.class));
		// 「在地圖上做標記」範例
		detailPages.add(new DetailPage(R.string.title_Markers,
				MarkersActivity.class));
		// 「在地圖上繪製直線與多邊形」範例
		detailPages.add(new DetailPage(R.string.title_PolylinesPolygons,
				PolylinesPolygonsActivity.class));
		// 「將地址或地名轉成地圖上的標記」範例
		detailPages.add(new DetailPage(R.string.title_Geocoder,
				GeocoderActivity.class));

		// 將各個範例標題文字存入List後套用在ListView選項列上
		List<String> detailTitles = new ArrayList<>();
		for (DetailPage detailPage : detailPages) {
			detailTitles.add(getString(detailPage.titleId));
		}
		setListAdapter(new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, detailTitles));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// 點擊ListView選項列後即開啓對應的Activity頁面
		DetailPage detailpage = detailPages.get(position);
		startActivity(new Intent(this, detailpage.detailActivity));
	}

}
