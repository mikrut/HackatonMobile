package ru.bmstu.iu6.hackatonmobile.network;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.bmstu.iu6.hackatonmobile.R;
import ru.bmstu.iu6.hackatonmobile.database.RequirementHelper;
import ru.bmstu.iu6.hackatonmobile.models.RequirementModel;

/**
 * Created by mikrut on 05.12.15.
 */
public class BeaconDispatcher {
    Context context;
    public final static int MAJOR_METRO = 0;
    public final static int MAJOR_STORE = 1;
    public final static int MAJOR_RESTAURANT = 2;
    public final static int MAJOR_MEMORIAL = 3;
    public final static int MAJOR_LOST  = 10;


    public BeaconDispatcher(Context context) {
        this.context = context;
    }

    public void dispatchBeacon(String UUID, int major, int minor) {
        // url: http://sitenotifier.herokuapp.com/goods/spot
        // POST: uuid, major, minor, group - name of good or name of station (String), [maxPrice, minPrice]
        // lost: {alert => true/false}
        // store: {'text' => true/false}
        Bundle params = new Bundle();
        params.putString("uuid", UUID);
        params.putInt("major", major);
        params.putInt("minor", minor);

        final String method = "POST";
        switch(major) {
            case MAJOR_METRO:
            case MAJOR_STORE:
            case MAJOR_RESTAURANT:
            case MAJOR_MEMORIAL:
                RequirementModel model = pluckType(major, "/goods/restaurant", method, params);
                if (model != null) {
                    dispatchTypedResult(model);
                }
                break;
            case MAJOR_LOST:
                JSONObject result = JSONGetter.getAPIResponse("/goods/spot", method, params);
                dispatchLostResult(result);
                break;
        }
    }

    @Nullable
    public RequirementModel pluckType(int major, String suburl, String method, Bundle params) {
        RequirementHelper requirementHelper = new RequirementHelper(context);
        ArrayList<RequirementModel> models = requirementHelper.getModelByType(major);

        GregorianCalendar now = new GregorianCalendar();
        int current_day_index = (now.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7;
        Log.v("model pluck", String.valueOf(current_day_index));
        for (RequirementModel model : models) {
            if ((model.getFoundTime() == null ||
                    model.getUpdatedTime().after(model.getFoundTime()) ||
                    (now.after(model.getFoundTime()) && now.get(Calendar.DATE) != model.getFoundTime().get(Calendar.DATE)))
            &&
                            (model.getDaysMask() & 1 << current_day_index) != 0) {
                params.putString("group", model.getCategory());
                if (model.getType() == RequirementModel.TYPE_FOOD ||
                        model.getType() == RequirementModel.TYPE_STORE) {
                    params.putInt("maxPrice", model.getMaxPrice());
                }
                JSONObject response = JSONGetter.getAPIResponse(suburl, method, params);
                try {
                    if (response.has("text") && response.getBoolean("text")){
                        return model;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void dispatchTypedResult(RequirementModel model) {
        GregorianCalendar now = new GregorianCalendar();
        model.setFoundTime(now);

        RequirementHelper requirementHelper = new RequirementHelper(context);
        requirementHelper.saveModel(model);

        String[] foundHeaders = context.getResources().getStringArray(R.array.headers_founds_array);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle(foundHeaders[model.getType()])
                        .setContentText(model.getCategory());

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(model.getId(), mBuilder.build());
    }

    public void dispatchLostResult(JSONObject result) {
        try {
            if (result.has("alert") && result.getBoolean("alert")) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                                .setContentTitle("Поблизости обнаружен пропавший человек!")
                                .setContentText("Свяжитесь со службой LisaAlert.");

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0xdeadbeef, mBuilder.build());
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
