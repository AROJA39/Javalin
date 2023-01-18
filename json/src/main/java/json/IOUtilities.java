package json;

import java.io.*;
import java.io.InputStreamReader;

public class IOUtilities {
    public static final String readFullyAsString(InputStream is ) throws IOException {
        BufferedReader brBufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuffer stringBuffer = new StringBuffer();
        String aux = null;
        while ((aux = brBufferedReader.readLine()) != null) {
            stringBuffer.append(aux);
        }
        return stringBuffer.toString();
    }
}
 
