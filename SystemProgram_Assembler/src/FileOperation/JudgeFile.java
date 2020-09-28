package FileOperation;

import java.io.File;
import java.util.Scanner;

public class JudgeFile {

	private File file;
	private String filePath = null;
	
	public JudgeFile() {
		Scanner keyboardInput = new Scanner(System.in); 
		System.out.println("Please enter your file path: ");
		filePath = keyboardInput.nextLine();
		
		while(!isFileExist(filePath) | !isASMFile(filePath)) {
			if(!isFileExist(filePath)) {
				System.out.println("Not found file.");
			}
			else if(!isASMFile(filePath)) {
				System.out.println("Not \".asm\" file.");
			}
			System.out.println("Please enter again your file path: ");
			filePath = keyboardInput.nextLine();
		}
		System.out.println("File path: " + filePath);
		keyboardInput.close();
	}
	
	/*檔案是否存在 */
	private boolean isFileExist(String filePath) {
		file = new File(filePath);
		return file.exists();
	}
	
	/*是否為ASM */
	private boolean isASMFile(String filePath) {
		String extension = this.getExtension();
		if(extension.equalsIgnoreCase("asm")) {
			return true;
		}
		return false;
	}
	/*取得副檔名 */
	private String getExtension() {
		int startIndex = file.getName().lastIndexOf(46) + 1;
		int endIndex = file.getName().length();
		return file.getName().substring(startIndex, endIndex);
	}
	
	/*回傳檔案路徑 */
	public String getFilePath() {
		return filePath;
	}
	
}
