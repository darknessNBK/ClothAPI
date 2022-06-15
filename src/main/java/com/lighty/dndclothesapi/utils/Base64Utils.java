package com.lighty.dndclothesapi.utils;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class Base64Utils {
    @SneakyThrows
    public static BufferedImage base64StringToImg(final String base64String) {
        String base64 = base64String.split(",")[0];
        return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64)));
    }
    @SneakyThrows
    public static String imgToBase64String(final BufferedImage img, final String formatName) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final OutputStream b64os = Base64.getEncoder().wrap(os);
        ImageIO.write(img, formatName, b64os);
        return os.toString();
    }
}