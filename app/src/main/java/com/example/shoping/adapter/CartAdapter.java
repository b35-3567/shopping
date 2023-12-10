package com.example.shoping.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoping.R;
import com.example.shoping.model.MyCartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MyCartModel> list;

    public CartAdapter(Context context, ArrayList<MyCartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_cart, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.cart_img);
        holder.cart_name.setText(list.get(position).getProductName());
        holder.cart_totalprice.setText("Giá:" + list.get(position).getTotalPrice());
        holder.cart_size.setText("Size:" + list.get(position).getProductSize());
        holder.cart_soluong.setText("Số lượng:" + list.get(position).getQuantity());
        holder.edit.setOnClickListener(v -> {
            MyCartModel myCartModel = list.get(holder.getAdapterPosition());
            dialogUpdateSanPham(myCartModel, holder.getAdapterPosition());
        });
        holder.delete.setOnClickListener(v -> {
            MyCartModel myCartModel = list.get(holder.getAdapterPosition());
            deleteCartItem(myCartModel.getProductId(), holder);
        });
    }

    private void deleteCartItem(String productId, ViewHolder holder) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference userCartRef = db.collection("CurrentUser")
                    .document(auth.getCurrentUser().getUid())
                    .collection("AddToCart");

            if (productId != null) {
                userCartRef
                        .whereEqualTo("productId", productId)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete()
                                            .addOnSuccessListener(aVoid -> {
                                                // Kiểm tra nếu vị trí là hợp lệ trước khi xóa
                                                int adapterPosition = holder.getAdapterPosition();
                                                if (adapterPosition != RecyclerView.NO_POSITION) {
                                                    list.remove(adapterPosition);
                                                    notifyItemRemoved(adapterPosition);
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(context, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.e("DeleteCartItem", "Error deleting item: " + e.getMessage());
                                            });
                                }
                            } else {
                                Toast.makeText(context, "Lỗi khi truy vấn: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("DeleteCartItem", "Query error: " + task.getException().getMessage());
                            }
                        });
            } else {
                Toast.makeText(context, "Lỗi khi lấy ID sản phẩm", Toast.LENGTH_SHORT).show();
                Log.e("DeleteCartItem", "Product ID is null");
            }
        } else {
            Toast.makeText(context, "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            Log.e("DeleteCartItem", "User not authenticated");
        }
    }

    private void updateCart(
            MyCartModel cartItem,
            TextView txtM, TextView txtL,
            RadioButton rdoM, RadioButton rdoL,
            int[] giaSizeM, int[] giaSizeL, int[] soLuong, TextView txtProductPrice, int position) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference userCartRef = db.collection("CurrentUser")
                    .document(auth.getCurrentUser().getUid())
                    .collection("AddToCart");



            String productId = cartItem.getProductId();
            if (productId != null) {
                int newPrice = 0;
                String selectedSize = "";

                if ((rdoM.isChecked() && "M".equals(cartItem.getProductSize()))    ||
                     (rdoL.isChecked() && "L".equals(cartItem.getProductSize()))) {

                    if (rdoM.isChecked()) {
                        newPrice = giaSizeM[0];
                        selectedSize = "M";
                    } else if (rdoL.isChecked()) {
                        newPrice = giaSizeL[0];
                        selectedSize = "L";
                    }
                }
//Add these lines for debugging
                Log.d("UpdateCart", "Selected Size: " + selectedSize);
                Log.d("UpdateCart", "giaSizeM: " + giaSizeM[0]);
                Log.d("UpdateCart", "giaSizeL: " + giaSizeL[0]);
                if (!selectedSize.isEmpty()) {
                    final MyCartModel finalCartItem = cartItem;
                    final String finalSelectedSize = selectedSize;
                    final int finalNewPrice = newPrice;
                    final int finalSoLuong = soLuong[0];

                    finalCartItem.setProductPrice((long) finalNewPrice);

                    // Check if the document exists before updating
                    userCartRef.whereEqualTo("productId", productId).get().addOnCompleteListener(queryDocumentSnapshotsTask -> {
                        if (queryDocumentSnapshotsTask.isSuccessful()) {
                            QuerySnapshot querySnapshot = queryDocumentSnapshotsTask.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Document exists, proceed with the update
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                Map<String, Object> updateData = new HashMap<>();
                                updateData.put("quantity", finalSoLuong);
                                updateData.put("productSize", finalSelectedSize);
                                updateData.put("productPrice", finalCartItem.getProductPrice());
                                updateData.put("totalPrice", (int) (finalCartItem.getProductPrice() * finalSoLuong));

                                documentSnapshot.getReference().update(updateData)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                finalCartItem.setQuantity(finalSoLuong);
                                                finalCartItem.setProductSize(finalSelectedSize);
                                                finalCartItem.setTotalPrice((int) (finalCartItem.getProductPrice() * finalSoLuong));
                                                list.set(position, finalCartItem);
                                                notifyItemChanged(position);

                                                Toast.makeText(context, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Lỗi khi chỉnh sửa: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.e("UpdateCart", "Error updating cart: " + task.getException().getMessage());
                                            }
                                        });
                            } else {
                                Toast.makeText(context, "Lỗi: Không tìm thấy sản phẩm để cập nhật", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Lỗi khi truy vấn
                            Log.e("UpdateCart", "Error querying cart: " + queryDocumentSnapshotsTask.getException().getMessage());
                        }
                    });
                } else {
                    Toast.makeText(context, "Lỗi khi lấy thông tin size", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Lỗi khi lấy ID sản phẩm", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }


    }



    private void dialogUpdateSanPham(MyCartModel myCartModel, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_edit, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView txtProductName = view.findViewById(R.id.detail_name);
        TextView txtProductPrice = view.findViewById(R.id.detail_price);
        TextView txtProductId = view.findViewById(R.id.detail_id);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        RadioButton rdoM = view.findViewById(R.id.rdo_M);
        RadioButton rdoL = view.findViewById(R.id.rdo_L);
        TextView txtM = view.findViewById(R.id.txt_M);
        TextView txtL = view.findViewById(R.id.txt_L);

        ImageView addItem = view.findViewById(R.id.addItem);
        TextView txtSoluong = view.findViewById(R.id.txt_soluong);
        ImageView removeItem = view.findViewById(R.id.RemoveItem);
        ImageView detail_img = view.findViewById(R.id.detail_img);
        Button btnUpdate = view.findViewById(R.id.btn_update);

        txtProductName.setText(myCartModel.getProductName());
        txtProductPrice.setText("Price: " + myCartModel.getTotalPrice());
        Glide.with(context).load(myCartModel.getImg_url()).into(detail_img);
        txtSoluong.setText(String.valueOf(myCartModel.getQuantity()));
        txtProductId.setText(myCartModel.getProductId());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String productId = myCartModel.getProductId();

        db.collection("SIZE_Product")
                .whereEqualTo("product_id", productId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            String sizeName = document.getString("size_name");

                            if (sizeName != null) {
                                if (sizeName.equals("M")) {
                                    int priceM = document.getLong("size_price").intValue();
                                    txtM.setText(String.valueOf(priceM));
                                } else if (sizeName.equals("L")) {
                                    int priceL = document.getLong("size_price").intValue();
                                    txtL.setText(String.valueOf(priceL));
                                }

                                if (myCartModel.getProductSize() != null) {
                                    if ("M".equals(myCartModel.getProductSize())) {
                                        rdoM.setChecked(true);
                                    } else if ("L".equals(myCartModel.getProductSize())) {
                                        rdoL.setChecked(true);
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Lỗi: size_name is null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Lỗi khi lấy thông tin size: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        final int[] soLuong = {1};
        final int[] giaSizeM = {0};
        final int[] giaSizeL = {0};

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdo_M) {
                giaSizeM[0] = Integer.parseInt(txtM.getText().toString());
                giaSizeL[0] = 0; // Đặt giá trị giaSizeL về 0 khi chọn rdo_M
                rdoL.setChecked(false);
                updateUI(txtM, txtL, txtSoluong, rdoM, rdoL, giaSizeM, giaSizeL, soLuong, txtProductPrice);
            } else if (checkedId == R.id.rdo_L) {
                giaSizeL[0] = Integer.parseInt(txtL.getText().toString());
                giaSizeM[0] = 0; // Đặt giá trị giaSizeM về 0 khi chọn rdo_L
                rdoM.setChecked(false);
                updateUI(txtM, txtL, txtSoluong, rdoM, rdoL, giaSizeM, giaSizeL, soLuong, txtProductPrice);
            }
        });

        rdoM.setOnClickListener(v -> {
            rdoL.setChecked(false);
            if (txtM != null) {
                giaSizeM[0] = Integer.parseInt(txtM.getText().toString());
                updateUI(txtM, txtL, txtSoluong, rdoM, rdoL, giaSizeM, giaSizeL, soLuong, txtProductPrice);
            }
        });

        rdoL.setOnClickListener(v -> {
            rdoM.setChecked(false);

            if (txtL != null) {
                giaSizeL[0] = Integer.parseInt(txtL.getText().toString());
                updateUI(txtM, txtL, txtSoluong, rdoM, rdoL, giaSizeM, giaSizeL, soLuong, txtProductPrice);
            }
        });

        addItem.setOnClickListener(v -> {
            soLuong[0]++;
            updateUI(txtM, txtL, txtSoluong, rdoM, rdoL, giaSizeM, giaSizeL, soLuong, txtProductPrice);
        });

        removeItem.setOnClickListener(v -> {
            if (soLuong[0] > 1) {
                soLuong[0]--;
                updateUI(txtM, txtL, txtSoluong, rdoM, rdoL, giaSizeM, giaSizeL, soLuong, txtProductPrice);
            }
        });

        btnUpdate.setOnClickListener(v -> {
            MyCartModel cartItem = list.get(position);
            updateCart(cartItem, txtM, txtL, rdoM, rdoL, giaSizeM, giaSizeL, soLuong, txtProductPrice, position);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateUI(
            TextView txtM, TextView txtL, TextView txtSoluong,
            RadioButton rdoM, RadioButton rdoL,
            int[] giaSizeM, int[] giaSizeL, int[] soLuong, TextView txtProductPrice) {

        if (txtM != null && txtL != null && giaSizeM != null && giaSizeL != null) {
            txtSoluong.setText(String.valueOf(soLuong[0]));

            int giaMoi = 0;

            String priceM = txtM.getText().toString();
            int PriceM = Integer.parseInt(priceM);
            String priceL = txtL.getText().toString();
            int PriceL = Integer.parseInt(priceL);

            if (rdoM.isChecked() && giaSizeM != null) {
                giaMoi = PriceM * soLuong[0];
            } else if (rdoL.isChecked() && giaSizeL != null) {
                giaMoi = PriceL * soLuong[0];
            }

            txtProductPrice.setText("Giá: " + giaMoi + "VNĐ");
        }
    }






    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cart_name, cart_totalprice, cart_size, cart_soluong;
        ImageView cart_img, edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_totalprice = itemView.findViewById(R.id.cart_price);
            cart_size = itemView.findViewById(R.id.cart_size);
            cart_img = itemView.findViewById(R.id.cart_img);
            cart_soluong = itemView.findViewById(R.id.cart_soluong);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
