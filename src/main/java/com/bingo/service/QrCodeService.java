package com.bingo.service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Service
public class QrCodeService {

    /**
     * Genera un código QR para una URL dada.
     * 
     * @param url    La URL que codificará el QR (ej:
     *               http://192.168.1.10:8080/player/join)
     * @param width  El ancho de la imagen en píxeles.
     * @param height La altura de la imagen en píxeles.
     * @return Un array de bytes que representa la imagen PNG del QR.
     * @throws Exception si la generación falla.
     */
    public byte[] generateQrCode(String url, int width, int height) throws Exception {

        // 1. Configuración del código QR
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // Nivel de corrección de error (H=Alto, permite escanear aunque esté dañado)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // Codificación de caracteres (UTF-8)
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // 2. Crear la matriz de bits del código QR
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height, hints);

        // 3. Escribir la matriz de bits como una imagen PNG
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }
}
