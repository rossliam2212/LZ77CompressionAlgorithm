public class Main {

    public static void main(String[] args) {
        // ----- Encoding -----

        // Charstream from first LZ77 example on moodle (8x8)
//        String charstream = "AAABBAAAAABCCBAAABCDDDBABCDEEDCBBDEBBEDBABDCCDBAAABDCBAAAAABBAAA";

        // Charstream from second LZ77 example on moodle (8x8)
        String charstream = "AABBBBAAABCCCCBABCADADCBCADADADCCDADADACBCDADACBABCCCCBAAABBBBAA";

        // Charstream from assignment question (16x16)
//        String charstream = "AAAAAAAAAAAAAAAAABBBBBBBBBBBBBBAABAAAAAAAAAAAABAABAAAAAAAAAAAABAABBBBBBBBBBBBBBAABBBBBBCCBDBBBBAABBBBBCDDCABBBBAABBBBCDEEDCBBBBAABBBCDEAAEDCBBBAABBCDEEEEEEDCBBAABBBDEDCCDEDBBBAABBBDECCCCEDBBBAABBCDECCCCEDCBBAABBBBBBBBBBBBBBAAAAAAAAAAAAAAAAAFFFFFFFFFFFFFFFF";
//
        Encoder encoder = new Encoder(charstream);
        encoder.startEncoding();


        // ----- Decoding -----

        // Codestream from first charstream
//        String codestream = "A A A B B <B:3> <9:3> C C <7:4> <9:2> D D D <7:2> <9:3> E E D C B B <9:2> <C:2> <7:2> B A <7:2> C C <9:3> A <7:4> <9:4> <7:3> <2:4>";

        // Codestream from second charstream
//        String codestream = "A A B B <E:2> <A:2> <9:2> C C <E:2> <7:2> <9:2> A D <E:2> <7:2> <9:5> <7:3> C <9:5> A C B <7:5> <9:2> A <7:2> C <E:2> <9:2> A <7:2> B <E:2> <9:2>";

        Decoder decoder = new Decoder(encoder.getCodestream());
        decoder.startDecoding();


        // -- Testing --
//        String test = "AAAAAAAAAAAAAAAAABBBBBBBBBBBBBBAABAAAAAAAAAAAABAABABABABABABABABABABABABCCBDACDDCBCCBDABCCBDCDEEDCCCBDABCCBCDEABEDCCBDABCCCDEEEEEEDCBDABCCBDEDCCDEDCCBABCCBDECCCCEDCCBABCCCDECCCCEDCCBABCCCBCCCBCCCBCCABABABABABABABABAFFFFFFFFFFFFFFFF";
//
//        for (int i = 0; i < charstream.length(); i++) {
//            if (charstream.charAt(i) != test.charAt(i)) {
//                System.out.println(i);
//                break;
//            }
//        }
    }
}
