package com.example.socialapp.ui.home;

// HomeFragment.java
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialapp.AddPostActivity;
import com.example.socialapp.Post;
import com.example.socialapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int REQUEST_ADD_POST = 1;

    private List<Post> postList;
    private ArrayAdapter<Post> postAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        postList = new ArrayList<>();
        postAdapter = new ArrayAdapter<Post>(requireContext(), R.layout.post_item, R.id.postTextView, postList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View itemView = super.getView(position, convertView, parent);

                ImageView imageView = itemView.findViewById(R.id.postImageView);
                TextView usernameTextView = itemView.findViewById(R.id.usernameTextView);
                TextView captionTextView = itemView.findViewById(R.id.captionTextView);

                Post post = getItem(position);

                if (post != null) {
                    if (post.getImageData() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(post.getImageData(), 0, post.getImageData().length);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageBitmap(null);
                    }

                    usernameTextView.setText(post.getUserEmail());
                    captionTextView.setText(post.getCaption());
                }

                return itemView;
            }
        };

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(postAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddPost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), AddPostActivity.class);
                startActivityForResult(intent, REQUEST_ADD_POST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_POST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Post newPost = (Post) data.getSerializableExtra("newPost");
                postList.add(newPost);
                postAdapter.notifyDataSetChanged();
            }
        }
    }
}
