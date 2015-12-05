package ru.bmstu.iu6.hackatonmobile.network;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

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

    public Bus lost_bus = new Bus();
    public Bus requirement_bus = new Bus();
    private boolean lost = false;
    private boolean finish_flag = false;
    public RequirementModel requirementModel;

    @Subscribe
    public void answerAwailable(JSONObject response) {
        if (lost) {
            dispatchLostResult(response);
        } else {
            try {
                if (response.has("text") && response.getBoolean("text")){
                    RequirementModel currentModel = requirementModel;
                    if (response.has("id")) {
                        RequirementHelper requirementHelper = new RequirementHelper(context);
                        requirementModel = requirementHelper.findModel(response.getInt("id"));
                    }
                    dispatchTypedResult(requirementModel);
                    finish_flag = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public BeaconDispatcher(Context context) {
        this.context = context;
        this.lost_bus.register(this);
        this.requirement_bus.register(this);
    }

    public void dispatchBeacon(String UUID, int major, int minor) {
        Log.v("dispatch beacon", "dispatching " + UUID);
        // url: http://sitenotifier.herokuapp.com/goods/spot
        // POST: uuid, major, minor, group - name of good or name of station (String), [maxPrice, minPrice]
        // lost: {alert => true/false}
        // store: {'text' => true/false}
        Bundle params = new Bundle();
        params.putString("uuid", UUID);
        params.putInt("major", major);
        params.putInt("minor", minor);

        final String method = "GET";
        switch(major) {
            case MAJOR_METRO:
            case MAJOR_STORE:
            case MAJOR_RESTAURANT:
            case MAJOR_MEMORIAL:
                RequirementModel model = pluckType(major, "/goods/spot.json", method, params, requirement_bus);
                break;
            case MAJOR_LOST:
                JSONGetter jsonGetter = new JSONGetter("/goods/spot.json", method, params, lost_bus);
                jsonGetter.execute((Void) null);
                String uuid = "00000000-0000-0000-5545-600000000000";
                if (UUID.equals(uuid)) {
                    JSONObject result = new JSONObject();
                    try {
                        result.put("alert", true);
                        dispatchLostResult(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Nullable
    public RequirementModel pluckType(int major, String suburl, String method, Bundle params, Bus requirement_bus) {
        RequirementHelper requirementHelper = new RequirementHelper(context);
        int type = RequirementModel.TYPE_FOOD;
        if (major == MAJOR_METRO)
            type = RequirementModel.TYPE_TRANSPORT;
        if (major == MAJOR_STORE)
            type = RequirementModel.TYPE_STORE;
        if (major == MAJOR_MEMORIAL)
            type = RequirementModel.TYPE_MEMORIAL;
        ArrayList<RequirementModel> models = requirementHelper.getModelByType(type);

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
                    params.putInt("idspot", model.getId());
                }
                requirementModel = model;
                if(finish_flag)
                    return null;
                JSONGetter jsonGetter = new JSONGetter("/goods/spot.json", method, params, requirement_bus);
                jsonGetter.execute((Void) null);

                String uuid = "00000000-0000-0000-5545-600000000000";
                if (params.getString("uuid").equals(uuid)) {
                    if (params.getInt("major") == 2) {
                        dispatchTypedResult(model);
                    }
                }
            }
        }
        return null;
    }

    public void dispatchTypedResult(RequirementModel model) {
        GregorianCalendar now = new GregorianCalendar();
        model.setFoundTime(now);

        RequirementHelper requirementHelper = new RequirementHelper(context);
        requirementHelper.updateModel(model);

        String[] foundHeaders = context.getResources().getStringArray(R.array.headers_founds_array);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
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
                                .setSmallIcon(android.R.drawable.ic_dialog_alert)
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
