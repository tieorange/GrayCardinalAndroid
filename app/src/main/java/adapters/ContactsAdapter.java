package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieorange.graycardinal.app.R;

import java.util.List;

import models.Contact;


public class ContactsAdapter extends BaseAdapter {
    private List<Contact> mContact;
    private LayoutInflater mInflater = null;

    static class ViewHolder {
        ImageView photo;
        TextView name;
    }

    public ContactsAdapter(Context context, List<Contact> item) {
        this.mContact = item;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mContact.size();
    }

    @Override
    public Contact getItem(int position) {
        return mContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = mInflater.inflate(R.layout.list_raw_contact, null);

            viewHolder = new ViewHolder();
            viewHolder.photo = (ImageView) view.findViewById(R.id.list_raw_contact_photo);
            viewHolder.name = (TextView) view.findViewById(R.id.list_raw_contact_name);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Contact friend = mContact.get(position);

        if (friend != null) {
            viewHolder.name.setText(mContact.get(position).getName());

            Bitmap photo = mContact.get(position).getPhoto();
            if (photo == null) {
                viewHolder.photo.setVisibility(View.GONE);
            } else {
                viewHolder.photo.setVisibility(View.VISIBLE);
                viewHolder.photo.setImageBitmap(photo);
            }
        }
        return view;
    }

}
