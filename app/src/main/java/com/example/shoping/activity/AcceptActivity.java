package com.example.shoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoping.R;
import com.example.shoping.adapter.DatHangAdapter;
import com.example.shoping.fragment.Fragment_sale;
import com.example.shoping.fragment.Fragment_ship;
import com.example.shoping.model.MyCartModel;
import com.example.shoping.model.VoucherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class AcceptActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static final int REQUEST_VOUCHER = 1;

    ArrayList<VoucherModel> voucherModelArrayList;
    private DatHangAdapter datHangAdapter;
    private ArrayList<MyCartModel> cartList;
    private TextView textThanhDanh, textSoDienThoai2,buyAdd;
    private FragmentActivity mActivity;
    private Fragment_sale fragmentSale;
    private boolean isFragmentOpen = false;
    private  FirebaseFirestore db;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    private EditText edt_dia_chi;
    TextView txt_sumtotal;

    private double totalProductPrice = 0.0; // Biến để tích lũy tổng giá tiền
    private static final int REQUEST_CODE_ADD_PRODUCT = 123; // Hằng số để xác định yêu cầu
    TextView nameVoucherTextView;
private Button dat_hang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        // Initialize RecyclerView
        textThanhDanh = findViewById(R.id.textThanhDanh);
        edt_dia_chi=findViewById(R.id.edt_dia_chi);
        textSoDienThoai2 = findViewById(R.id.textSoDienThoai2);
        buyAdd=findViewById(R.id.buyAdd);
        dat_hang=findViewById(R.id.dat_hang);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartList = new ArrayList<>();
        datHangAdapter = new DatHangAdapter(this, cartList);
        recyclerView.setAdapter(datHangAdapter);
// Trước khi sử dụng voucherModelArrayList, hãy khởi tạo nó
        voucherModelArrayList = new ArrayList<>();

       TextView next_accept=findViewById(R.id.next_accept);

        mActivity = this; // Khởi tạo biến mActivity


        fragmentSale = new Fragment_sale();
dat_hang.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
MyOrder();
    }
});
        next_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (!isFragmentOpen) {
                    openFragment();
                } else {
                    closeFragment();
                }*/
                startActivityForResult(new Intent(AcceptActivity.this,VoucherActivity.class),REQUEST_VOUCHER);

            }
        });



