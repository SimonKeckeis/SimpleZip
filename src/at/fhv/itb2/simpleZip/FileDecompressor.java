package at.fhv.itb2.simpleZip;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class to extract all files from a zip  or gzip file input.
 * @author ske2577
 * <p>Created on: 1.6.17</p>
 * @version 1
 *
 */
public class FileDecompressor{

	private File _sourcePath;
	private String _folderOutputName;
	
	public FileDecompressor(File sourceFile, String folderOutputName) throws FileNotFoundException {
		_sourcePath = sourceFile;
		_folderOutputName = folderOutputName;

	}
	
	/**
	 * Method to extract files from a zip file. <p>Output will a a new directory with all decompressed files, if the zip file is not empty.
	 * @throws FileNotFoundException 
	 */
	public void decompressFromZip() throws FileNotFoundException{
		//Extract path until source file directory. Add a new folder for extracted files in this directory.
		String outputPath = _sourcePath.getAbsolutePath().substring(0, _sourcePath.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + _folderOutputName;
		File output = new File(outputPath);
		
		if(_sourcePath.isFile() == false && !((_sourcePath.getAbsolutePath().substring(_sourcePath.getAbsolutePath().lastIndexOf("."))).equals("zip"))){
			throw new FileNotFoundException();
		}
		
		
		if(output.exists()){
			System.out.println("Directory name is already taken. Please choose another one.");
			return;
		}else{
			output.mkdir();
		}

		try{
			ZipInputStream is = new ZipInputStream(new FileInputStream(_sourcePath));
			System.out.println("Extracting files to: " + outputPath + "\n");
			ZipEntry e = is.getNextEntry();
			
			while(e != null){
				String fileName = e.getName();
				File out = new File(outputPath + File.separator + fileName);
				
				System.out.println("Extracting file: " + fileName);
				
				new File(out.getParent()).mkdirs();
				
				BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(out));
				
				byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		        
		        os.close();
		        is.closeEntry();
		        e = is.getNextEntry();
			}
			
			is.close();
			
			System.out.println("\nComplete.");
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to extract the content from a gzip file. <p>Output will a a new directory with the decompressed file, if the gzip file is not empty.
	 * <p><b>Only a single file can be extracted from a gzip file</b>
	 * @throws FileNotFoundException 
	 */
	public void decompressFromGzip() throws FileNotFoundException{
		
		if(_sourcePath.isDirectory() == false){
			throw new FileNotFoundException();
		}
		
		//Extract path until source file directory. Add a new folder for extracted files in this directory.
		String outputPath = _sourcePath.getAbsolutePath().substring(0, _sourcePath.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + _folderOutputName;
		File output = new File(outputPath);
		
		ArrayList<File> fileList = findFiles(_sourcePath, ".gz");
		
		if(!fileList.isEmpty()){

			if(output.exists()){
				System.out.println("Directory name is already taken. Please choose another one.");
				return;
			}else{
				output.mkdir();
			}
	
			
			try{
				System.out.println("Extracting files to: " + outputPath + "\n");
				
				for (File file : fileList) {

					GZIPInputStream is = new GZIPInputStream(new FileInputStream(file));
					File out = new File(outputPath + File.separator + file.getName().substring(0, file.getName().lastIndexOf(".")));
					BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(out));
					
					System.out.println("Extracting file ... ");
					
					byte[] buffer = new byte[1024];
			        int length;
			        while ((length = is.read(buffer)) > 0) {
			            os.write(buffer, 0, length);
			        }
			        
			        os.close();
		
					is.close();
				}
				System.out.println("\nComplete.");
				
			}catch (IOException e) {
				e.printStackTrace();
			}
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
