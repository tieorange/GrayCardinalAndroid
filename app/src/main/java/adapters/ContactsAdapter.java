package adapters;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.tieorange.pember.app.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import models.Contact;
import tools.ContactsHelper;


public class ContactsAdapter extends ArrayAdapter {

    private List<Contact> mList, mListOriginal;
    private LayoutInflater mInflater = null;
    private Context mContext;

    public ContactsAdapter(Context context, List<Contact> item) {
        this.mList = item;
        this.mListOriginal = item;
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public Context getContext() {
        return mContext;
    }

    public List<Contact> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Contact getItem(int position) {
        return mList.get(position);
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
            viewHolder.relativeLayout = (RelativeLayout) view
                    .findViewById(R.id.list_raw_contact_relative_layout);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Contact friend = mList.get(position);

        if (friend != null) {
            viewHolder.name.setText(mList.get(position).getName());

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.name
                    .getLayoutParams();

            if (friend.getPhotoName() == null) {
                viewHolder.photo.setVisibility(View.GONE);
                params.setMargins(0, ContactsHelper.convertToPixels(10, mContext), 0, 0);
            } else {
                Bitmap photo = ContactsHelper
                        .loadBitmapFromStorage(friend.getPhotoName(), mContext);

                viewHolder.photo.setVisibility(View.VISIBLE);
                viewHolder.photo.setImageBitmap(photo);

                params.setMargins(0, ContactsHelper.convertToPixels(20, mContext), 0, 0);
            }
            viewHolder.name.setLayoutParams(params);
        }
        return view;
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence filterString) {
                FilterResults results = new FilterResults();
                // We implement here the filter logic
                if (filterString == null || filterString.toString()
                        .isEmpty()) {
                    // No filter implemented - we return all the list
                    //put the original list of contacts (prevent backspace bug)
                    mList = mListOriginal;
                    results.values = mList;
                    results.count = mList.size();
                } else {
                    // We perform filtering operation
                    List<Contact> contactsList = new ArrayList<Contact>();

                    for (Contact p : mListOriginal) {
                        if (p.getName().toUpperCase()
                                .contains(filterString.toString().toUpperCase())) {
                            contactsList.add(p);
                        }
                    }
                    results.values = contactsList;
                    results.count = contactsList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Now we have to inform the adapter about the new list filtered
                mList = (List<Contact>) results.values;
                notifyDataSetChanged();

            }
        };
    }


    static class ViewHolder {

        RelativeLayout relativeLayout;
        ImageView photo;
        TextView name;
    }

}