buyAdd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      // showFragmentHome();
        // Sử dụng startActivityForResult thay vì startActivity
        Intent intent = new Intent(AcceptActivity.this, YourFragmentActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT);
    }
});

        // Load data from Firebase
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                            // Tính tổng giá tiền
                            totalProductPrice += cartModel.getTotalPrice();

                            cartList.add(cartModel);
                            datHangAdapter.notifyDataSetChanged();
                        }
                        TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);
                        totalAmountTextView.setText("" + totalProductPrice);
                    }
                });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users").child(auth.getCurrentUser().getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);

                    // Update TextViews with user data
                    textThanhDanh.setText(name);
                    textSoDienThoai2.setText(phone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        datHangAdapter.notifyDataSetChanged();
    }

    private void showFragmentHome() {
        // Tạo một Fragment_home mới
        Fragment_ship fragmentHome = new Fragment_ship();

        // Thực hiện FragmentTransaction để thêm Fragment_home vào layout của AcceptActivity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentHome)
                .addToBackStack(null)
                .commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VOUCHER && resultCode == RESULT_OK) {
            // Nhận danh sách voucher được chọn từ ActivityVoucher
            ArrayList<String> selectedVouchers = data.getStringArrayListExtra("selectedVouchers");

            // Hiển thị danh sách voucher trong TextView
            StringBuilder selectedVouchersText = new StringBuilder("");
            for (String voucher : selectedVouchers) {
                selectedVouchersText.append(voucher).append(", ");
            }
            // Loại bỏ dấu phẩy cuối cùng
            if (selectedVouchersText.length() > 2) {
                selectedVouchersText.delete(selectedVouchersText.length() - 2, selectedVouchersText.length());
            }
            nameVoucherTextView = findViewById(R.id.name_voucher);
           nameVoucherTextView.setText(selectedVouchersText.toString());
           // Gọi hàm để lấy giá trị voucher từ Firebase
            getVoucherValueFromFirebase(selectedVouchersText.toString());
            if (requestCode == REQUEST_CODE_ADD_PRODUCT && resultCode == RESULT_OK) {
                // Nhận kết quả từ YourFragmentActivity và thực hiện cập nhật RecyclerView ở đây
                updateRecyclerView();
            }
        }
    }

    private void getVoucherValueFromFirebase(String voucherName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Truy vấn bộ sưu tập "Voucher" với các điều kiện
        db.collection("Voucher")
                .whereEqualTo("name_sale", voucherName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        double voucherDiscount = 0.0; // Khởi tạo giảm giá voucher

                        for (DocumentSnapshot document : task.getResult()) {
                            // Lấy giá trị từ trường value trong tài liệu
                            double voucherValue = document.getDouble("value");

                            // Kiểm tra nếu totalProductPrice lớn hơn hoặc bằng 100000
                            if (totalProductPrice >= 100000) {
                                // Trừ giá trị voucher nếu điều kiện được đáp ứng
                                voucherDiscount += voucherValue;
                            }
                        }

                        // Tính tổng tiền sau khi áp dụng giảm giá
                        double finalTotal = totalProductPrice - voucherDiscount;
txt_sumtotal=findViewById(R.id.txt_sumtotal);
txt_sumtotal.setText( ""+finalTotal);

                    } else {
                        // Xử lý lỗi
                    }
                });
    }


    // Hàm để cập nhật RecyclerView
    private void updateRecyclerView() {
        // Cập nhật danh sách sản phẩm trong giỏ hàng và thông báo cho adapter
        // (Đảm bảo thực hiện trên luồng UI chính)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Thực hiện cập nhật RecyclerView ở đây
                datHangAdapter.notifyDataSetChanged();
            }
        });
    }
    //add vô
    private void MyOrder() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        String saveCurrentDate, saveCurrentTime;

        // Lấy thời gian hiện tại
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        // Tạo HashMap để lưu thông tin đơn hàng
        final HashMap<String, Object> billMap = new HashMap<>();

        // Thêm thông tin vào HashMap
        billMap.put("address", edt_dia_chi.getText().toString()); // Địa chỉ từ EditText edt_dia_chi
        billMap.put("name", textThanhDanh.getText().toString()); // Tên từ TextView textThanhDanh
        billMap.put("phone", textSoDienThoai2.getText().toString()); // Số điện thoại từ TextView textSoDienThoai2
        billMap.put("totalAmount", txt_sumtotal.getText().toString()); // Tổng số tiền từ TextView txt_sumtotal
        billMap.put("voucherName", nameVoucherTextView.getText().toString()); // Tên voucher từ TextView name_voucher
        billMap.put("orderTime", saveCurrentTime); // Thời gian đặt hàng
        billMap.put("orderDate", saveCurrentDate); // Ngày đặt hàng

        // Tạo danh sách sản phẩm từ cartList và thêm vào HashMap
        ArrayList<HashMap<String, Object>> productList = new ArrayList<>();
        for (MyCartModel cartModel : cartList) {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("productName", cartModel.getProductName());
            productMap.put("productSize", cartModel.getProductSize());
            productMap.put("quantity", cartModel.getQuantity());
            productMap.put("totalPrice", cartModel.getTotalPrice());
            productMap.put("img_url", cartModel.getImg_url());

            productList.add(productMap);
        }
        billMap.put("products", productList);

        // Lưu dữ liệu vào Firestore
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrder").add(billMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Xử lý khi lưu thành công
                        Toast.makeText(AcceptActivity.this, "Đã đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        // Xóa dữ liệu trong giỏ hàng sau khi đặt hàng
                        clearCartData();
                        // Đóng AcceptActivity sau khi đặt hàng thành công
                        finish();
                    } else {
                        // Xử lý khi lưu thất bại
                        Toast.makeText(AcceptActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearCartData() {
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            // Xóa dữ liệu trong collection "AddToCart"
                            db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                    .collection("AddToCart").document(documentSnapshot.getId()).delete();
                        }
                        // Xóa dữ liệu trong cartList
                        cartList.clear();
                        datHangAdapter.notifyDataSetChanged();
                        // Xóa dữ liệu trong các TextView và EditText
                        edt_dia_chi.setText("");
                        textThanhDanh.setText("");
                        textSoDienThoai2.setText("");
                        txt_sumtotal.setText("");
                        nameVoucherTextView.setText("");
                        // Thông báo đặt hàng thành công
                        Toast.makeText(AcceptActivity.this, "Đã đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
