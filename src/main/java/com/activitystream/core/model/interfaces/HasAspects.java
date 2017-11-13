package com.activitystream.core.model.interfaces;

import com.activitystream.core.model.aspects.AspectManager;

public interface HasAspects extends LinkedElement {

    AspectManager getAspectManager();

}
