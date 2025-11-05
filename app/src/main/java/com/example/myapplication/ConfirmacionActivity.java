package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class ConfirmacionActivity extends AppCompatActivity {

    private TextView tvResumenPedido, tvInfoCliente, tvDireccionEntrega, tvTotal;
    private Button btnConfirmarPedido;

    private Map<String, Integer> carrito = new HashMap<>();
    private Map<String, Double> precios = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);

        // Inicializar precios
        inicializarPrecios();

        // Inicializar vistas
        initViews();

        // Recibir datos
        recibirDatos();

        // Mostrar resumen
        mostrarResumenCompleto();

        btnConfirmarPedido.setOnClickListener(v -> confirmarPedido());
    }

    private void inicializarPrecios() {
        precios.put("Pizza Pepperoni", 249.0);
        precios.put("Hamburguesa Clásica", 169.0);
        precios.put("Tacos de Carne", 139.0);
        precios.put("Comida China", 199.0);
    }

    private void initViews() {
        tvResumenPedido = findViewById(R.id.tvResumenPedido);
        tvInfoCliente = findViewById(R.id.tvInfoCliente);
        tvDireccionEntrega = findViewById(R.id.tvDireccionEntrega);
        tvTotal = findViewById(R.id.tvTotal);
        btnConfirmarPedido = findViewById(R.id.btnConfirmarPedido);
    }

    private void recibirDatos() {
        Intent intent = getIntent();


        Bundle carritoBundle = intent.getBundleExtra("CARRITO");
        if (carritoBundle != null) {
            for (String key : precios.keySet()) {
                if (carritoBundle.containsKey(key)) {
                    carrito.put(key, carritoBundle.getInt(key));
                }
            }
        }
    }

    private void mostrarResumenCompleto() {
        Intent intent = getIntent();
        String nombreCliente = intent.getStringExtra("NOMBRE_CLIENTE");
        String direccion = intent.getStringExtra("DIRECCION");
        String ciudad = intent.getStringExtra("CIUDAD");
        String estado = intent.getStringExtra("ESTADO");
        String pais = intent.getStringExtra("PAIS");
        double total = intent.getDoubleExtra("TOTAL", 0.0);


        StringBuilder resumenPedido = new StringBuilder("Detalles del Pedido:\n\n");
        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            String producto = entry.getKey();
            int cantidad = entry.getValue();
            double precio = precios.get(producto);
            double subtotal = precio * cantidad;

            resumenPedido.append(String.format("• %s\n  Cantidad: %d - $%.2f c/u\n  Subtotal: $%.2f\n\n",
                    producto, cantidad, precio, subtotal));
        }
        tvResumenPedido.setText(resumenPedido.toString());


        String infoCliente = "Información del Cliente:\n" + nombreCliente;
        tvInfoCliente.setText(infoCliente);


        String direccionCompleta = "Dirección de Entrega:\n" + direccion + "\n" +
                ciudad + ", " + estado + "\n" + pais;
        tvDireccionEntrega.setText(direccionCompleta);


        tvTotal.setText(String.format("Total: $%.2f", total));
    }

    private void confirmarPedido() {

        String nombreCliente = getIntent().getStringExtra("NOMBRE_CLIENTE");

        Toast.makeText(this, "¡Pedido Confirmado! Gracias " + nombreCliente, Toast.LENGTH_LONG).show();


        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(ConfirmacionActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }, 3000);
    }
}