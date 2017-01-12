package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.learning;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

import static android.R.attr.description;
import static com.google.android.gms.internal.zzng.fa;
import static sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.drawable.ukawa;

/**
 * Created by muchbeer on 1/12/2017.
 */

public class UkawaAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY_BREAKNEWS = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView titleView;
        public final TextView ukawaId;
        public final TextView ukawaLikes;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            titleView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            ukawaId = (TextView) view.findViewById(R.id.list_item_id_textview);
            ukawaLikes = (TextView) view.findViewById(R.id.list_item_ukawa_likes_textview);
        }
    }
    public UkawaAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_TODAY_BREAKNEWS: {
                layoutId = R.layout.list_item_breaknews_today;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item_forecast_json;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
   //     ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Use placeholder image for now
        viewHolder.iconView.setImageResource(R.drawable.ic_launcher);

        // Read date from cursor
        String dateString = cursor.getString(ForecastFragment.COL_UKAWA_DATE);
        // Find TextView and set formatted date on it
        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateString));
      //  viewHolder.dateView.setText("dateString");
     //   Log.v("Check date", dateString);


        // Read weather forecast from cursor
        String ukawatitle = cursor.getString(ForecastFragment.COL_UKAWA_TITLE);
        // Find TextView and set weather forecast on it
        viewHolder.titleView.setText(ukawatitle);

        // Read weather forecast from cursor
        String ukawaId = cursor.getString(ForecastFragment.COL_UKAWA_ID);
        // Find TextView and set weather forecast on it
        String stringUkawaId = String.valueOf(ukawaId);
        viewHolder.ukawaId.setText(ukawaId);

        // Read weather forecast from cursor
        String ukawalikes = cursor.getString(ForecastFragment.COL_UKAWA_LIKE_VIEW);
        // Find TextView and set weather forecast on it
        viewHolder.ukawaLikes.setText(ukawalikes);

    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY_BREAKNEWS : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
