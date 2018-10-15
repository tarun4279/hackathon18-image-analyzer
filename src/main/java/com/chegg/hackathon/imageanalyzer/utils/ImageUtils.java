package com.chegg.hackathon.imageanalyzer.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.protobuf.ByteString;

public class ImageUtils {

	
	public static  ByteString convertRemoteImageToByteString(String remoteImageUrl) throws IOException {
		
		URL url = new URL(remoteImageUrl);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
         
        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
     
        return ByteString.copyFrom(output.toByteArray());
		
	}
	
	
}
