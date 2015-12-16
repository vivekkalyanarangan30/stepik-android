package org.stepic.droid.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.yandex.metrica.YandexMetrica;

import org.jetbrains.annotations.NotNull;
import org.stepic.droid.R;
import org.stepic.droid.base.MainApplication;
import org.stepic.droid.core.IShell;
import org.stepic.droid.preferences.UserPreferences;
import org.stepic.droid.store.operations.DatabaseManager;
import org.stepic.droid.util.AppConstants;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class VideoQualityDialog extends DialogFragment {


    @Inject
    IShell mShell;

    @Inject
    DatabaseManager mDbManager;

    @Inject
    UserPreferences mUserPreferences;

    private static Map<String, Integer> mQualityToPositionMap = new HashMap<>();
    private static Map<Integer, String> mPositionToQualityMap = new HashMap<>();

    static {
        mQualityToPositionMap.put("270", 0);
        mQualityToPositionMap.put("360", 1);
        mQualityToPositionMap.put("720", 2);
        mQualityToPositionMap.put("1080", 3);

        mPositionToQualityMap.put(0, "270");
        mPositionToQualityMap.put(1, "360");
        mPositionToQualityMap.put(2, "720");
        mPositionToQualityMap.put(3, "1080");
    }


    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MainApplication.component().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.video_quality)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        YandexMetrica.reportEvent(AppConstants.METRICA_CANCEL_VIDEO_QUALITY);
                    }
                })
                .setSingleChoiceItems(R.array.video_quality,
                        mQualityToPositionMap.get(mUserPreferences.getQualityVideo()),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        mUserPreferences.storeQualityVideo(mPositionToQualityMap.get(which));
                                        return null;
                                    }
                                };
                                task.execute();
                                dialog.dismiss();
                            }
                        });

        return builder.create();
    }
}