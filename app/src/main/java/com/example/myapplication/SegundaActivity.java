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

public class SegundaActivity extends AppCompatActivity {

    private EditText etCardNumber, etExpiration, etCVV, etCardholderName;
    private EditText etAddress, etPostalCode, etCity, etState, etCountry;
    private Button btnSaveCard;
    private TextView tvProductoInfo;

    private Map<String, Integer> carrito = new HashMap<>();
    private Map<String, Double> precios = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // Inicializar precios
        inicializarPrecios();

        // Inicializar vistas
        initViews();

        // Recibir datos del Intent
        recibirDatos();

        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormularioCompleto()) {
                    procesarPago();
                }
            }
        });
    }

    private void inicializarPrecios() {
        precios.put("Pizza Pepperoni", 249.0);
        precios.put("Hamburguesa Clásica", 169.0);
        precios.put("Tacos de Carne", 139.0);
        precios.put("Comida China", 199.0);
    }

    private void initViews() {
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiration = findViewById(R.id.etExpiration);
        etCVV = findViewById(R.id.etCVV);
        etCardholderName = findViewById(R.id.etCardholderName);
        etAddress = findViewById(R.id.etAddress);
        etPostalCode = findViewById(R.id.etPostalCode);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etCountry = findViewById(R.id.etCountry);
        btnSaveCard = findViewById(R.id.btnSaveCard);
        tvProductoInfo = findViewById(R.id.tvProductoInfo);
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
            mostrarResumenCarrito();
        }
    }

    private void mostrarResumenCarrito() {
        StringBuilder resumen = new StringBuilder("Tu Pedido:\n");
        double total = 0;

        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            String producto = entry.getKey();
            int cantidad = entry.getValue();
            double precio = precios.get(producto);
            double subtotal = precio * cantidad;
            total += subtotal;

            resumen.append(String.format("• %s x%d - $%.2f\n", producto, cantidad, subtotal));
        }

        resumen.append(String.format("\nTotal: $%.2f", total));
        tvProductoInfo.setText(resumen.toString());
        tvProductoInfo.setVisibility(View.VISIBLE);
    }

    private boolean validarFormularioCompleto() {
        if (!validarTarjeta()) return false;
        if (!validarDireccion()) return false;
        return true;
    }

    private boolean validarTarjeta() {
        if (etCardNumber.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa el número de tarjeta");
            return false;
        }
        if (etExpiration.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa la fecha de expiración");
            return false;
        }
        if (etCVV.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa el CVV");
            return false;
        }
        if (etCardholderName.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa el nombre del titular");
            return false;
        }
        return true;
    }

    private boolean validarDireccion() {
        if (etAddress.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa la dirección de entrega");
            return false;
        }
        if (etPostalCode.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa el código postal");
            return false;
        }
        if (etCity.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa la ciudad");
            return false;
        }
        if (etState.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa el estado");
            return false;
        }
        if (etCountry.getText().toString().trim().isEmpty()) {
            mostrarError("Ingresa el país");
            return false;
        }
        return true;
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void procesarPago() {

        String nombreTitular = etCardholderName.getText().toString();
        String direccion = etAddress.getText().toString();
        String ciudad = etCity.getText().toString();
        String estado = etState.getText().toString();
        String pais = etCountry.getText().toString();


        double total = calcularTotal();


        Intent intent = new Intent(SegundaActivity.this, ConfirmacionActivity.class);
        intent.putExtra("NOMBRE_CLIENTE", nombreTitular);
        intent.putExtra("DIRECCION", direccion);
        intent.putExtra("CIUDAD", ciudad);
        intent.putExtra("ESTADO", estado);
        intent.putExtra("PAIS", pais);
        intent.putExtra("TOTAL", total);


        Bundle carritoBundle = new Bundle();
        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            carritoBundle.putInt(entry.getKey(), entry.getValue());
        }
        intent.putExtra("CARRITO", carritoBundle);

        startActivity(intent);
    }

    private double calcularTotal() {
        double total = 0;
        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            total += precios.get(entry.getKey()) * entry.getValue();
        }
        return total;
    }
}