package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/25/15.
 */
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PostCardAdapter extends RecyclerView.Adapter<PostCardAdapter.DataDetailsHolder> {
    private static String LOG_TAG = "PostCardAdapter";
    private ArrayList<DataDetails> myDataset = null;

    public static class DataDetailsHolder extends RecyclerView.ViewHolder {

        TextView postID;
        TextView title;
        TextView author;
        TextView time;
        TextView description;
        TextView content;
        TextView category;
        TextView views;
        TextView viewsDesc;
        TextView comment;
        TextView commentDesc;


        public DataDetailsHolder(View itemView) {
            super(itemView);

            postID = (TextView) itemView.findViewById(R.id.postID);
            title = (TextView) itemView.findViewById(R.id.title_text);
            author = (TextView) itemView.findViewById(R.id.author_text);
            time = (TextView) itemView.findViewById(R.id.date_text);
            description = (TextView) itemView.findViewById(R.id.description_text);
            content = (TextView) itemView.findViewById(R.id.content_text);
            category = (TextView) itemView.findViewById(R.id.category_text);
            views = (TextView) itemView.findViewById(R.id.viewCount_text);
            viewsDesc = (TextView) itemView.findViewById(R.id.viewDesc_text);
            comment = (TextView) itemView.findViewById(R.id.commentCount_text);
            commentDesc = (TextView) itemView.findViewById(R.id.commentCountDesc_text);

        }

    }

    public PostCardAdapter(ArrayList<DataDetails> myData) {
        this.myDataset = myData;
        if (myDataset == null){
            Log.i("myDataset content:", "EMPTY");
        } else {
            Log.i("myDataset content:", "FILLED");
        }

    }

    @Override
    public DataDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);

        DataDetailsHolder dataObjectHolder = new DataDetailsHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataDetailsHolder holder, int position) {

        holder.postID.setText(myDataset.get(position).getmPostID());
        holder.title.setText(myDataset.get(position).getmTitle());
        holder.author.setText(myDataset.get(position).getmAuthor());
        holder.time.setText(myDataset.get(position).getmTime());
        holder.description.setText(myDataset.get(position).getmPostDesc());
        holder.content.setText(myDataset.get(position).getmPostCont());
        holder.category.setText(myDataset.get(position).getmCategory());
        holder.views.setText(myDataset.get(position).getmViewsStr());
        holder.viewsDesc.setText(myDataset.get(position).getmViewsDesc());
        holder.comment.setText(myDataset.get(position).getmCommentsStr());
        holder.commentDesc.setText(myDataset.get(position).getmCommentsDesc());
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
