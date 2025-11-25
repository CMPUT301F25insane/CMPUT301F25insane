package com.example.camaraderie.data;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.graphics.Bitmap;
import android.graphics.Color;

public class QRGenerator {

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
