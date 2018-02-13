package app.petkoul.help_us;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adapter_Custom_ListView_Invite extends ArrayAdapter<User> {

    private ArrayList<User> dataSet;
    Context mContext;



    // View lookup cache
    private static class ViewHolder {
        TextView name_surname;
        TextView email;
        Button invite_btn;
    }

    public Adapter_Custom_ListView_Invite(ArrayList<User> data, Context context) {
        super(context, R.layout.invite_item, data);
        this.dataSet = data;
        this.mContext=context;

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.invite_item, parent, false);
            viewHolder.name_surname = (TextView) convertView.findViewById(R.id.name_surname);
            viewHolder.email = (TextView) convertView.findViewById(R.id.email);
            viewHolder.invite_btn = (Button) convertView.findViewById(R.id.invite_btn);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        viewHolder.name_surname.setText(dataModel.getName()+" "+dataModel.getSurname());
        viewHolder.email.setText(dataModel.getEmail());
        viewHolder.invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(),"Invited user ",Toast.LENGTH_SHORT).show();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}