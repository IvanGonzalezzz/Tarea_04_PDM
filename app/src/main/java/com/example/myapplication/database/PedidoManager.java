package com.example.myapplication.database;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PedidoManager {

    private static final String PREFS_NAME = "FoodExpressPrefs";
    private static final String PEDIDOS_KEY = "pedidos";
    private static final String LAST_ID_KEY = "lastPedidoId";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public PedidoManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }


    public void savePedido(Pedido pedido) {
        List<Pedido> pedidos = getAllPedidos();


        if (pedido.getId() == 0) {
            int lastId = sharedPreferences.getInt(LAST_ID_KEY, 0);
            lastId++;
            pedido.setId(lastId);
            sharedPreferences.edit().putInt(LAST_ID_KEY, lastId).apply();
        }


        boolean exists = false;
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getId() == pedido.getId()) {
                pedidos.set(i, pedido);
                exists = true;
                break;
            }
        }


        if (!exists) {
            pedidos.add(pedido);
        }


        String pedidosJson = gson.toJson(pedidos);
        sharedPreferences.edit().putString(PEDIDOS_KEY, pedidosJson).apply();
    }


    public List<Pedido> getAllPedidos() {
        String pedidosJson = sharedPreferences.getString(PEDIDOS_KEY, "[]");
        Type type = new TypeToken<List<Pedido>>(){}.getType();
        List<Pedido> pedidos = gson.fromJson(pedidosJson, type);
        return pedidos != null ? pedidos : new ArrayList<>();
    }


    public Pedido getPedidoById(int id) {
        List<Pedido> pedidos = getAllPedidos();
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        return null;
    }


    public void deletePedido(int id) {
        List<Pedido> pedidos = getAllPedidos();
        List<Pedido> updatedPedidos = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getId() != id) {
                updatedPedidos.add(pedido);
            }
        }

        String pedidosJson = gson.toJson(updatedPedidos);
        sharedPreferences.edit().putString(PEDIDOS_KEY, pedidosJson).apply();
    }


    public void deleteAllPedidos() {
        sharedPreferences.edit().remove(PEDIDOS_KEY).apply();
        sharedPreferences.edit().putInt(LAST_ID_KEY, 0).apply();
    }


    public int getTotalPedidos() {
        return getAllPedidos().size();
    }
}