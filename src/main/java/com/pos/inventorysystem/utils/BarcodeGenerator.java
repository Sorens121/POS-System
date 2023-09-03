package com.pos.inventorysystem.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.canvas.Canvas;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BarcodeGenerator {
    public WritableImage generateBarcodeImage(String content) {
        try {
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.CODE_128, 280, 100);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            WritableImage image = new WritableImage(width, height);
            javafx.scene.paint.Color blackColor = Color.BLACK;
            javafx.scene.paint.Color whiteColor = Color.WHITE;

            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++) {
                    image.getPixelWriter().setColor(j, i, bitMatrix.get(j, i) ? blackColor : whiteColor);
                }
            }
            String filepath = "C:\\Users\\TROJAN HORSE\\Barcode\\" + content +".png";
            //saveImageToFile(image, filepath);

            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Canvas generateBarcodeImageWithAdditionalText(String content, String additionalText) {
        try{
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.CODE_128, 280, 100);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Canvas canvas = new Canvas(width, height + 30);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            Color blackColor = Color.BLACK;
            Color whiteColor = Color.WHITE;

            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    gc.setFill(bitMatrix.get(j, i) ? blackColor : whiteColor);
                    gc.fillRect(j, i, 1,1);
                }
            }

            gc.setFill(blackColor);
            gc.setFont(new Font(13));
            gc.fillText(additionalText, 5, height + 20);

            String filepath = "C:\\Users\\TROJAN HORSE\\Barcode\\" + content +".png";
            saveImageToFile(canvas, filepath);

            return canvas;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveImageToFile(Canvas canvas, String filePath) {
        File file = new File(filePath);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null), "png", file);
            System.out.println("Image saved to: " + filePath);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
