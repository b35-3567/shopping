package com.example.shoping.DbHelper;

import com.example.shoping.model.ProductModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreHelper {

    private FirebaseFirestore db;
    private CollectionReference productsCollection;

    public FirestoreHelper() {
        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();
        // Tham chiếu đến bộ sưu tập "products"
       // productsCollection = db.collection("products");
    }

    // Phương thức để thêm một sản phẩm vào Firestore
  /* public void addProduct(ProductModel product) {
        // Thêm sản phẩm vào bộ sưu tập "products"
      productsCollection.add(product);
    }*/

}
