package com.dastrix.myapplication.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dastrix.myapplication.Models.Cart;
import com.dastrix.myapplication.Models.Category;
import com.dastrix.myapplication.R;

import java.util.List;
import java.util.Objects;

public class CartItemsAdapter extends ArrayAdapter<Cart> {

    private final LayoutInflater layoutInflater;
    private final List<Cart> cartList;
    private final int layoutListRow;

    public CartItemsAdapter(@NonNull Context context, int resource, @NonNull List<Cart> objects) {
        super(context, resource, objects);

        cartList = objects;
        layoutListRow = resource;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(layoutListRow, null);

        Cart cart = cartList.get(position);

        if(cart != null) {
            final TextView productName = convertView.findViewById(R.id.productName);
            TextView amount = convertView.findViewById(R.id.amount);

            if(amount != null)
                amount.setText(String.valueOf(cart.getAmount()));

            if(productName != null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table = database.getReference("Category");

                table.child(String.valueOf(cart.getCategoryID())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Category category = snapshot.getValue(Category.class);
                        productName.setText(Objects.requireNonNull(category).getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }

        return convertView;
    }

}
