package com.gcox.fansmeet.customview;

import android.content.Context;
import android.graphics.Rect;
import android.text.*;
import android.text.method.QwertyKeyListener;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.*;
import com.gcox.fansmeet.customview.autolinktextview.*;
import com.gcox.fansmeet.customview.hashtag.FollowItem;
import com.gcox.fansmeet.customview.hashtag.UserTagListPopUp;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

import static android.widget.MultiAutoCompleteTextView.Tokenizer;
import static com.gcox.fansmeet.util.Utils.dpToPx;
import static com.gcox.fansmeet.util.Utils.getScreenHeight;

/**
 * Created by linh on 20/06/2017.
 */

public class TaggableEditText extends CustomFontEditText implements TextWatcher, ViewTreeObserver.OnGlobalLayoutListener{
    private static final int SYSTEM_STATUS_HEIGHT = dpToPx(24);
    private static final int USER_TAG_ITEM_HEIGHT = dpToPx(50);
    private static final int POPUP_WINDOW_MARGIN = dpToPx(12);
    private static final int POPUP_WINDOW_PADDING = dpToPx(10);
    private int mThreshold = 1;
    private boolean mOpenBefore;
    private int mLastKeyCode = KeyEvent.KEYCODE_UNKNOWN;

    UserTagListPopUp mPopupWindow;
    private Tokenizer mTokenizer;
    Realm realm;
    List<FollowItem> mTaggedFollowers;

    @AutoLinkMode
    private String[] autoLinkModes = new String[]{AutoLinkMode.MODE_MENTION};

    int lastTagPosition;
    Layout mLayout;

    View mAnchorView;

    private AutoLinkOnClickListener mAutoLinkOnClickListener;

    public TaggableEditText(Context context) {
        super(context);
        constructor(context, null, 0);
    }

