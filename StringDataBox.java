package edu.berkeley.cs186.database.databox;

import java.nio.charset.Charset;

public class StringDataBox extends DataBox {
    private String s;

    // Construct an m-byte string. If s has more than m-bytes, it is truncated to
    // its first m bytes. If s has fewer than m bytes, it is padded with null bytes
    // until it is exactly m bytes long.
    //
    //   - new StringDataBox("123", 5).getString()     == "123\x0\x0"
    //   - new StringDataBox("12345", 5).getString()   == "12345"
    //   - new StringDataBox("1234567", 5).getString() == "12345"
    public StringDataBox(String s, int m) {
        if (m <= 0) {
            String msg = String.format("Cannot construct a %d-byte string. " +
                                       "Strings must be at least one byte.", m);
            throw new DataBoxException(msg);
        }

        if (m < s.length()) {
            this.s = s.substring(0, m);
        } else {
            this.s = s + new String(new char[m - s.length()]);
        }
        assert(this.s.length() == m);
    }

    @Override
    public Type type() {
        return Type.stringType(s.length());
    }

    @Override
    public String getString() {
        return s.indexOf(0) < 0 ? s : s.substring(0, s.indexOf(0));
    }

    @Override
    public byte[] toBytes() {
        return s.getBytes(Charset.forName("ascii"));
    }

    @Override
    public String toString() {
        // TODO(proj0): replace the following line with `return s;`
        return "FIX ME";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StringDataBox)) {
            return false;
        }
        StringDataBox s = (StringDataBox) o;
        // Trim off trailing null bytes for string matching
        String ts = this.s.replaceAll("\0*$", "");
        String os = s.s.replaceAll("\0*$", "");
        return os.equals(ts);
    }

    @Override
    public int hashCode() {
        return s.hashCode();
    }

    @Override
    public int compareTo(DataBox d) {
        if (!(d instanceof StringDataBox)) {
            String err = String.format("Invalid comparison between %s and %s.",
                                       toString(), d.toString());
            throw new DataBoxException(err);
        }
        StringDataBox s = (StringDataBox) d;
        return this.s.compareTo(s.s);
    }
}
