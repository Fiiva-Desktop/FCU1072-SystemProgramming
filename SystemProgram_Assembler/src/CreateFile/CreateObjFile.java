package CreateFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import FileOperation.LoadFile;

public class CreateObjFile {
	
	private File objFile;
	private String objFileName;
	
	public CreateObjFile(LoadFile asmFile) {		
		objFileName = asmFile.getFileIn() + asmFile.getFileName() + ".obj";
		objFile = new File(objFileName);
		writeObjFile(asmFile);
	}
	
	private void writeObjFile(LoadFile asmFile) {
		int programStartLoc = asmFile.getLocationValue(0);
		int programLength = asmFile.getLocationValue(asmFile.getLines()-1) - programStartLoc;
		try {
		// create file
			this.objFile.createNewFile();
		// write file
			// H card
			FileWriter writeObjFile = new FileWriter(this.objFileName);
			BufferedWriter objContent = new BufferedWriter(writeObjFile);
			objContent.write("H" +	String.format("%-6s", asmFile.getLabelString(0).toUpperCase()) + 
									String.format("%6s", Integer.toHexString(programStartLoc).toUpperCase()).replace(" ", "0") + 
									String.format("%6s", Integer.toHexString(programLength).toUpperCase()).replace(" ", "0") + "\n");
			
			System.out.print("H" +	String.format("%-6s", asmFile.getLabelString(0).toUpperCase()) +
									String.format("%6s", Integer.toHexString(programStartLoc).toUpperCase()).replace(" ", "0") + 
									String.format("%6s", Integer.toHexString(programLength).toUpperCase()).replace(" ", "0") + "\n");
			
			// T card
			int tempLoc = asmFile.getLocationValue(0);
			int i = 1, firstTCard = 1;
			int endloc = 0;
			int noObjCodeLineLen = 0;
			String endVal = asmFile.getValueString(asmFile.getLines()-1);
			if(asmFile.getLabelString(0).equals(endVal)) endloc = asmFile.getLocationValue(0);
			do {
				String tempStr = new String();
				int tempStrLen = 0;			
				line:
				for(; i < asmFile.getLines() ; i++) {		
					if(asmFile.getLabelString(i).equals(endVal)) endloc = asmFile.getLocationValue(i);
					String tempObjCode =  asmFile.getObjectCode(i).replace(" ", "");				
					if(asmFile.getInstrString(i).equalsIgnoreCase("END")) break line;
					else if(asmFile.getInstrString(i).length() >= 3 &&
							asmFile.getInstrString(i).substring(0, asmFile.getInstrString(i).length()-1).equalsIgnoreCase("RES")) {
						if(	asmFile.getInstrString(i).toUpperCase().charAt(asmFile.getInstrString(i).length()-1) == 'W')
							noObjCodeLineLen += Integer.valueOf(asmFile.getValueString(i))*3;
						else noObjCodeLineLen += Integer.valueOf(asmFile.getValueString(i));
						if(firstTCard == 1) {
							tempLoc += noObjCodeLineLen;
							noObjCodeLineLen = 0;
						}
						i++;
						break line;
					}
					else if((tempStrLen+(tempObjCode.length()/2)) <= 30) {
						tempStr += tempObjCode.toUpperCase();
						tempStrLen += tempObjCode.length()/2;
						firstTCard = 0;
					}
					else break line;
				}
				
				// T card
				if(tempStrLen > 0) {
					objContent.write("T" +	String.format("%6s", Integer.toHexString(tempLoc).toUpperCase()).replace(" ", "0"));
					System.out.print("T" +	String.format("%6s", Integer.toHexString(tempLoc).toUpperCase()).replace(" ", "0"));
					objContent.write(String.format("%2s", Integer.toHexString(tempStrLen).toUpperCase()).replace(" ", "0") + tempStr + "\n");
					System.out.print(String.format("%2s", Integer.toHexString(tempStrLen).toUpperCase()).replace(" ", "0") + tempStr + "\n");
					tempLoc = tempLoc + tempStrLen + noObjCodeLineLen;
					noObjCodeLineLen = 0;
				}	
			}while(i < asmFile.getLines()-1);
			
			// M card
			for(i = 0 ; i < asmFile.getLines() ; i++) {
				if(asmFile.getInstrString(i).charAt(0) == '+') {
					int plusLoc = asmFile.getLocationValue(i) + 1;
					objContent.write("M" + String.format("%6s", Integer.toHexString(plusLoc)).toUpperCase().replace(" ", "0") + "05\n");
					System.out.print("M" + String.format("%6s", Integer.toHexString(plusLoc)).toUpperCase().replace(" ", "0") + "05\n");
				}
			}
			
			// E card
			objContent.write("E" + String.format("%6s", Integer.toHexString(endloc).toUpperCase()).replace(" ", "0"));
			System.out.println("E" + String.format("%6s", Integer.toHexString(endloc).toUpperCase()).replace(" ", "0"));
			objContent.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
