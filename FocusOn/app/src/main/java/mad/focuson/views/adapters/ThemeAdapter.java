package mad.focuson.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ThemeAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Integer> themeImages;

    public ThemeAdapter(Context context, ArrayList<Integer> themeImages) {
        this.context = context;
        this.themeImages = themeImages;
    }

    @Override
    public int getCount() {
        return themeImages.size();
    }

    @Override
    public Object getItem(int position) {
        return themeImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(themeImages.get(position));
        return imageView;
    }
}