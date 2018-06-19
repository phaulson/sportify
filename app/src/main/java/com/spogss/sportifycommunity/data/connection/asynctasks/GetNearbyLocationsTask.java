package com.spogss.sportifycommunity.data.connection.asynctasks;

import com.spogss.sportifycommunity.data.Coordinate;
import com.spogss.sportifycommunity.data.Location;
import com.spogss.sportifycommunity.data.LocationType;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.fragment.TabFragmentSearch;

import java.util.ArrayList;
import java.util.Collection;

public class GetNearbyLocationsTask extends ClientTask<Void, Void, Collection<Location>>{
    private Coordinate position;
    private double radius;
    private SportifyClient client;
    private Collection<LocationType> types;

    public GetNearbyLocationsTask(Coordinate position, double radius, Collection<LocationType> types) {
        this.position = position;
        this.radius = radius;
        this.types = types;
        client = SportifyClient.newInstance();
    }

    @Override
    protected Collection<Location> doInBackground(Void... params) {

        return client.getNearbyLocations(position,radius,types);
    }

    @Override
    protected void onPostExecute(final Collection<Location> locations) {
        getListener().onSuccess(QueryType.GET_NEARBY_LOCATIONS, locations);
    }

    @Override
    protected void onCancelled() {
        getListener().onSuccess(QueryType.GET_NEARBY_LOCATIONS);
    }
}
