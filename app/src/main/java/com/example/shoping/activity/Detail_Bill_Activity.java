package com.example.shoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoping.R;
import com.example.shoping.adapter.Detai_bill_Adapter;
import com.example.shoping.model.OrderHistoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Detail_Bill_Activity extends AppCompatActivity {
    private RecyclerView history_rec;
    private Detai_bill_Adapter detaiBillAdapter;
    private ArrayList<OrderHistoryModel.ProductItem> list;
    private TextView textNgNhn, textSoDienThoai, textThanhDanh, textSoDienThoai2;
    private TextView totalAmountTextView, nameVoucherTextView, edtDiaChiTextView;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        textNgNhn = findViewById(R.id.textNgNhn);
        textSoDienThoai = findViewById(R.id.textSoDienThoai);
        textThanhDanh = findViewById(R.id.textThanhDanh);
        textSoDienThoai2 = findViewById(R.id.textSoDienThoai2);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        nameVoucherTextView = findViewById(R.id.name_voucher);
        edtDiaChiTextView = findViewById(R.id.edt_dia_chi);

        history_rec = findViewById(R.id.detail_bill_rec);
        history_rec.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        detaiBillAdapter = new Detai_bill_Adapter(this, list);
        history_rec.setAdapter(detaiBillAdapter);


        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String orderId = intent.getStringExtra("orderId");

            // Thực hiện truy vấn dữ liệu từ Firebase dựa trên orderId
            db.collection("CurrentUser")
                    .document(auth.getCurrentUser().getUid())
                    .collection("MyOrder")
                    .document(orderId)  // Thêm điều kiện truy vấn theo orderId
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Dữ liệu lấy được từ Firebase
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Lấy thông tin từ DocumentSnapshot
                                String address = document.getString("address");
                                String name = document.getString("name");
                                String orderDate = document.getString("orderDate");
                                String orderTime = document.getString("orderTime");
                                String phone = document.getString("phone");
                                String totalAmount = document.getString("totalAmount");
                                String voucherName = document.getString("voucherName");

                                // Lấy danh sách sản phẩm
                                List<OrderHistoryModel.ProductItem> productList = new ArrayList<>();
                                List<Map<String, Object>> products = (List<Map<String, Object>>) document.get("products");
                                if (products != null) {
                                    for (Map<String, Object> product : products) {
                                        // Lấy thông tin sản phẩm như đã làm trước đó

                                        String imgUrl = (String) product.get("img_url");
                                        String productName = (String) product.get("productName");
                                        String productSize = (String) product.get("productSize");
                                        long quantity = (long) product.get("quantity");
                                        long totalPrice = (long) product.get("totalPrice");

                                        // Tạo đối tượng ProductItem từ thông tin sản phẩm
                                        OrderHistoryModel.ProductItem productItem = new OrderHistoryModel.ProductItem(imgUrl, productName, productSize, (int) quantity, (int) totalPrice);

                                        // Thêm vào danh sách sản phẩm
                                        productList.add(productItem);

                                        // In ra Log để kiểm tra thông tin sản phẩm
                                        Log.d("ProductInfo", "Product Name: " + productName);
                                        Log.d("ProductInfo", "Quantity: " + quantity);

                                    }
                                }



                                // Hiển thị thông tin lên giao diện
                                displayDataOnUI(address, name, orderDate, orderTime, phone, totalAmount, voucherName, productList);
                            } else {
                                // Document không tồn tại
                                // Xử lý theo ý của bạn, ví dụ có thể hiển thị thông báo lỗi
                            }
                        } else {
                            // Xử lý lỗi nếu cần
                        }
                    });
        }


    }

    private void displayDataOnUI(String address, String name, String orderDate, String orderTime,
                                 String phone, String totalAmount, String voucherName,
                                 List<OrderHistoryModel.ProductItem> productList) {
        // Thực hiện hiển thị thông tin trên giao diện
        textThanhDanh.setText(""+ name);
        textSoDienThoai2.setText("" + phone);
        totalAmountTextView.setText(" " + totalAmount);
        nameVoucherTextView.setText(" " + voucherName);
        edtDiaChiTextView.setText(" " + address);

        // Cập nhật RecyclerView Adapter
        detaiBillAdapter.updateData(productList);
    }

 }
