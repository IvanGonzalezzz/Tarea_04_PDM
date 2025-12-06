package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.Pedido;
import com.example.myapplication.database.PedidoViewModel;

import java.util.HashMap;
import java.util.Map;

public class SegundaActivity extends AppCompatActivity {

    private EditText etCardNumber, etExpiration, etCVV, etCardholderName;
    private EditText etAddress, etPostalCode, etCity, etState, etCountry;
    private Button btnSaveCard;
    private TextView tvProductoInfo;

    private Map<String, Integer> carrito = new HashMap<>();
    private Map<String, Double> precios = new HashMap<>();

    private PedidoViewModel pedidoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);


        pedidoViewModel = new PedidoViewModel(getApplication());


        inicializarPrecios();

        initViews();

        recibirDatos();

        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormularioCompleto()) {
                    procesarPago();
                }
            }
        });

        Log.d("FOOD_EXPRESS", "SegundaActivity iniciada - Pantalla de pago");
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

            Log.d("FOOD_EXPRESS", "Carrito recibido con " + carrito.size() + " productos");
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

        resumen.append(String.format("\nTotal: $%.2f MXN", total));
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
        Log.w("FOOD_EXPRESS", "Error de validación: " + mensaje);
    }

    private void procesarPago() {
        String nombreTitular = etCardholderName.getText().toString();
        String direccion = etAddress.getText().toString();
        String ciudad = etCity.getText().toString();
        String estado = etState.getText().toString();
        String pais = etCountry.getText().toString();
        String codigoPostal = etPostalCode.getText().toString();

        double total = calcularTotal();

        guardarPedidoEnBD(nombreTitular, direccion, ciudad, estado, pais, codigoPostal, total);

        Intent intent = new Intent(SegundaActivity.this, ConfirmacionActivity.class);
        intent.putExtra("NOMBRE_CLIENTE", nombreTitular);
        intent.putExtra("DIRECCION", direccion);
        intent.putExtra("CIUDAD", ciudad);
        intent.putExtra("ESTADO", estado);
        intent.putExtra("PAIS", pais);
        intent.putExtra("CODIGO_POSTAL", codigoPostal);
        intent.putExtra("TOTAL", total);

        Bundle carritoBundle = new Bundle();
        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            carritoBundle.putInt(entry.getKey(), entry.getValue());
        }
        intent.putExtra("CARRITO", carritoBundle);

        startActivity(intent);

        Log.i("FOOD_EXPRESS", "Pago procesado para: " + nombreTitular + ", Total: $" + total);
    }

    private double calcularTotal() {
        double total = 0;
        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            total += precios.get(entry.getKey()) * entry.getValue();
        }
        return total;
    }

    private void guardarPedidoEnBD(String nombreCliente, String direccion, String ciudad,
                                   String estado, String pais, String codigoPostal, double total) {
        Pedido pedido = new Pedido();
        pedido.setClienteNombre(nombreCliente);
        pedido.setDireccion(direccion);
        pedido.setCiudad(ciudad);
        pedido.setEstado(estado);
        pedido.setPais(pais);
        pedido.setCodigoPostal(codigoPostal);
        pedido.setTotal(total);
        pedido.setProductos(new HashMap<>(carrito));
        pedido.setEstadoPedido("CONFIRMADO");

        pedidoViewModel.insert(pedido);

        Log.i("FOOD_EXPRESS", "Pedido guardado en SharedPreferences para: " + nombreCliente);
        Log.i("FOOD_EXPRESS", "Total pedidos guardados: " + pedidoViewModel.getTotalPedidos());
    }
}