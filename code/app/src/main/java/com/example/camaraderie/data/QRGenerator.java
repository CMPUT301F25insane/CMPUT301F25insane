package com.example.camaraderie.data;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * This function actually creates the QR code for the event we are making.
 */

public class QRGenerator {

    /**
     * This function takes in a text, width, and height and creates the QR code using the BitMap class
     * @param text
     * @param width
     * @param height
     * @return
     * It returns the created QR code in the form of a Bitmap
     */
    public Bitmap generateQRCode(String text, int width, int height){
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for(int x = 0; x<width; x++){
                for(int y = 0; y<height; y++){
                    bitmap.setPixel(x, y, matrix.get(x,y) ? Color.BLACK:Color.WHITE);
                }

            }
            return bitmap;
        } catch (WriterException e){
            e.printStackTrace();
            return null;
        }
        }
}
