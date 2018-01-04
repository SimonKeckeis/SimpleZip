package at.fhv.itb2.simpleZip;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Class to compress all files with a given extension from a source directory.
 * @author ske2577
 * <p>Created on: 1.6.17</p>
 * @version 1
 *
 */
public class FileCompressor{
	
	private File _dir;
	private String _extension;
	private String _outputName;
	
	public FileCompressor(File dir, String extension, String outputName) throws FileNotFoundException {

		if(dir.isDirectory()){
			_dir = dir;
		}else{
			throw new FileNotFoundException("Directory was not found.");
		}

		_extension = extension;
		_outputName = outputName;
	}
	

	/**
	 * Method to compress files of a directory with a specific extension. <p>Output will a a new zip file, if there are any relevant files to compress.
	 */
	public void compressToZip(){
		String outputPath = _dir.getAbsolutePath() + File.separator + _outputName + ".zip";
		File output = new File(outputPath);
		ArrayList<File> fileList = findFiles(_dir, _extension);
		
		
		if(!(fileList.isEmpty())){
		    try {
		    	ZipOutputStream os = new ZipOutputStream(new FileOutputStream(output));
		    	System.out.println("Adding files to: " + outputPath + "\n");
		    	
		    	for (File file : fileList) {		    	
		
			    	BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
					ZipEntry e = new ZipEntry(file.getName());
					os.putNextEntry(e);

					byte[] buffer = new byte[1024];
			        int length;
			        while ((length = is.read(buffer)) > 0) {
			            os.write(buffer, 0, length);
			        }
			        
			        is.close();
			        System.out.println("Added file: " + file.getName());
			        
			        os.closeEntry();
		    	}

		    	os.close();
			        
		    	System.out.println("\nCompleted.");
		    	 
				} catch (IOException e) {
					e.printStackTrace();
				}
		}else{
			System.out.println("No relevant files found.");
		}
	}
	
	/**
	 * Method to compress files of a directory with a specific extension. <p>Output will a a new gzip file, if there are any relevant files to compress.
	 * <p>It is advised to only compress one file into a gzip file, since the decompress file will be a single file. 
	 */
	public void compressToGzip(){
		String outputPath = _dir.getAbsolutePath() + File.separator + _outputName;
		File output = new File(outputPath);
		
		if(output.exists()){
			System.out.println("Filename is already taken. Please choose another one.");
			return;
		}
		
		ArrayList<File> fileList = findFiles(_dir, _extension);
		
		if(!(fileList.isEmpty())){
			output.mkdir();
		    try {
		    	System.out.println("Adding files to: " + outputPath + "\n");
		    	
		    	for (File file : fileList) {
		    		File out = new File(outputPath + File.separator + file.getName()+ ".gz");
		    		GZIPOutputStream os = new GZIPOutputStream(new FileOutputStream(out));
		    		BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));					
					
					byte[] buffer = new byte[1024];
			        int length;
			        while ((length = is.read(buffer)) > 0) {
			            os.write(buffer, 0, length);
			        }
			        
			        is.close();
			        System.out.println("Added file: " + file.getName());
			        os.close();
		    	}

			        
		    	System.out.println("\nCompleted.");
		    	 
				} catch (IOException e) {
					e.printStackTrace();
				}

		}else{
			System.out.println("No relevant files found.");
		}
		
	}
	
	/**
	 * Method to find all files with a specific extension in a directory.
	 * @param dir is the directory to be searched
	 * @param extension is the extension of the files to find
	 * @return A list of all relevant files
	 */
	public ArrayList<File> findFiles(File dir, String extension){
		File[] actualFiles = dir.listFiles();
		ArrayList<File> relevantFiles = new ArrayList<>();
		
		for (File file : actualFiles) {
			if(file.isFile()){
				if(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")).equals(extension)){
					relevantFiles.add(file);
				}
			}
		}
		
		return relevantFiles;
	}
}
