package com.activitystream.model.interfaces;

import com.activitystream.model.stream.CustomerEvent;

public interface BaseSubEvent {

    /************  Utility Functions ************/

    void detach();

    void reattach(CustomerEvent parentEvent);

    CustomerEvent getParentEvent();

}
