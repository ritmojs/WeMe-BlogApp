package com.e.wemefinal.Data;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.wemefinal.Model.Blog;
import com.e.wemefinal.R;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
private Context context;
private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     Blog blog=blogList.get(position);
     String imageUrl=null;
     holder.title.setText(blog.getTitle());
     holder.desc.setText(blog.getDesc());
     holder.timestamp.setText(blog.getTimestamp());
     java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
     String formattedDate= dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
     holder.timestamp.setText(formattedDate);
     imageUrl=blog.getImage();
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       public TextView title;
       public TextView desc;
       public TextView timestamp;
       public ImageView image;
       String userid;
        public ViewHolder(@NonNull View view,Context ctx) {
            super(view);
            context=ctx;
            title=(TextView)view.findViewById(R.id.postTitleList);
            desc=(TextView)view.findViewById(R.id.postText);
            image=(ImageView)view.findViewById(R.id.postImageList);
            timestamp=(TextView)view.findViewById(R.id.timeStamp);
            userid=null;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
