package com.example.fitnotes.set;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnotes.R;
import com.example.fitnotes.exercise.ExerciseItem;
import com.example.fitnotes.workout.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private Context context;
    private List<SetItem> setList;
    private SetItemChangeListener itemChangeListener;

    public void setItemChangeListener(SetItemChangeListener listener) {
        this.itemChangeListener = listener;
    }
    public void removeSetItem(SetItem setItem) {
        int position = setList.indexOf(setItem);
        if (position != -1) {
            setList.remove(position);
            notifyItemRemoved(position);

            for (int i = position; i < setList.size(); i++) {
                notifyItemChanged(i); // Notify to rebind the view
            }
        }
    }
    public void addSet(SetItem newItem) {
        setList.add(newItem);
        notifyItemInserted(setList.size() - 1);
    }
    public void setSetList(List<SetItem> setList){
        this.setList = setList;
        notifyDataSetChanged();
    }
    public List<SetItem> getSetList(){
        return setList;
    }
    public SetAdapter(Context context) {
        this.context = context;
        this.setList = new ArrayList<>();
    }
    @NonNull
    @Override
    public SetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_sets, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetAdapter.ViewHolder holder, int position) {
        SetItem set = setList.get(position);
        holder.textView_setNumber.setText(String.valueOf(position + 1));

        holder.editText_rep.setTag(position);

        holder.editText_rep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for your implementation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int adapterPosition = (int) holder.editText_rep.getTag();

                // Not needed for your implementation
                String newValueStr = charSequence.toString();
                try {
                    int newValue = Integer.parseInt(newValueStr);
                    if (adapterPosition >= 0 && adapterPosition < setList.size()) {

                        setList.get(adapterPosition).setStoredRep(newValue);
                        itemChangeListener.onRepValueChanged(adapterPosition, newValue);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid input if needed
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int adapterPosition = (int) holder.editText_rep.getTag();

                String newValueStr = editable.toString();
                try {
                    int newValue = Integer.parseInt(newValueStr);
                    if (adapterPosition >= 0 && adapterPosition < setList.size()) {

                        setList.get(adapterPosition).setStoredRep(newValue);
                        itemChangeListener.onRepValueChanged(adapterPosition, newValue);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid input if needed
                }
            }
        });

        holder.editText_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for your implementation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int adapterPosition = (int) holder.editText_rep.getTag();

                String newValueStr = charSequence.toString();
                try {
                    double newValue = Double.parseDouble(newValueStr);

                    if (adapterPosition >= 0 && adapterPosition < setList.size()) {

                        setList.get(adapterPosition).setStoredWeight(newValue);
                        itemChangeListener.onWeightValueChanged(adapterPosition, newValue);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid input if needed
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int adapterPosition = (int) holder.editText_rep.getTag();
                String newValueStr = editable.toString();
                try {
                    double newValue = Double.parseDouble(newValueStr);
                    if (adapterPosition >= 0 && adapterPosition < setList.size()) {

                        setList.get(adapterPosition).setStoredWeight(newValue);
                        itemChangeListener.onWeightValueChanged(adapterPosition, newValue);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid input if needed
                }
            }
        });

        holder.editText_rep.setText(String.valueOf(set.getStoredRep()));
        holder.editText_weight.setText(String.valueOf(set.getStoredWeight()));

    }

    @Override
    public int getItemCount() {
        return setList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_setNumber;
        EditText editText_rep, editText_weight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editText_rep = itemView.findViewById(R.id.editText_rep);
            editText_weight = itemView.findViewById(R.id.editText_weight);
            textView_setNumber = itemView.findViewById(R.id.textView_setNumber);

        }
    }
}
