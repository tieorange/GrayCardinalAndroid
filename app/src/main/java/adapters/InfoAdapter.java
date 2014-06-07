package adapters;

import com.tieorange.pember.app.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import models.ContactInfo;


public class InfoAdapter extends BaseAdapter {

    private List<ContactInfo> mList, mListOriginal;
    private LayoutInflater mInflater = null;
    private Context mContext;

    public InfoAdapter(Context context, List<ContactInfo> items) {
        this.mContext = context;
        this.mList = items;
        this.mListOriginal = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public List<ContactInfo> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ContactInfo getItem(int position) {
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
            view = mInflater.inflate(R.layout.list_raw_contact_info, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.list_raw_info_name);
            viewHolder.value = (TextView) view.findViewById(R.id.list_raw_info_value);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ContactInfo info = mList.get(position);

        if (info != null) {
            viewHolder.name.setText(mList.get(position).getName());

            if (info.getValue() == null) {
                viewHolder.value
                        .setText(mContext.getResources().getText(R.string.enter_some_info_here));
            } else {
                viewHolder.value
                        .setText(mList.get(position).getValue());
            }
        }
        return view;
    }

    public Filter getFilter() {
        return new Filter() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
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
                    List<ContactInfo> contactInfoList = new ArrayList<ContactInfo>();

                    for (ContactInfo p : mListOriginal) {
                        if (p.getName().toUpperCase()
                                .contains(filterString.toString().toUpperCase())
                                || p.getValue().toUpperCase()
                                .contains(filterString.toString().toUpperCase())) {
                            contactInfoList.add(p);
                        }
                    }
                    results.values = contactInfoList;
                    results.count = contactInfoList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Now we have to inform the adapter about the new list filtered
                mList = (List<ContactInfo>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    static class ViewHolder {

        TextView name;
        TextView value;
    }

}
