package com.aj.roundrectoverlay;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.util.TypedValue;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookInitPackageResources, IXposedHookZygoteInit, IXposedHookLoadPackage {
    Boolean authentic = true;
    String system = "android";
    String ui = "com.android.systemui";
    String resources;
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (resParam.packageName.equals(system)) {
            XResources.setSystemWideReplacement(system, "bool", "config_useRoundIcon", false);
            XResources.setSystemWideReplacement(system, "dimen", "config_bottomDialogCornerRadius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            XResources.setSystemWideReplacement(system, "dimen", "config_dialogCornerRadius",
                    new XResources.DimensionReplacement(2.0f, TypedValue.COMPLEX_UNIT_DIP));
            XResources.setSystemWideReplacement(system, "string", "config_icon_mask",
                    "M50,0L88,0 C94.4,0 100,5.4 100 12 L100,88 C100,94.6 94.6 100 88 100 L12,100 C5.4,100 0,94.6 0,88 L0 12 C0 5.4 5.4 0 12 0 L50,0 Z");

            XModuleResources res = XModuleResources.createInstance(resources, null);
            XResources.setSystemWideReplacement(system, "drawable", "notification_icon_circle",
                    res.fwd(R.drawable.notification_icon_circle));

            if (!authentic) {
                XResources.setSystemWideReplacement(system, "drawable", "conversation_badge_background",
                        res.fwd(R.drawable.conversation_badge_background));
                XResources.setSystemWideReplacement(system, "drawable", "conversation_badge_ring",
                        res.fwd(R.drawable.conversation_badge_ring));
            }

        } else if (resParam.packageName.equals(ui)) {
            resParam.res.setReplacement(ui, "dimen", "global_actions_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_track_inset",
                    new XResources.DimensionReplacement(2.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_background_rounded_corner",
                    new XResources.DimensionReplacement(10.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "qs_security_footer_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "qs_corner_radius",
                    new XResources.DimensionReplacement(6.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "qs_media_album_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius_small",
                    new XResources.DimensionReplacement(2.0f, TypedValue.COMPLEX_UNIT_DIP));
            resParam.res.setReplacement(ui, "dimen", "notification_scrim_corner_radius",
                    new XResources.DimensionReplacement((32.0f/28.0f)*4.0f, TypedValue.COMPLEX_UNIT_DIP));

            XModuleResources res = XModuleResources.createInstance(resources, null);
            resParam.res.setReplacement(ui, "drawable", "qs_tile_background_shape",
                    res.fwd(R.drawable.qs_tile_background_shape));
            resParam.res.setReplacement(ui, "drawable", "qs_media_outline_layout_bg",
                    res.fwd(R.drawable.qs_media_outline_layout_bg));
            /*resParam.res.setReplacement(ui, "drawable", "qs_media_solid_button",
                    res.fwd(R.drawable.qs_media_solid_button));*/
            /*resParam.res.setReplacement(ui, "dimen", "qs_media_album_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));*/
            //<dimen name="qs_media_album_radius">14dp</dimen>
        }

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        resources = startupParam.modulePath;
    }

    String setLib = "com.android.settingslib";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals(setLib) || loadPackageParam.packageName.equals(ui)) {
            /*findAndHookMethod(setLib + ".notification.ConversationIconFactory.ConversationIconDrawable",
                    loadPackageParam.classLoader,
                    "draw",
                    Canvas.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            setObjectField(param.thisObject, "CENTER_RADIUS", 5.0f);
                        /*Drawable badgeIcon = (Drawable) getObjectField(param.thisObject, "mBadgeIcon");
                        int pos = (int) getIntField(param.thisObject, "badgeCenter");
                        Paint paint = (Paint) getObjectField(param.thisObject, "mPaddingPaint");
                        //int rad = (int) getIntField(param.thisObject, "radius");
                        if (badgeIcon != null) {
                            Canvas canvas = (Canvas) param.args[0];
                            canvas.drawRoundRect(0, 0, pos*2, pos*2, 2.0f, 2.0f, paint);
                        }*
                        }

                        //@Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Drawable badgeIcon = (Drawable) getObjectField(param.thisObject, "mBadgeIcon");
                            int pos = (int) getIntField(param.thisObject, "badgeCenter");
                            Paint paint = (Paint) getObjectField(param.thisObject, "mPaddingPaint");
                            //int rad = (int) getIntField(param.thisObject, "radius");
                            if (badgeIcon != null) {
                                Canvas canvas = (Canvas) param.args[0];
                                canvas.drawRoundRect(0, 0, pos*2, pos*2, 2.0f, 2.0f, paint);
                            }
                        }
                    });*/
        } else if (loadPackageParam.packageName.equals(system)) {
            /*findAndHookMethod("com.android.internal.widget.ConversationLayout",
                    loadPackageParam.classLoader,
                    "onFinishInflate",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Thread.dumpStack();
                        }
                    });*/
        }
    }
}
