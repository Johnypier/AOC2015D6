package advent.of.code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static int numberOfLightsLit = 0;

    public static void main(String[] args) {
        // Part 1 & Part 2
        int[][] matrixPartOne = new int[1000][1000];
        int[][] matrixPartTwo = new int[1000][1000];
        for (Operation operation : parseOperations()) {
            performTheOperation(matrixPartOne, matrixPartTwo, operation);
        }
        System.out.println("Number of lights that are lit: " + numberOfLightsLit);
        int totalBrightness = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                totalBrightness += matrixPartTwo[i][j];
            }
        }
        System.out.println("Total brightness: " + totalBrightness);
    }

    private static List<String> readFile() {
        try {
            String strPath = Main.class.getClassLoader().getResource("input.txt").getPath();
            if (strPath == null) {
                System.err.println("Could not find the input file! Make sure that it is in the resources folder.");
                return Collections.emptyList();
            }
            return Files.readAllLines(Path.of(strPath));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not read the file, check the stack trace for more information.");
            return Collections.emptyList();
        }
    }

    private static List<Operation> parseOperations() {
        List<String> fileLines = readFile();
        List<Operation> operations = new ArrayList<>();

        for (String str : fileLines) {
            operations.add(createOperation(str));
        }

        return operations;
    }

    private static Operation createOperation(String line) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(line);

        int i = 0;
        int[] indexes = new int[4];
        while (matcher.find()) {
            indexes[i] = Integer.parseInt(matcher.group());
            i++;
        }

        if (line.contains("off")) {
            return new Operation(OperationType.OFF, indexes[1], indexes[3], indexes[0], indexes[2]);
        }
        if (line.contains("on")) {
            return new Operation(OperationType.ON, indexes[1], indexes[3], indexes[0], indexes[2]);
        }
        if (line.contains("toggle")) {
            return new Operation(OperationType.TOGGLE, indexes[1], indexes[3], indexes[0], indexes[2]);
        }
        return null;
    }


    private static void performTheOperation(int[][] matrixPartOne, int[][] matrixPartTwo, Operation operation) {
        // Part 1
        switch (operation.operationType()) {
            case OFF -> fillCellsWithValue(matrixPartOne, operation, 0, false);
            case ON -> fillCellsWithValue(matrixPartOne, operation, 1, false);
            case TOGGLE -> fillCellsWithValue(matrixPartOne, operation, 0, true);
        }
        // Part 2
        switch (operation.operationType()) {
            case OFF -> adjustLightBrightness(matrixPartTwo, operation, -1);
            case ON -> adjustLightBrightness(matrixPartTwo, operation, 1);
            case TOGGLE -> adjustLightBrightness(matrixPartTwo, operation, 2);
        }
    }

    // Part 1
    private static void fillCellsWithValue(int[][] matrix, Operation operation, int value, boolean inverse) {
        for (int i = operation.rowIndexStart(); i < operation.rowIndexEnd() + 1; i++) {
            for (int j = operation.fillRangeStart(); j < operation.fillRangeEnd() + 1; j++) {
                if (inverse) {
                    if (matrix[i][j] == 1) {
                        numberOfLightsLit--;
                        matrix[i][j] = 0;
                    } else {
                        numberOfLightsLit++;
                        matrix[i][j] = 1;
                    }
                } else {
                    if (value == 0 && matrix[i][j] == 1) {
                        numberOfLightsLit--;
                    }
                    if (value == 1 && matrix[i][j] == 0) {
                        numberOfLightsLit++;
                    }
                    matrix[i][j] = value;
                }
            }
        }
    }

    // Part 2
    private static void adjustLightBrightness(int[][] matrix, Operation operation, int value) {
        for (int i = operation.rowIndexStart(); i < operation.rowIndexEnd() + 1; i++) {
            for (int j = operation.fillRangeStart(); j < operation.fillRangeEnd() + 1; j++) {
                if (value == -1 && matrix[i][j] == 0) {
                    continue;
                }
                matrix[i][j] += value;
            }
        }
    }
}
