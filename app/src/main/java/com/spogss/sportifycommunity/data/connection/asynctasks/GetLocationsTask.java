package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.Coordinate;
import com.spogss.sportifycommunity.data.Location;
import com.spogss.sportifycommunity.data.LocationType;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;

import java.util.Collection;

public class GetLocationsTask extends ClientTask<Void, Void, Collection<Location>> {
    private int userid;
    private SportifyClient client;

    public GetLocationsTask(int userid) {
        this.userid = userid;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Collection<Location> doInBackground(Void... params) {

        return client.getLocations(userid);
    }

    @Override
    protected void onPostExecute(final Collection<Location> locations) {
        getListener().onSuccess(QueryType.GET_LOCATIONS, locations);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.GET_LOCATIONS);
    }
}
