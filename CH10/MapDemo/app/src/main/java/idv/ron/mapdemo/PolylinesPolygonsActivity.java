package idv.ron.mapdemo;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PolylinesPolygonsActivity extends FragmentActivity {
    private GoogleMap map; // 儲存著地圖資訊
    private Marker marker_taroko; // 太魯閣國家公園標記
    private Marker marker_yushan; // 玉山國家公園標記
    private Marker marker_kenting; // 墾丁國家公園標記
    private Marker marker_yangmingshan; // 陽明山國家公園標記
    private LatLng taroko; // 太魯閣國家公園緯經度
    private LatLng yushan; // 玉山國家公園緯經度
    private LatLng kenting; // 墾丁國家公園緯經度
    private LatLng yangmingshan; // 陽明山國家公園緯經度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyline_polygon_activity);
        initPoints();
    }

    // 初始化所有地點的緯經度
    private void initPoints() {
        taroko = new LatLng(24.151287, 121.625537);
        yushan = new LatLng(23.791952, 120.861379);
        kenting = new LatLng(21.985712, 120.813217);
        yangmingshan = new LatLng(25.091075, 121.559834);
    }

    // 初始化地圖
    private void initMap() {
        // 檢查GoogleMap物件是否存在
        if (map == null) {
            // 從SupportMapFragment取得GoogleMap物件
            map = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fmMap)).getMap();
            if (map != null) {
                setUpMap();
            }
        }
    }

    // 完成地圖相關設定
    private void setUpMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(taroko) // 鏡頭焦點在太魯閣國家公園
                .zoom(7) // 地圖縮放層級定為7
                .build();
        // 改變鏡頭焦點到指定的新地點
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        addMarkersToMap();

        // 如果不自訂InfoWindowAdapter會自動套用預設訊息視窗
        map.setInfoWindowAdapter(new MyInfoWindowAdapter());

        MyMarkerListener myMarkerListener = new MyMarkerListener();
        // 註冊OnMarkerClickListener，當標記被點擊時會自動呼叫該Listener的方法
        map.setOnMarkerClickListener(myMarkerListener);
        // 註冊OnInfoWindowClickListener，當標記訊息視窗被點擊時會自動呼叫該Listener的方法
        map.setOnInfoWindowClickListener(myMarkerListener);

        addPolylinesPolygonsToMap();
    }

    // 加入多個標記
    private void addMarkersToMap() {
        marker_taroko = map.addMarker(new MarkerOptions()
                // 標記位置
                .position(taroko)
                        // 標記標題
                .title(getString(R.string.marker_title_taroko))
                        // 標記描述
                .snippet(getString(R.string.marker_snippet_taroko))
                        // 自訂標記圖示
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

        marker_yushan = map.addMarker(new MarkerOptions().position(yushan)
                .title(getString(R.string.marker_title_yushan))
                .snippet(getString(R.string.marker_snippet_yushan))
                        // 長按標記可以拖曳該標記
                .draggable(true));

        marker_kenting = map.addMarker(new MarkerOptions().position(kenting)
                .title(getString(R.string.marker_title_kenting))
                .snippet(getString(R.string.marker_snippet_kenting))
                        // 使用預設標記，但顏色改成綠色
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        marker_yangmingshan = map.addMarker(new MarkerOptions()
                .position(yangmingshan)
                .title(getString(R.string.marker_title_yangmingshan))
                .snippet(getString(R.string.marker_snippet_yangmingshan))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    private void addPolylinesPolygonsToMap() {
        // 連續線
        Polyline polyline = map.addPolyline(
                // 建立PolylineOptions物件
                new PolylineOptions()
                        // 加入頂點，會依照加入順序繪製直線
                        .add(yushan, yangmingshan, taroko)
                                // 設定線的粗細(像素)，預設為10像素
                        .width(5)
                                // 設定線的顏色(ARGB)，預設為黑色
                        .color(Color.MAGENTA)
                                // 與其他圖形在Z軸上的高低順序，預設為0
                                // 數字大的圖形會蓋掉小的圖形
                        .zIndex(1));

        // 可以利用Polyline物件改變原來屬性設定
        polyline.setWidth(6);

        // 多邊形
        map.addPolygon(
                // 建立PolygonOptions物件
                new PolygonOptions()
                        // 加入頂點
                        .add(yushan, taroko, kenting)
                                // 設定外框線的粗細(像素)，預設為10像素
                        .strokeWidth(5)
                                // 設定外框線的顏色(ARGB)，預設為黑色
                        .strokeColor(Color.BLUE)
                                // 設定填充的顏色(ARGB)，預設為黑色
                        .fillColor(Color.argb(200, 100, 150, 0)));

        // 圓形
        map.addCircle(
                // 建立CircleOptions物件
                new CircleOptions()
                        // 必須設定圓心，因為沒有預設值
                        .center(yushan)
                                // 半徑長度(公尺)
                        .radius(100000)
                                // 設定外框線的粗細(像素)，預設為10像素
                        .strokeWidth(5)
                                // 顏色為TRANSPARENT代表完全透明
                        .strokeColor(Color.TRANSPARENT)
                                // 設定填充的顏色(ARGB)，預設為黑色
                        .fillColor(Color.argb(100, 0, 0, 100)));
    }

    // 實作與標記相關的監聽器方法
    private class MyMarkerListener implements OnMarkerClickListener,
            OnInfoWindowClickListener {
        @Override
        // 點擊地圖上的標記
        public boolean onMarkerClick(final Marker marker) {
            return false;
        }

        @Override
        // 點擊標記的訊息視窗
        public void onInfoWindowClick(Marker marker) {
            // Toast標記的標題
            Toast.makeText(PolylinesPolygonsActivity.this, marker.getTitle(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    // 自訂InfoWindowAdapter，當點擊標記時會跳出自訂風格的訊息視窗
    private class MyInfoWindowAdapter implements InfoWindowAdapter {
        private final View infoWindow;

        MyInfoWindowAdapter() {
            // 取得指定layout檔，方便標記訊息視窗套用
            infoWindow = getLayoutInflater().inflate(
                    R.layout.custom_info_window, null);
        }

        @Override
        // 回傳設計好的訊息視窗樣式
        // 回傳null會自動呼叫getInfoContents(Marker)
        public View getInfoWindow(Marker marker) {
            int logoId;
            // 使用equals()方法檢查2個標記是否相同，千萬別用「==」檢查
            if (marker.equals(marker_yangmingshan)) {
                logoId = R.drawable.logo_yangmingshan;
            } else if (marker.equals(marker_taroko)) {
                logoId = R.drawable.logo_taroko;
            } else if (marker.equals(marker_yushan)) {
                logoId = R.drawable.logo_yushan;
            } else if (marker.equals(marker_kenting)) {
                logoId = R.drawable.logo_kenting;
            } else {
                // 呼叫setImageResource(int)傳遞0則不會顯示任何圖形
                logoId = 0;
            }

            // 顯示圖示
            ImageView iv_logo = ((ImageView) infoWindow
                    .findViewById(R.id.ivLogo));
            iv_logo.setImageResource(logoId);

            // 顯示標題
            String title = marker.getTitle();
            TextView tv_title = ((TextView) infoWindow
                    .findViewById(R.id.tvTitle));
            tv_title.setText(title);

            // 顯示描述
            String snippet = marker.getSnippet();
            TextView tv_snippet = ((TextView) infoWindow
                    .findViewById(R.id.tvSnippet));
            tv_snippet.setText(snippet);

            return infoWindow;
        }

        @Override
        // 當getInfoWindow(Marker)回傳null時才會呼叫此方法
        // 此方法如果再回傳null，代表套用預設視窗樣式
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    // 執行與地圖有關的方法前應該先呼叫此方法以檢查GoogleMap物件是否存在
    private boolean isMapReady() {
        if (map == null) {
            Toast.makeText(this, R.string.msg_MapNotReady, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    // 按下「清除」按鈕清除所有標記
    public void onClearMapClick(View view) {
        if (!isMapReady()) {
            return;
        }
        map.clear();
    }

    // 按下「重置」按鈕重新打上標記
    public void onResetMapClick(View view) {
        if (!isMapReady()) {
            return;
        }
        // 先清除Map上的標記再重新標記以避免重複標記
        map.clear();
        addMarkersToMap();
        addPolylinesPolygonsToMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMap();
    }
}
