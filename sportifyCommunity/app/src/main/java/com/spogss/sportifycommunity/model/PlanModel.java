package com.spogss.sportifycommunity.model;

import com.spogss.sportifycommunity.data.Plan;

import java.io.Serializable;

/**
 * Created by Pauli on 25.04.2018.
 */

public class PlanModel implements Serializable {
    private Plan plan;
    private int numberOfSubscribers;
    private boolean subscribed;

    public PlanModel(Plan plan, int numberOfSubscribers, boolean subscribed) {
        this.plan = plan;
        this.numberOfSubscribers = numberOfSubscribers;
        this.subscribed = subscribed;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public int getNumberOfSubscribers() {
        return numberOfSubscribers;
    }

    public void setNumberOfSubscribers(int numberOfSubscribers) {
        this.numberOfSubscribers = numberOfSubscribers;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
