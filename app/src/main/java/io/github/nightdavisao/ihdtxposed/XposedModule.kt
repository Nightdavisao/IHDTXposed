package io.github.nightdavisao.ihdtxposed

import android.os.Build
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage


class XposedModule: IXposedHookLoadPackage {
    override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam?) {
        if (param != null && param.packageName != "android") return

        val classLoader = param!!.classLoader
        // com.android.server.power.batterysaver.BatterySaverPolicy
        val policyClazz = XposedHelpers.findClass("com.android.server.power.batterysaver.BatterySaverPolicy\$Policy", classLoader)

        XposedBridge.hookAllConstructors(policyClazz, object : XC_MethodHook() {
            override fun afterHookedMethod(paramHook: MethodHookParam?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (paramHook != null) {
                        XposedHelpers.setBooleanField(paramHook.thisObject, "enableNightMode", false)
                    }
                }
            }
        })
    }
}