package at.fhv.itb2.simpleZip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Class to provide user input to compress or decompress files.
 * @author ske2577
 * <p>Created on: 1.6.17</p>
 * @version 1
 *
 */
public class SimpleZip {
	
	/**
	 *  Enum is used to choose between zip and gzip format.
	  * @author ske2577
	  * <p>Created on: 1.6.17</p>
	  * @version 1
	  *
	  */
	enum FileFormat{
		ZIP, GZIP;
	}
	
	/**
	 * Method to print the basic information needed to use the application. The contents of the method can be retrieved with the 'help' command.
	 */
	public static void printHeader(){
		System.out.println("\nTo compress: -f <zip | gzip> -p <extension> <archivename>");
		System.out.println("To decompress: -f <zip | gzip> -u <archivename>");
		System.out.println("To view instructions: help" );
		System.out.println("To exit: exit" );
	}
	

	public static void main(String[] args) throws IOException {
		boolean exit = false;
		Scanner scan; 
		String mainUserInput;
		
		printHeader();
		
		while(exit == false){

			System.out.println("\nEnter commands in format: \n" );
			scan= new Scanner(System.in);
			mainUserInput = scan.nextLine();

			if(mainUserInput.equals("exit")){
				System.exit(0);
			}
			
			if(mainUserInput.equals("help")){
				printHeader();
				continue;
			}

			String[] inputStrings = mainUserInput.split("\\s+"); //splits archive name if user input contains 'space'
			
			//0 = -f
			//1 = zip or gzip
			//2 = -p or -u
			//3 = extension or archivename
			//4 = archivename
			
			String input;
			
			if(inputStrings.length > 3){
			
				switch (inputStrings[2]) {
				case "-p":
					
					//concatenates split up filename
					for (int i = 5; i < inputStrings.length; i++) {
						inputStrings[4] += inputStrings[i];
					}
					
					if(inputStrings.length >= 5){				
						System.out.println("\nEnter a source directory: \n" );
						scan= new Scanner(System.in);
						input = scan.nextLine();
						
						File dir = new File(input);
						FileCompressor fc = null;
						
						try {
							fc = new FileCompressor(dir, inputStrings[3], inputStrings[4]);
						} catch (FileNotFoundException e) {
							System.out.println("Source path is not a directory.");
							break;
						}

						switch (inputStrings[1]) {
						case "zip":
							fc.compressToZip();
							break;
						case "gzip":
							fc.compressToGzip();
							break;
						default:
							System.out.println("Unknown format.");
							break;
						}
					}else{
						System.out.println("Incomplete command.");
					}
					break;
				case "-u":
					
					System.out.println("Enter a source file path: \n" );
					scan= new Scanner(System.in);
					input = scan.nextLine();
					
					//concatenates split up filename
					for (int i = 4; i < inputStrings.length; i++) {
						inputStrings[3] += inputStrings[i];
					}
					
					File sourcePath = new File(input);
					FileDecompressor fd = null;
					
					try {
						fd = new FileDecompressor(sourcePath, inputStrings[3]);
					} catch (FileNotFoundException e) {
						System.out.println("Source file was not found.");
						break;
					}
					
					switch (inputStrings[1]) {
					case "zip":
						try {
							fd.decompressFromZip();
						} catch (FileNotFoundException e) {
							System.out.println("File not found");
						}
						break;
					case "gzip":
						try {
							fd.decompressFromGzip();
						} catch (FileNotFoundException e) {
							System.out.println("File not found");
						}
						break;
					default:
						System.out.println("Unknown format.");
						break;
					}
					break;
				default:
					System.out.println("Unknown command.");
					break;
				}
			}else{
				System.out.println("Incomplete command");
			}
		}
	}
}
		
		
