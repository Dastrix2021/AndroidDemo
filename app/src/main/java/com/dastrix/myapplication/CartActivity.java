package com.dastrix.myapplication;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dastrix.myapplication.Helpers.CartItemsAdapter;
import com.dastrix.myapplication.Helpers.JSONHelper;
import com.dastrix.myapplication.Models.Cart;
import com.dastrix.myapplication.Models.Order;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btnMakeOrder = findViewById(R.id.btnMakeOrder);
        listView = findViewById(R.id.shopping_cart);
        List<Cart> cartList = JSONHelper.importFromJSON(this);

        if(cartList != null) {
            CartItemsAdapter arrayAdapter = new CartItemsAdapter(CartActivity.this, R.layout.cart_item, cartList);
            listView.setAdapter(arrayAdapter);

            Toast.makeText(this, "Данные восстановлены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Не удалось подгрузить данные", Toast.LENGTH_SHORT).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table = database.getReference("Order");

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Cart> cartList = JSONHelper.importFromJSON(CartActivity.this);
                        if(cartList == null) {
                            Toast.makeText(CartActivity.this, "Корзина не сформирована", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String activeUser = com.dastrix.myapplication.SignIn.getDefaults("phone", CartActivity.this);
                        Order order = new Order(JSONHelper.createJSONString(cartList), activeUser);

                        long tsLong = System.currentTimeMillis() / 1000;

                        table.child(Long.toString(tsLong)).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                List<Cart> cartList = new ArrayList<>();
                                JSONHelper.exportToJSON(CartActivity.this, cartList);

                                CartItemsAdapter arrayAdapter = new CartItemsAdapter(CartActivity.this, R.layout.cart_item, cartList);
                                listView.setAdapter(arrayAdapter);

                                Toast.makeText(CartActivity.this, "Заказ сформирован", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}