package com.activitystream.core.model.entities;

import java.io.Serializable;
import java.util.List;

public interface BaseEntity extends Serializable {


    /************  Utility Functions ************/

    /************  Analytics ************/


    String getElementType();

    List<String> getAllowedRelTypes();

}
