package com.snixs.myapp2.Chat;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.snixs.myapp2.R;


import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private ArrayList<MessageObject> messageList;
    private FirebaseUser fuser;

    public MessageAdapter(ArrayList<MessageObject> messageList){
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, null, false);
            MessageAdapterViewHolder rcv = new MessageAdapterViewHolder(layoutView);
            return rcv;
        } else {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, null, false);
            MessageAdapterViewHolder rcv = new MessageAdapterViewHolder(layoutView);
            return rcv;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapterViewHolder holder, final int position) {
        if(!messageList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid()))
        holder.mSender.setText(messageList.get(position).getSenderName());
        holder.mMessage.setText(messageList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(messageList.get(position).getSenderId().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView mMessage,mSender;
        MessageAdapterViewHolder(View view){
            super(view);
            mMessage = view.findViewById(R.id.message);
            mSender = view.findViewById(R.id.sender);
        }
    }
}