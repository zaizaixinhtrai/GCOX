package com.gcox.fansmeet.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.gcox.fansmeet.R;


/**
 * Created by Ngoc on 4/19/2017.
 */

public class CopyTextUtils {

    public static void showOptionCopyText(Context context, View view, String text, PopupMenu.OnDismissListener onDismissListener) {
        PopupMenu popupMenuOption = new PopupMenu(context, view);
        popupMenuOption.getMenu().add(0, 0, 0, context.getString(R.string.copy_this_text));
        popupMenuOption.setOnMenuItemClickListener(item -> {
            switch (item.getOrder()) {
                case 0:
                    CopyClipboard(context, text, "");

                    break;
            }
            return true;
        });

        popupMenuOption.setOnDismissListener(onDismissListener);
        popupMenuOption.show();

    }

    public static void CopyClipboard(Context context, String textCopy, String textForToast) {
        ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", textCopy);
        myClipboard.setPrimaryClip(myClip);

        String text;
        if (StringUtil.isNullOrEmptyString(textForToast)) {
            text = context.getString(R.string.text_copied);
        } else {
            text = textForToast;
        }

        Toast.makeText(context.getApplicationContext(), text,
                Toast.LENGTH_SHORT).show();
    }
}
