package com.example.roomdb;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;


    public MainAdapter(Activity context,List<MainData> dataList){
        this.context=context;
        this.dataList=dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        MainData data=dataList.get(position);
        database=RoomDB.getInstance(context);
        holder.textView.setText(data.getText());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData d=dataList.get(holder.getAdapterPosition());
                final int sID=d.getID();
                String sText=d.getText();

                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);

                int width= WindowManager.LayoutParams.MATCH_PARENT;
                int height= WindowManager.LayoutParams.WRAP_CONTENT;


                dialog.getWindow().setLayout(width,height);
                dialog.show();

                final EditText editText=dialog.findViewById(R.id.editText);
                Button btnUpdate=dialog.findViewById(R.id.btnUpdate);

                editText.setText(sText);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String uText=editText.getText().toString().trim();
                        database.mainDao().update(sID,uText);

                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();
                    }
                });



            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData d=dataList.get(holder.getAdapterPosition());
                database.mainDao().delete(d);

                int position=holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,dataList.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView textView;
        ImageButton btnEdit,btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view);
            btnEdit=itemView.findViewById(R.id.bt_edit);
            btnDelete=itemView.findViewById(R.id.bt_delete);
        }
    }
}
