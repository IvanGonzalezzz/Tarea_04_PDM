package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etBuscar;
    private Button btnOrdenar1, btnOrdenar2, btnOrdenar3, btnOrdenar4, btnIr;
    private TextView tvCantidadPizza, tvCantidadHamburguesa, tvCantidadTacos, tvCantidadChina;

    // Carrito de compras
    private Map<String, Integer> carrito = new HashMap<>();
    private Map<String, Double> precios = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar precios MEXICANOS
        inicializarPrecios();

        // Inicializar vistas
        initViews();

        // Configurar listeners
        configurarListeners();

        // Actualizar contadores iniciales
        actualizarContadores();
    }

    private void inicializarPrecios() {
        precios.put("Pizza Pepperoni", 249.0);
        precios.put("Hamburguesa Clásica", 169.0);
        precios.put("Tacos de Carne", 139.0);
        precios.put("Comida China", 199.0);
    }

    private void initViews() {
        etBuscar = findViewById(R.id.etBuscar);
        btnOrdenar1 = findViewById(R.id.btnOrdenar1);
        btnOrdenar2 = findViewById(R.id.btnOrdenar2);
        btnOrdenar3 = findViewById(R.id.btnOrdenar3);
        btnOrdenar4 = findViewById(R.id.btnOrdenar4);
        btnIr = findViewById(R.id.btnIr);


        tvCantidadPizza = findViewById(R.id.tvCantidadPizza);
        tvCantidadHamburguesa = findViewById(R.id.tvCantidadHamburguesa);
        tvCantidadTacos = findViewById(R.id.tvCantidadTacos);
        tvCantidadChina = findViewById(R.id.tvCantidadChina);
    }

    private void configurarListeners() {
        btnOrdenar1.setOnClickListener(v -> ordenarProducto("Pizza Pepperoni", 249.0));
        btnOrdenar2.setOnClickListener(v -> ordenarProducto("Hamburguesa Clásica", 169.0));
        btnOrdenar3.setOnClickListener(v -> ordenarProducto("Tacos de Carne", 139.0));
        btnOrdenar4.setOnClickListener(v -> ordenarProducto("Comida China", 199.0));

        btnIr.setOnClickListener(v -> irACarrito());
    }

    private void ordenarProducto(String nombreProducto, double precio) {

        int cantidadActual = carrito.getOrDefault(nombreProducto, 0);
        carrito.put(nombreProducto, cantidadActual + 1);


        actualizarContadores();


        String mensaje = nombreProducto + " agregado\nTotal: " + (cantidadActual + 1);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void actualizarContadores() {

        tvCantidadPizza.setText("Cantidad: " + carrito.getOrDefault("Pizza Pepperoni", 0));
        tvCantidadHamburguesa.setText("Cantidad: " + carrito.getOrDefault("Hamburguesa Clásica", 0));
        tvCantidadTacos.setText("Cantidad: " + carrito.getOrDefault("Tacos de Carne", 0));
        tvCantidadChina.setText("Cantidad: " + carrito.getOrDefault("Comida China", 0));
    }

    private void irACarrito() {
        if (carrito.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(MainActivity.this, SegundaActivity.class);


        Bundle bundle = new Bundle();
        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            bundle.putInt(entry.getKey(), entry.getValue());
        }
        intent.putExtra("CARRITO", bundle);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        carrito.clear();
        actualizarContadores();
    }
}