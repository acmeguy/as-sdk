package com.activitystream.core.model.interfaces;

import com.activitystream.core.model.stream.CustomerEvent;

public interface BaseSubEvent {

    /************  Utility Functions ************/

    void detach();

    void reattach(CustomerEvent parentEvent);

    CustomerEvent getParentEvent();

}
