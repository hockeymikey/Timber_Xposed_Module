package proofofconcept.dontsuemeplz.timber;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.newInstance;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import org.json.JSONObject;



public class Main implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.tinder")) {
            return;
        }

        final Class class1 = XposedHelpers.findClass(Obfuscator.TINDER_MANAGER_CLASS_KEYS, lpparam.classLoader);

        //XposedHelpers.findClass("com.tinder.adapters.g$b", lpparam.classLoader); //FIXME what does it do?

        XposedBridge.hookAllMethods(class1, "a", new XC_MethodHook() { //FIXME still "a"?
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                for (final Object o : param.args) {
                    if (o != null) {
                        XposedBridge.log("curr=" + o.toString() + "- instanceOf=" + o.getClass());
                    }
                }
            }
        });

        findAndHookMethod("com.tinder.activities.ActivityMain", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Activity profile_activity = (Activity) param.thisObject;
                Toast.makeText(profile_activity.getApplicationContext(), "Timber for Tinder 4.2.3", Toast.LENGTH_SHORT).show();
            }
        });

        findAndHookMethod(Obfuscator.TINDER_MANAGER_CLASS_KEYS, lpparam.classLoader, Obfuscator.KEY_TINDER_PLUS_ENABLED_BOOLEAN, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log("Tinder KEY_TINDER_PLUS_ENABLED boolean");
                param.setResult(true);
            }
        });

        findAndHookMethod(Obfuscator.TINDER_MANAGER_CLASS_KEYS, lpparam.classLoader, Obfuscator.KEY_HAS_PLUS_SUBSCRIPTION_ENABLED_BOOLEAN, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log("Tinder KEY_HAS_PLUS_SUBSCRIPTION_ENABLED boolean");
                param.setResult(true);
            }
        });

        findAndHookMethod(Obfuscator.TINDER_MODEL_CLASS, lpparam.classLoader, Obfuscator.TINDER_HASHMAP, String.class, Object.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("Tinder HashMap " + param.args[0] + " " + param.args[1]);
                final String s = (String) param.args[0];
                if (s.equalsIgnoreCase("tinderPlus") || s.equalsIgnoreCase("KEY_TINDER_PLUS_ENABLED") || s.equalsIgnoreCase("KEY_TINDER_PLUS_ENABLED") || s.equalsIgnoreCase("KEY_HAS_PLUS_SUBSCRIPTION_ENABLED")) {
                    param.args[1] = true;
                }
                if (s.equalsIgnoreCase("HAS_VIEWED_ROADBLOCK")) {
                    param.args[1] = false;
                }
                if (s.equalsIgnoreCase("percentLikesLeft")) {
                    param.args[1] = "100";
                }
                if (s.equalsIgnoreCase("timeRemaining")) {
                    param.args[1] = "0";
                }
            }
        });

        findAndHookMethod(Obfuscator.TINDER_MANAGER_CLASS_NUMBERS, lpparam.classLoader, Obfuscator.GET_LONG, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("get Long =" + param.getResult());
                param.setResult(0L);
            }
        });

        findAndHookMethod(Obfuscator.TINDER_MANAGER_CLASS_NUMBERS, lpparam.classLoader, Obfuscator.GET_PERCENTAGE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("real % =" + param.getResult());
                param.setResult(100);
            }
        });

        findAndHookMethod(Obfuscator.TINDER_FRAGMENTS_CLASS, lpparam.classLoader, Obfuscator.NUMBER, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[0] = 100;
            }
        });
    }
}
