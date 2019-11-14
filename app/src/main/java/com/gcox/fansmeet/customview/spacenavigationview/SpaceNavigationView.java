/*
 * Space Navigation library for Android
 * Copyright (c) 2016 Arman Chatikyan (https://github.com/armcha/Space-Navigation-View).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gcox.fansmeet.customview.spacenavigationview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.features.main.AppNavigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpaceNavigationView extends RelativeLayout {

    private static final String TAG = "SpaceNavigationView";

    private static final String CURRENT_SELECTED_ITEM_BUNDLE_KEY = "currentItem";

    private static final String BADGES_ITEM_BUNDLE_KEY = "badgeItem";

    private static final String CHANGED_ICON_AND_TEXT_BUNDLE_KEY = "changedIconAndText";

    private static final String CENTER_BUTTON_ICON_KEY = "centerButtonIconKey";

    private static final String CENTER_BUTTON_COLOR_KEY = "centerButtonColorKey";

    private static final String SPACE_BACKGROUND_COLOR_KEY = "backgroundColorKey";

    private static final String BADGE_FULL_TEXT_KEY = "badgeFullTextKey";

    private static final String VISIBILITY = "visibilty";

    private static final int NOT_DEFINED = -777; //random number, not - 1 because it is Color.WHITE

    private static final int MAX_SPACE_ITEM_SIZE = 4;

    private static final int MIN_SPACE_ITEM_SIZE = 2;
    private final int spaceNavigationHeight = (int) getResources().getDimension(R.dimen.space_navigation_height);
    private final int mainContentHeight = (int) getResources().getDimension(R.dimen.main_content_height);
    private int centerContentWidth = (int) getResources().getDimension(R.dimen.center_content_width);
    //    private final int centerButtonSize = (int) getResources().getDimension(R.dimen.space_center_button_default_size);
    private List<SpaceItem> spaceItems = new ArrayList<>();
    private List<View> spaceItemList = new ArrayList<>();
    private List<RelativeLayout> badgeList = new ArrayList<>();
    private HashMap<Integer, Object> badgeSaveInstanceHashMap = new HashMap<>();
    private HashMap<Integer, SpaceItem> changedItemAndIconHashMap = new HashMap<>();
    private SpaceOnClickListener spaceOnClickListener;
    private SpaceOnLongClickListener spaceOnLongClickListener;
    private Bundle savedInstanceState;
    private CenterButton centerButton;
    private RelativeLayout centerBackgroundView;
    private LinearLayout leftContent, rightContent, cencelContent;
    private BezierView centerContent;
    private Typeface customFont;
    private Context context;
    private int spaceItemIconSize = NOT_DEFINED;

    private int spaceItemIconOnlySize = NOT_DEFINED;

    private int spaceItemTextSize = NOT_DEFINED;

    private int spaceBackgroundColor = NOT_DEFINED;

    private int centerButtonColor = NOT_DEFINED;

    private int activeCentreButtonIconColor = NOT_DEFINED;

    private int inActiveCentreButtonIconColor = NOT_DEFINED;

    private int activeCentreButtonBackgroundColor = NOT_DEFINED;

    private int centerButtonIcon = NOT_DEFINED;

    private int activeSpaceItemColor = NOT_DEFINED;

    private int inActiveSpaceItemColor = NOT_DEFINED;

    private int centerButtonRippleColor = NOT_DEFINED;

    private int centerButtonSize = NOT_DEFINED;
    private int currentSelectedItem = 0;

    private int contentWidth;

    private boolean isCenterButtonSelectable = false;

    private boolean isCentrePartLinear = false;

    private boolean isTextOnlyMode = false;

    private boolean isIconOnlyMode = false;

    private boolean isCustomFont = false;

    private boolean isCentreButtonIconColorFilterEnabled = true;

    private boolean shouldShowBadgeWithNinePlus = true;

    private int centerButtonElevation = NOT_DEFINED;

    /**
     * Constructors
     */
    public SpaceNavigationView(Context context) {
        this(context, null);
    }

    public SpaceNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpaceNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    /**
     * Init custom attributes
     *
     * @param attrs attributes
     */
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            Resources resources = getResources();

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpaceNavigationView);
            spaceItemIconSize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_item_icon_size, resources.getDimensionPixelSize(R.dimen.space_item_icon_default_size));
            spaceItemIconOnlySize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_item_icon_only_size, resources.getDimensionPixelSize(R.dimen.space_item_icon_only_size));
            spaceItemTextSize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_item_text_size, resources.getDimensionPixelSize(R.dimen.space_item_text_default_size));
            spaceItemIconOnlySize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_item_icon_only_size, resources.getDimensionPixelSize(R.dimen.space_item_icon_only_size));
            spaceBackgroundColor = typedArray.getColor(R.styleable.SpaceNavigationView_space_background_color, resources.getColor(R.color.space_default_color));
            centerButtonColor = typedArray.getColor(R.styleable.SpaceNavigationView_center_button_color, resources.getColor(R.color.center_button_color));
            centerButtonSize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_center_button_size, resources.getDimensionPixelSize(R.dimen.space_center_button_default_size));
            activeSpaceItemColor = typedArray.getColor(R.styleable.SpaceNavigationView_active_item_color, resources.getColor(R.color.text_select_color));
            inActiveSpaceItemColor = typedArray.getColor(R.styleable.SpaceNavigationView_inactive_item_color, resources.getColor(R.color.default_inactive_item_color));
            centerButtonIcon = typedArray.getResourceId(R.styleable.SpaceNavigationView_center_button_icon, R.drawable.near_me);
            isCentrePartLinear = typedArray.getBoolean(R.styleable.SpaceNavigationView_center_part_linear, false);
            activeCentreButtonIconColor = typedArray.getColor(R.styleable.SpaceNavigationView_active_center_button_icon_color, resources.getColor(R.color.space_white));
            inActiveCentreButtonIconColor = typedArray.getColor(R.styleable.SpaceNavigationView_inactive_center_button_icon_color, resources.getColor(R.color.default_inactive_item_color));
            activeCentreButtonBackgroundColor = typedArray.getColor(R.styleable.SpaceNavigationView_active_center_button_background_color, resources.getColor(R.color.center_button_color));
            centerButtonElevation = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_center_button_elevation, resources.getDimensionPixelSize(R.dimen.center_button_default_elevation));

            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * Set default colors and sizes
         */
        if (spaceBackgroundColor == NOT_DEFINED)
            spaceBackgroundColor = ContextCompat.getColor(context, R.color.space_default_color);

        if (centerButtonColor == NOT_DEFINED)
            centerButtonColor = ContextCompat.getColor(context, R.color.center_button_color);

        if (centerButtonIcon == NOT_DEFINED)
            centerButtonIcon = R.drawable.near_me;

        if (activeSpaceItemColor == NOT_DEFINED)
            activeSpaceItemColor = ContextCompat.getColor(context, R.color.space_white);

        if (inActiveSpaceItemColor == NOT_DEFINED)
            inActiveSpaceItemColor = ContextCompat.getColor(context, R.color.default_inactive_item_color);

        if (spaceItemTextSize == NOT_DEFINED)
