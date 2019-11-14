package com.gcox.fansmeet.features.profile.userprofile.gift;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.core.adapter.BaseRecyclerViewLoadMore;
import com.gcox.fansmeet.customview.CustomFontTextView;
import com.gcox.fansmeet.customview.CustomTypefaceSpan;
import com.gcox.fansmeet.util.ImageLoaderUtil;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanhBan on 12/20/2016.
 */

public class GiftRecyclerViewAdapter extends BaseRecyclerViewLoadMore<GiftRecyclerViewAdapter.SendGiftHolderVew, GiftItemModel> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
//    private ViewPager viewPager;

    private ArrayList<GiftItemModel> array_send;
    //    private int chosenPosition = -1;
    private int pageNumber;
    private int lastChosenPage = -1;

    private boolean itemTransparent = true;

    OnGiftSelectedListener mGiftSelectedListener;

    public GiftRecyclerViewAdapter(Context context, RecyclerView recyclerView, ArrayList<GiftItemModel> objects, int pageNumber) {
        super(recyclerView, objects);
        this.mContext = context;
//        this.viewPager = viewPager;
        this.array_send = objects;
        this.pageNumber = pageNumber;
        Timber.e("pageNumber ->" + pageNumber);
        insertMissingItems(array_send, pageNumber);
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void insertMissingItems(ArrayList<GiftItemModel> array_send, int pageNumber) {
        int requireItems = GiftPagerAdapter.MAX_ITEM_PER_PAGE;
        if (array_send.size() < requireItems) {
            for (int i = array_send.size(); i < requireItems; i++) {
                array_send.add(new GiftItemModel());
            }
        }
    }

    public void setBackgroundTransparent(boolean itemTransparent) {
        this.itemTransparent = itemTransparent;
    }

    @Override
    public SendGiftHolderVew onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.send_gift_row_gridview, parent, false);
        return new SendGiftHolderVew(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SendGiftHolderVew) {
            SendGiftHolderVew giftHolderVew = (SendGiftHolderVew) holder;
            int currentPossition = holder.getAdapterPosition();

            GiftItemModel itemModel = array_send.get(currentPossition);

            if (itemModel.getId() == null || TextUtils.isEmpty(itemModel.getId().toString())) {
                giftHolderVew.txt_bean.setVisibility(View.INVISIBLE);
                if (itemTransparent) {
                    giftHolderVew.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.live_gift_item_bg));
                } else {
                    giftHolderVew.itemView.setBackgroundColor(Color.WHITE);
                }
                giftHolderVew.itemView.setClickable(false);
                return;
            }
            ImageLoaderUtil.displayMediaImage(mContext, itemModel.getImage(), giftHolderVew.imv_gird_image);
            giftHolderVew.txt_bean.setText(String.valueOf(itemModel.getGem()));

            if (itemModel.isChoose()) {
                giftHolderVew.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.send_gift_image_bolder_choose));
            } else {
                giftHolderVew.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.send_gift_image_bolder));
            }

//            if(itemModel.getAmount() ==0){
//                if(itemModel.getGiftType()!=0){
//                    giftHolderVew.txt_bean.setVisibility(View.INVISIBLE);
//                }else {
//                    giftHolderVew.txt_bean.setVisibility(View.VISIBLE);
//                    giftHolderVew.txt_bean.setCustomDrawableStart(R.drawable.refill_gem_icon);
//                    giftHolderVew.txt_bean.setText(String.valueOf(itemModel.getCostBean()));
//                    if (itemTransparent) {
//                        giftHolderVew.txt_bean.setTextColor(ContextCompat.getColor(mContext, R.color.white));
//                    }
//                }
//
//            }else{
//                //free gift
//                giftHolderVew.txt_bean.setCompoundDrawables(null,null,null,null);
//                giftHolderVew.txt_bean.setText(getFreeText(itemModel.getAmount()));
//            }


            giftHolderVew.imv_gird_image.setOnClickListener(v -> {
                mGiftSelectedListener.onGiftItemSelected(itemModel, pageNumber, currentPossition);
                itemModel.setChoose(true);
                lastChosenPage = pageNumber;
                notifyItemChanged(currentPossition);
                Timber.e("notified changed %s", currentPossition);
            });
        }
    }

    private CharSequence getFreeText(int amount) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(formatString(mContext.getString(R.string.label_free), Color.parseColor("#ffd460"), Typeface.BOLD))
                .append(" ")
                .append(formatString(String.format("x%s", amount), itemTransparent ? Color.parseColor("#FFFFFF") : Color.parseColor("#9b9b9b"), Typeface.BOLD));
        return builder.subSequence(0, builder.length());
    }

    private SpannableString formatString(String text, int color, int style) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }

        SpannableString spannableString = new SpannableString(text);

        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), 0);
        spannableString.setSpan(new StyleSpan(style), 0, text.length(), 0);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/opensansbold.ttf");
        spannableString.setSpan(new CustomTypefaceSpan("", font), 0, text.length(), 0);
        return spannableString;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return array_send.size();
    }

    @Override
    public void handleItem(SendGiftHolderVew viewHolder, GiftItemModel item, int postiotn) {

    }

    public void setOnItemSelectedListener(OnGiftSelectedListener listener) {
        this.mGiftSelectedListener = listener;
    }

    public void unSelectItem(int previousSelection) {
        array_send.get(previousSelection).setChoose(false);
        notifyItemChanged(previousSelection);
    }

    public static class SendGiftHolderVew extends RecyclerView.ViewHolder {
        View itemView;
        ImageView imv_gird_image;
        CustomFontTextView txt_bean;
        FrameLayout flGiftItemContainer;

        public SendGiftHolderVew(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imv_gird_image = itemView.findViewById(R.id.imv_gird_image);
            txt_bean = itemView.findViewById(R.id.txt_bean);
            flGiftItemContainer = itemView.findViewById(R.id.flGiftItemContainer);
        }
    }

    public interface CompleteSendGift {
        void onSendSuccess(GiftItemModel ItemSend, long senderTotalBean, long senderTotalGold, long receiverTotalBean, long receiverTotalGoldFans, int votingScores, List<String> topFanList);
    }

    public interface OnGiftSelectedListener {
        void onGiftItemSelected(GiftItemModel item, int pageNumber, int position);
    }
}


