package testing;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;
import org.junit.Before;

public class TextBuddyTest extends TextBuddy {

	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_NO_RESULT_FOUND = "No result found. Please try different keywords.";
	private static final String MESSAGE_INVALID_FORMAT = "Invalid Command Format";
	
	private static final String TEST_STRING_1 = "Amy has an apple.";
	private static final String TEST_STRING_2 = "Banana";
	private static final String TEST_STRING_3 = "3 monkeys";
	private static final String TEST_STRING_4 = "Zebra";
	private static final String TEST_STRING_5 = "angle";

	private static final String TEST_LIST_SORTED_1 = "1. " + TEST_STRING_3 + "\n" + 
							 "2. " + TEST_STRING_1 + "\n" + 
							 "3. " + TEST_STRING_2 + "\n" + 
							 "4. " + TEST_STRING_4 + "";
	private static final String TEST_LIST_SORTED_2 = "1. " + TEST_STRING_3 + "\n" + 
		     					 "2. " + TEST_STRING_1 + "\n" + 
		     					 "3. " + TEST_STRING_2 + "" ; 

	@Before
	public void setUpFile() throws Exception {
		String[] args = {"test.txt"};
		TextBuddy.setTextFile(args);
	}
	
	@Test
	public void testDisplay2() throws IOException {
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add " + TEST_STRING_1);
		TextBuddy.executeCommand("add " + TEST_STRING_2);
		TextBuddy.executeCommand("add " + TEST_STRING_3);
		assertEquals(TEST_LIST_SORTED_2, TextBuddy.executeCommand("sort"));
	}
	
	@Test
	public void testSort1() throws IOException {
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add " + TEST_STRING_1);
		TextBuddy.executeCommand("add " + TEST_STRING_2);
		TextBuddy.executeCommand("add " + TEST_STRING_3);
		TextBuddy.executeCommand("add " + TEST_STRING_4);
		assertEquals(TEST_LIST_SORTED_1, TextBuddy.executeCommand("sort"));
	}
	
	@Test
	public void testEmptyFileSort() throws IOException {
		TextBuddy.executeCommand("clear");
		assertEquals(String.format(MESSAGE_EMPTY, fileName), TextBuddy.executeCommand("sort"));
	}
	
	@Test 
	public void testInvalidFormatSort() throws IOException {
		TextBuddy.executeCommand("clear");
		assertEquals(String.format(MESSAGE_INVALID_FORMAT, fileName), TextBuddy.executeCommand("sort 1"));
		assertEquals(String.format(MESSAGE_INVALID_FORMAT, fileName), TextBuddy.executeCommand("so"));
	}
	
	@Test
	public void testEmptyFileSearch() throws IOException {
		TextBuddy.executeCommand("clear");
		assertEquals(String.format(MESSAGE_EMPTY, fileName), TextBuddy.executeCommand("search task"));
	}
	
	@Test
	public void testInvalidFormatSearch() throws IOException {
		TextBuddy.executeCommand("clear");
		assertEquals(String.format(MESSAGE_INVALID_FORMAT, fileName), TextBuddy.executeCommand("search"));
		assertEquals(String.format(MESSAGE_INVALID_FORMAT, fileName), TextBuddy.executeCommand("sear"));
	}
	
	@Test
	public void testNoResultFound() throws IOException {
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add " + TEST_STRING_1);
		assertEquals(String.format(MESSAGE_NO_RESULT_FOUND, fileName), TextBuddy.executeCommand("search task"));
	}
	
	@Test
	public void testSearch1() throws IOException {
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add " + TEST_STRING_1);
		TextBuddy.executeCommand("add " + TEST_STRING_2);
		TextBuddy.executeCommand("add " + TEST_STRING_3);
		TextBuddy.executeCommand("add " + TEST_STRING_4);
		TextBuddy.executeCommand("add " + TEST_STRING_5);
		assertEquals("1. "+TEST_STRING_1 +"\n", TextBuddy.executeCommand("search amy"));
	}
	
	@Test
	public void testSearch2() throws IOException {
		TextBuddy.executeCommand("clear");
		TextBuddy.executeCommand("add " + TEST_STRING_1);
		TextBuddy.executeCommand("add " + TEST_STRING_2);
		TextBuddy.executeCommand("add " + TEST_STRING_3);
		TextBuddy.executeCommand("add " + TEST_STRING_4);
		TextBuddy.executeCommand("add " + TEST_STRING_5);
		assertEquals("1. " + TEST_STRING_1 +"\n" + "2. " + TEST_STRING_2 +"\n" +"3. " + TEST_STRING_5 + "\n", TextBuddy.executeCommand("search an"));
	}
}
