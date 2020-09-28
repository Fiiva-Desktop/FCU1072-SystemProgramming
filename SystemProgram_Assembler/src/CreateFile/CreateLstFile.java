package CreateFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import FileOperation.LoadFile;

public class CreateLstFile {

	private File lstFile;
	private String lstFileName;
	
	public CreateLstFile(LoadFile asmFile) {
		
		lstFileName = asmFile.getFileIn() + asmFile.getFileName() + ".lst";
		lstFile = new File(this.lstFileName);
		writeLstFile(asmFile);		
	}
	
	private void writeLstFile(LoadFile asmFile) {
		try {
			this.lstFile.createNewFile();
			FileWriter writeLstFile = new FileWriter(this.lstFileName);
			BufferedWriter lstContent = new BufferedWriter(writeLstFile);
			
			for(int i = 0 ; i < asmFile.getLines() ; i++) {
				lstContent.write(	String.format("%5s\t", Integer.toHexString(asmFile.getLocationValue(i)).toUpperCase()).replace(" ", "0") +
									String.format("%-6s\t", asmFile.getObjectCode(i).toUpperCase()) +
									String.format("%-8s", asmFile.getLabelString(i).toUpperCase()) + 
									String.format("%-8s", asmFile.getInstrString(i).toUpperCase()) + 
									String.format("%-8s", asmFile.getValueString(i).toUpperCase()) + "\n");
			}
			lstContent.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
