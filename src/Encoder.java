import java.lang.reflect.Array;
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

    public void startEncoding() {
        printCharstream();
        printTableStart();

        addCharstreamToWindow();

        printCodestream();

        calculateEfficiency();
    }

    private void calculateEfficiency() {
        System.out.println(codestreamLength);
        double efficiency = 1 - ((double)codestreamLength / (double)charstream.length());
        int efficiencyPercentage = (int)Math.ceil(efficiency * 100);
        System.out.println("\nEfficiency: \n" + efficiency + " ≃ " + efficiencyPercentage + "% reduction.");
    }

    private void addCharstreamToWindow() {
        String currentChar;
        boolean firstTwoCharFlag = true;
        int firstTwoChars = 0;
        int matches = 0;

        int counter = 0; // FOR TESTING

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
                        window.add(charsComingIntoWindow.get(i));
                        charstreamArray.remove(0);
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
        returnData.add(Integer.toString(numberOfMatches)); // Last element in the arraylist is the number of matching characters
        return returnData;
    }

    private boolean checkCharsInWindow(ArrayList<String> charsComingIntoWindow) {
//        System.out.println(charsComingIntoWindow);
        int matchesFound = 0;
        int windowCounter = 0;
        int windowCounterReset = 0;
        boolean resetValues = false;

        int tempStartIndex = 0;
        int tempEndIndex = 0;
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

    private void checkWindowSize() {
        if (window.size() > WINDOW_SIZE) {
            while (window.size() != WINDOW_SIZE) {
                window.remove(0);
            }
        }
    }

    private void setWindowCharacters(int numberOfMatches) {
        int currentWindowLength = window.size();
        switch (currentWindowLength) {
                case 1:
                    f = Colors.GREEN + window.get(0) + Colors.RESET;
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

    private void resetWindowColors() {
        a = Colors.RESET;
        b = Colors.RESET;
        c = Colors.RESET;
        d = Colors.RESET;
        e = Colors.RESET;
        f = Colors.RESET;
        zero = Colors.RESET;
        one = Colors.RESET;
        two = Colors.RESET;
        three = Colors.RESET;
        four = Colors.RESET;
        five = Colors.RESET;
        six = Colors.RESET;
        seven = Colors.RESET;
        eight = Colors.RESET;
        nine = Colors.RESET;
    }

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
