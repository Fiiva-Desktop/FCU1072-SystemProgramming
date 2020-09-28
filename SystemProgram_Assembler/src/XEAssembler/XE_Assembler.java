package XEAssembler;

import CreateFile.*;
import FileOperation.JudgeFile;
import FileOperation.LoadFile;

public class XE_Assembler {
	
	public static void main(String[] args) {
		String filePath = new JudgeFile().getFilePath();
		LoadFile XEtable = new LoadFile(filePath);
		new ProduceValue_XE(XEtable);
		for(int i = 0 ; i < XEtable.getLines() ; i++) {
			System.out.println(	String.format("%5s\t", Integer.toHexString(XEtable.getLocationValue(i)).toUpperCase()).replace(" ", "0") +
								String.format("%-6s\t", XEtable.getObjectCode(i).toUpperCase()) +
								String.format("%-8s", XEtable.getLabelString(i)) + 
								String.format("%-8s", XEtable.getInstrString(i).toUpperCase()) + 
								String.format("%-8s", XEtable.getValueString(i)));
		}
		System.out.println("\n");
		new CreateLstFile(XEtable);
		new CreateObjFile(XEtable);
	}
}
/*
C:\Users\Fiiva\Desktop\D0693694_XE.asm
*/