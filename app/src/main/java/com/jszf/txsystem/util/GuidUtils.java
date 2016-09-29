package com.jszf.txsystem.util;

import android.util.Log;

import java.util.UUID;

/**
 * @author jacking
 *         Created at 2016/9/8 9:55 .
 */
public class GuidUtils {

    public static String getVarUUID(){

        UUID uuid = UUID.randomUUID();

        String uniqueId = uuid.toString();

        Log.d("debug", "----->UUID" + uuid);

        return uniqueId;

    }
}
