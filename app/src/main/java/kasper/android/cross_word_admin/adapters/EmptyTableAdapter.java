package kasper.android.cross_word_admin.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import kasper.android.cross_word_admin.R;

public class EmptyTableAdapter extends RecyclerView.Adapter<EmptyTableAdapter.EmptyTableItemHolder> {

    private String[] tableData;
    public String[] getTableData() {
        return tableData;
    }

    private int tableSize;

    public EmptyTableAdapter(String[] tableData) {
        this.tableData = tableData;
        this.tableSize = (int)(Math.sqrt(tableData.length));
    }

    @Override
    public EmptyTableItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_empty_table, parent, false);

        itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, parent.getMeasuredHeight() / tableSize));

        return new EmptyTableItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EmptyTableItemHolder holder, final int position) {

        holder.contentET.setText(tableData[position]);
        holder.contentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tableData[holder.getAdapterPosition()] = s.charAt(0) + "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tableSize * this.tableSize;
    }

    class EmptyTableItemHolder extends RecyclerView.ViewHolder {

        EditText contentET;

        EmptyTableItemHolder(View itemView) {
            super(itemView);
            this.contentET = itemView.findViewById(R.id.adapter_empty_table_content_edit_text);
        }
    }
}