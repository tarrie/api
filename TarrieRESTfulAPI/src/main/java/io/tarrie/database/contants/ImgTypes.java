package io.tarrie.database.contants;

import java.util.Arrays;
import java.util.HashSet;

public class ImgTypes {
    public static final String GIF = "image/gif";
    public static final String JPG = "image/jpg";
    public static final String JPEG = "image/jpeg";
    public static final String PNG =  "image/png";
    public static final HashSet<String> ACCEPTABLE_MIME_IMAGES =
            new HashSet<String>(Arrays.asList("image/gif", "image/jpg", "image/jpeg", "image/png"));

}
