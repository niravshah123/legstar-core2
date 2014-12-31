package legstar.samples.custdat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.legstar.base.context.CobolContext;
import com.legstar.base.context.EbcdicCobolContext;
import com.legstar.base.converter.Cob2ObjectConverter;

/**
 * Sample code that invokes a converter to transform host data read off a file
 * into a java Hash Map.
 * <p/>
 * This sample uses the {@link CobolCustomerData} class that was generated by
 * the legstar base generator starting from the CUSTDAT COBOL copybook.
 * <p/>
 * In this sample the host data is read from a file for convenience but it could
 * come from any source (RPC, JMS, ...).
 *
 */
public class CustdatSample {

    public static void main(final String[] args) {
        CustdatSample main = new CustdatSample();
        main.convert(args[0]);
    }

    public void convert(String filePath) {

        try {
            // Characteristics (such as code page) of the mainframe data
            CobolContext cobolContext = new EbcdicCobolContext();

            // The main COBOL structure model (generated by
            // legstar-base-generator)
            CobolCustomerData cobolType = new CobolCustomerData();

            // Fill a buffer with enough mainframe data
            byte[] hostData = readFromFile(filePath, cobolType.getMaxBytesLen());

            // Convert the mainframe data to java
            Cob2ObjectConverter converter = new Cob2ObjectConverter(cobolContext,
                    hostData, 0);
            converter.visit(cobolType);

            // Print out the result hash map
            System.out.println(converter.getResultObject());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Fill a buffer with raw bytes read off a file.
     * 
     * @param filePath the input file location on file system
     * @param length the number of bytes to read off the file
     * @return a buffer filled with raw host data
     * @throws IOException if file cannot be read
     */
    private byte[] readFromFile(String filePath, int length) throws IOException {
        byte[] buffer = new byte[length];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filePath));
            int r = buffer.length;
            while (r > 0) {
                int p = buffer.length - r;
                int c = fis.read(buffer, p, r);
                if (c == -1) {
                    break;
                }
                r -= c;
            }
            return buffer;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

    }

}
