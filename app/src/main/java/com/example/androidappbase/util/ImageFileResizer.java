package com.example.androidappbase.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageFileResizer {

    private final static int MAX_PIXEL = 640;


    public static File resizedFile(@NonNull Context context, @NonNull File file){
        Matrix matrix = new Matrix();
        matrix = getResizedMatrix(file, matrix);
        matrix = getRotatedMatrix(file, matrix);
        File resizedFile = getTemporaryTransformedFile(context,file, matrix);
        return resizedFile;
    }

    /**
     * リサイズするマトリクスを取得
     * 縮小の場合のみ、縮小のマトリクスをセットして返す
     *
     * @param file 入力画像
     * @param matrix 元のマトリクス
     * @return matrix リサイズ後のマトリクス
     */
    public static Matrix getResizedMatrix(File file, Matrix matrix) {
        // リサイズチェック用にメタデータ読み込み
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        int height = options.outHeight;
        int width = options.outWidth;

        // リサイズ比の取得（画像の短辺がMAX_PIXELになる比を求めます）
        float scale = Math.max((float) MAX_PIXEL / width, (float) MAX_PIXEL / height);

        // 縮小のみのため、scaleは1.0未満の場合のみマトリクス設定
        if (scale < 1.0) {
            matrix.postScale(scale, scale);
        }

        return matrix;
    }

    /**
     * 画像の回転後のマトリクスを取得
     *
     * @param file 入力画像
     * @param matrix 元のマトリクス
     * @return matrix 回転後のマトリクス
     */
    public static Matrix getRotatedMatrix(File file, Matrix matrix){
        ExifInterface exifInterface = null;

        try {
            exifInterface = new ExifInterface(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return matrix;
        }

        // 画像の向きを取得
        int orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        // 画像を回転させる処理をマトリクスに追加
        switch (orientation) {
            case ExifInterface.ORIENTATION_UNDEFINED:
                break;
            case ExifInterface.ORIENTATION_NORMAL:
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                // 水平方向にリフレクト
                matrix.postScale(-1f, 1f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                // 180度回転
                matrix.postRotate(180f);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                // 垂直方向にリフレクト
                matrix.postScale(1f, -1f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                // 反時計回り90度回転
                matrix.postRotate(90f);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                // 時計回り90度回転し、垂直方向にリフレクト
                matrix.postRotate(-90f);
                matrix.postScale(1f, -1f);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                // 反時計回り90度回転し、垂直方向にリフレクト
                matrix.postRotate(90f);
                matrix.postScale(1f, -1f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                // 反時計回りに270度回転（時計回りに90度回転）
                matrix.postRotate(-90f);
                break;
        }
        return matrix;
    }

    /**
     * リサイズ・回転後の画像を取得
     * 一時ファイルを作成する
     *
     * @param file オリジナル画像
     * @param matrix 回転・縮小を設定したマトリクス
     * @return file 一時保存先のファイル
     */
    public static File getTemporaryTransformedFile(Context context, File file, Matrix matrix) {
        // 元画像の取得
        Bitmap originalPicture = BitmapFactory.decodeFile(file.getPath());
        int height = originalPicture.getHeight();
        int width = originalPicture.getWidth();

        // マトリクスをつけることで縮小、向きを反映した画像を生成
        Bitmap resizedPicture = Bitmap.createBitmap(originalPicture, 0, 0, width, height, matrix, true);

        // 一時ファイルの保存
        try {
            LogUtils.d(context.getCacheDir().toString());
            File destination = new File(context.getCacheDir() + "temp.jpg");
            FileOutputStream outputStream = new FileOutputStream(destination);
            resizedPicture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // ファイルのインスタンスをリサイズ後のものに変更
            file = destination;
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogUtils.d(resizedPicture.getHeight());
        LogUtils.d(resizedPicture.getWidth());

        return file;
    }


}
