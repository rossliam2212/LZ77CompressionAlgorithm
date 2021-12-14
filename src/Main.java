public class Main {

    public static void main(String[] args) {
        // Charstream from first LZ77 example on moodle
        String charstream = "AAABBAAAAABCCBAAABCDDDBABCDEEDCBBDEBBEDBABDCCDBAAABDCBAAAAABBAAA";

        // Charstream from second LZ77 example on moodle
//        String charstream = "AABBBBAAABCCCCBABCADADCBCADADADCCDADADACBCDADACBABCCCCBAAABBBBAA";

        Encoder encoder = new Encoder(charstream);
        encoder.startEncoding();
    }
}