    public TaggableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructor(context, attrs, 0);
    }

    public TaggableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        constructor(context, attrs, defStyleAttr);
    }

    public void setAnchorView(View anchorView) {
        mAnchorView = anchorView;
    }

    public void setAutoLinkOnClickListener(AutoLinkOnClickListener autoLinkOnClickListener) {
        mAutoLinkOnClickListener = autoLinkOnClickListener;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            dismissDropDown();
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        // Perform validation if the view is losing focus.
        if (!focused) {
            performValidation();
            dismissDropDown();
        }
        Timber.d("onFocusChanged %s", String.valueOf(focused));
    }

    @Override
    protected void onDetachedFromWindow() {
        realm.close();
        dismissDropDown();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        Timber.d("onKeyPreIme");
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mLastKeyCode = keyCode;
        if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing()){
            dismissDropDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        boolean consumed = mPopup.onKeyUp(keyCode, event);
//        if (consumed) {
//            switch (keyCode) {
//                // if the list accepts the key events and the key event
//                // was a click, the text view gets the selected item
//                // from the drop down as its content
//                case KeyEvent.KEYCODE_ENTER:
//                case KeyEvent.KEYCODE_DPAD_CENTER:
//                case KeyEvent.KEYCODE_TAB:
//                    if (event.hasNoModifiers()) {
//                        performCompletion();
//                    }
//                    return true;
//            }
//        }
//        if (isPopupShowing() && keyCode == KeyEvent.KEYCODE_TAB && event.hasNoModifiers()) {
//            performCompletion(null);
//            return true;
//        }
//        return super.onKeyUp(keyCode, event);
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        doBeforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Timber.d("onTextChanged %s", s);
        Timber.d("onTextChanged %d - %d - %d", start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        doAfterTextChanged(s);
    }

    @Override
    public void onGlobalLayout() {
        mLayout = getLayout();
    }

    private void constructor(Context context, AttributeSet attrs, int defStyleAttr){
        mTokenizer = new AtTokenizer();
        realm = Realm.getDefaultInstance();
        mTaggedFollowers = new ArrayList<>();
        setCallback();
        getTextLayout();
    }

    /**
     * find all tagged username to highlight them
     */
    public void setTextAndFormatText(String text) {
        List<AutoLinkItem> autoLinkItems = AutoLinkUtil.matchedRanges(text, autoLinkModes);
        extractTaggedUsers(autoLinkItems);
        setText(makeSpannableString(text, autoLinkItems));
    }

    private void extractTaggedUsers(List<AutoLinkItem> autoLinkItems){
        if (!autoLinkItems.isEmpty()) {
            String[] userNames = new String[autoLinkItems.size()];
            for (int i = 0; i < autoLinkItems.size(); i++) {
                userNames[i] = (autoLinkItems.get(i).getMatchedTextExcludeWildCard());
            }
            RealmResults<FollowItem> results = realm.where(FollowItem.class)
                    .in("UserName", userNames).findAll();
            Timber.d("extractTaggedUsers %d", results.size());
            mTaggedFollowers.addAll(realm.copyFromRealm(results));
        }
    }

    private SpannableString makeSpannableString(String text, List<AutoLinkItem> autoLinkItems) {
        final SpannableString spannableString = new SpannableString(text);
        for (final AutoLinkItem autoLinkItem : autoLinkItems) {
            if ( isUserHasTaggedByUserName(autoLinkItem.getMatchedTextExcludeWildCard())){
                AutoLinkUtil.formatAutoLink(spannableString, autoLinkItem, false, mAutoLinkOnClickListener);
            }
        }
        return spannableString;
    }

    private boolean isUserHasTaggedByUserName(String username){
        for (FollowItem follower : mTaggedFollowers){
            if (follower.getUserName().equals(username)){
                return true;
            }
        }
        return false;
    }

    private void getTextLayout(){
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public String getStringTaggedUsersIdAndClear(){
        String result = getTaggedUsersId();
        mTaggedFollowers.clear();
        setText("");
        return result;
    }

    public String getTaggedUsersId(){
        return TextUtils.join(",", getTaggedUserIdsList());
    }

    private List<String> getTaggedUserIdsList(){
        List<String> result = new ArrayList<>();
        for (FollowItem item : mTaggedFollowers) {
            result.add(String.valueOf(item.getUserId()));
        }
        return result;
    }

    private List<String> getTokeList(){
        List<String> tokenList = new ArrayList<>();
        Editable e = getText();
        int i = getText().length();
        while (i > 0) {
            int start = mTokenizer.findTokenStart(e, i);
            int end = mTokenizer.findTokenEnd(e, start);

            CharSequence sub = e.subSequence(start, end);
            if (TextUtils.isEmpty(sub)) {
                e.replace(start, i, "");
            } else {
                if (!tokenList.contains(sub.toString())){
                    tokenList.add(sub.toString());
                }
                Timber.d("sub string %s", sub);
            }

            i = start-1;
        }
        return tokenList;
    }

    private void setupPopUpWindow(RealmResults<FollowItem> realmResults){
        mPopupWindow = UserTagListPopUp.newInstance(getContext(), realmResults, (caller, tagItem) -> performCompletion(tagItem));
    }

    private void setCallback(){
        addTextChangedListener(this);
    }

    private void showDropDown(RealmResults<FollowItem> realmResults, String query){
        if (mPopupWindow == null){
            setupPopUpWindow(realmResults);
        }

        int pos = getSelectionStart();
        int line = mLayout.getLineForOffset(pos);
        int baseline = mLayout.getLineBaseline(line);
        int selectedLineTop = mLayout.getLineTop(line);
        int selectedLineBottom = mLayout.getLineBottom(line);
        int heightOfOneLine = selectedLineBottom - selectedLineTop;
        int bottom = getHeight();
        if (selectedLineTop > bottom - heightOfOneLine){
            selectedLineTop = bottom - heightOfOneLine;
        }
        if (selectedLineBottom > bottom){
            selectedLineBottom = bottom;
        }

        if (mAnchorView == null){
            mAnchorView = this;
        }
        int[] anchorPositionOnScreen = new int[2];
        mAnchorView.getLocationOnScreen(anchorPositionOnScreen);
        int anchorTopOnScreen = anchorPositionOnScreen[1] - SYSTEM_STATUS_HEIGHT;
        int y = anchorTopOnScreen + selectedLineBottom;
        int heightOfScreen = getScreenHeight();
        boolean popupShouldShowTop = false;

        int maxHeightAvailableAtTop = 0;
        if (y > heightOfScreen * 0.3){
            popupShouldShowTop = true;
            maxHeightAvailableAtTop = anchorTopOnScreen + selectedLineTop - (int)(heightOfOneLine * 0.5);
        }
        Timber.d("pos %d", pos);
        Timber.d("line %d", line);
        Timber.d("baseline %d", baseline);
        Timber.d("selectedLineTop %d", selectedLineTop);
        Timber.d("selectedLineBottom %d", selectedLineBottom);
        Timber.d("maxHeightAvailableAtTop %d", maxHeightAvailableAtTop);
        Timber.d("bottom %d", bottom);
        Timber.d("y %d", y);

        if (popupShouldShowTop){
            int heightOfAllItem = realmResults.size() * USER_TAG_ITEM_HEIGHT + POPUP_WINDOW_PADDING;
            int height = heightOfAllItem;
            if (heightOfAllItem > maxHeightAvailableAtTop){
                height = maxHeightAvailableAtTop;
            }
            y = maxHeightAvailableAtTop - height + SYSTEM_STATUS_HEIGHT;
            if(mPopupWindow!=null) {
                mPopupWindow.showAtLocation(mAnchorView, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, y);
                mPopupWindow.update(0, y, mPopupWindow.getWidth(), height, true);
            }
        }else {
            y = selectedLineBottom - bottom;
            if(mPopupWindow!=null) {
                mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupWindow.showAsDropDown(mAnchorView, POPUP_WINDOW_MARGIN, y, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            }
        }
        if(mPopupWindow!=null) mPopupWindow.updateList(realmResults, query);
    }

    /**
     * <p>Returns the number of characters the user must type before the drop
     * down list is shown.</p>
     *
     * @return the minimum number of characters to type to show the drop down
     *
     * @see #setThreshold(int)
     *
     * @attr ref android.R.styleable#AutoCompleteTextView_completionThreshold
     */
    public int getThreshold() {
        return mThreshold;
    }

    /**
     * <p>Specifies the minimum number of characters the user has to type in the
     * edit box before the drop down list is shown.</p>
     *
     * <p>When <code>threshold</code> is less than or equals 0, a threshold of
     * 1 is applied.</p>
     *
     * @param threshold the number of characters to type before the drop down
     *                  is shown
     *
     * @see #getThreshold()
     *
     * @attr ref android.R.styleable#AutoCompleteTextView_completionThreshold
     */
    public void setThreshold(int threshold) {
        if (threshold <= 0) {
            threshold = 1;
        }

        mThreshold = threshold;
    }

    void doBeforeTextChanged(CharSequence s, int start, int count, int after) {
        int length = s.length();
        if(count == 0 || length <= start){ //(length > start && s.charAt(start) == AtTokenizer.TOKEN_END)
            return;
        }//should do bellow code if user has deleted text
        int tokenStart = mTokenizer.findTokenStart(s, start);
        int tokenEnd = mTokenizer.findTokenEnd(s, tokenStart);
        if (tokenEnd > tokenStart && tokenStart <= start && tokenEnd>= start){
            if (removeTaggedUser(s.subSequence(tokenStart, tokenEnd).toString())) {
                clearHighLightSpan(tokenStart, tokenEnd);
            }else{
                removeTaggedUser(s.subSequence(tokenStart, start).toString());
                clearHighLightSpan(tokenStart, start);
            }
        }
        mOpenBefore = isPopupShowing();
        Timber.v("before text changed: open=%s", String.valueOf(mOpenBefore));
    }

    private boolean removeTaggedUser(String userName){
        for (int i=0; i<mTaggedFollowers.size(); i++){
            FollowItem item = mTaggedFollowers.get(i);
            if (userName.equals(item.getUserName())){
                mTaggedFollowers.remove(i);
                Timber.d("remove tagged user %s", userName);
                return true;
            }
        }
        return false;
    }

    private void clearHighLightSpan(int tokenStart, int tokenEnd){
        Editable editable = getText();
        TouchableSpan[] spans = editable.getSpans(tokenStart, tokenEnd, TouchableSpan.class);
        for (int i = 0; i < spans.length; i++) {
            editable.removeSpan(spans[i]);
        }
    }

    void doAfterTextChanged(Editable s) {
        Timber.d("doAfterTextChanged");
        try {
            performFiltering(s, mLastKeyCode);
        }catch (IndexOutOfBoundsException e){
            Timber.e(e);
        }
    }

    /**
     * Instead of validating the entire text, this subclass method validates
     * each token of the text individually.  Empty tokens are removed.
     */
    public void performValidation() {
        Editable e = getText();
        int i = getText().length();
        while (i > 0) {
            int start = mTokenizer.findTokenStart(e, i);
            int end = mTokenizer.findTokenEnd(e, start);

            CharSequence sub = e.subSequence(start, end);
            if (TextUtils.isEmpty(sub)) {
                e.replace(start, i, "");
            } else {
                e.replace(start, i, mTokenizer.terminateToken(sub));
            }

            i = start;
        }
    }

    private void performCompletion(FollowItem tagItem) {
        replaceText(tagItem.getUserName());
        dismissDropDown();
        mTaggedFollowers.add(realm.copyFromRealm(tagItem));
    }

    /**
     * <p>Performs the text completion by replacing the range from
     * {@link Tokenizer#findTokenStart} to {@link #getSelectionEnd} by the
     * the result of passing <code>text</code> through
     * {@link Tokenizer#terminateToken}.
     * In addition, the replaced region will be marked as an AutoText
     * substition so that if the user immediately presses DEL, the
     * completion will be undone.
     * Subclasses may override this method to do some different
     * insertion of the content into the edit box.</p>
     *
     * @param text the selected suggestion in the drop down list
     */
    protected void replaceText(CharSequence text) {
        clearComposingText();

        int end = getSelectionEnd();
        int start = mTokenizer.findTokenStart(getText(), end);

        Editable editable = getText();
        String original = TextUtils.substring(editable, start, end);

        QwertyKeyListener.markAsReplaced(editable, start, end, original);
        editable.replace(start, end, mTokenizer.terminateToken(text));
        lastTagPosition = start;
    }

    /**
     * Instead of filtering on the entire contents of the edit box,
     * this subclass method filters on the range from
     * {@link Tokenizer#findTokenStart} to {@link #getSelectionEnd}
     * if the length of that range meets or exceeds {@link #getThreshold}.
     */
    protected void performFiltering(CharSequence text, int keyCode) throws IndexOutOfBoundsException{
        if (enoughToFilter()) {
            int end = getSelectionStart();
            int start = mTokenizer.findTokenStart(text, end);

            performFiltering(text, start, end, keyCode);
        } else {
            if (isPopupShowing()) {
                dismissDropDown();
            }
        }
    }

    /**
     * <p>Starts filtering the content of the drop down list. The filtering
     * pattern is the specified range of text from the edit box. Subclasses may
     * override this method to filter with a different pattern, for
     * instance a smaller substring of <code>text</code>.</p>
     */
    protected void performFiltering(CharSequence text, int start, int end, int keyCode) throws IndexOutOfBoundsException {
        String[] userNames = getTaggedUserIdsList().toArray(new String[0]);
        String query = text.subSequence(start, end).toString();
        RealmQuery<FollowItem> realmQuery = realm.where(FollowItem.class)
                .beginGroup()
                .contains("UserName", query).or().contains("DisplayName", query, Case.INSENSITIVE)
                .endGroup();
        if (userNames.length > 0) {
            realmQuery.not().in("UserId", userNames);
        }
        RealmResults<FollowItem> realmResults = realmQuery.findAll();
        Timber.d("query %s result %d", query, realmResults.size());
        if (realmResults.isEmpty()){
            dismissDropDown();
        }else {
            showDropDown(realmResults, query);
        }
        Timber.d("realm path %s", realm.getPath());
    }

    /**
     * Instead of filtering whenever the total length of the text
     * exceeds the threshhold, this subclass filters only when the
     * length of the range from
     * {@link Tokenizer#findTokenStart} to {@link #getSelectionEnd}
     * meets or exceeds {@link #getThreshold}.
     */
    public boolean enoughToFilter() {
        Editable text = getText();

        int end = getSelectionEnd();
        if (end < 0 || mTokenizer == null) {
            return false;
        }

        int start = mTokenizer.findTokenStart(text, end);

        if (end - start >= getThreshold()) {
            return true;
        } else {
            return false;
        }
    }

    private void updateDropDownForFilter(int count) {
        // Not attached to window, don't update drop-down
        if (getWindowVisibility() == View.GONE) return;

        /*
         * This checks enoughToFilter() again because filtering requests
         * are asynchronous, so the result may come back after enough text
         * has since been deleted to make it no longer appropriate
         * to filter.
         */

        final boolean enoughToFilter = enoughToFilter();
        if ((count > 0) && enoughToFilter) {
            if (hasFocus() && hasWindowFocus()) {
//                showDropDown(realmResults);
            }
        } else if (isPopupShowing()) {
            dismissDropDown();
            // When the filter text is changed, the first update from the adapter may show an empty
            // count (when the query is being performed on the network). Future updates when some
            // content has been retrieved should still be able to update the list.
//            mPopupCanBeUpdated = true;
        }
    }

    /**
     * <p>Indicates whether the popup menu is showing.</p>
     *
     * @return true if the popup menu is showing, false otherwise
     */
    public boolean isPopupShowing() {
        boolean result = mPopupWindow != null && mPopupWindow.isShowing();
        Timber.d("is popup showing %s", String.valueOf(result));
        return result;
    }

    /**
     * <p>Closes the drop down if present on screen.</p>
     */
    public void dismissDropDown() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    private static ClickableSpan getHighLightedSpan(CharSequence text){
        return AutoLinkUtil.getTouchableSpan(AutoLinkUtil.mentionModeColor, false, AutoLinkMode.MODE_MENTION, text.toString(), null);
    }

    private static class AtTokenizer implements Tokenizer {
        public static final char TOKEN_START = '@';
        public static final char TOKEN_END = ' ';
        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && text.charAt(i - 1) != TOKEN_START) {
                i--;
            }

            //Check if token really started with @, else we don't have a valid token
            if (i < 1 || text.charAt(i - 1) != TOKEN_START) {
                return cursor;
            }

            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == TOKEN_END) {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == TOKEN_END) {
                i--;
            }

            if (i > 0 && text.charAt(i - 1) == TOKEN_END) {
                return text;
            } else {
                SpannableString sp = new SpannableString(text + " ");
                sp.setSpan(getHighLightedSpan(text), 0, text.length(), 0);

                return sp;
            }
        }
    }
}
