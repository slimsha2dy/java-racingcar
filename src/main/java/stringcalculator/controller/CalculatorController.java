package stringcalculator.controller;

import stringcalculator.view.InputView;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CalculatorController {
    private static final int NULL_OR_EMPTY_RESULT = 0;
    private static final String REGEX = "//(.)\\\\n(.*)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static final int DELIMITER_INDEX = 1;
    private static final int NUMBERS_INDEX = 2;
    private static final String COMMA = ",";
    private static final String COLON = ",";
    private static final String DELIMITER_DIVIDER = "|";
    private static final String DEFAULT_DELIMITERS = COMMA + DELIMITER_DIVIDER + COLON;
    private static final String EMPTY_STRING = "";

    private final InputView inputView;

    public CalculatorController(Scanner scanner) {
        this.inputView = new InputView(scanner);
    }

    public int run() {
        String input = inputView.scanInput();

        return getResult(input);
    }

    public int getResult(String input) {
        if (checkNullOrEmpty(input)) {
            return NULL_OR_EMPTY_RESULT;
        }

        String delimiters = DEFAULT_DELIMITERS;

        String numbers = getNumbers(input);

        if (hasCustomDelimiter(input)) {
            delimiters += (DELIMITER_DIVIDER + getCustomDelimiter(input));
        }

        return splitAndSum(numbers, delimiters);
    }

    private boolean hasCustomDelimiter(String input) {
        Matcher matcher = PATTERN.matcher(input);

        return matcher.find();
    }

    public String getCustomDelimiter(String input) {
        String customDelimiter = EMPTY_STRING;

        Matcher matcher = PATTERN.matcher(input);

        if (matcher.find()) {
            customDelimiter = matcher.group(DELIMITER_INDEX);
            isValidDelimiter(customDelimiter);
        }

        return customDelimiter;
    }

    private void isValidDelimiter(String customDelimiter) {
        if (customDelimiter.equals(",") || customDelimiter.equals(":")) {
            throw new RuntimeException("커스텀 구분자로 기본 구분자가 지정될 수 없습니다.");
        }
    }

    public String getNumbers(String input) {
        Matcher matcher = PATTERN.matcher(input);

        if (matcher.find()) {
            return matcher.group(NUMBERS_INDEX);
        }
        return input;
    }

    public int splitAndSum(String value, String delimiters) {
        List<Integer> numbers = splitNumbers(value, delimiters);

        return numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public List<Integer> splitNumbers(String value, String delimiters) {
        String[] numbers = value.split(delimiters);

        isValidNumbers(numbers);

        return Arrays.stream(numbers)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private void isValidNumbers(String[] numbers) {
        for (String number : numbers) {
            checkCharacterNullOrEmpty(number);
            checkNumber(number);
        }
    }

    private void checkCharacterNullOrEmpty(String value) {
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("숫자 문자열만 입력해 주세요");
        }
    }

    private void checkNumber(String value) {
        int number = 0;

        try {
            number = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("숫자 문자열만 입력해 주세요");
        }

        if (number < 0) {
            throw new RuntimeException("양수 문자열만 입력해 주세요");
        }
    }

    private boolean checkNullOrEmpty(String numbers) {
        return numbers == null || numbers.isEmpty();
    }
}
