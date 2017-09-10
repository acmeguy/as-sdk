package com.activitystream.model.interfaces;

import com.activitystream.model.aspects.AspectManager;

public interface HasAspects extends LinkedElement {

    AspectManager getAspectManager();

}
