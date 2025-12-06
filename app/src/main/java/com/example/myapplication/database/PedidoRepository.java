package com.example.myapplication.database;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class PedidoRepository {

    private PedidoManager pedidoManager;
    private MutableLiveData<List<Pedido>> allPedidosLiveData;

    public PedidoRepository(Application application) {
        pedidoManager = new PedidoManager(application);
        allPedidosLiveData = new MutableLiveData<>();
        refreshPedidos();
    }

    public void insert(Pedido pedido) {
        pedidoManager.savePedido(pedido);
        refreshPedidos();
    }

    public LiveData<List<Pedido>> getAllPedidos() {
        return allPedidosLiveData;
    }

    public void deleteAll() {
        pedidoManager.deleteAllPedidos();
        refreshPedidos();
    }

    public int getTotalPedidos() {
        return pedidoManager.getTotalPedidos();
    }

    private void refreshPedidos() {
        List<Pedido> pedidos = pedidoManager.getAllPedidos();
        allPedidosLiveData.postValue(pedidos);
    }
}