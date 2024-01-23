package com.alexandreconrado.easylife.scripts;

import android.content.Context;

import androidx.biometric.BiometricManager;

public class BiometricChecker {

    private final Context context;

    public BiometricChecker(Context context) {
        this.context = context;
    }

    public boolean isFaceIdSupported() {
        BiometricManager biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    public boolean isFingerprintSupported() {
        BiometricManager biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    public boolean isBiometricSupported() {
        BiometricManager biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }
}
