package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;


/**
 * Created by muchbeer on 6/19/2015.
 */
public class AdapterSoko extends RecyclerView.Adapter<AdapterSoko.ViewHolderSokoni> {


    private final LayoutInflater layoutInflater;
    private final VolleySingleton volleySingleTon;
    private final ImageLoader imageLoader;
    private int SHARE_CODE = 1;

    private Filter planetFilter;

    private ArrayList<Soko> planetList;
    public ArrayList<Soko> origPlanetList = new ArrayList<>();
    private ArrayList<Soko> slistSokoni = new ArrayList<>();
    private static Context context;
    private static ClickListener clickListener;
    private String URL_FOR_NULL = "http://sokouhuru.com/ukawa/loag.jpg";
    private CharSequence SOKOHURU = "sokouhuru";


    public AdapterSoko(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        volleySingleTon = VolleySingleton.getsInstance();
        imageLoader = volleySingleTon.getImageLoader();

       // L.t(context, "message");
    }

    public void setSokoList(ArrayList<Soko> listSokoni) {
        this.slistSokoni = listSokoni;
        notifyItemRangeChanged(0, listSokoni.size());
        this.origPlanetList = listSokoni;
    }

    @Override
    public ViewHolderSokoni onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =  layoutInflater.inflate(R.layout.fragment_rais_feel, parent, false);
        ViewHolderSokoni viewHolderSokoni = new ViewHolderSokoni(view);


        return viewHolderSokoni;
    }

    @Override
    public void onBindViewHolder(final ViewHolderSokoni holder, int position) {
        Soko currentItem = slistSokoni.get(position);

        holder.sokoTitle.setText(currentItem.getName());
        holder.sokoDate.setText(currentItem.getPostdate());
 holder.sokoMoto.setText(currentItem.getTitle());

        //Soko Huru detail

        String urlThumbnail = currentItem.getImage();
        String urlThumbnailReplacement = URL_FOR_NULL;
        if(urlThumbnail.length() > 10) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean b) {
                    holder.sokoThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    // L.t(context, volleyError.toString());
                    // holder.sokoThumbnail.setImageBitmap();
                }
            });
        }
else {
           // img.setImageBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.chen_gong));

            imageLoader.get(urlThumbnailReplacement, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean b) {
                    holder.sokoThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    // L.t(context, volleyError.toString());
                    // holder.sokoThumbnail.setImageBitmap();
                }
            });
        }


  }

public void setClickListener(ClickListener clickListener) {
    this.clickListener = clickListener;
}


    @Override
    public int getItemCount() {

        return slistSokoni.size();
    }



        public static class ViewHolderSokoni extends RecyclerView.ViewHolder implements View.OnClickListener{

       private static final int SHARING_CODE = 1;
       public ImageView sokoThumbnail;
        public TextView sokoTitle;
        public TextView sokoDate;
            public TextView  sokoMoto;
            public TextView sokoContact;
     //  private ClickListener clickListener;


       public ViewHolderSokoni(View itemView) {
            super(itemView);
                
            sokoThumbnail = (ImageView) itemView.findViewById(R.id.image);
            sokoTitle = (TextView) itemView.findViewById(R.id.ukawaUser);
            sokoDate = (TextView) itemView.findViewById(R.id.ukawaDate);
            sokoMoto = (TextView) itemView.findViewById(R.id.txtTitleBest);



            itemView.setOnClickListener(this);


        }


       @Override
       public void onClick(View view) {
         //  Intent startIntent = new Intent(context, MainActivityDetail.class);

         //  ((MainActivity)context).startActivityForResult(i, SHARING_CODE );

            if(clickListener!=null) {
                clickListener.itemClicked(view, getPosition());

            }
       }


   }
public interface ClickListener{

    public void itemClicked(View view, int position);
}

    public interface ClickListenerSearch {
        public void itemClicked2(View view, int positionSearch);
    }


}
