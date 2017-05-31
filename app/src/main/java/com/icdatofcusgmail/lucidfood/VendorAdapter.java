package com.icdatofcusgmail.lucidfood;

import android.content.Context;
import android.util.SparseBooleanArray;
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
 * updated using ListViewGH still relevant for checking all.........
 */

public class VendorAdapter extends BaseAdapter implements Filterable {

    private Context c;
    static ArrayList<Icdat> PhilFvsEithin = new ArrayList<>();
    private ArrayList<Icdat> icdats;
    private ArrayList<Icdat> filterList;
    private CustomFilter filter;
    private Map<Integer, Boolean> isCheckMap = new HashMap<>();
    private List<Map<String, String>> data;
    private SparseBooleanArray mSelectedItemsIds;

    public VendorAdapter(Context c,  ArrayList<Icdat> icdats) {
        super();
        mSelectedItemsIds = new SparseBooleanArray();
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
                        Icdat icdat = new Icdat(filterList.get(i).getFoodname(), filterList.get(i).getFoodimage(), filterList.get(i).getSmooth());
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.va_imagemodel, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textmodel);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imagemodel);
            holder.smooth = (SmoothCheckBox) convertView.findViewById(R.id.smoothie);

            //This gives toast if smoothie is clicked directly [i.e. not the image been selected,
            //will know after fooditems are parsed to listview in foodmenu activity

           /* holder.smooth.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(c, "This is cooler", Toast.LENGTH_SHORT).show();
                        getCount();
                    }
                }
            }); */

            convertView.setTag(holder);
            convertView.setTag(R.id.textmodel, holder.textView);
            convertView.setTag(R.id.imagemodel, holder.imageView);
            convertView.setTag(R.id.smoothie, holder.smooth);

        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.smooth.setTag(position);

        }
        final Icdat icdat = icdats.get(position);

        boolean canRemove = icdat.isSelected();

        //Our Views
        ImageView smile = (ImageView) convertView.findViewById(R.id.imagemodel);
        smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PhilFvsEithin.size() > 0 && PhilFvsEithin.contains(icdat))
                    Toast.makeText(c, "Already Added", Toast.LENGTH_SHORT).show();
                else
                    PhilFvsEithin.add(icdat);
            }
        });

        TextView laugh = (TextView) convertView.findViewById(R.id.textmodel);
        laugh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PhilFvsEithin.size() > 0 && PhilFvsEithin.contains(icdat))
                    Toast.makeText(c, "Already Added now", Toast.LENGTH_SHORT).show();
                else
                    PhilFvsEithin.add(icdat);
            }
        });

        SmoothCheckBox smoothCheckBox = (SmoothCheckBox) convertView.findViewById(R.id.smoothie);
        smoothCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean isChecked) {
                if (isChecked)
                    PhilFvsEithin.add(icdat);
                else
                    PhilFvsEithin.remove(icdat);
            }
        });


         //Set Data
        smile.setImageResource(icdats.get(position).getFoodimage());
        laugh.setText(icdats.get(position).getFoodname());
      //  smoothCheckBox.setChecked(true);
        holder.smooth.setTag(icdat);
//    smoothCheckBox.setChecked(icdats.get(position).getSmooth());


//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             //   setItemCheckBoxStatus(v, position);  Preventing 1 item selected, 2 items selected
//            }
//        });
        return convertView;
    }

    public void setItemCheckBoxStatus(View view, int position) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.smooth.toggle();
        isCheckMap.put(position, holder.smooth.isChecked());
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


    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }


    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

/*    public Map<Integer,Boolean> getIsSelected() {
        return isCheckMap;
    }

    public void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        this.isCheckMap = isSelected;
    }   */


}
