import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decoder {
    private String codetream;
    private List<String> codestreamArray;

    private String charstream;

    private ArrayList<String> window;
    private final int WINDOW_SIZE = 16;
    String zero = "", one = "", two = "", three = "", four = "", five = "", six = "", seven = "", eight = "", nine = "", a = "", b = "", c = "", d = "", e = "", f = "";


    public Decoder(String codestream) {
        this.codetream = codestream;
        charstream = "";
        codestreamArray = new ArrayList<>(Arrays.asList(codestream.split(" ")));
        window = new ArrayList<>();
    }

    /**
     * This method starts the decoding process.
     */
    public void startDecoding() {
        System.out.println(Colors.GREEN + "********************\n*** " + Colors.RESET + "LZ77 Decoder" + Colors.GREEN + " ***\n********************" + Colors.RESET);

        printCodestream();
        printTableStart();

        addCodestreamToWindow();

        printCharstream();
        System.out.println(charstream.length());
    }

    /**
     * This method is responsible for adding the codestream to the window.
     */
    private void addCodestreamToWindow() {
        String currentCode;
        int counter = 0; // -- For Testing --

        while (codestreamArray.size() != 0) {
            checkWindowSize();
            ArrayList<String> charsToAdd = new ArrayList<>();
            currentCode = codestreamArray.get(0);

            if (currentCode.length() > 1) {
                int startIndex = checkCharacterAtPrefix(currentCode.charAt(1));
                int numberOfChars = Integer.parseInt(String.valueOf(currentCode.charAt(3)));
                charsToAdd = getCharsToAdd(startIndex, numberOfChars);
                addCharsToWindow(charsToAdd);
                addToCharstream(false, " ", charsToAdd);
            } else {
                addToCharstream(true, currentCode, charsToAdd);
                codestreamArray.remove(0);
                window.add(currentCode);
            }

            checkWindowSize();
            setWindowCharacters();

            // -- For Testing --
//            counter++;
//            if (counter == 30) break;
        }
    }

    /**
     * This method gets the characters that are to be added to the window arraylist.
     * @param startIndex The index of the first character to add.
     * @param numberOfChars The number of characters that are being added.
     * @return An arraylist of all the characters being added.
     */
    private ArrayList<String> getCharsToAdd(int startIndex, int numberOfChars) {
        ArrayList<String> charsToAdd = new ArrayList<>();
        for (int i = startIndex; i < window.size(); i++) {
            charsToAdd.add(window.get(i));
            if (charsToAdd.size() == numberOfChars) break;
        }
        return charsToAdd;
    }

    /**
     * This method adds the new characters to the window arraylist and also remove the code from the codestream arraylist.
     * @param charsToAdd The characters that are being added.
     */
    private void addCharsToWindow(ArrayList<String> charsToAdd) {
        for (int i = 0; i < charsToAdd.size(); i++) {
            window.add(charsToAdd.get(i));
        }
        codestreamArray.remove(0);
    }

    /**
     * This method finds the index of the first character from the window that is to be added to the charstream.
     * @param prefix The prefix of the code in the codestream (e.g. <B:3> -> B is the prefix -> B Column)
     * @return The index of the first character.
     */
    private int checkCharacterAtPrefix(char prefix) {
        int offset = 0;

        switch (prefix) {
            case 'F':
                offset = 1;
                break;
            case 'E':
                offset = 2;
                break;
            case 'D':
                offset = 3;
                break;
            case 'C':
                offset = 4;
                break;
            case 'B':
                offset = 5;
                break;
            case 'A':
                offset = 6;
                break;
            case '9':
                offset = 7;
                break;
            case '8':
                offset = 8;
                break;
            case '7':
                offset = 9;
                break;
            case '6':
                offset = 10;
                break;
            case '5':
                offset = 11;
                break;
            case '4':
                offset = 12;
                break;
            case '3':
                offset = 13;
                break;
            case '2':
                offset = 14;
                break;
            case '1':
                offset = 15;
                break;
            case '0':
                offset = 16;
                break;
        }
        return window.size() - offset;
    }

    /**
     * This method sets the characters in the window arraylist to the correct position in the decoding table every iteration.
     */
    private void setWindowCharacters() {
        int currentWindowLength = window.size();
        switch (currentWindowLength) {
            case 1:
                f = window.get(0);
                break;
            case 2:
                f = window.get(currentWindowLength - 1);
                e = window.get(0);
                break;
            case 3:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(0);
                break;
            case 4:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(0);
                break;
            case 5:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(0);
                break;
            case 6:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(0);
                break;
            case 7:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(0);
                break;
            case 8:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(0);
                break;
            case 9:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(currentWindowLength - 8);
                seven = window.get(0);
                break;
            case 10:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(currentWindowLength - 8);
                seven = window.get(currentWindowLength - 9);
                six = window.get(0);
                break;
            case 11:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(currentWindowLength - 8);
                seven = window.get(currentWindowLength - 9);
                six = window.get(currentWindowLength - 10);
                five = window.get(0);
                break;
            case 12:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(currentWindowLength - 8);
                seven = window.get(currentWindowLength - 9);
                six = window.get(currentWindowLength - 10);
                five = window.get(currentWindowLength - 11);
                four = window.get(0);
                break;
            case 13:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(currentWindowLength - 8);
                seven = window.get(currentWindowLength - 9);
                six = window.get(currentWindowLength - 10);
                five = window.get(currentWindowLength - 11);
                four = window.get(currentWindowLength - 12);
                three = window.get(0);
                break;
            case 14:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(currentWindowLength - 8);
                seven = window.get(currentWindowLength - 9);
                six = window.get(currentWindowLength - 10);
                five = window.get(currentWindowLength - 11);
                four = window.get(currentWindowLength - 12);
                three = window.get(currentWindowLength - 13);
                two = window.get(0);
                break;
            case 15:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(currentWindowLength - 8);
                seven = window.get(currentWindowLength - 9);
                six = window.get(currentWindowLength - 10);
                five = window.get(currentWindowLength - 11);
                four = window.get(currentWindowLength - 12);
                three = window.get(currentWindowLength - 13);
                two = window.get(currentWindowLength - 14);
                one = window.get(0);
                break;
            case 16:
                f = window.get(currentWindowLength - 1);
                e = window.get(currentWindowLength - 2);
                d = window.get(currentWindowLength - 3);
                c = window.get(currentWindowLength - 4);
                b = window.get(currentWindowLength - 5);
                a = window.get(currentWindowLength - 6);
                nine = window.get(currentWindowLength - 7);
                eight = window.get(currentWindowLength - 8);
                seven = window.get(currentWindowLength - 9);
                six = window.get(currentWindowLength - 10);
                five = window.get(currentWindowLength - 11);
                four = window.get(currentWindowLength - 12);
                three = window.get(currentWindowLength - 13);
                two = window.get(currentWindowLength - 14);
                one = window.get(currentWindowLength - 15);
                zero = window.get(0);
                break;
        }
        printTableRow(zero, one, two, three, four, five, six, seven, eight, nine, a, b, c, d, e, f);
    }

    /**
     * This method checks the size of the window array list.
     * As the window can only be a maximum of 16 in length, if the window's size is greater than 16 then a loop is called to remove
     * the first element in the array each iteration until the window is 16 in length.
     */
    private void checkWindowSize() {
        if (window.size() > WINDOW_SIZE) {
            while (window.size() != WINDOW_SIZE) {
                window.remove(0);
            }
        }
    }

    /**
     * This method adds characters back to the charstream.
     * @param singleCharacter Whether only a single character is being added or not.
     * @param character The character to add, if only a single character is being added.
     * @param charsToAdd The characters to add, if more than one character is being added.
     */
    private void addToCharstream(boolean singleCharacter, String character, ArrayList<String> charsToAdd) {
        if (singleCharacter) {
            charstream += character;
        }
        else {
            for (int i = 0; i < charsToAdd.size(); i++) {
                charstream += charsToAdd.get(i);
            }
        }
    }

    /**
     * This method prints out each row in the encoding table as it is calculated,
     * along with the updated charstream beside each row.
     * @param zero Character in the "zero" column.
     * @param one Character in the "one" column.
     * @param two Character in the "two" column.
     * @param three Character in the "three" column.
     * @param four Character in the "four" column.
     * @param five Character in the "five" column.
     * @param six Character in the "six" column.
     * @param seven Character in the "seven" column.
     * @param eight Character in the "eight" column.
     * @param nine Character in the "nine" column.
     * @param a Character in the "a" column.
     * @param b Character in the "b" column.
     * @param c Character in the "c" column.
     * @param d Character in the "d" column.
     * @param e Character in the "e" column.
     * @param f Character in the "f" column.
     */
    private void printTableRow(String zero, String one, String two, String three, String four, String five, String six, String seven, String eight, String nine,
                               String a, String b, String c, String d, String e, String f) {

        String updatedCodestream = "";
        for (String s : codestreamArray) {
            updatedCodestream += s + " ";
        }
        System.out.printf("| %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %s \n", zero, one, two,
                three, four, five, six, seven, eight, nine, a, b, c, d, e, f, ""+updatedCodestream);
    }

    /**
     * This method prints out the start of the encoding table with the headings,
     * along with the starting charstream outside the first row.
     */
    private void printTableStart() {
        System.out.println("\nDecoding Table:");

        // Column Headings
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("| %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | \n", "0", "1", "2",
                "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F");
        System.out.println("-----------------------------------------------------------------");


        // First Row
        System.out.printf("| %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %s \n", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", codetream);
    }

    /**
     * This method prints out the codestream.
     */
    private void printCodestream() {
        System.out.println("\nCodestream: \n" + codetream);
    }

    /**
     * This method prints out the charstream.
     */
    private void printCharstream() {
        System.out.println("\nCharstream: \n" + charstream);
    }
}
