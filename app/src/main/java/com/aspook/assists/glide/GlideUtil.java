package com.aspook.assists.glide;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;

import java.io.File;

/**
 * Created by ASPOOK on 17/7/20.
 */

public class GlideUtil {

    private GlideUtil() {

    }

    /**
     * common loading image method
     *
     * @param context
     * @param imgUrl
     * @param target
     */
    public static void loadImage(Context context, String imgUrl, ImageView target) {
        Glide.with(context).load(imgUrl).into(target);
    }

    /**
     * loading image with animation
     *
     * @param context
     * @param imgUrl
     * @param target
     */
    public static void loadImageWithCrossFade(Context context, String imgUrl, ImageView target) {
        Glide.with(context).load(imgUrl)
                .crossFade()
                .into(target);
    }

    /**
     * loading with setting animation or not
     *
     * @param context
     * @param imgUrl
     * @param target
     * @param needAnimation
     */
    public static void loadImage(Context context, String imgUrl, ImageView target, boolean needAnimation) {
        if (needAnimation) {
            loadImageWithCrossFade(context, imgUrl, target);
        } else {
            loadImage(context, imgUrl, target);
        }
    }

    /**
     * loading with placeholder image
     * <p>
     * NOTE:if use {@link com.aspook.assists.widget.CircleImageView}, needAnimation must be false
     *
     * @param context
     * @param imgUrl
     * @param target
     * @param needAnimation
     * @param placeHolderImgResId placeholder image or -1
     */
    public static void loadImageWithPlaceHolder(Context context, String imgUrl, ImageView target, boolean needAnimation, int placeHolderImgResId) {
        if (placeHolderImgResId >= 0) {
            if (needAnimation) {
                Glide.with(context).load(imgUrl)
                        .placeholder(placeHolderImgResId)
                        .crossFade()
                        .into(target);
            } else {
                Glide.with(context).load(imgUrl)
                        .dontAnimate()
                        .placeholder(placeHolderImgResId)
                        .into(target);
            }
        } else {
            loadImage(context, imgUrl, target, needAnimation);
        }
    }

    /**
     * loading with default image
     *
     * @param context
     * @param imgUrl
     * @param target
     * @param needAnimation
     * @param defaultImgResId default image or -1
     */
    public static void loadImageWithDefaultImage(Context context, String imgUrl, ImageView target, boolean needAnimation, int defaultImgResId) {
        if (defaultImgResId >= 0) {
            if (needAnimation) {
                Glide.with(context).load(imgUrl)
                        .error(defaultImgResId)
                        .crossFade()
                        .into(target);
            } else {
                Glide.with(context).load(imgUrl)
                        .error(defaultImgResId)
                        .into(target);
            }
        } else {
            loadImage(context, imgUrl, target, needAnimation);
        }
    }

    /**
     * loading with setting placeholder and default image
     * <p>
     * NOTE:if use {@link com.aspook.assists.widget.CircleImageView}, needAnimation must be false
     *
     * @param context
     * @param imgUrl
     * @param target
     * @param needAnimation
     * @param placeHolderImgResId placeholder image or -1
     * @param defaultImgResId     default image or -1
     */
    public static void loadImage(Context context, String imgUrl, final ImageView target, boolean needAnimation, int placeHolderImgResId, int defaultImgResId) {
        if (placeHolderImgResId >= 0) {
            if (defaultImgResId >= 0) {
                /**
                 * need placeholder image and default image
                 */
                if (needAnimation) {
                    /**
                     * need animation
                     */
                    Glide.with(context).load(imgUrl)
                            .placeholder(placeHolderImgResId)
                            .error(defaultImgResId)
                            .crossFade()
                            .into(target);
                } else {
                    /**
                     * don't need animation
                     */
                    Glide.with(context).load(imgUrl)
                            .dontAnimate()
                            .placeholder(placeHolderImgResId)
                            .error(defaultImgResId)
                            .into(target);
                }
            } else {
                /**
                 * need placeholder image, don't need default image
                 */
                loadImageWithPlaceHolder(context, imgUrl, target, needAnimation, placeHolderImgResId);
            }
        } else {
            if (defaultImgResId >= 0) {
                /**
                 * need default image, don't need placeholder image
                 */
                loadImageWithDefaultImage(context, imgUrl, target, needAnimation, defaultImgResId);
            } else {
                /**
                 * don't need default image and don't need placeholder image
                 */
                loadImage(context, imgUrl, target, needAnimation);
            }
        }
    }

    /**
     * clear disk cache , need async operation
     *
     * @param context
     */
    public static void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    /**
     * clear memory cache，must in mainThread
     *
     * @param context
     */
    public static void clearMemoryAsPossible(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * clear all image cache
     */
    public static void clearImageAllCache(Context context) {
        clearDiskCache(context);
        clearMemoryAsPossible(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    private static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
