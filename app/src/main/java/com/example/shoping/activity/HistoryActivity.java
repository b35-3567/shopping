package com.example.shoping.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoping.R;
import com.example.shoping.adapter.History_adapter;
import com.example.shoping.model.OrderHistoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView history_rec;
    private History_adapter historyAdapter;
    private ArrayList<OrderHistoryModel> list;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        history_rec = findViewById(R.id.history_rec);
        list = new ArrayList<>();

        history_rec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        historyAdapter = new History_adapter(HistoryActivity.this, list);
        history_rec.setAdapter(historyAdapter);

        // Load dữ liệu từ Firebase
        loadHistoryData();
    }

    private void loadHistoryData() {
        // Lấy dữ liệu từ Firebase
        db.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("MyOrder")
                .orderBy("orderDate", Query.Direction.ASCENDING)  // Sắp xếp theo thứ tự giảm dần của thời gian đặt hàng
                .orderBy("orderTime", Query.Direction.ASCENDING)  // Sau đó, sắp xếp giảm dần theo thời gian đặt hàng
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Dữ liệu lấy được từ Firebase
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy ID của document
                            String orderId = document.getId();

                            // Lấy thông tin chi tiết đơn hàng
                            String address = document.getString("address");
                            String name = document.getString("name");
                            String orderDate = document.getString("orderDate");
                            String orderTime = document.getString("orderTime");
                            String phone = document.getString("phone");
                            String totalAmount = document.getString("totalAmount");
                            String voucherName = document.getString("voucherName");

                            // In ra Log để kiểm tra thông tin đơn hàng
                            Log.d("OrderInfo", "Order ID: " + orderId);
                            Log.d("OrderInfo", "Address: " + address);
                            Log.d("OrderInfo", "Name: " + name);
                            Log.d("OrderInfo", "Order Date: " + orderDate);
                            Log.d("OrderInfo", "Order Time: " + orderTime);
                            Log.d("OrderInfo", "Phone: " + phone);
                            Log.d("OrderInfo", "Total Amount: " + totalAmount);
                            Log.d("OrderInfo", "Voucher Name: " + voucherName);

                            // Lấy danh sách sản phẩm
                            List<OrderHistoryModel.ProductItem> productList = new ArrayList<>();
                            List<Map<String, Object>> products = (List<Map<String, Object>>) document.get("products");
                            if (products != null) {
                                for (Map<String, Object> product : products) {
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

                            // Tạo đối tượng OrderHistoryModel từ thông tin lấy được
                            OrderHistoryModel historyModel = new OrderHistoryModel(orderId, address, name, orderDate, orderTime, phone, productList, totalAmount, voucherName);

                            // Thêm ID vào đối tượng OrderHistoryModel
                            historyModel.setId(orderId);

                            // Thêm đối tượng OrderHistoryModel vào danh sách
                          // list.add(historyModel);
                            // Thêm đối tượng OrderHistoryModel vào đầu danh sách
                            list.add(0, historyModel);

                        }

                        // Khởi tạo và thiết lập RecyclerView Adapter
                        historyAdapter = new History_adapter(HistoryActivity.this, list);
                        history_rec.setAdapter(historyAdapter);
                    } else {
                        // Xử lý lỗi nếu cần
                        Log.e("LoadHistoryError", "Error loading history data", task.getException());
                    }
                });
    }
}