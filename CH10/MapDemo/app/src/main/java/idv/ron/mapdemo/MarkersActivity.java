package idv.ron.mapdemo;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MarkersActivity extends FragmentActivity {
    private GoogleMap map; // 儲存著地圖資訊
    private Marker marker_taroko; // 太魯閣國家公園標記
    private Marker marker_yushan; // 玉山國家公園標記
    private Marker marker_kenting; // 墾丁國家公園標記
    private Marker marker_yangmingshan; // 陽明山國家公園標記
    private TextView tvMarkerDrag; // 顯示標記被拖曳後的相關訊息，例如緯經度
    private LatLng taroko; // 太魯閣國家公園緯經度
    private LatLng yushan; // 玉山國家公園緯經度
    private LatLng kenting; // 墾丁國家公園緯經度
    private LatLng yangmingshan; // 陽明山國家公園緯經度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_activity);
        tvMarkerDrag = (TextView) findViewById(R.id.tvMarkerDrag);
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
        map.setMyLocationEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(taroko) // 鏡頭焦點在太魯閣國家公園
                .zoom(7) // 地圖縮放層級定為7
                .build();
        // 改變鏡頭焦點到指定的新地點
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);

        addMarkersToMap();

        // 如果不套用自訂InfoWindowAdapter會自動套用預設訊息視窗
        map.setInfoWindowAdapter(new MyInfoWindowAdapter());

        MyMarkerListener myMarkerListener = new MyMarkerListener();
        // 註冊OnMarkerClickListener，當標記被點擊時會自動呼叫該Listener的方法
        map.setOnMarkerClickListener(myMarkerListener);
        // 註冊OnInfoWindowClickListener，當標記訊息視窗被點擊時會自動呼叫該Listener的方法
        map.setOnInfoWindowClickListener(myMarkerListener);
        // 註冊OnMarkerDragListener，當標記被拖曳時會自動呼叫該Listener的方法
        map.setOnMarkerDragListener(myMarkerListener);
    }

    // 在地圖上加入多個標記
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

    // 實作與標記相關的監聽器方法
    private class MyMarkerListener implements OnMarkerClickListener,
            OnInfoWindowClickListener, OnMarkerDragListener {
        @Override
        // 點擊地圖上的標記
        public boolean onMarkerClick(Marker marker) {
            showToast(marker.getTitle());
            return false;
        }

        @Override
        // 點擊標記的訊息視窗
        public void onInfoWindowClick(Marker marker) {
            // Toast標記的標題
            showToast(marker.getTitle());
        }

        @Override
        // 開始拖曳標記
        public void onMarkerDragStart(Marker marker) {
            tvMarkerDrag.setText("onMarkerDragStart");
        }

        @Override
        // 結束拖曳標記
        public void onMarkerDragEnd(Marker marker) {
            tvMarkerDrag.setText("onMarkerDragEnd");
        }

        @Override
        // 拖曳標記過程中會不斷呼叫此方法
        public void onMarkerDrag(Marker marker) {
            // 以TextView顯示標記的緯經度
            tvMarkerDrag.setText("onMarkerDrag.  Current Position: "
                    + marker.getPosition());
        }
    }

    // 自訂InfoWindowAdapter，當點擊標記時會跳出自訂風格的訊息視窗
    private class MyInfoWindowAdapter implements InfoWindowAdapter {
        private final View infoWindow;

        MyInfoWindowAdapter() {
            // 取得指定layout檔，方便標記訊息視窗套用
            infoWindow = LayoutInflater.from(MarkersActivity.this).inflate(
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
            ImageView ivLogo = ((ImageView) infoWindow
                    .findViewById(R.id.ivLogo));
            ivLogo.setImageResource(logoId);

            // 顯示標題
            String title = marker.getTitle();
            TextView tvTitle = ((TextView) infoWindow
                    .findViewById(R.id.tvTitle));
            tvTitle.setText(title);

            // 顯示描述
            String snippet = marker.getSnippet();
            TextView tvSnippet = ((TextView) infoWindow
                    .findViewById(R.id.tvSnippet));
            tvSnippet.setText(snippet);

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
        // 先清除Map上的標記再重新打上標記以避免標記重複
        map.clear();
        addMarkersToMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMap();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
