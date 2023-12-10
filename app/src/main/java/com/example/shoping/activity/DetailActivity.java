package com.example.shoping.activity;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shoping.R;
import com.example.shoping.model.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity  {
    Button btn_add_cart;
    final int[] soLuong = {1};
    final int[] giaSizeM = {0}; // Thay bằng giá size M thực tế
    final int[] giaSizeL = {0}; // Thay bằng giá size L thực tế
    TextView detail_price,detail_des,txt_soluong,detail_name,detail_id;
    ImageView detail_img,addItem,removeItem;
    RadioButton radioButtonM,radioButtonL  ;
    private RadioGroup radioGroup;
    TextView txt_M;
    TextView txt_L;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ProductModel productModel=null;

    FirebaseUser currentUser;
  private   final HashMap<String,Object> cartMap=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        radioButtonM = findViewById(R.id.rdo_M);
        radioButtonL = findViewById(R.id.rdo_L);
        detail_price=findViewById(R.id.detail_price);
        detail_des=findViewById(R.id.detail_des);
        detail_img=findViewById(R.id.detail_img);
        detail_name=findViewById(R.id.detail_name);
        detail_id=findViewById(R.id.detail_id);
        radioGroup = findViewById(R.id.radioGroup);
        addItem=findViewById(R.id.addItem);
        removeItem=findViewById(R.id.RemoveItem);
        txt_soluong=findViewById(R.id.txt_soluong);
        btn_add_cart =findViewById(R.id.btn_add_cart);
        // In YourNextActivity

        final Object object=getIntent().getSerializableExtra("detail");
        if(object instanceof ProductModel){
            productModel=(ProductModel)object;
        }
        if(productModel != null){
            Glide.with(getApplicationContext()).load(productModel.getImg_url()).into(detail_img);
            detail_des.setText(productModel.getDescription());
            detail_price.setText("Giá:"+Integer.toString(productModel.getPrice())+"VNĐ");
            detail_name.setText(productModel.getName());
            detail_id.setText(productModel.getDocumentId());
        }


// Uncheck both radio buttons
        radioButtonM.setChecked(false);
        radioButtonL.setChecked(false);

// Set sự kiện cho RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Kiểm tra RadioButton nào được chọn và cập nhật giá của size M hoặc L
            if (checkedId == R.id.rdo_M) {
                giaSizeM[0] = Integer.parseInt(txt_M.getText().toString());
            } else if (checkedId == R.id.rdo_L) {
                giaSizeL[0] = Integer.parseInt(txt_L.getText().toString());
            }

            // Cập nhật UI khi size thay đổi
            updateUI();
        });

// Set sự kiện cho RadioButton M
        radioButtonM.setOnClickListener(view -> {
            radioButtonL.setChecked(false);  // Hủy chọn RadioButton L

            // Check if txt_M is not null before accessing its text
            if (txt_M != null) {
                giaSizeM[0] = Integer.parseInt(txt_M.getText().toString());
                Log.d(TAG, "giáM: " + Arrays.toString(giaSizeM));
                updateUI();
            }
        });

// Set sự kiện cho RadioButton L
        radioButtonL.setOnClickListener(view -> {
            radioButtonM.setChecked(false);  // Hủy chọn RadioButton M

            // Check if txt_L is not null before accessing its text
            if (txt_L != null) {
                giaSizeL[0] = Integer.parseInt(txt_L.getText().toString());
                Log.d(TAG, "giáL: " + Arrays.toString(giaSizeL));
                updateUI();
            }
        });


        db = FirebaseFirestore.getInstance();

        // Thực hiện truy vấn dữ liệu cho size "M"
        String sizeM = "M";
        db.collection("SIZE_Product")
                .whereEqualTo("size_name", sizeM)
                .whereEqualTo("product_id", productModel.getDocumentId())

                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Lấy giá trị từ trường size_price
                            Long sizePriceM = documentSnapshot.getLong("size_price");

                            // Kiểm tra xem size_price có giá trị không và gán vào TextView
                            if (sizePriceM != null) {
                                txt_M = findViewById(R.id.txt_M);
                                txt_M.setText(String.valueOf(sizePriceM));
                                // Log giá trị sizeM
                                // After updating txt_M, call updateUI
                                updateUI();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu có
                    }
                });

// Thực hiện truy vấn dữ liệu cho size "L"
        String sizeL = "L";
        db.collection("SIZE_Product")
                .whereEqualTo("size_name", sizeL)
                .whereEqualTo("product_id", productModel.getDocumentId())

                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Lấy giá trị từ trường size_price
                            Long sizePriceL = documentSnapshot.getLong("size_price");

                            // Kiểm tra xem size_price có giá trị không và gán vào TextView
                            if (sizePriceL != null) {
                                txt_L = findViewById(R.id.txt_L);
                                txt_L.setText(String.valueOf(sizePriceL));
                                // After updating txt_L, call updateUI
                                updateUI();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu có

                    }
                });

// Lắng nghe sự kiện cho nút thêm
        addItem.setOnClickListener(view -> {
            soLuong[0]++;
            updateUI();
        });

// Lắng nghe sự kiện cho nút giảm
        removeItem.setOnClickListener(view -> {
            if (soLuong[0] > 1) {
                soLuong[0]--;
                updateUI();
            }
        });
        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });


    }
    private void addedToCart() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", productModel.getName());

        // Get the selected size and set the corresponding product price
        int selectedPrice;
        String productSize = ""; // Initialize productSize as an empty string
        if (radioButtonM.isChecked()) {
            selectedPrice = giaSizeM[0];
            productSize = "M"; // Set productSize to "M"
        } else if (radioButtonL.isChecked()) {
            selectedPrice = giaSizeL[0];
            productSize = "L"; // Set productSize to "L"
        } else {
            // Handle the case where neither radio button is checked (optional)
            selectedPrice = 0; // or some default value
        }

        cartMap.put("productId", productModel.getDocumentId());
        cartMap.put("productPrice", selectedPrice);
        cartMap.put("productSize", productSize); // Add productSize to the cartMap
        cartMap.put("quantity", soLuong[0]);

        // Calculate and set the totalPrice
        int totalPrice = selectedPrice * soLuong[0];
        cartMap.put("totalPrice", totalPrice);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);

        cartMap.put("img_url", productModel.getImg_url());

        // Tạo document với ID của người dùng trong collection "CurrentUser"
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap)
                .addOnCompleteListener(task -> {
                    Toast.makeText(DetailActivity.this, "Đã thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    // Phương thức để cập nhật giao diện người dùng
    private void updateUI() {
        // Hiển thị số lượng mới
        // Kiểm tra xem cả hai txt_M và txt_L đều có giá trị
        if (txt_M != null && txt_L != null) {
            // Hiển thị số lượng mới
            txt_soluong.setText(String.valueOf(soLuong[0]));

            // Tính giá dựa trên số lượng và giá của size M hoặc L
            int gia;
            if (radioButtonM.isChecked()) {
                gia = giaSizeM[0];
            } else if (radioButtonL.isChecked()) {
                gia = giaSizeL[0];
            } else {
                // Handle the case where neither radio button is checked (optional)
                gia = 0; // or some default value
            }

            int giaTong = soLuong[0] * gia;

            // Hiển thị giá mới
            detail_price.setText("Giá: " + giaTong + "VNĐ");
        }
    }
}
