package com.dastrix.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dastrix.myapplication.Helpers.JSONHelper;
import com.dastrix.myapplication.Models.Cart;
import com.dastrix.myapplication.Models.Category;
import com.dastrix.myapplication.Models.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoodDetail extends AppCompatActivity {

    public static int ID = 0;
    private ImageView mainPhoto;
    private TextView foodMainName, price, foodFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_food_detail);

        mainPhoto = findViewById(R.id.mainPhoto);
        foodMainName = findViewById(R.id.foodMainName);
        price = findViewById(R.id.price);
        foodFullName = findViewById(R.id.foodFullName);
        Button btnGoToCart = findViewById(R.id.btnGoToCart);

        btnGoToCart.setOnClickListener(view -> startActivity(new Intent(FoodDetail.this, CartActivity.class)));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table = database.getReference("Category");

        table.child(String.valueOf(ID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = snapshot.getValue(Category.class);

                foodMainName.setText(Objects.requireNonNull(category).getName());

                int id = getApplicationContext().getResources().getIdentifier("drawable/" + category.getImage(), null, getApplicationContext().getPackageName());
                mainPhoto.setImageResource(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodDetail.this, "?????? ???????????????? ????????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        final DatabaseReference table_food = database.getReference("Food");
        table_food.child(String.valueOf(ID)).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food foodItem = snapshot.getValue(Food.class);

                price.setText(Objects.requireNonNull(foodItem).getPrice() + " ????????????");
                foodFullName.setText(foodItem.getFull_text());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodDetail.this, "?????? ???????????????? ????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btnAddToCart(View view) {
        List<Cart> cartList = JSONHelper.importFromJSON(this);
        if(cartList == null) {
            cartList = new ArrayList<>();
            cartList.add(new Cart(ID, 1));
        } else {
            boolean isFound = false;
            for(Cart el: cartList) {
                if(el.getCategoryID() == ID) {
                    el.setAmount(el.getAmount() + 1);
                    isFound = true;
                }
            }

            if(!isFound)
                cartList.add(new Cart(ID, 1));
        }

        boolean result = JSONHelper.exportToJSON(this, cartList);
        if(result) {
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
            Button btnCart = (Button) view;
            btnCart.setText("??????????????????");
        } else {
            Toast.makeText(this, "???? ??????????????????", Toast.LENGTH_SHORT).show();
        }
    }

}