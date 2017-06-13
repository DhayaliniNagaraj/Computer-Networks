package dijkstra;

import java.util.Scanner;

/**
 * This Class is used only for display purpose.
 * 
 * @author
 * 
 */
public final class Display {

	private Display() {
	}

	/**
	 * Prints the main menu.
	 */
	public static void printMainMenu() {
		System.out.println("(1) Create a Network Topology ");
		System.out.println("(2) Build a Connection Table ");
		System.out.println("(3) Shortest Path to Destination Router ");
		System.out.println("(4) Modify a topology ");
		System.out.println("(5) Exit");
	}

	/**
	 * Retrieves the input file name.
	 * 
	 * @param userCommand
	 * @return
	 */
	public static String getFileInput(Scanner userCommand) {
		System.out.println("Enter the filename:");
		String fileName = userCommand.next();
		if (fileName.isEmpty() || fileName == null) {
			System.out.println("FileName is Invalid");
			return null;
		}
		return fileName;
	}

	/**
	 * Retrieves the source router.
	 * 
	 * @param userCommand
	 * @return
	 */
	public static String getSourceRouter(Scanner userCommand) {
		System.out.println("Select a source router: ");
		String sourceRouter = userCommand.next();
		if (sourceRouter.isEmpty() || sourceRouter == null
				|| !sourceRouter.startsWith("R")) {
			System.out.println("Source Router is invalid, router should start with R1, for example R1");
			return null;
		}
		return sourceRouter;
	}

	/**
	 * Retrieves the destination router.
	 * 
	 * @param userCommand
	 * @return
	 */
	public static String getDestinationRouter(Scanner userCommand) {
		System.out.println("Select a destination router: ");
		String destinationRouter = userCommand.next();
		if (destinationRouter.isEmpty() || destinationRouter == null
				|| !destinationRouter.startsWith("R")) {
			System.out.println("Destination Router is invalid, router should start with R1, for example R1");
			return null;
		}
		return destinationRouter;
	}

	/**
	 * Retrieves the router to be removed.
	 * 
	 * @param userCommand
	 * @return
	 */
	public static String getRouterToRemove(Scanner userCommand) {
		System.out.println("Select a router to be removed: ");
		String routerToRemove = userCommand.next();
		if (routerToRemove.isEmpty() || routerToRemove == null
				|| !routerToRemove.startsWith("R")) {
			System.out.println("Router name is invalid, router should start with R1, for example R1");
			return null;
		}
		return routerToRemove;
	}

	/**
	 * Prints the error message when input is invalid.
	 */
	public static void printUserInputError() {
		System.out.println("Enter a number between 1 to 5");
	}

	/**
	 * Prints GoodBye Message.
	 */
	public static void printGoodBye() {
		System.out.println("Exit CS542 project. Good Bye!");
	}

	/**
	 * Prints the error message when exception occurs.
	 */
	public static void printExceptionError() {
		System.out.println("Invalid Input..Enter a number between 1 to 5");
	}
	
	/**
	 * Prints the error message when source router not available.
	 */
	public static void printSourceRouterError() {
		System.out.println("The source router is not available");
	}
	
	/**
	 * Prints the error message when destination router not available.
	 */
	public static void printDestinationRouterError() {
		System.out.println("The destination router is not available");
	}

	/**
	 * Prints Connection Table for option 2.
	 * 
	 */
	public static void printConnectionTable(String sourceRouter) {
		System.out.println(sourceRouter + " Connection Table");
		System.out.println("Destination   Interface");
		System.out.println("------------------------");
		System.out.println("------------------------");
	}

}
