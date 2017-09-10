package com.activitystream.model.utils;

import com.activitystream.model.ASConstants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class StreamIdUtils {

    public static byte[] streamIdToBytes(UUID streamId) {
        if (streamId == null)
            return null;

        return ByteBuffer.allocate(16).order(ByteOrder.BIG_ENDIAN)
                .putLong(streamId.getMostSignificantBits()).putLong(streamId.getLeastSignificantBits()).array();
    }

    public static List<byte[]> streamIdsToBytes(Collection<UUID> streamIds) {
        List<byte[]> bytes = new ArrayList<>(streamIds.size());
        for (UUID streamId : streamIds) {
            bytes.add(streamIdToBytes(streamId));
        }
        return bytes;
    }

    public static UUID bytesToStreamId(byte[] bytes) {
        if (bytes == null)
            return null;

        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    /*
    footprint is a lower case entity_ref for entities
    footprint is a calculated value for events (repeatable)
    */
    public static UUID calculateStreamId(String footprint) {
        return UUID.nameUUIDFromBytes(footprint.getBytes());
    }

    public static UUID calculateStreamId(String entityType, String entityId) {
        return calculateStreamId((entityType.split(":")[0] + "/" + entityId).toLowerCase());
    }

    public static String streamIdToSqlValue(UUID streamId) {
        return "'" + Base64.getEncoder().encodeToString(streamIdToBytes(streamId)) + "'.decode('base64')";
    }
}
