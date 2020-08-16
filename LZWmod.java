import java.util.Arrays;

public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W
    private static int W = 9;         // codeword width
    private static final int M = 65536;
    private static final int X = 16;

    public static void compress(boolean reset) {
        TSTmod<Integer> st = new TSTmod<Integer>();
        for (int i = 0; i < R; i++) {
            StringBuilder init = new StringBuilder();
            st.put(init.append((char)i), i);
            //System.out.println(init + ": " + st.get(init));
        }
        int code = R+1;  // R is codeword for EOF

        StringBuilder input = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        input.append(BinaryStdIn.readChar());

        BinaryStdOut.write(reset);
        while (!BinaryStdIn.isEmpty()) {
            temp = new StringBuilder(input);
            temp.append(BinaryStdIn.readChar());

            if (!st.contains(temp) && st.contains(input)) {
                BinaryStdOut.write(st.get(input), W);      // Print s's encoding.
                //System.err.println(input + ": " + st.get(input));
                //System.out.println(st.get(input));
                if (code < M)   st.put(temp, code++);
                if (code == L && W < X && L < M) {
                    W++;
                    L = (int)Math.pow(2, W);
                }

                if (code == M && reset) {
                    System.err.println("reset");
                    st = new TSTmod<Integer>();
                    W = 9;
                    L = 512;
                    for (int i = 0; i < R; i++) {
                        StringBuilder init = new StringBuilder();
                        st.put(init.append((char)i), i);
                    }
                    code = R+1;  // R is codeword for EOF
                }

                //System.out.println("" + temp + ": " + st.get(temp));
                input = new StringBuilder();
                input.append(temp.charAt(temp.length()-1));
            }
            else
                input = temp;
        }
        BinaryStdOut.write(st.get(input), W);
        // System.err.println(input + ": " + st.get(input));

        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }


    public static void expand() {
        String[] st = new String[L];
        int i; // next available codeword value
        boolean reset = BinaryStdIn.readBoolean();

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            if ((i+1) == L && W < X && L < M) {
                W++;
                L = (int)Math.pow(2, W);
                st = Arrays.copyOf(st, L);
            }

            if ((i+1) == M && reset) {
                System.err.println("reset");
                W = 9;
                L = 512;
                st = Arrays.copyOf(st, L);
                i = R;
            }

            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            //System.err.println(codeword);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L && i < M) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if      (args[0].equals("-")) {
            if (args[1].equals("r"))
                compress(true);
            else if (args[1].equals("n"))
                compress(false);
        }
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}
