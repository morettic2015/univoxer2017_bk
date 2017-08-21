-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-keep class org.apache.commons.logging.**

-keep class net.gotev.** { *; }
-keep class org.pjsip.pjsua2.** { *; }
-keep class com.google.android.gms.** { *; }

-keep public enum com.univoxer.android.network.RequestManager$** {
    **[] $VALUES;
    public *;
}

#Fucking Gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }