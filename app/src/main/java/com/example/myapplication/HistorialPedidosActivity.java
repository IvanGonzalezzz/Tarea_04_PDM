package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.database.Pedido;
import com.example.myapplication.database.PedidoViewModel;
import java.util.List;

public class HistorialPedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PedidoAdapter adapter;
    private PedidoViewModel pedidoViewModel;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        Log.d("FOOD_EXPRESS", "HistorialPedidosActivity - onCreate iniciado");

        try {

            setTitle("Historial de Pedidos");


            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Historial de Pedidos");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                Log.w("FOOD_EXPRESS", "ActionBar es null, usando setTitle de Activity");
            }


            recyclerView = findViewById(R.id.recyclerViewPedidos);
            tvEmpty = findViewById(R.id.tvEmpty);

            if (recyclerView == null) {
                Log.e("FOOD_EXPRESS", "recyclerView es NULL!");
                Toast.makeText(this, "Error: RecyclerView no encontrado", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }


            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new PedidoAdapter();
            recyclerView.setAdapter(adapter);


            pedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);


            pedidoViewModel.getAllPedidos().observe(this, new Observer<List<Pedido>>() {
                @Override
                public void onChanged(List<Pedido> pedidos) {
                    Log.d("FOOD_EXPRESS", "Observador de pedidos llamado. Pedidos: " +
                            (pedidos != null ? pedidos.size() : "null"));

                    if (adapter != null) {
                        adapter.setPedidos(pedidos != null ? pedidos : new java.util.ArrayList<>());
                    }

                    if (pedidos == null || pedidos.isEmpty()) {
                        Log.d("FOOD_EXPRESS", "No hay pedidos para mostrar");
                        if (tvEmpty != null) {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                        if (recyclerView != null) {
                            recyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        Log.d("FOOD_EXPRESS", "Mostrando " + pedidos.size() + " pedidos");
                        if (tvEmpty != null) {
                            tvEmpty.setVisibility(View.GONE);
                        }
                        if (recyclerView != null) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            Log.d("FOOD_EXPRESS", "HistorialPedidosActivity creada exitosamente");

        } catch (Exception e) {
            Log.e("FOOD_EXPRESS", "Error en onCreate de HistorialPedidosActivity: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar el historial: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}