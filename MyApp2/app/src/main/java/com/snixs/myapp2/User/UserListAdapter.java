package com.snixs.myapp2.User;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snixs.myapp2.R;
import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private ArrayList<UserObject> userList;
    private OnUserListener mOnUserListener;

    public UserListAdapter(ArrayList<UserObject> userList, OnUserListener onUserListener){
        this.userList = userList;
        this.mOnUserListener = onUserListener;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        UserListViewHolder rcv = new UserListViewHolder(layoutView, mOnUserListener);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, final int position) {
        holder.mName.setText(userList.get(position).getName());
        holder.mPhone.setText(userList.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }



    class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName, mPhone;
        LinearLayout mLayout;

        OnUserListener onUserListener;

        public UserListViewHolder(View view, OnUserListener onUserListener){
            super(view);
            mName = view.findViewById(R.id.name);
            mPhone = view.findViewById(R.id.phone);
            mLayout = view.findViewById(R.id.userLayout);
            this.onUserListener = onUserListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onUserListener.OnUserClick(getAdapterPosition());
        }
    }

    public interface OnUserListener{
        void OnUserClick(int position);
    }
}