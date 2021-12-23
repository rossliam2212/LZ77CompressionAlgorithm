import java.util.ArrayList;
import java.util.Scanner;

public class ImagePrinter {
    private String charstream;
    private ArrayList<String> roots;
    private ArrayList<String> rootColors;

    public ImagePrinter(String charstream) {
        this.charstream = charstream;
        roots = new ArrayList<>();
        rootColors = new ArrayList<>();
    }

    /**
     * This method is responsible for gathering all the information and then printing out the image.
     */
    public void printImage() {
        getRoots();
        getColors();

        int dimensions = (int) Math.sqrt(charstream.length());

        System.out.println("\nImage:");
        int index;
        int counter = 0;

        for (int i = 0; i < charstream.length(); i++) {
            counter++;
            String currentChar = Character.toString(charstream.charAt(i));
            for (int j = 0; j < roots.size(); j++) {
                if (currentChar.equals(roots.get(j))) {
                    index = j;
                    System.out.print(rootColors.get(index) + Colors.RESET);
                }
            }
            if (counter % dimensions == 0)
                System.out.print("\n");
        }
    }

    /**
     * This method takes in input from the user on what colors to assign to each root.
     */
    private void getColors() {
        Scanner scanner = new Scanner(System.in);
        String color;
        System.out.println("\nPlease enter the colors for each root: (e.g. red) ");

        for (int i = 0; i < roots.size(); i++) {
            System.out.print(roots.get(i) + " > ");
            color = scanner.nextLine();
            setColors(i, color);
        }
    }

    /**
     * This method takes in the index of a root from the root arraylist and sets the same index in the rootColors arraylist to a
     * block character with the correct color.
     * @param index The index of the root in the root arraylist.
     * @param color The color to set the block.
     */
    private void setColors(int index, String color) {
        String blockCharacter = "\u2588" + "\u2588";
        switch (color) {
            case "green":
                rootColors.set(index, Colors.GREEN + blockCharacter);
                break;
            case "red":
                rootColors.set(index, Colors.RED + blockCharacter);
                break;
            case "yellow":
                rootColors.set(index, Colors.YELLOW + blockCharacter);
                break;
            case "orange":
                rootColors.set(index, Colors.ORANGE + blockCharacter);
                break;
            case "purple":
                rootColors.set(index, Colors.PURPLE + blockCharacter);
                break;
            case "black":
                rootColors.set(index, Colors.BLACK + blockCharacter);
                break;
            case "grey":
                rootColors.set(index, Colors.GREY + blockCharacter);
                break;
            default:
                rootColors.set(index, Colors.RESET + blockCharacter);
                break;
        }
    }

    /**
     * This method gets all the individual roots from the charstream and adds them to the roots arraylist.
     */
    private void getRoots() {
        char[] arr = charstream.toCharArray();

        char firstRoot = arr[0];
        char previous = firstRoot;
        roots.add(Character.toString(firstRoot));

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > firstRoot && arr[i] > previous) {
                previous = arr[i];
                roots.add(Character.toString(arr[i]));
            }
        }

        while (roots.size() != rootColors.size()) rootColors.add(" ");
    }
}
