package com.gcox.fansmeet.features.editvideo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.util.ImageLoaderUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 贴图列表Adapter
 */
public class StickerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String DEFAULT_PATH = "assets://";
    private Context mContext;
    private ImageClick mImageClick = new ImageClick();
    private List<String> mImagePathList = new ArrayList<String>();// 图片路径列表
    private OnStickerItemClick mOnStickerItemClick;

    public StickerAdapter(Context context) {
        super();
        mContext = context;
    }

    public void setOnStickerItemClick(OnStickerItemClick itemClick) {
        mOnStickerItemClick = itemClick;
    }

    public class ImageHolder extends ViewHolder {
        public ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.sticker_img);
        }
    }

    @Override
    public int getItemCount() {
        return mImagePathList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.sticker_item, parent, false);
        ImageHolder holer = new ImageHolder(v);
        return holer;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageHolder imageHoler = (ImageHolder) holder;
        String path = mImagePathList.get(position);
        ImageLoaderUtil.displayUserImage(mContext, DEFAULT_PATH + path, imageHoler.image, false, null);
        imageHoler.image.setTag(path);
        imageHoler.image.setOnClickListener(mImageClick);
    }

    public void addStickerImages(String folderPath) {
        mImagePathList.clear();
        try {
            String[] files = mContext.getAssets()
                    .list(folderPath);
            for (String name : files) {
                mImagePathList.add(folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }

    /**
     * 选择贴图
     */
    private final class ImageClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            String data = (String) v.getTag();
            if (mOnStickerItemClick != null) {
                mOnStickerItemClick.selectedStickerItem(data);
            }
        }
    }

    public interface OnStickerItemClick {
        void selectedStickerItem(String path);
    }
}
