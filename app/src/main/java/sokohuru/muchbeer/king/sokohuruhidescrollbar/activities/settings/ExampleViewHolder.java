package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

public class ExampleViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvText;

    public ExampleViewHolder(View itemView) {
        super(itemView);

        tvText = (TextView) itemView.findViewById(R.id.tvText);
    }

    public void bind(ExampleModel model) {
        tvText.setText(model.getText());
    }
}
