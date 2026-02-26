package ProjetAlgo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
/*auteur BENNABI ghiles Rayane
 *       BAOUCHE Mohamed Djaouad
 */
public class GenerateurCSV {

    // Tailles à tester 
    private static final int[] NS = {100, 1000, 2500, 5000, 7500, 10000, 30000, 50000, 75000, 100000};
    private static final int REPETITIONS = 7;
    private static final int WARMUP = 2;
    private static final long SEED = 123456789L;

    public static void main(String[] args) throws Exception {
        File outDir = new File("target/bench");
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new IllegalStateException("Impossible de créer : " + outDir.getPath());
        }

        File csv = new File(outDir, "results.csv");

        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(csv), StandardCharsets.UTF_8))) {

            out.println("n,mode,build_abr_ns,build_arn_ns,search_abr_ns,search_arn_ns");

            for (int n : NS) {
                System.out.println("Test en cours pour n = " + n + "...");
                writeLine(out, n, true);   // random
                writeLine(out, n, false);  // sorted
                out.flush();
            }
        }

        System.out.println("CSV généré : " + csv.getPath());
    }

    private static void writeLine(PrintWriter out, int n, boolean randomMode) {
        String mode = randomMode ? "random" : "sorted";
        Random rnd = new Random(SEED);

        ArrayList<Integer> keys = new ArrayList<>(n);
        for (int i = 0; i < n; i++) keys.add(i);
        if (randomMode) Collections.shuffle(keys, rnd); // sinon sorted (0..n-1)

        // warmup JVM
        for (int i = 0; i < WARMUP; i++) {
            ABR<Integer> abrW = new ABR<>();
            ARN<Integer> arnW = new ARN<>();
            build(abrW, keys);
            build(arnW, keys);
            search(abrW, 2 * n);
            search(arnW, 2 * n);
        }

        long buildAbr = 0, buildArn = 0, searchAbr = 0, searchArn = 0;

        for (int rep = 0; rep < REPETITIONS; rep++) {
            ABR<Integer> abr = new ABR<>();
            long t0 = System.nanoTime();
            build(abr, keys);
            long t1 = System.nanoTime();
            buildAbr += (t1 - t0);

            ARN<Integer> arn = new ARN<>();
            long t2 = System.nanoTime();
            build(arn, keys);
            long t3 = System.nanoTime();
            buildArn += (t3 - t2);

            long t4 = System.nanoTime();
            search(abr, 2 * n);
            long t5 = System.nanoTime();
            searchAbr += (t5 - t4);

            long t6 = System.nanoTime();
            search(arn, 2 * n);
            long t7 = System.nanoTime();
            searchArn += (t7 - t6);
        }

        buildAbr /= REPETITIONS;
        buildArn /= REPETITIONS;
        searchAbr /= REPETITIONS;
        searchArn /= REPETITIONS;

        out.printf(Locale.US, "%d,%s,%d,%d,%d,%d%n", n, mode, buildAbr, buildArn, searchAbr, searchArn);
    }

    private static void build(ABR<Integer> tree, Iterable<Integer> keys) {
        for (Integer k : keys) tree.add(k);
    }

    private static void build(ARN<Integer> tree, Iterable<Integer> keys) {
        for (Integer k : keys) tree.add(k);
    }

    private static void search(ABR<Integer> tree, int limitExclusive) {
        for (int x = 0; x < limitExclusive; x++) tree.contains(x);
    }

    private static void search(ARN<Integer> tree, int limitExclusive) {
        for (int x = 0; x < limitExclusive; x++) tree.contains(x);
    }
}