//            spaceItemTextSize = (int) getResources().getDimension(R.dimen.space_item_text_default_size);

            if (spaceItemIconSize == NOT_DEFINED)
                spaceItemIconSize = (int) getResources().getDimension(R.dimen.space_item_icon_default_size);

        if (spaceItemIconOnlySize == NOT_DEFINED)
            spaceItemIconOnlySize = (int) getResources().getDimension(R.dimen.space_item_icon_only_size);

        if (centerButtonSize == NOT_DEFINED)
            centerButtonSize = (int) getResources().getDimension(R.dimen.space_center_button_default_size);

        if (centerButtonRippleColor == NOT_DEFINED)
            centerButtonRippleColor = ContextCompat.getColor(context, R.color.colorBackgroundHighlightWhite);

        if (activeCentreButtonIconColor == NOT_DEFINED)
            activeCentreButtonIconColor = ContextCompat.getColor(context, R.color.space_white);

        if (inActiveCentreButtonIconColor == NOT_DEFINED)
            inActiveCentreButtonIconColor = ContextCompat.getColor(context, R.color.default_inactive_item_color);

        /**
         * Set main layout size and color
         */
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = spaceNavigationHeight;
        setBackgroundColor(ContextCompat.getColor(context, R.color.bottom_bar_color));
        setLayoutParams(params);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        /**
         * Restore current item index from savedInstance
         */
        restoreCurrentItem();

        /**
         * Trow exceptions if items size is greater than 4 or lesser than 2
         */
        if (spaceItems.size() < MIN_SPACE_ITEM_SIZE && !isInEditMode()) {
            throw new NullPointerException("Your space item count must be greater than 1 ," +
                    " your current items count isa : " + spaceItems.size());
        }

        if (spaceItems.size() > MAX_SPACE_ITEM_SIZE && !isInEditMode()) {
            throw new IndexOutOfBoundsException("Your items count maximum can be 4," +
                    " your current items count is : " + spaceItems.size());
        }

        /**
         * Get left or right content width
         */
        contentWidth = (width - centerContentWidth) / 2;

        /**
         * Removing all view for not being duplicated
         */
        removeAllViews();

        /**
         * Views initializations and customizing
         */
        initAndAddViewsToMainView();

        /**
         * Redraw main view to make subviews visible
         */
        postRequestLayout();

        /**
         * Retore Translation height
         */

        restoreTranslation();
    }

    //private methods

    /**
     * Views initializations and customizing
     */
    private void initAndAddViewsToMainView() {

        RelativeLayout mainContent = new RelativeLayout(context);
        centerBackgroundView = new RelativeLayout(context);

        leftContent = new LinearLayout(context);
        rightContent = new LinearLayout(context);
        cencelContent = new LinearLayout(context);

        centerContent = buildBezierView();
        centerContent.setBackgroundColor(ContextCompat.getColor(context, R.color.bottom_bar_color));
        centerButton = new CenterButton(context);
        centerButton.setSize(centerButtonSize);
        centerButton.setUseCompatPadding(false);
        centerButton.setCompatElevation(centerButtonElevation);
        centerButton.setRippleColor(centerButtonRippleColor);
        centerButton.setBackgroundTintList(ColorStateList.valueOf(centerButtonColor));
        centerButton.setImageResource(centerButtonIcon);

        if (isCentreButtonIconColorFilterEnabled || isCenterButtonSelectable)
            centerButton.getDrawable().setColorFilter(inActiveCentreButtonIconColor, PorterDuff.Mode.SRC_IN);

        centerButton.setOnClickListener(view -> {
//            if (spaceOnClickListener != null) {
//                centerButton.setSelected(!centerButton.isSelected());
//                spaceOnClickListener.onCentreButtonClick();
//            }
            if (isCenterButtonSelectable)
                updateSpaceItems(-1);
        });
        centerButton.setOnLongClickListener(v -> {
            if (spaceOnLongClickListener != null)
                spaceOnLongClickListener.onCentreButtonLongClick();

            return true;
        });

        /**
         * Set fab layout params
         */
        LayoutParams fabParams = new LayoutParams(centerButtonSize, centerButtonSize);
        fabParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        /**
         * Main content size
         */
        LayoutParams mainContentParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainContentHeight);
        mainContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /**
         * Centre content size
         */
        LayoutParams centerContentParams = new LayoutParams(centerContentWidth, spaceNavigationHeight);
        centerContentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        centerContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /**
         * Centre Background View content size and position
         */
        LayoutParams centerBackgroundViewParams = new LayoutParams(centerContentWidth, mainContentHeight);
        centerBackgroundViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        centerBackgroundViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /**
         * Left content size
         */
        LayoutParams leftContentParams = new LayoutParams(contentWidth, mainContentHeight);
        leftContentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftContentParams.addRule(LinearLayout.HORIZONTAL);
        leftContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /**
         * Right content size
         */
        LayoutParams rightContentParams = new LayoutParams(contentWidth, mainContentHeight);
        rightContentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightContentParams.addRule(LinearLayout.HORIZONTAL);
        rightContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /**
         *  Cencel content size
         */

        LayoutParams cencelContentParams = new LayoutParams(contentWidth, mainContentHeight);
        cencelContentParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        cencelContentParams.addRule(LinearLayout.HORIZONTAL);
        cencelContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /**
         * Adding views background colors
         */
        setBackgroundColors();

        /**
         * Adding view to centerContent
         */
        addCenterButton();
        centerContent.addView(cencelContent, cencelContentParams);

        /**
         * Adding views to mainContent
         */
        addView(leftContent, leftContentParams);
        addView(rightContent, rightContentParams);

        /**
         * Adding views to mainView
         */
        addView(centerBackgroundView, centerBackgroundViewParams);
        addView(centerContent, centerContentParams);
        addView(mainContent, mainContentParams);

        /**
         * Restore changed icons and texts from savedInstance
         */
        restoreChangedIconsAndTexts();

        /**
         * Adding current space items to left and right content
         */
        addSpaceItems(leftContent, rightContent);
    }

    private void addCenterButton() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutParams textAndIconContainerParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        RelativeLayout textAndIconContainer = (RelativeLayout) inflater.inflate(R.layout.space_item_view, this, false);
        textAndIconContainer.setLayoutParams(textAndIconContainerParams);

        ImageView spaceItemIcon = textAndIconContainer.findViewById(R.id.space_icon);
        spaceItemIcon.setBackgroundResource(R.drawable.nav_cancel_button_selector);
        TextView spaceItemText = textAndIconContainer.findViewById(R.id.space_text);
        spaceItemText.setTextSize(TypedValue.COMPLEX_UNIT_PX, spaceItemTextSize);
        spaceItemText.setText(R.string.bottom_bar_post);
        spaceItemText.setTextColor(Color.parseColor("#ffffff"));
        textAndIconContainer.setOnClickListener(view -> {
            if (spaceOnClickListener != null) {
                centerButton.setSelected(!centerButton.isSelected());
                spaceOnClickListener.onCentreButtonClick();
            }
            if (isCenterButtonSelectable)
                updateSpaceItems(-1);
        });
        textAndIconContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.bottom_bar_color));
        cencelContent.addView(textAndIconContainer, textAndIconContainerParams);

    }

    /**
     * Adding given space items to content
     *
     * @param leftContent  to left content
     * @param rightContent and right content
     */
    private void addSpaceItems(LinearLayout leftContent, LinearLayout rightContent) {

        /**
         * Removing all views for not being duplicated
         */
        if (leftContent.getChildCount() > 0 || rightContent.getChildCount() > 0) {
            leftContent.removeAllViews();
            rightContent.removeAllViews();
        }

        /**
         * Clear spaceItemList and badgeList for not being duplicated
         */
        spaceItemList.clear();
        badgeList.clear();

        /**
         * Getting LayoutInflater to inflate space item view from XML
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < spaceItems.size(); i++) {
            final int index = i;
            int targetWidth;

            if (spaceItems.size() > MIN_SPACE_ITEM_SIZE) {
                targetWidth = contentWidth / 2;
            } else {
                targetWidth = contentWidth;
            }

            LayoutParams textAndIconContainerParams = new LayoutParams(
                    targetWidth, mainContentHeight);
            RelativeLayout textAndIconContainer = (RelativeLayout) inflater.inflate(R.layout.space_item_view, this, false);
            textAndIconContainer.setLayoutParams(textAndIconContainerParams);

            ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
            TextView spaceItemText = (TextView) textAndIconContainer.findViewById(R.id.space_text);
            RelativeLayout badgeContainer = (RelativeLayout) textAndIconContainer.findViewById(R.id.badge_container);
            spaceItemIcon.setImageResource(spaceItems.get(i).getItemIcon());
            spaceItemText.setText(spaceItems.get(i).getItemName());
            spaceItemText.setTextSize(TypedValue.COMPLEX_UNIT_PX, spaceItemTextSize);

            /**
             * Set custom font to space item textView
             */
            if (isCustomFont)
                spaceItemText.setTypeface(customFont);

            /**
             * Hide item icon and show only text
             */
            if (isTextOnlyMode)
                Utils.changeViewVisibilityGone(spaceItemIcon);

            /**
             * Hide item text and change icon size
             */
            ViewGroup.LayoutParams iconParams = spaceItemIcon.getLayoutParams();
            if (isIconOnlyMode) {
                if (spaceItems.get(i).getItemIcon() == R.drawable.nav_live_selector) {
                    iconParams.height = (int) getResources().getDimension(R.dimen.space_live_item_icon_height);
                    iconParams.width = (int) getResources().getDimension(R.dimen.space_live_item_icon_width);
                } else {
                    iconParams.height = (int) getResources().getDimension(R.dimen.space_item_icon_height);
                    iconParams.width = (int) getResources().getDimension(R.dimen.space_item_icon_width);
                }

                spaceItemIcon.setLayoutParams(iconParams);
                Utils.changeViewVisibilityGone(spaceItemText);
            } else {
                iconParams.height = spaceItemIconSize;
                iconParams.width = spaceItemIconSize;
                spaceItemIcon.setLayoutParams(iconParams);
            }

            /**
             * Adding space items to item list for future
             */
            spaceItemList.add(textAndIconContainer);

            /**
             * Adding badge items to badge list for future
             */
            badgeList.add(badgeContainer);

            /**
             * Adding sub views to left and right sides
             */
            if (spaceItems.size() == MIN_SPACE_ITEM_SIZE && leftContent.getChildCount() == 1) {
                rightContent.addView(textAndIconContainer, textAndIconContainerParams);
            } else if (spaceItems.size() > MIN_SPACE_ITEM_SIZE && leftContent.getChildCount() == 2) {
                rightContent.addView(textAndIconContainer, textAndIconContainerParams);
            } else {
                leftContent.addView(textAndIconContainer, textAndIconContainerParams);
            }

            /**
             * Changing current selected item tint
             */
            if (i == currentSelectedItem) {
                spaceItemIcon.setSelected(true);
                spaceItemText.setTextColor(activeSpaceItemColor);
//                Utils.changeImageViewTint(spaceItemIcon, activeSpaceItemColor);
            } else {
                spaceItemIcon.setSelected(false);
                spaceItemText.setTextColor(inActiveSpaceItemColor);
//                Utils.changeImageViewTint(spaceItemIcon, inActiveSpaceItemColor);
            }

            textAndIconContainer.setOnClickListener(view -> updateSpaceItems(index));

            textAndIconContainer.setOnLongClickListener(v -> {
                if (spaceOnLongClickListener != null)
                    spaceOnLongClickListener.onItemLongClick(index, spaceItems.get(index).getItemName());
                return true;
            });
        }

        /**
         * Restore available badges from saveInstance
         */
        restoreBadges();
    }

    /**
     * Update selected item and change it's and non selected item tint
     *
     * @param selectedIndex item in index
     */
    private void updateSpaceItems(final int selectedIndex) {

        /**
         * return if item already selected
         */
        if (currentSelectedItem == selectedIndex) {
            if (spaceOnClickListener != null && selectedIndex >= 0)
                spaceOnClickListener.onItemReselected(selectedIndex, spaceItems.get(selectedIndex).getItemName());

            return;
        }

        if (isCenterButtonSelectable) {
            /**
             * Selects the center button as current
             */
            if (selectedIndex == -1) {
                if (centerButton != null) {
                    centerButton.getDrawable().setColorFilter(activeCentreButtonIconColor, PorterDuff.Mode.SRC_IN);

                    if (activeCentreButtonBackgroundColor != NOT_DEFINED) {
                        centerButton.setBackgroundTintList(ColorStateList.valueOf(activeCentreButtonBackgroundColor));
                    }
                }
            }

            /**
             * Removes selection from center button
             */
            if (currentSelectedItem == -1) {
                if (centerButton != null) {
                    centerButton.getDrawable().setColorFilter(inActiveCentreButtonIconColor, PorterDuff.Mode.SRC_IN);

                    if (activeCentreButtonBackgroundColor != NOT_DEFINED) {
                        centerButton.setBackgroundTintList(ColorStateList.valueOf(centerButtonColor));
                    }
                }
            }
        }

        /**
         * Change active and inactive icon and text color
         */
        for (int i = 0; i < spaceItemList.size(); i++) {
            if (i == selectedIndex) {
                RelativeLayout textAndIconContainer = (RelativeLayout) spaceItemList.get(selectedIndex);
                ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                TextView spaceItemText = (TextView) textAndIconContainer.findViewById(R.id.space_text);
                spaceItemText.setTextColor(activeSpaceItemColor);
                spaceItemIcon.setSelected(true);
//                Utils.changeImageViewTint(spaceItemIcon, activeSpaceItemColor);
            } else if (i == currentSelectedItem) {
                RelativeLayout textAndIconContainer = (RelativeLayout) spaceItemList.get(i);
                ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                TextView spaceItemText = (TextView) textAndIconContainer.findViewById(R.id.space_text);
                spaceItemText.setTextColor(inActiveSpaceItemColor);
                spaceItemIcon.setSelected(false);
//                Utils.changeImageViewTint(spaceItemIcon, inActiveSpaceItemColor);
            }
        }

        /**
         * Set a listener that gets fired when the selected item changes
         *
         * @param listener a listener for monitoring changes in item selection
         */
        if (spaceOnClickListener != null && selectedIndex >= 0)
            spaceOnClickListener.onItemClick(selectedIndex, spaceItems.get(selectedIndex).getItemName());

        /**
         * Change current selected item index
         */
        currentSelectedItem = selectedIndex;
    }

    /**
     * Set views background colors
     */
    private void setBackgroundColors() {
        rightContent.setBackgroundColor(spaceBackgroundColor);
        centerBackgroundView.setBackgroundColor(spaceBackgroundColor);
        leftContent.setBackgroundColor(spaceBackgroundColor);
        cencelContent.setBackgroundColor(spaceBackgroundColor);


    }

    /**
     * Indicate event queue that we have changed the View hierarchy during a layout pass
     */
    private void postRequestLayout() {
        SpaceNavigationView.this.getHandler().post(SpaceNavigationView.this::requestLayout);
    }

    /**
     * Restore current item index from savedInstance
     */
    private void restoreCurrentItem() {
        Bundle restoredBundle = savedInstanceState;
        if (restoredBundle != null) {
            if (restoredBundle.containsKey(CURRENT_SELECTED_ITEM_BUNDLE_KEY))
                currentSelectedItem = restoredBundle.getInt(CURRENT_SELECTED_ITEM_BUNDLE_KEY, 0);
        }
    }

    /**
     * Restore available badges from saveInstance
     */
    @SuppressWarnings("unchecked")
    private void restoreBadges() {
        Bundle restoredBundle = savedInstanceState;

        if (restoredBundle != null) {
            if (restoredBundle.containsKey(BADGE_FULL_TEXT_KEY)) {
                shouldShowBadgeWithNinePlus = restoredBundle.getBoolean(BADGE_FULL_TEXT_KEY);
            }

            if (restoredBundle.containsKey(BADGES_ITEM_BUNDLE_KEY)) {
                badgeSaveInstanceHashMap = (HashMap<Integer, Object>) savedInstanceState.getSerializable(BADGES_ITEM_BUNDLE_KEY);
                if (badgeSaveInstanceHashMap != null) {
                    for (Integer integer : badgeSaveInstanceHashMap.keySet()) {
                        BadgeHelper.forceShowBadge(
                                badgeList.get(integer),
                                (BadgeItem) badgeSaveInstanceHashMap.get(integer),
                                shouldShowBadgeWithNinePlus);
                    }
                }
            }
        }
    }

    /**
     * Restore changed icons,colors and texts from saveInstance
     */
    @SuppressWarnings("unchecked")
    private void restoreChangedIconsAndTexts() {
        Bundle restoredBundle = savedInstanceState;
        if (restoredBundle != null) {
            if (restoredBundle.containsKey(CHANGED_ICON_AND_TEXT_BUNDLE_KEY)) {
                changedItemAndIconHashMap = (HashMap<Integer, SpaceItem>) restoredBundle.getSerializable(CHANGED_ICON_AND_TEXT_BUNDLE_KEY);
                if (changedItemAndIconHashMap != null) {
                    SpaceItem spaceItem;
                    for (int i = 0; i < changedItemAndIconHashMap.size(); i++) {
                        spaceItem = changedItemAndIconHashMap.get(i);
                        spaceItems.get(i).setItemIcon(spaceItem.getItemIcon());
                        spaceItems.get(i).setItemName(spaceItem.getItemName());
                    }
                }
            }

            if (restoredBundle.containsKey(CENTER_BUTTON_ICON_KEY)) {
                centerButtonIcon = restoredBundle.getInt(CENTER_BUTTON_ICON_KEY);
                centerButton.setImageResource(centerButtonIcon);
            }

            if (restoredBundle.containsKey(SPACE_BACKGROUND_COLOR_KEY)) {
                int backgroundColor = restoredBundle.getInt(SPACE_BACKGROUND_COLOR_KEY);
                changeSpaceBackgroundColor(backgroundColor);
            }
        }
    }

    /**
     * Creating bezier view with params
     *
     * @return created bezier view
     */
    private BezierView buildBezierView() {
        BezierView bezierView = new BezierView(context, spaceBackgroundColor);
        bezierView.build(centerContentWidth, spaceNavigationHeight - mainContentHeight, isCentrePartLinear);
        return bezierView;
    }

    /**
     * Throw Array Index Out Of Bounds Exception
     *
     * @param itemIndex item index to show on logs
     */
    private void throwArrayIndexOutOfBoundsException(int itemIndex) {
        throw new ArrayIndexOutOfBoundsException("Your item index can't be 0 or greater than space item size," +
                " your items size is " + spaceItems.size() + ", your current index is :" + itemIndex);
    }

    //public methods

    /**
     * Initialization with savedInstanceState to save current selected
     * position and current badges
     *
     * @param savedInstanceState bundle to saveInstance
     */
    public void initWithSaveInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    /**
     * Save badges and current position
     *
     * @param outState bundle to saveInstance
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_SELECTED_ITEM_BUNDLE_KEY, currentSelectedItem);
        outState.putInt(CENTER_BUTTON_ICON_KEY, centerButtonIcon);
        outState.putInt(SPACE_BACKGROUND_COLOR_KEY, spaceBackgroundColor);
        outState.putBoolean(BADGE_FULL_TEXT_KEY, shouldShowBadgeWithNinePlus);
        outState.putFloat(VISIBILITY, this.getTranslationY());

        if (badgeSaveInstanceHashMap.size() > 0)
            outState.putSerializable(BADGES_ITEM_BUNDLE_KEY, badgeSaveInstanceHashMap);
        if (changedItemAndIconHashMap.size() > 0)
            outState.putSerializable(CHANGED_ICON_AND_TEXT_BUNDLE_KEY, changedItemAndIconHashMap);
    }

    /**
     * Set center circle button background color
     *
     * @param centerButtonColor target color
     */
    public void setCenterButtonColor(@ColorInt int centerButtonColor) {
        this.centerButtonColor = centerButtonColor;
    }

    /**
     * Set main background color
     *
     * @param spaceBackgroundColor target color
     */
    public void setSpaceBackgroundColor(@ColorInt int spaceBackgroundColor) {
        this.spaceBackgroundColor = spaceBackgroundColor;
    }

    /**
     * Set center button icon
     *
     * @param centerButtonIcon target icon
     */
    public void setCenterButtonIcon(int centerButtonIcon) {
        this.centerButtonIcon = centerButtonIcon;
    }

    /**
     * Set active center button color
     *
     * @param activeCentreButtonBackgroundColor color to change
     */
    public void setActiveCentreButtonBackgroundColor(@ColorInt int activeCentreButtonBackgroundColor) {
        this.activeCentreButtonBackgroundColor = activeCentreButtonBackgroundColor;
    }

    /**
     * Set active item text color
     *
     * @param activeSpaceItemColor color to change
     */
    public void setActiveSpaceItemColor(@ColorInt int activeSpaceItemColor) {
        this.activeSpaceItemColor = activeSpaceItemColor;
    }

    /**
     * Set inactive item text color
     *
     * @param inActiveSpaceItemColor color to change
     */
    public void setInActiveSpaceItemColor(@ColorInt int inActiveSpaceItemColor) {
        this.inActiveSpaceItemColor = inActiveSpaceItemColor;
    }

    /**
     * Set item icon size
     *
     * @param spaceItemIconSize target size
     */
    public void setSpaceItemIconSize(int spaceItemIconSize) {
        this.spaceItemIconSize = spaceItemIconSize;
    }

    /**
     * Set item icon size if showIconOnly() called
     *
     * @param spaceItemIconOnlySize target size
     */
    public void setSpaceItemIconSizeInOnlyIconMode(int spaceItemIconOnlySize) {
        this.spaceItemIconOnlySize = spaceItemIconOnlySize;
    }

    /**
     * Set item text size
     *
     * @param spaceItemTextSize target size
     */
    public void setSpaceItemTextSize(int spaceItemTextSize) {
        this.spaceItemTextSize = spaceItemTextSize;
    }

    /**
     * Set center button pressed state color
     *
     * @param centerButtonRippleColor Target color
     */
    public void setCenterButtonRippleColor(int centerButtonRippleColor) {
        this.centerButtonRippleColor = centerButtonRippleColor;
    }

    /**
     * Show only text in item
     */
    public void showTextOnly() {
        isTextOnlyMode = true;
    }

    /**
     * Show only icon in item
     */
    public void showIconOnly() {
        isIconOnlyMode = true;
    }

    /**
     * Makes center button selectable
     */
    public void setCenterButtonSelectable(boolean isSelectable) {
        this.isCenterButtonSelectable = isSelectable;
    }

    /**
     * Add space item to navigation
     *
     * @param spaceItem item to add
     */
    public void addSpaceItem(SpaceItem spaceItem) {
        spaceItems.add(spaceItem);
    }

    /**
     * Add space item to navigation with index
     *
     * @param index     item index
     * @param spaceItem item to add
     */
    public void addSpaceItem(@AppNavigation int index, SpaceItem spaceItem) {
        spaceItems.add(index, spaceItem);
    }

    public void addLiveSpaceItem(@AppNavigation int index, SpaceItem spaceItem) {
        if (spaceItems == null) return;
        if (index >= spaceItems.size()) return;
        if (spaceItem.getItemIcon() == spaceItems.get(index).getItemIcon())
            return;
        spaceItems.set(index, spaceItem);
        addSpaceItems(leftContent, rightContent);
    }

    /**
     * Change current selected item to center button
     */
    public void setCentreButtonSelected() {
        if (!isCenterButtonSelectable)
            throw new ArrayIndexOutOfBoundsException("Please be more careful, you must set the center button selectable");
        else
            updateSpaceItems(-1);
    }

    /**
     * Set space item and center click
     *
     * @param spaceOnClickListener space click listener
     */
    public void setSpaceOnClickListener(SpaceOnClickListener spaceOnClickListener) {
        this.spaceOnClickListener = spaceOnClickListener;
    }

    /**
     * Set space item and center button long click
     *
     * @param spaceOnLongClickListener space long click listener
     */
    public void setSpaceOnLongClickListener(SpaceOnLongClickListener spaceOnLongClickListener) {
        this.spaceOnLongClickListener = spaceOnLongClickListener;
    }

    /**
     * Change current selected item to given index
     * Note: -1 represents the center button
     *
     * @param indexToChange given index
     */
    public void changeCurrentItem(int indexToChange) {
        if (indexToChange < -1 || indexToChange > spaceItems.size())
            throw new ArrayIndexOutOfBoundsException("Please be more careful, we do't have such item : " + indexToChange);
        else {
            updateSpaceItems(indexToChange);
        }
    }

    /**
     * Show badge at index
     *
     * @param itemIndex index
     * @param badgeText badge count text
     */
    public void showBadgeAtIndex(@AppNavigation int itemIndex, int badgeText, @ColorInt int badgeColor) {
        if (itemIndex < 0 || itemIndex > spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
        } else {
            if (itemIndex >= badgeList.size()) return;
            RelativeLayout badgeView = badgeList.get(itemIndex);
            if (badgeView.getVisibility() == VISIBLE) {
                Log.d(TAG, "Badge at index: " + itemIndex + " already visible");
                return;
            }
            /**
             * Set circle background to badge view
             */
            badgeView.setBackground(BadgeHelper.makeShapeDrawable(badgeColor));

            BadgeItem badgeItem = new BadgeItem(itemIndex, badgeText, badgeColor);
            BadgeHelper.showBadge(badgeView, badgeItem, shouldShowBadgeWithNinePlus);
            badgeSaveInstanceHashMap.put(itemIndex, badgeItem);
        }
    }

    /**
     * Restore translation height from saveInstance
     */
    @SuppressWarnings("unchecked")
    private void restoreTranslation() {
        Bundle restoredBundle = savedInstanceState;

        if (restoredBundle != null) {
            if (restoredBundle.containsKey(VISIBILITY)) {
                this.setTranslationY(restoredBundle.getFloat(VISIBILITY));
            }

        }
    }

    /**
     * Hide badge at index
     *
     * @param index badge index
     * @deprecated Use {@link #hideBadgeAtIndex(int index)} instead.
     */
    @Deprecated
    public void hideBudgeAtIndex(final int index) {
        if (badgeList.get(index).getVisibility() == GONE) {
            Log.d(TAG, "Badge at index: " + index + " already hidden");
        } else {
            BadgeHelper.hideBadge(badgeList.get(index));
            badgeSaveInstanceHashMap.remove(index);
        }
    }

    /**
     * Hide badge at index
     *
     * @param index badge index
     */
    public void hideBadgeAtIndex(final int index) {
        if (index >= badgeList.size()) return;
        if (badgeList.get(index).getVisibility() == GONE) {
            Log.d(TAG, "Badge at index: " + index + " already hidden");
        } else {
            BadgeHelper.hideBadge(badgeList.get(index));
            badgeSaveInstanceHashMap.remove(index);
        }
    }

    /**
     * Hiding all available badges
     *
     * @deprecated Use {@link #hideAllBadges()} instead.
     */
    @Deprecated
    public void hideAllBudges() {
        for (RelativeLayout badge : badgeList) {
            if (badge.getVisibility() == VISIBLE)
                BadgeHelper.hideBadge(badge);
        }
        badgeSaveInstanceHashMap.clear();
    }

    /**
     * Hiding all available badges
     */
    public void hideAllBadges() {
        for (RelativeLayout badge : badgeList) {
            if (badge.getVisibility() == VISIBLE)
                BadgeHelper.hideBadge(badge);
        }
        badgeSaveInstanceHashMap.clear();
    }

    public void hideCancelButton() {
        centerContentWidth = 0;
    }

    /**
     * Change badge text at index
     *
     * @param badgeIndex target index
     * @param badgeText  badge count text to change
     */
    public void changeBadgeTextAtIndex(int badgeIndex, int badgeText) {
        if (badgeSaveInstanceHashMap.get(badgeIndex) != null &&
                (((BadgeItem) badgeSaveInstanceHashMap.get(badgeIndex)).getIntBadgeText() != badgeText)) {
            BadgeItem currentBadgeItem = (BadgeItem) badgeSaveInstanceHashMap.get(badgeIndex);
            BadgeItem badgeItemForSave = new BadgeItem(badgeIndex, badgeText, currentBadgeItem.getBadgeColor());
            BadgeHelper.forceShowBadge(
                    badgeList.get(badgeIndex),
                    badgeItemForSave,
                    shouldShowBadgeWithNinePlus);
            badgeSaveInstanceHashMap.put(badgeIndex, badgeItemForSave);
        }
    }

    /**
     * Get Center button status
     *
     * @return button state
     */
    public boolean isCenterButtonSelected() {
        return centerButton != null && centerButton.isSelected();
    }

    public void setIsCenterButtonSelected(boolean isSelected) {
        if (centerButton != null) centerButton.setSelected(isSelected);
    }


    /**
     * Get center button instance
     *
     * @return
     */
    @NonNull
    public CenterButton getCenterButton() {
        return centerButton;
    }

    /**
     * Set custom font for space item textView
     *
     * @param customFont custom font
     */
    public void setFont(Typeface customFont) {
        isCustomFont = true;
        this.customFont = customFont;
    }

    public void setCentreButtonIconColorFilterEnabled(boolean enabled) {
        isCentreButtonIconColorFilterEnabled = enabled;
    }

    /**
     * Change center button icon if space navigation already set up
     *
     * @param icon Target icon to change
     */
    public void changeCenterButtonIcon(int icon) {
        if (centerButton == null) {
            Log.e(TAG, "You should call setCenterButtonIcon() instead, " +
                    "changeCenterButtonIcon works if space navigation already set up");
        } else {
            centerButton.setImageResource(icon);
            centerButtonIcon = icon;
        }
    }

    /**
     * Change item icon if space navigation already set up
     *
     * @param itemIndex Target position
     * @param newIcon   Icon to change
     */
    public void changeItemIconAtPosition(int itemIndex, int newIcon) {
        if (itemIndex < 0 || itemIndex > spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
        } else {
            SpaceItem spaceItem = spaceItems.get(itemIndex);
            RelativeLayout textAndIconContainer = (RelativeLayout) spaceItemList.get(itemIndex);
            ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
            spaceItemIcon.setImageResource(newIcon);
            spaceItem.setItemIcon(newIcon);
            changedItemAndIconHashMap.put(itemIndex, spaceItem);
        }
    }

    /**
     * Change item text if space navigation already set up
     *
     * @param itemIndex Target position
     * @param newText   Text to change
     */
    public void changeItemTextAtPosition(int itemIndex, String newText) {
        if (itemIndex < 0 || itemIndex > spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
        } else {
            SpaceItem spaceItem = spaceItems.get(itemIndex);
            RelativeLayout textAndIconContainer = (RelativeLayout) spaceItemList.get(itemIndex);
            TextView spaceItemIcon = (TextView) textAndIconContainer.findViewById(R.id.space_text);
            spaceItemIcon.setText(newText);
            spaceItem.setItemName(newText);
            changedItemAndIconHashMap.put(itemIndex, spaceItem);
        }
    }

    /**
     * Change space background color if space view already set up
     *
     * @param color Target color to change
     */
    public void changeSpaceBackgroundColor(@ColorInt int color) {
        if (color == spaceBackgroundColor) {
            Log.d(TAG, "changeSpaceBackgroundColor: color already changed");
            return;
        }

        spaceBackgroundColor = color;
        setBackgroundColors();
        centerContent.changeBackgroundColor(color);
    }


    /**
     * If you want to show full badge text or show 9+
     *
     * @param shouldShowBadgeWithNinePlus false for full text
     */
    public void shouldShowFullBadgeText(boolean shouldShowBadgeWithNinePlus) {
        this.shouldShowBadgeWithNinePlus = shouldShowBadgeWithNinePlus;
    }

    /**
     * set active center button color
     *
     * @param color target color
     */
    public void setActiveCentreButtonIconColor(@ColorInt int color) {
        activeCentreButtonIconColor = color;
    }

    /**
     * set inactive center button color
     *
     * @param color target color
     */
    public void setInActiveCentreButtonIconColor(@ColorInt int color) {
        inActiveCentreButtonIconColor = color;
    }
}
