package SICAssembler;

import CreateFile.CreateLstFile;
import CreateFile.CreateObjFile;
import FileOperation.JudgeFile;
import FileOperation.LoadFile;


public class SIC_Assembler {

	public static void main(String[] args) {
		String filePath = new JudgeFile().getFilePath();
		LoadFile SICtable = new LoadFile(filePath);
		new ProduceValue(SICtable);
		for(int i = 0 ; i < SICtable.getLines() ; i++) {
			System.out.println( String.format("%4s\t", Integer.toHexString(SICtable.getLocationValue(i)).toUpperCase()).replace(" ", "0") +
								String.format("%6s\t", SICtable.getObjectCode(i).toUpperCase()) +
								String.format("%-8s", SICtable.getLabelString(i).toUpperCase()) + 
								String.format("%-8s", SICtable.getInstrString(i).toUpperCase()) + 
								String.format("%-8s", SICtable.getValueString(i).toUpperCase()));
		}
		new CreateObjFile(SICtable);
		new CreateLstFile(SICtable);
		System.out.println("Finish!");
	}
	
}
