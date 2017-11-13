package com.activitystream.core.model.interfaces;

import com.activitystream.core.model.stream.AbstractBaseEvent;

import java.util.List;

public interface CanContainSubEvents {

    List<AbstractBaseEvent> getSubEvents();

}
