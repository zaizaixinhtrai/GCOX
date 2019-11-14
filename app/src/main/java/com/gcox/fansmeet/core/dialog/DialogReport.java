package com.gcox.fansmeet.core.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gcox.fansmeet.R;

/**
 * Created by User on 5/25/2016.
 */
public class DialogReport extends DialogFragment {

    private static final String BUNDEL_TITLE = "BUNDEL_TITLE";

    @Bind(R.id.radio_sexual)
    TextView radioSexual;
    @Bind(R.id.radio_violent)
    TextView radioViolent;
    @Bind(R.id.radio_hateful)
    TextView radioHateful;
    @Bind(R.id.radio_harmful)
    TextView radioHarmful;
    @Bind(R.id.radio_child)
    TextView radioChild;
    @Bind(R.id.radio_spam)
    TextView radioSpam;
    @Bind(R.id.radio_infringes)
    TextView radioInfringes;

    private ChooseReportListenner chooseReportListenner;

    public void setChooseReportListenner(ChooseReportListenner chooseReportListenner) {
        this.chooseReportListenner = chooseReportListenner;
    }

    public static DialogReport newInstance() {

        return new DialogReport();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_report, container);
        ButterKnife.bind(this, view);

        inti();

        return view;
    }

    private void inti() {

        radioSexual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReportListenner != null) {
                    chooseReportListenner.choseReport(getContext().getString(R.string.report_sexual_content));
                }

                getDialog().dismiss();
            }
        });

        radioViolent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReportListenner != null) {
                    chooseReportListenner.choseReport(getContext().getString(R.string.report_violent));
                }

                getDialog().dismiss();
            }
        });

        radioHateful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReportListenner != null) {
                    chooseReportListenner.choseReport(getContext().getString(R.string.report_hateful));
                }

                getDialog().dismiss();
            }
        });

        radioHarmful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReportListenner != null) {
                    chooseReportListenner.choseReport(getContext().getString(R.string.report_harmful));
                }

                getDialog().dismiss();
            }
        });

        radioChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReportListenner != null) {
                    chooseReportListenner.choseReport(getContext().getString(R.string.report_child));
                }

                getDialog().dismiss();
            }
        });

        radioSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReportListenner != null) {
                    chooseReportListenner.choseReport(getContext().getString(R.string.report_spam));
                }

                getDialog().dismiss();
            }
        });

        radioInfringes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseReportListenner != null) {
                    chooseReportListenner.choseReport(getContext().getString(R.string.report_infringes));
                }

                getDialog().dismiss();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface ChooseReportListenner {
        void choseReport(String reason);
    }
}
