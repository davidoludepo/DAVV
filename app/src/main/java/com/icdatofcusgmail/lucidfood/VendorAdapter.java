package com.icdatofcusgmail.lucidfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DAVID OGUNDEPO on 03/27/2017.
 *
 *
 *
 * updated using ListViewGH still relevetant for checking all.........
 */

public class VendorAdapter extends BaseAdapter implements Filterable {

    private Context c;
    private ArrayList<Icdat> icdats;
    private ArrayList<Icdat> filterList;
    CustomFilter filter;
    private Map<Integer, Boolean> isCheckMap = new HashMap<>();
    private List<Map<String, String>> data;

    public VendorAdapter(Context c, ArrayList<Icdat> icdats) {
        this.c = c;
        this.icdats = icdats;

        configCheckMap(false);
    }

    public void configCheckMap(boolean bool) {
        for (int i = 0; i < icdats.size(); i++) {
            isCheckMap.put(i, bool);
        }
    }

    @Override
    public int getCount() {
        return icdats == null ? 0 : icdats.size();
    }

    @Override
    public Object getItem(int position) {
        return icdats.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return 0; 04/26/2017 with CheckBoxInListView-masterGH
        return icdats.indexOf(getItem(position));
    }

    @Override
    public Filter getFilter() {

        if (filter == null)
        {
            filter = new CustomFilter();
        }

        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0)
            {
                //CONSTRAINT TO UPPER
                constraint = constraint.toString().toUpperCase();
                ArrayList<Icdat> filters = new ArrayList<>();

                //FILTERING
                for (int i=0; i<filterList.size();i++)
                {
                    if (filterList.get(i).getFoodname().toUpperCase().contains(constraint))
                    {
                        Icdat icdat = new Icdat(filterList.get(i).getFoodname(), filterList.get(i).getFoodimage());
                        filters.add(icdat);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else
            {
                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            icdats = (ArrayList<Icdat>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.va_imagemodel, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textmodel);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imagemodel);
            holder.smooth = (com.icdatofcusgmail.lucidfood.SmoothCheckBox) convertView.findViewById(R.id.smoothie);

         //   holder.smooth.setOnCheckedChangeListener((VendorFragment) c);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.smooth.setTag(position);
        }
        Icdat icdat = icdats.get(position);

        boolean canRemove = icdat.isSelected();

        //Our Views
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imagemodel);
        TextView textView = (TextView) convertView.findViewById(R.id.textmodel);
        final SmoothCheckBox smooth = (SmoothCheckBox) convertView.findViewById(R.id.smoothie );
        smooth.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean isChecked) {
                isCheckMap.put(position, isChecked);
                if (isChecked){
                    Toast.makeText(c, "This is cooler", Toast.LENGTH_SHORT).show();
                    getCount();
                }
            }
        });

        //Set Data
        imageView.setImageResource(icdats.get(position).getFoodimage());
        textView.setText(icdats.get(position).getFoodname());
        smooth.setChecked(isCheckMap.get(position));
       // holder.smooth.setChecked(icdat.isSelected());
        holder.smooth.setTag(icdat);

        if (!canRemove) {
            smooth.setVisibility(View.GONE);
            smooth.setChecked(false);
        } else {
            smooth.setVisibility(View.VISIBLE);

            if (isCheckMap.get(position) == null)
                isCheckMap.put(position, false);
        }



        return convertView;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }


    public static class ViewHolder {
        public SmoothCheckBox smooth;
        public TextView textView;
        public ImageView imageView;
        public Object object = null;

    }

    public ArrayList<Icdat> getIcdats() {
        return icdats;
    }


}
