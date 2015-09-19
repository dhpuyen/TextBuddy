package testing;

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Collections;

/**
 * This is a CLI (Command Line Interface) program to manipulate text in a file
 * providing commands: add, delete, display, clear, sort, search, exit
 * 
 * @author Dinh Hoang Phuong Uyen
 * @matric number A0112077N
 * @group w10-2j
 */
public class TextBuddy {

	enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, EXIT, INVALID, SORT, SEARCH
	};

	public static final String MESSAGE_WELCOME = "Welcome to TextBuddy.  %1$s is ready for use";
	public static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	public static final String MESSAGE_DELETE = "deleted from %1$s: \"%2$s\"";
	public static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	public static final String MESSAGE_EMPTY = "%1$s is empty";
	public static final String MESSAGE_NO_RESULT_FOUND = "No result found. Please try different keywords.";
	public static final String MESSAGE_INVALID_FORMAT = "Invalid Command Format";
	public static final String MESSAGE_INVALID_ADD = "Invalid Addition. No content was entered";
	public static final String MESSAGE_INVALID_DELETE = "Invalid Deletion. The item is not available.";
	public static final String MESSAGE_INVALID_ARGS = "Invalid Arguments";

	public static final int PARAM_SIZE_FOR_DISPLAY = 0;
	public static final int PARAM_SIZE_FOR_DELETE = 1;
	public static final int PARAM_SIZE_FOR_CLEAR = 0;
	public static final int PARAM_SIZE_FOR_SORT = 0;

	public static Scanner scanner = new Scanner(System.in);
	public static ArrayList<String> textContent = new ArrayList<String>();
	public static String fileName;

	public static void main(String[] args) throws IOException {
		setTextFile(args);
		showToUser(String.format(MESSAGE_WELCOME, fileName));
		readInput();
	}

	public static void showToUser(String text) {
		System.out.println(text);
	}

	public static void setTextFile(String[] args) {
		if (!isValidArguments(args)) {
			showToUser(MESSAGE_INVALID_ARGS);
			System.exit(0);
		} else {
			fileName = args[0];
		}
	}

	public static void readInput() throws IOException {
		while (true) {
			System.out.print("Enter command: ");
			String userCommand = scanner.nextLine();
			String output = executeCommand(userCommand);
			showToUser(output);
		}
	}

	/**
	 * This operation is used to determine what to do according to the commands
	 * entered
	 * 
	 * @param commandTypeString
	 *            the first word of the command line
	 */
	public static String executeCommand(String userCommand) throws IOException {
		String commandTypeString = getCommand(userCommand);
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
		String parameters = getParam(userCommand);

		switch (commandType) {
		case ADD :
			return addContent(parameters);

		case DISPLAY :
			return getContent(parameters);

		case DELETE :
			return deleteContent(parameters);

		case CLEAR :
			return clearContent(parameters);

		case SORT :
			return sortContent(parameters);

		case SEARCH :
			return searchContent(parameters);

		case INVALID :
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);

		case EXIT :
			saveProgram();
			System.exit(0);

		default :
			return MESSAGE_INVALID_FORMAT;
		}
	}

	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * 
	 */
	public static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return COMMAND_TYPE.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("sort")) {
			return COMMAND_TYPE.SORT;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return COMMAND_TYPE.SEARCH;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	/**
	 * This operation is used to add lines to the file
	 *
	 * @param lineToAdd
	 *            is the string user wants to be entered into the file
	 * 
	 */
	public static String addContent(String lineToAdd) {
		if (!hasCorrectParamsLength(lineToAdd, COMMAND_TYPE.ADD)) {
			return MESSAGE_INVALID_ADD;
		} else {
			textContent.add(lineToAdd);
			return String.format(MESSAGE_ADDED, fileName, lineToAdd);
		}
	}

	/**
	 * This operation is used to delete a line in TextBuddy file
	 * 
	 * @param position
	 *            the serial number of the line user wants to be deleted
	 * @throws NumberFormatException
	 *             If position is not a number
	 * @throws IndexOutofBoundsException
	 *             If position exceeds the number of lines in TextBuddy file
	 */
	public static String deleteContent(String position) throws NumberFormatException, IndexOutOfBoundsException {
		try {
			if (!hasCorrectParamsLength(position, COMMAND_TYPE.DELETE)) {
				return MESSAGE_INVALID_FORMAT;
			} else {
				int actualPosition = getActualPosition(position);
				String textRemoved = textContent.remove(actualPosition);
				return String.format(MESSAGE_DELETE, fileName, textRemoved);
			}
		} catch (NumberFormatException nfe) {
			return MESSAGE_INVALID_FORMAT;
		} catch (IndexOutOfBoundsException e) {
			return MESSAGE_INVALID_DELETE;
		}
	}

	/**
	 * This operation is used to delete all content in TextBuddy file
	 */
	public static String clearContent(String text) {
		if (!hasCorrectParamsLength(text, COMMAND_TYPE.CLEAR)) {
			return MESSAGE_INVALID_FORMAT;
		} else {
			textContent.clear();
			return String.format(MESSAGE_CLEAR, fileName);
		}
	}

	/**
	 * This operation is used to sort the list in alphabetical order
	 */
	public static String sortContent(String text) {
		if (!hasCorrectParamsLength(text, COMMAND_TYPE.SORT)) {
			return MESSAGE_INVALID_FORMAT;
		} else if (textContent.isEmpty()) {
			return String.format(MESSAGE_EMPTY, fileName);
		} else {
			Collections.sort(textContent);
			return formatContent(textContent);
		}
	}

	/**
	 * This operation is used to searchContent for tasks that contain entered
	 * keyword
	 * 
	 * @return a list of tasks that contain keywords
	 */
	public static String searchContent(String keyword) {
		ArrayList<String> foundList = new ArrayList<String>();

		if (!hasCorrectParamsLength(keyword, COMMAND_TYPE.SEARCH)) {
			return MESSAGE_INVALID_FORMAT;
		} else if (textContent.isEmpty()) {
			return String.format(MESSAGE_EMPTY, fileName);
		} else {
			for (int i = 0; i < textContent.size(); i++) {
				if ((textContent.get(i).toUpperCase()).contains(keyword.toUpperCase())) {
					foundList.add(textContent.get(i));
				}
			}

			if (foundList.isEmpty()) {
				return MESSAGE_NO_RESULT_FOUND;
			} else {
				return formatContent(foundList);
			}
		}
	}

	/**
	 * This operation is used to write the Content in ArrayList into the file
	 * 
	 * @throws IOException
	 *             If an input or output exception occurred
	 */
	public static void saveProgram() throws IOException {
		try {
			File file = new File(fileName);

			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(formatContent(textContent));

			bufferedWriter.close();
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This operation is used to get Content in the ArrayList
	 *
	 * @return Content in the list
	 */
	public static String getContent(String text) {
		if (!hasCorrectParamsLength(text, COMMAND_TYPE.DISPLAY)) {
			return MESSAGE_INVALID_FORMAT;
		} else if (textContent.isEmpty()) {
			return String.format(MESSAGE_EMPTY, fileName);
		} else {
			return formatContent(textContent);
		}
	}

	/**
	 * This operation is used to format all the stored Content in Arraylist
	 * 
	 * @return Content after formatted
	 */
	public static String formatContent(ArrayList<String> listToFormat) {
		int count = 1;
		String formattedContentList = new String();

		for (int i = 0; i < listToFormat.size(); i++) {
			formattedContentList = addNumber(formattedContentList, count);
			formattedContentList += listToFormat.get(i);
			if (!isLastLine(textContent.size(), i)) {
				formattedContentList += "\n";
			}
			count++;
		}
		return formattedContentList;
	}

	/**
	 * This operation is used to get the actual current position of the line in
	 * the Arraylist
	 * 
	 * @param positionStringType
	 *            the number of a line
	 * @return the position of the line
	 */
	public static int getActualPosition(String positionStringType) {
		int positionIntegerType = Integer.parseInt(positionStringType);
		return positionIntegerType - 1;
	}

	// add the number in front of the text to show its order in the list
	public static String addNumber(String list, int count) {
		String number = "" + count + "." + " ";
		return list += number;
	}

	public static boolean isLastLine(int size, int positionOfIterator) {
		return size - 1 == positionOfIterator;
	}

	public static boolean isValidArguments(String[] args) {
		return args.length == 1;
	}

	public static String getCommand(String userCommand) {
		String commandType = userCommand.trim().split("\\s+")[0];
		return commandType;
	}

	public static String getParam(String userCommand) {
		return userCommand.replace(getCommand(userCommand), "").trim();
	}

	/**
	 * This operation is used to check if the parameters input is valid
	 * 
	 * @param inputParams
	 *            the parameters user input, excluding the command
	 * @param commandType
	 *            the type of command
	 */
	public static boolean hasCorrectParamsLength(String inputParams, COMMAND_TYPE commandType) {
		int paramsLength = getParamsLength(inputParams);

		switch (commandType) {
		case ADD :
			if (paramsLength > 0) {
				return true;
			} else {
				return false;
			}

		case SEARCH :
			if (paramsLength > 0) {
				return true;
			} else {
				return false;
			}

		case DELETE :
			return hasSameLength(paramsLength, PARAM_SIZE_FOR_DELETE);

		case DISPLAY :
			return hasSameLength(paramsLength, PARAM_SIZE_FOR_DISPLAY);

		case SORT :
			return hasSameLength(paramsLength, PARAM_SIZE_FOR_SORT);

		case CLEAR :
			return hasSameLength(paramsLength, PARAM_SIZE_FOR_CLEAR);

		default :
			return false;
		}
	}

	public static boolean hasSameLength(int expectedLength, int inputLength) {
		if (expectedLength == inputLength) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This operation is used to count the number of parameters
	 * 
	 * @param inputParams
	 *            the parameters user enter, excluding the command word
	 * @return the number of parameters
	 */
	public static int getParamsLength(String inputParams) {
		if (inputParams.equals("")) {
			return 0;
		} else {
			String[] parameters = inputParams.trim().split("\\s+");
			return parameters.length;
		}
	}
}
