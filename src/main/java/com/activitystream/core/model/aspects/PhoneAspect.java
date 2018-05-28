package com.activitystream.core.model.aspects;

import com.activitystream.sdk.ASConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhoneAspect extends AbstractMapAspect {
    public static final AspectType ASPECT_TYPE = new AspectType(ASConstants.ASPECTS_PHONE, PhoneAspect::new);

    protected static final Logger logger = LoggerFactory.getLogger(PhoneAspect.class);

    public PhoneAspect() {}

    @Override
    public void loadFromValue(Object value) {
        super.loadFromValue(value);
    }

    @Override
    public AspectType getAspectType() {
        return ASPECT_TYPE;
    }

   /* @Override
    public Object put(Object key, Object value) {

    }
    /*
    *
    *
    * country_code
	Country code for number

	area_code
	Area code for number

	number		Phone number

	number_type
	One of: "Mobile", "Home", "Work"

	is_anonymized
    *
    *
    * */
}
