public class Main {

    public static void main(String[] args) {
        // Charstream from first LZ77 example on moodle (8x8)
        String charstream = "AAABBAAAAABCCBAAABCDDDBABCDEEDCBBDEBBEDBABDCCDBAAABDCBAAAAABBAAA";

        // Charstream from second LZ77 example on moodle (8x8)
//        String charstream = "AABBBBAAABCCCCBABCADADCBCADADADCCDADADACBCDADACBABCCCCBAAABBBBAA";

        // Charstream from assignment question (16x16)
//        String charstream = "AAAAAAAAAAAAAAAAABBBBBBBBBBBBBBAABAAAAAAAAAAAABAABAAAAAAAAAAAABAABBBBBBBBBBBBBBAABBBBBBCCBDBBBBAABBBBBCDDCABBBBAABBBBCDEEDCBBBBAABBBCDEAAEDCBBBAABBCDEEEEEEDCBBAABBBDEDCCDEDBBBAABBBDECCCCEDBBBAABBCDECCCCEDCBBAABBBBBBBBBBBBBBAAAAAAAAAAAAAAAAAFFFFFFFFFFFFFFFF";

        Encoder encoder = new Encoder(charstream);
        encoder.startEncoding();
    }
}
