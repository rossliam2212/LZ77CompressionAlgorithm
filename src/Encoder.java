import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Encoder {
    private final String charstream;
    private List<String> charstreamArray;

    private String codestream;
    private int codestreamLength;

    private ArrayList<String> window;
    private final int WINDOW_SIZE = 16;
    private int[] windowIndexes;
    String zero = "", one = "", two = "", three = "", four = "", five = "", six = "", seven = "", eight = "", nine = "", a = "", b = "", c = "", d = "", e = "", f = "";

    public Encoder(String charstream) {
        this.charstream = charstream;
        codestream = "";
        codestreamLength = 0;
        charstreamArray = new ArrayList<>(Arrays.asList(charstream.split("")));
        window = new ArrayList<>();
        windowIndexes = new int[2];
    }

    /**
     * This method starts of the encoding process.
     */
    public void startEncoding() {
        printCharstream();
        printTableStart();

        addCharstreamToWindow();

        printCodestream();

        calculateEfficiency();
    }

    /**
     * This method calculates the efficiency of the compression.
     */
    private void calculateEfficiency() {
        double efficiency = 1 - ((double)codestreamLength / (double)charstream.length());
        int efficiencyPercentage = (int)Math.ceil(efficiency * 100);
        System.out.println("\nEfficiency: \n" + efficiency + " ≃ " + efficiencyPercentage + "% reduction.");
    }

    /**
     * This method is responsible for adding the charstream to the window.
     * At the beginning of the process the first two characters in the charstream are added to the window without any checks as the
     * checks wouldn't be necessary.
     * After the first two characters have been added, the checkCharsComingIntoWindow() method is called each iteration to get the
     * characters that are coming into the window and the number of matches.
     * The number of matches is then checked so the correct code is added to the codestream, and so the correct number of characters
     * are added to the window, and the correct number of characters are removed from the charstream.
     */
    private void addCharstreamToWindow() {
        String currentChar;
        boolean firstTwoCharFlag = true;
        int firstTwoChars = 0;
        int matches = 0;

        //int counter = 0; // FOR TESTING

        while (charstreamArray.size() != 0) {
            checkWindowSize();
            currentChar = charstreamArray.get(0);

            if (firstTwoCharFlag) {
                window.add(currentChar);
                charstreamArray.remove(0);
                addToCodestream(true, currentChar, 0);

                firstTwoChars++;
                if (firstTwoChars == 2) firstTwoCharFlag = false;
            }
            else {
                ArrayList<String> charsComingIntoWindow = checkCharsComingIntoWindow();
                int numberOfMatchingChars = Integer.parseInt(charsComingIntoWindow.get(charsComingIntoWindow.size()-1));
                matches = numberOfMatchingChars;

                if (numberOfMatchingChars >= 2) {
                    addToCodestream(false, " ", numberOfMatchingChars);
                    for (int i = 0;  i < numberOfMatchingChars; i++) {
                        window.add(charsComingIntoWindow.get(i)); // Characters being added to the window.
                        charstreamArray.remove(0); // Characters being removed from the charstream.
                    }
                } else {
                    window.add(currentChar);
                    charstreamArray.remove(0);
                    addToCodestream(true, currentChar, 0);
                }
            }
            checkWindowSize();
            setWindowCharacters(matches);

//             -- FOR TESTING --
//            counter++;
//            if (counter == 34) {
////                System.out.println(Arrays.toString(windowIndexes));
//                break;
//            }
        }
    }

    /**
     * This method is responsible for checking the characters that are set to come into the window.
     * An arraylist, charsComingIn, is created that holds the first two characters at the beginning of the charstream.
     * The checkCharsInWindow() method is then called with charsComingIn arraylist as its parameter, to check if the first
     * two characters match any that are currently in the window.
     * If there is a match, then the next third character in the charstream is added to the charsComingIn arraylist and
     * the checkCharsInWindow() method is called again.
     * This process is repeated until there are either no more matches or not characters left in the charstream.
     * @return An arraylist of the characters that are coming into the window, with the number of matches at the end of the arraylist.
     */
    private ArrayList<String> checkCharsComingIntoWindow() {
        int indexCounter = 0;
        int numberOfMatches = 0;
        ArrayList<String> charsComingIn = new ArrayList<>();
        ArrayList<String> returnData;

        if (charstreamArray.size() >= 2) {
            charsComingIn.add(charstreamArray.get(indexCounter));
            indexCounter++;
            charsComingIn.add(charstreamArray.get(indexCounter));

            if (checkCharsInWindow(charsComingIn)) {
                numberOfMatches = 2;
                indexCounter++;

                if (indexCounter < charstreamArray.size()) {
                    charsComingIn.add(charstreamArray.get(indexCounter));

                    while (checkCharsInWindow(charsComingIn)) {
                        numberOfMatches++;
                        indexCounter++;
                        if (indexCounter >= charstreamArray.size()) break;
                        charsComingIn.add(charstreamArray.get(indexCounter));
                    }
                }
            }
        }

        if (charsComingIn.size() != numberOfMatches) {
            while (charsComingIn.size() != numberOfMatches)
                charsComingIn.remove(charsComingIn.size()-1);
        }
        returnData = charsComingIn;
        returnData.add(Integer.toString(numberOfMatches)); // Last element in the arraylist is the number of matching characters.
        return returnData; // returnData holds the characters that are coming into the window along with the number of matches at the end ^.
    }

    /**
     * This method is responsible for checking if the characters that are set to come into the window from the charstream match any of
     * the characters that are currently in the window.
     * @param charsComingIntoWindow The next characters in the charstream that are coming into the window.
     * @return True if the characters in the charsComingIntoWindow array match a set of characters currently in the window,
     * False otherwise.
     */
    private boolean checkCharsInWindow(ArrayList<String> charsComingIntoWindow) {
//        System.out.println(charsComingIntoWindow);
        int matchesFound = 0;
        int windowCounter = 0;
        int windowCounterReset = 0;
        boolean resetValues = false;

        int tempStartIndex = 0;
        int tempEndIndex;
        boolean startIndexFlag = true;

        for (int i = 0; i < charsComingIntoWindow.size(); i++) {
            if (resetValues) {
                i = 0;
                matchesFound = 0;
                if (windowCounter >= window.size()-1 || windowCounterReset >= window.size() - 1) return false;
                windowCounter = windowCounterReset + 1;
                windowCounterReset++;
                startIndexFlag = true;
                resetValues = false;
            }

            if (windowCounter > window.size()-1) return false;
            if (charsComingIntoWindow.get(i).equals(window.get(windowCounter))) {
                if (startIndexFlag) {
                    tempStartIndex = windowCounter;
                    startIndexFlag = false;
                }
                matchesFound++;
                windowCounter++;

                for (int j = i+1; j < charsComingIntoWindow.size(); j++) {
                    if (windowCounter > window.size()-1) return false;
                    if (charsComingIntoWindow.get(j).equals(window.get(windowCounter))) { // This line is breaking the second example
                        tempEndIndex = windowCounter;
                        matchesFound++;
                        windowCounter++;
                    } else {
                        resetValues = true;
                        break;
                    }

                    if (matchesFound == charsComingIntoWindow.size()) {
//                        System.out.println("matchesFound: " + matchesFound);
                        windowIndexes[0] = tempStartIndex;
                        windowIndexes[1] = tempEndIndex;
                        return true;
                    }
                }
            } else {
                resetValues = true;
            }
        }
        return false;
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
     * This method sets the characters in the window to the correct position every iteration.
     * @param numberOfMatches *CURRENTLY NOT BEING USED*
     */
    private void setWindowCharacters(int numberOfMatches) {
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
     * This method adds codes to the codestream.
     * @param singleCharacter Whether the code will be a single character or not.
     * @param character The character to add, if the code is to be a single character.
     * @param numberOfMatches If the code is not a single character, this is the number of matches that goes with the code prefix (e.g. <E:2>).
     */
    private void addToCodestream(boolean singleCharacter, String character, int numberOfMatches) {
        codestreamLength++;
        if (singleCharacter)
            codestream += character + " ";
        else {
            int prefixColumn = (WINDOW_SIZE - window.size()) + windowIndexes[0];
            String codePrefix = "";
            switch (prefixColumn) {
                case 0:
                    codePrefix = "0";
                    break;
                case 1:
                    codePrefix = "1";
                    break;
                case 2:
                    codePrefix = "2";
                    break;
                case 3:
                    codePrefix = "3";
                    break;
                case 4:
                    codePrefix = "4";
                    break;
                case 5:
                    codePrefix = "5";
                    break;
                case 6:
                    codePrefix = "6";
                    break;
                case 7:
                    codePrefix = "7";
                    break;
                case 8:
                    codePrefix = "8";
                    break;
                case 9:
                    codePrefix = "9";
                    break;
                case 10:
                    codePrefix = "A";
                    break;
                case 11:
                    codePrefix = "B";
                    break;
                case 12:
                    codePrefix = "C";
                    break;
                case 13:
                    codePrefix = "D";
                    break;
                case 14:
                    codePrefix = "E";
                    break;
                case 15:
                    codePrefix = "F";
                    break;
            }
            String code = "<" + codePrefix + ":" + numberOfMatches + "> ";
            codestream += code;
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

        StringBuilder updatedCharstream = new StringBuilder();
        for (String s : charstreamArray) {
            updatedCharstream.append(s).append(" ");
        }
        System.out.printf("| %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %s \n", zero, one, two,
                three, four, five, six, seven, eight, nine, a, b, c, d, e, f, " "+updatedCharstream);
    }

    /**
     * This method prints out the start of the encoding table with the headings,
     * along with the starting charstream outside the first row.
     */
    private void printTableStart() {
        System.out.println("Encoding Table:");

        // Column Headings
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("| %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | \n", "0", "1", "2",
                "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F");
        System.out.println("-----------------------------------------------------------------");

        StringBuilder spacedCharstream = new StringBuilder();
        for (String s : charstreamArray) {
            spacedCharstream.append(s).append(" ");
        }
        // First Row
        System.out.printf("| %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %-1s | %s \n", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "+spacedCharstream);
//        System.out.println("-----------------------------------------------------------------");
    }

    /**
     * This method prints out the codestream.
     */
    private void printCodestream() {
        System.out.println("\nCodestream: \n" + codestream);
    }

    /**
     * This method prints out the charstream.
     */
    private void printCharstream() {
        System.out.println("Charstream: \n" + charstream + "\n");
    }
}
