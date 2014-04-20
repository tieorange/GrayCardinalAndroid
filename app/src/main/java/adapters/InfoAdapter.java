package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tieorange.graycardinal.app.R;

import java.util.List;

import models.ContactInfo;


public class InfoAdapter extends BaseAdapter {
    private List<ContactInfo> mContactInfo;
    private LayoutInflater mInflater = null;

    static class ViewHolder {
        TextView name;
        TextView value;
    }

    public InfoAdapter(Context context, List<ContactInfo> items) {
        this.mContactInfo = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mContactInfo.size();
    }

    @Override
    public ContactInfo getItem(int position) {
        return mContactInfo.get(position);
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
            view = mInflater.inflate(R.layout.list_raw_contact_info, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.list_raw_info_name);
            viewHolder.value = (TextView) view.findViewById(R.id.list_raw_info_value);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ContactInfo info = mContactInfo.get(position);

        if (info != null) {
            viewHolder.name.setText(mContactInfo.get(position).getName());
            viewHolder.value.setText(mContactInfo.get(position).getValue());
        }
        return view;
    }

}
