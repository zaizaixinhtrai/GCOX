package com.gcox.fansmeet.models;

import com.gcox.fansmeet.features.profile.ItemModelClassNewsFeed;

/**
 * Created by User on 9/20/2016.
 */
public class UserPostModel {

    private int Type;
    private ItemModelClassNewsFeed Post;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }



    public ItemModelClassNewsFeed getPost() {
        return Post;
    }

    public void setPost(ItemModelClassNewsFeed post) {
        Post = post;
    }

}
