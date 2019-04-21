package com.example.imagesgallery.service.image.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Diskcache 관련 부분은 구글 개발자 사이트 코드 참고
 */

class DiskCache {
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 100;
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static final String DIR_NAME = "imagesgallery";

    private DiskLruCache mDiskCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.PNG;

    void init(Context context) {
        final File diskCacheDir = getDiskCacheDir(context, DIR_NAME);
        new InitDiskCacheTask().execute(diskCacheDir);
    }

    private class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                try {
                    File diskCacheDir = params[0];
                    mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, DISK_CACHE_SIZE);
                    mDiskCacheStarting = false; // Finished initialization
                    mDiskCacheLock.notifyAll(); // Wake any waiting threads
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor) throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0));
            return bitmap.compress(mCompressFormat, 100, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        File cacheDir;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null) {
                cacheDir = context.getExternalCacheDir();
            } else {
                cacheDir = context.getCacheDir();
            }
        } else {
            cacheDir = context.getCacheDir();
        }

        return new File(cacheDir.getPath() + File.separator + uniqueName);
    }

    boolean isExistDiskCache(String key) {
        return containsKey(key);
    }

    void addBitmapToDiskCache(String key, Bitmap data) {
        // Also add to disk cache
        synchronized (mDiskCacheLock) {
            DiskLruCache.Editor editor = null;
            try {
                editor = mDiskCache.edit(key);
                if (editor == null) {
                    return;
                }

                if (writeBitmapToFile(data, editor)) {
                    mDiskCache.flush();
                    editor.commit();
                } else {
                    editor.abort();
                }
            } catch (IOException e) {
                try {
                    if (editor != null) {
                        editor.abort();
                    }
                } catch (IOException ignored) {
                }
            }
        }

    }

    Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }

            Bitmap bitmap;
            DiskLruCache.Snapshot snapshot = null;
            try {

                snapshot = mDiskCache.get(key);
                if (snapshot == null) {
                    return null;
                }
                final InputStream in = snapshot.getInputStream(0);
                if (in != null) {
                    final BufferedInputStream buffIn = new BufferedInputStream(in);
                    bitmap = BitmapFactory.decodeStream(buffIn);
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (snapshot != null) {
                    snapshot.close();
                }
            }
        }
        return null;
    }

    private boolean containsKey(String key) {
        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(key);
            contained = snapshot != null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return contained;
    }

    void clearCache() {
        try {
            mDiskCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private static boolean isExternalStorageRemovable() {
            return Environment.isExternalStorageRemovable();
    }
}
