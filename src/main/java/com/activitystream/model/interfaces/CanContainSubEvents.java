package com.activitystream.model.interfaces;

import com.activitystream.model.stream.AbstractBaseEvent;

import java.util.List;

public interface CanContainSubEvents {

    List<AbstractBaseEvent> getSubEvents();

}
