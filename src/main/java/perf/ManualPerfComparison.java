package perf;

import java.io.*;

import com.ning.jcbm.lzma.LzmaDriver;

/**
 * Simple manual performance micro-benchmark that compares compress and
 * decompress speeds of this LZF implementation with other codecs.
 */
public class ManualPerfComparison
{
    private final LzmaDriver driver = new LzmaDriver();
    
    private int size = 0;

    private final byte[] _uncompressed;

    private ManualPerfComparison(byte[] input) {
        _uncompressed = input;
    }
    
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1) {
            System.err.println("Usage: java ... [file]");
            System.exit(1);
        }
        File f = new File(args[0]);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream((int) f.length());
        byte[] buffer = new byte[4000];
        int count;
        FileInputStream in = new FileInputStream(f);
        
        while ((count = in.read(buffer)) > 0) {
            bytes.write(buffer, 0, count);
        }
        in.close();
        new ManualPerfComparison(bytes.toByteArray()).test();
    }
    
    private void test() throws Exception
    {

//        int i = 0;
        // Let's try to guestimate suitable size... to get to 5 megs to process
        final int REPS = (int) ((double) (5 * 1000 * 1000) / (double) _uncompressed.length);

        System.out.println("Read "+_uncompressed.length+" bytes to compress, uncompress; will do "+REPS+" repetitions");

        while (true) {
            try {  Thread.sleep(100L); } catch (InterruptedException ie) { }
//            int round = (i++ % 4);
            int round = 0;

            String msg;
            boolean lf = (round == 0);

            long msecs;
            
            switch (round) {

            case 0:
                msg = "LZMA compress/block";
                msecs = testLZMACompress(REPS, _uncompressed);
                break;
            default:
                throw new Error();
            }
            
            if (lf) {
                System.out.println();
            }
            System.out.println("Test '"+msg+"' ["+size+" bytes] -> "+msecs+" msecs");
        }
    }
    
    private final long testLZMACompress(int REPS, byte[] input) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(input.length);
        long start = System.currentTimeMillis();
        while (--REPS >= 0) {
            out.reset();
            driver.compressToStream(input, out);
        }
        size = out.size();
        long time = System.currentTimeMillis() - start;

        // due to a "feature" of LZMA-java, looks like we leak threads like crazy...
        System.gc();
        Thread.sleep(50L);

        return time;
    }
}
