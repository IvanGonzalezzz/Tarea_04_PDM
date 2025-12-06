package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText etBuscar;
    private Button btnOrdenar1, btnOrdenar2, btnOrdenar3, btnOrdenar4, btnIr;
    private TextView tvCantidadPizza, tvCantidadHamburguesa, tvCantidadTacos, tvCantidadChina;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;


    private Map<String, Integer> carrito = new HashMap<>();
    private Map<String, Double> precios = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FoodExpress");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        inicializarPrecios();

        initViews();

        configurarListeners();

        actualizarContadores();

        Log.d("FOOD_EXPRESS", "MainActivity iniciada");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_cart) {
            vaciarCarrito();
            Log.d("FOOD_EXPRESS", "Usuario presionó: Vaciar Carrito");
            return true;
        }
        else if (id == R.id.action_view_orders) {
            verPedidos();
            Log.d("FOOD_EXPRESS", "Usuario presionó: Ver Pedidos");
            return true;
        }
        else if (id == R.id.action_help) {
            mostrarAyuda();
            Log.d("FOOD_EXPRESS", "Usuario presionó: Ayuda");
            return true;
        }
        else if (id == R.id.action_settings) {
            mostrarConfiguracion();
            Log.d("FOOD_EXPRESS", "Usuario presionó: Configuración");
            return true;
        }
        else if (id == R.id.action_about) {
            mostrarAcercaDe();
            Log.d("FOOD_EXPRESS", "Usuario presionó: Acerca de");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Log.d("FOOD_EXPRESS", "Navigation Drawer: Inicio seleccionado");
            Toast.makeText(this, "Ya estás en la página principal", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_my_orders) {
            verMisPedidos();
            Log.d("FOOD_EXPRESS", "Navigation Drawer: Mis Pedidos seleccionado");
        }
        else if (id == R.id.nav_favorites) {
            verFavoritos();
            Log.d("FOOD_EXPRESS", "Navigation Drawer: Favoritos seleccionado");
        }
        else if (id == R.id.nav_profile) {
            editarPerfil();
            Log.d("FOOD_EXPRESS", "Navigation Drawer: Mi Perfil seleccionado");
        }
        else if (id == R.id.nav_addresses) {
            verDirecciones();
            Log.d("FOOD_EXPRESS", "Navigation Drawer: Mis Direcciones seleccionado");
        }
        else if (id == R.id.nav_payment) {
            verMetodosPago();
            Log.d("FOOD_EXPRESS", "Navigation Drawer: Métodos de Pago seleccionado");
        }
        else if (id == R.id.nav_help) {
            mostrarAyuda();
            Log.d("FOOD_EXPRESS", "Navigation Drawer: Centro de Ayuda seleccionado");
        }
        else if (id == R.id.nav_contact) {
            contactarSoporte();
            Log.d("FOOD_EXPRESS", "Navigation Drawer: Contáctanos seleccionado");
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        Log.d("FOOD_EXPRESS", "Producto agregado: " + nombreProducto + ", Cantidad: " + (cantidadActual + 1));

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
            Log.d("FOOD_EXPRESS", "Intento de ver carrito vacío");
            return;
        }

        Log.d("FOOD_EXPRESS", "Navegando a carrito con " + carrito.size() + " productos");

        Intent intent = new Intent(MainActivity.this, SegundaActivity.class);

        Bundle bundle = new Bundle();
        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            bundle.putInt(entry.getKey(), entry.getValue());
        }
        intent.putExtra("CARRITO", bundle);

        startActivity(intent);
    }

    private void vaciarCarrito() {
        carrito.clear();
        actualizarContadores();
        Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show();
        Log.i("FOOD_EXPRESS", "Carrito vaciado por el usuario");
    }

    private void verPedidos() {
        Intent intent = new Intent(MainActivity.this, HistorialPedidosActivity.class);
        startActivity(intent);
        Log.i("FOOD_EXPRESS", "Navegando a Historial de Pedidos");
    }

    private void mostrarAyuda() {
        Toast.makeText(this, "Centro de Ayuda - En desarrollo", Toast.LENGTH_SHORT).show();
    }

    private void mostrarConfiguracion() {
        Toast.makeText(this, "Configuración - En desarrollo", Toast.LENGTH_SHORT).show();
    }

    private void mostrarAcercaDe() {
        Toast.makeText(this, "FoodExpress v1.0 - Aplicación de pedidos de comida", Toast.LENGTH_LONG).show();
    }

    private void verMisPedidos() {
        Intent intent = new Intent(MainActivity.this, HistorialPedidosActivity.class);
        startActivity(intent);
    }

    private void verFavoritos() {
        Toast.makeText(this, "Favoritos - En desarrollo", Toast.LENGTH_SHORT).show();
    }

    private void editarPerfil() {
        Toast.makeText(this, "Editar Perfil - En desarrollo", Toast.LENGTH_SHORT).show();
    }

    private void verDirecciones() {
        Toast.makeText(this, "Mis Direcciones - En desarrollo", Toast.LENGTH_SHORT).show();
    }

    private void verMetodosPago() {
        Toast.makeText(this, "Métodos de Pago - En desarrollo", Toast.LENGTH_SHORT).show();
    }

    private void contactarSoporte() {
        Toast.makeText(this, "Soporte Técnico - En desarrollo", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (carrito != null && !carrito.isEmpty()) {
            Log.d("FOOD_EXPRESS", "Carrito limpiado al regresar de compra exitosa");
            carrito.clear();
            actualizarContadores();
        }
    }
}