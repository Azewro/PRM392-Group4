package taskmanager.android_mizu_shop.activity;


import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import taskmanager.android_mizu_shop.R;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_adokok);

        String keyword = getIntent().getStringExtra("keyword");
        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setText("Kết quả tìm kiếm cho: " + keyword);

        // TODO: Gọi API để tìm sản phẩm theo keyword và hiển thị trong RecyclerView
    }
}
