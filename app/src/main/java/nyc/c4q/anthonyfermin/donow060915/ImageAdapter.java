package nyc.c4q.anthonyfermin.donow060915;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by c4q-anthonyf on 6/10/15.
 */
public class ImageAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> images;
    private static LayoutInflater inflater=null;
    public ImageAdapter(Context context, ArrayList<Bitmap> images) {
        // TODO Auto-generated constructor stub
        this.context=context;
        this.images =images;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView;
        rowView = inflater.inflate(R.layout.image_list, null);
        ImageView img=(ImageView) rowView.findViewById(R.id.imageView);
        img.setImageBitmap(images.get(position));

        return rowView;
    }

}