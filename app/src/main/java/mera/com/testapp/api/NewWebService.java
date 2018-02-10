package mera.com.testapp.api;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import mera.com.testapp.api.db.DatabaseHelper;
import mera.com.testapp.api.models.State;
import mera.com.testapp.api.models.States;
import retrofit2.Call;

public class NewWebService extends IntentService {
    public static final String STATES_UPDATED_ACTION = "states_updated";

    public NewWebService() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<State> statesArray = new ArrayList<>();
        WebApiManager wm = new WebApiManager();
        Call<States> call = wm.getWebApiInterface().getStates();
        try
        {
            States states = wm.execute(call);
            if (states != null)
            {
                ArrayList<ArrayList<String>> statesRaw = states.getStates();
                for (ArrayList<String> stateRaw : statesRaw)
                {
                    statesArray.add(State.parse(stateRaw));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseHelper helper = DatabaseHelper.getInstance(getApplicationContext());
        helper.delete();
        helper.insert(statesArray);

        sendBroadcast(new Intent(STATES_UPDATED_ACTION));

    }
}
