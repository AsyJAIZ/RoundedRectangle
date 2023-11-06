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
    Boolean material1 = true;
    String system = "android";
    String ui = "com.android.systemui";
    String resources;
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        XModuleResources res = XModuleResources.createInstance(resources, null);
        if (resParam.packageName.equals(system)) {
            XResources.setSystemWideReplacement(system, "bool", "config_useRoundIcon", false);
            XResources.setSystemWideReplacement(system, "dimen", "config_bottomDialogCornerRadius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            XResources.setSystemWideReplacement(system, "dimen", "config_dialogCornerRadius",
                    res.fwd(R.dimen.dialogCornerRadius));
            XResources.setSystemWideReplacement(system, "string", "config_icon_mask",
                    res.fwd(R.string.config_icon_mask));

            if (material1) {
                // TODO: change text size
                XResources.setSystemWideReplacement(system, "dimen", "notification_headerless_min_height",
                        res.fwd(R.dimen.notification_headerless_min_height));

                // alternate_expand_target
                XResources.setSystemWideReplacement(system, "dimen", "notification_content_margin_start",
                        res.fwd(R.dimen.notification_headerless_min_height));

                XResources.setSystemWideReplacement(system, "dimen", "notification_icon_circle_start",
                        res.fwd(R.dimen.notification_icon_circle_start));
                XResources.setSystemWideReplacement(system, "dimen", "notification_icon_circle_padding",
                        res.fwd(R.dimen.notification_icon_circle_padding));
                XResources.setSystemWideReplacement(system, "dimen", "notification_icon_circle_size",
                        res.fwd(R.dimen.notification_icon_circle_size));

                if (!authentic)
                    XResources.setSystemWideReplacement(system, "drawable", "notification_icon_circle",
                        res.fwd(R.drawable.notification_icon_circle_m));

                // XResources.hookSystemWideLayout();
            } else {
                if (!authentic) {
                    XResources.setSystemWideReplacement(system, "drawable", "conversation_badge_background",
                            res.fwd(R.drawable.conversation_badge_background));
                    XResources.setSystemWideReplacement(system, "drawable", "conversation_badge_ring",
                            res.fwd(R.drawable.conversation_badge_ring));
                }

                XResources.setSystemWideReplacement(system, "drawable", "notification_icon_circle",
                        res.fwd(R.drawable.notification_icon_circle));
            }

        } else if (resParam.packageName.equals(ui)) {
            resParam.res.setReplacement(ui, "dimen", "global_actions_corner_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_corner_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_track_inset",
                    res.fwd(R.dimen.dialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "rounded_slider_background_rounded_corner",
                    res.fwd(R.dimen.rounded_slider_background_rounded_corner));
            /*resParam.res.setReplacement(ui, "dimen", "qs_security_footer_corner_radius",
                    new XResources.DimensionReplacement(4.0f, TypedValue.COMPLEX_UNIT_DIP));*/
            resParam.res.setReplacement(ui, "dimen", "qs_corner_radius",
                    res.fwd(R.dimen.qs_corner_radius));
            resParam.res.setReplacement(ui, "dimen", "qs_media_album_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius",
                    res.fwd(R.dimen.bottomDialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "notification_corner_radius_small",
                    res.fwd(R.dimen.dialogCornerRadius));
            resParam.res.setReplacement(ui, "dimen", "notification_scrim_corner_radius",
                    new XResources.DimensionReplacement((32.0f/28.0f)*4.0f, TypedValue.COMPLEX_UNIT_DIP));

            /*resParam.res.setReplacement(ui, "drawable", "qs_tile_background_shape",
                    res.fwd(R.drawable.qs_tile_background_shape));
            resParam.res.setReplacement(ui, "drawable", "qs_media_outline_layout_bg",
                    res.fwd(R.drawable.qs_media_outline_layout_bg));
            resParam.res.setReplacement(ui, "drawable", "qs_media_solid_button",
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
