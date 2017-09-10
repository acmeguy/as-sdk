package com.activitystream.model.aspects;

import com.activitystream.model.interfaces.AspectInterface;
import com.activitystream.model.interfaces.BaseStreamElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class AspectType {

    /**
     * How to handle new aspect values for entities that already have values.
     */
    public enum MergeStrategy {
        /**
         * New value replaces existing value.
         */
        REPLACE,
        /**
         * Fields missing from new value will be retained from previous value.
         */
        MERGE
    }

    public static final String ASPECT_SUPPORT_INHERITED = "inherited";
    public static final String ASPECT_SUPPORT_SUPPORTED = "supported";
    public static final String ASPECT_SUPPORT_STORED = "stored";

    public static final String ASPECT_INHERIT_VIA = "inherit_via";

    private static final Logger logger = LoggerFactory.getLogger(AspectType.class);

    private final String aspectSignature;
    private final Supplier<AspectInterface> aspectSupplier;
    private final MergeStrategy mergeStrategy;

    protected AspectType(String aspectSignature, Supplier<AspectInterface> aspectSupplier) {
        this(aspectSignature, aspectSupplier, null);
    }

    protected AspectType(String aspectSignature, Supplier<AspectInterface> aspectSupplier, MergeStrategy mergeStrategy) {
        this.aspectSignature = aspectSignature;
        this.aspectSupplier = aspectSupplier;
        this.mergeStrategy = mergeStrategy;
    }

    public String getAspectSignature() {
        return aspectSignature;
    }

    public MergeStrategy getMergeStrategy() {
        return mergeStrategy;
    }

    public boolean shouldCreateForEnrichment(BaseStreamElement root) {
        return false;
    }

    public AspectInterface newInstance() {
        return aspectSupplier.get();
    }

    public AspectInterface fromValue(Object value, BaseStreamElement root) {
        if (value == null)
            return null;
        AspectInterface newAspect = newInstance();
        newAspect.setRoot(root);
        newAspect.loadFromValue(value);
        return newAspect;
    }

    public static class Embedded extends AspectType {

        protected Embedded(String aspectSignature, Supplier<AspectInterface> aspectSupplier, MergeStrategy mergeStrategy) {
            super(aspectSignature, aspectSupplier, mergeStrategy);
        }

    }

}
