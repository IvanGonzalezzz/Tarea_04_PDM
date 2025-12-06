package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.database.Pedido;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> pedidos;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.bind(pedido);
    }

    @Override
    public int getItemCount() {
        return pedidos != null ? pedidos.size() : 0;
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPedidoId, tvCliente, tvDireccion, tvTotal, tvEstado, tvFecha, tvProductos;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPedidoId = itemView.findViewById(R.id.tvPedidoId);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvProductos = itemView.findViewById(R.id.tvProductos);
        }

        public void bind(Pedido pedido) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            tvPedidoId.setText("Pedido #" + pedido.getId());
            tvCliente.setText("Cliente: " + pedido.getClienteNombre());
            tvDireccion.setText("Dirección: " + pedido.getDireccion() + ", " + pedido.getCiudad());
            tvTotal.setText(String.format("Total: $%.2f MXN", pedido.getTotal()));
            tvEstado.setText("Estado: " + pedido.getEstadoPedido());
            tvFecha.setText("Fecha: " + dateFormat.format(pedido.getFechaPedido()));

            // Mostrar productos
            StringBuilder productosStr = new StringBuilder("Productos:\n");
            Map<String, Integer> productos = pedido.getProductos();
            if (productos != null) {
                for (Map.Entry<String, Integer> entry : productos.entrySet()) {
                    productosStr.append("• ").append(entry.getKey())
                            .append(" x").append(entry.getValue()).append("\n");
                }
            }
            tvProductos.setText(productosStr.toString());
        }
    }
}