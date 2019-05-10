package com.yg.bishe.util;

import com.tls.tls_sigature.tls_sigature;

public class UserSigUtil {
    public static String getUserSig(String name){
        String priKeyContent = "-----BEGIN PRIVATE KEY-----\n" +
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg6R+WnD/QcDG6MU2Q\n" +
                "FLtWdMyVixYQh6jeVi8oJ+3sTjShRANCAAQ661wguqZouXW1tXwyfQl7Y+IAZFDY\n" +
                "UDGUmh4c2EF3LA1arogFZAk5UpmlDatb9NN68EvWX56b5HlEIa0eu2/Y\n" +
                "-----END PRIVATE KEY-----\n";
        tls_sigature.GenTLSSignatureResult result = tls_sigature.genSig(1400208403, name, priKeyContent);
        return result.urlSig;
    }
}
