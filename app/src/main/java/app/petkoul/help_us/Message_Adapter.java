package app.petkoul.help_us;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.MyViewHolder> {

    private List<FriendlyMessage> friendlyMessageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView, messengerTextView;

        public MyViewHolder(View view) {
            super(view);
            messageTextView = (TextView) view.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) view.findViewById(R.id.messengerTextView);
        }
    }


    public Message_Adapter(List<FriendlyMessage> moviesList) {
        this.friendlyMessageList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FriendlyMessage message = friendlyMessageList.get(position);
        holder.messageTextView.setText(message.getText());
        holder.messengerTextView.setText(message.getName());
    }

    @Override
    public int getItemCount() {
        return friendlyMessageList.size();
    }
}
