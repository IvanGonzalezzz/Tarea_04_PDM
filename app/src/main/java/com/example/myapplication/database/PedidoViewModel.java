package com.example.myapplication.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class PedidoViewModel extends AndroidViewModel {

    private PedidoRepository repository;
    private LiveData<List<Pedido>> allPedidos;

    public PedidoViewModel(Application application) {
        super(application);
        repository = new PedidoRepository(application);
        allPedidos = repository.getAllPedidos();
    }

    public void insert(Pedido pedido) {
        repository.insert(pedido);
    }

    public LiveData<List<Pedido>> getAllPedidos() {
        return allPedidos;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public int getTotalPedidos() {
        return repository.getTotalPedidos();
    }
}