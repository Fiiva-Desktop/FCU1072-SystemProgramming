package XEAssembler;

import FileOperation.LoadFile;

public class ProduceValue_XE {

	private String[] instruList_XE = {
		//SIC instruction
			"LDA", "LDX", "LDL", "STA", "STX", "STL", "ADD", "SUB", "MUL", "DIV", "COMP", "TIX",	//12
			"JEQ", "JGT", "JLT", "J", "AND", "OR", "JSUB", "RSUB", "LDCH", "STCH",					//10
		//XE instruction
			"ADDF", "SUBF", "MULF", "DIVF", "LDB", "LDS", "LDF", "LDT", "STB", "STS",				//10
			"STF", "STT", "COMPF", null, "ADDR", "SUBR", "MULR", "DIVR", "COMPR", "SHIFTL",			//10
			"SHIFTR", "RMO", "SVC", "CLEAR", "TIXR", null, "FLOAT", "FIX", "NORM", null,			//10
			"LPS", "STI", "RD", "WD", "TD", null, "STSW", "SSK", "SIO", "HIO", "TIO"};				//11
	
	// pass 1
	public ProduceValue_XE(LoadFile xeTable) {
		for(int i = 0 ; i < xeTable.getLines() ; i++) {
			
			if(xeTable.getInstrString(i).equalsIgnoreCase("START") && i == 0) {
				int DecValue = new Integer(Integer.valueOf(xeTable.getValueString(0), 16));
				xeTable.addLocatTable(0, DecValue);
				xeTable.addLocatTable(1, DecValue);
			}
			else if(xeTable.getInstrString(i).length() == 4 && xeTable.getInstrString(i).substring(0, 3).equalsIgnoreCase("RES")) {
				int space = Integer.parseInt(xeTable.getValueString(i));
				if(xeTable.getInstrString(i).equalsIgnoreCase("RESW")) {
					space *= 3;
					int temp = new Integer(xeTable.getLocationValue(i) + space);
					xeTable.addLocatTable(temp);
				}
				else { // RESB
					int temp = new Integer(xeTable.getLocationValue(i) + space);
					xeTable.addLocatTable(temp);
				}
			}
			else if(xeTable.getInstrString(i).equalsIgnoreCase("BYTE")) {
				int length = xeTable.getValueString(i).substring(2, xeTable.getValueString(i).length()-1).length();
				if(xeTable.getValueString(i).toUpperCase().charAt(0) == 'C') ;
				else if(xeTable.getValueString(i).toUpperCase().charAt(0) == 'X') length /= 2;
				xeTable.addLocatTable(xeTable.getLocationValue(i)+length);
			}
			else if(xeTable.getInstrString(i).equalsIgnoreCase("WORD")) {
				xeTable.addLocatTable(xeTable.getLocationValue(i)+3);
			}
			else if(xeTable.getInstrString(i).charAt(0) == '+') {
				xeTable.addLocatTable(xeTable.getLocationValue(i)+4);
			}
			else if((	xeTable.getInstrString(i).toUpperCase().charAt(xeTable.getInstrString(i).length()-1) == 'R' ||
						xeTable.getInstrString(i).equalsIgnoreCase("SHIFTL")	||
						xeTable.getInstrString(i).equalsIgnoreCase("RMO")		||
						xeTable.getInstrString(i).equalsIgnoreCase("SVC")	)	&& 
						!xeTable.getInstrString(i).equalsIgnoreCase("OR")) {	// 2byte
				xeTable.addLocatTable(xeTable.getLocationValue(i)+2);
			}
			else if(xeTable.getInstrString(i).equalsIgnoreCase("FLOAT")	||
					xeTable.getInstrString(i).equalsIgnoreCase("FIX")	||
					xeTable.getInstrString(i).equalsIgnoreCase("NORM")	||
					xeTable.getInstrString(i).equalsIgnoreCase("SIO")	||
					xeTable.getInstrString(i).equalsIgnoreCase("HIO")	||
					xeTable.getInstrString(i).equalsIgnoreCase("HIO") ) {		// 1byte
				xeTable.addLocatTable(xeTable.getLocationValue(i)+1);
			}
			else if(xeTable.getInstrString(i).charAt(0) == '+') xeTable.addLocatTable(xeTable.getLocationValue(i)+4);
			else {				
				int temp = new Integer(xeTable.getLocationValue(i)+3);
				xeTable.addLocatTable(temp);
			}
		} // location table
//------------------------------------------------------------------------------------------------------------------------------------------		
		
		// pass 2
		String tempValue = new String();
		int registerB = 0;
		for(int i = 0 ; i < xeTable.getLines() ; i++) {
			if(	xeTable.getValueString(i).length() >= 3 && 
				xeTable.getValueString(i).substring(xeTable.getValueString(i).length()-2).equalsIgnoreCase(",X")) {
				tempValue = xeTable.getValueString(i).substring(0, xeTable.getValueString(i).length()-2);
			}
			else tempValue = xeTable.getValueString(i);
			
			if(xeTable.getInstrString(i).equalsIgnoreCase("START")) {
				if(i != 0) {
					System.out.println("Error: \"START\" in line" + (i+1));
					System.exit(i);
				}
				xeTable.addObjecTable("");
				continue;
			}
			if(xeTable.getInstrString(i).equalsIgnoreCase("END")) {
				if(i != xeTable.getLines()-1) {
					System.out.println("Error: \"END\" in line " + (i+1));
					System.exit(i);
				}
				xeTable.addObjecTable("");	
			}
			else if(xeTable.getInstrString(i).length() == 4 && xeTable.getInstrString(i).substring(0, 3).equalsIgnoreCase("RES")){
				xeTable.addObjecTable("");
			}
			else if(xeTable.getInstrString(i).equalsIgnoreCase("WORD")) {
				String temp = String.format("%6s", Integer.toHexString(Integer.parseInt(xeTable.getValueString(i)))).replace(" ", "0").toUpperCase();
				xeTable.addObjecTable(temp);
			}
			else if(xeTable.getInstrString(i).equalsIgnoreCase("BYTE")) {
				if(xeTable.getValueString(i).toUpperCase().charAt(0) == 'C') {
					String subs = xeTable.getValueString(i).substring(2, xeTable.getValueString(i).length()-1);
					String ascii = new String();					
					for(int j = 0 ; j < subs.length() ; j++) {
						ascii = ascii + String.valueOf(Integer.toHexString((int)subs.charAt(j)));
					}
					xeTable.addObjecTable(ascii);
				}
				else if(xeTable.getValueString(i).toUpperCase().charAt(0) == 'X') {
					String subs = xeTable.getValueString(i).substring(2, xeTable.getValueString(i).length()-1);
					xeTable.addObjecTable(subs);
				}				
			}
			else {
				String instruString = new String();
				int xbpe = 0;
				
				String valueString = new String();
				int instruFeature = 3;
				
				if(xeTable.getValueString(i).length() >= 3 && 
					xeTable.getValueString(i).substring(xeTable.getValueString(i).length()-2).equalsIgnoreCase(",X")) {
					instruString = xeTable.getInstrString(i);
					xbpe += 8;
				}
				
				// +instruction
				if(xeTable.getInstrString(i).charAt(0) == '+') {
					instruString = xeTable.getInstrString(i).substring(1);
					xbpe += 1;
				}
				else {
					instruString = xeTable.getInstrString(i);
				}
				
				// #value / @value
				if(xeTable.getValueString(i).equals("")) valueString = "0";
				else if(xeTable.getValueString(i).charAt(0) == '#') {
					valueString = tempValue.substring(1);
					instruFeature = 1;
				}
				else if(xeTable.getValueString(i).charAt(0) == '@') {
					valueString = tempValue.substring(1);
					instruFeature = 2;
				}
				else valueString = tempValue;
				
				int shortInstru = 0;
				String objCode = new String();
				// object code in 1 & 2 
				instructionSet:
				for(int ins = 0 ; ins < instruList_XE.length ; ins++) {			
					if(instruString.equalsIgnoreCase(this.instruList_XE[ins])) {					
						if(ins*4 >= 144 && ins*4 <= 184) {
							if(ins*4 == 120) {
								if(instruFeature == 1 && this.isNumeric(valueString)) registerB = Integer.valueOf(valueString);
								else {
									int loc1 = 0 , loc2 = 0;
									if(this.isNumeric(valueString)) loc1 = Integer.valueOf(valueString);
									extraValueProcess:
									for(int k = 0 ; k < xeTable.getLines() ; k++) {
										if(xeTable.getLabelString(k).equals(valueString)) {
											loc1 = xeTable.getLocationValue(k);
											break extraValueProcess;
										}
										else if(this.isNumeric(valueString) && loc1 == xeTable.getLocationValue(k)) loc2 = Integer.valueOf(xeTable.getObjectCode(k),16);
									}
									if(instruFeature == 1 && !this.isNumeric(valueString)) registerB = loc1; //getLocation;
									else if(instruFeature == 2 && this.isNumeric(valueString)) registerB = loc2;//loc -> loc;
									else if(instruFeature == 2 && !this.isNumeric(valueString)) {
										extraValueProcess2:
										for(int k = 0 ; k < xeTable.getLines() ; k++) {
											if(loc1 == xeTable.getLocationValue(k)) {
												loc2 = Integer.valueOf(xeTable.getObjectCode(k), 16);
												break extraValueProcess2;
											}
										}
										registerB = loc2;
									}; //getLoc -> loc;
								}
							}
							shortInstru = 2;
							objCode += Integer.toHexString(ins*4);
							char value = xeTable.getValueString(i).toUpperCase().charAt(0);
							int registerValue = 0;
							switch(value) {
								case 'T':
									registerValue++;
								case 'S':
									registerValue++;
								case 'B':
									registerValue++;
									registerValue++;
								case 'X':
									registerValue++;
							}
							objCode += Integer.toString(registerValue).toUpperCase();
							if(ins*4 <= 160 || ins*4 == 172 || ins*4 == 176) {
								if(xeTable.getValueString(i).length() == 3) {
								registerValue = 0;
								value = xeTable.getValueString(i).toUpperCase().charAt(2);
								switch(value) {
									case 'T':
										registerValue++;
									case 'S':
										registerValue++;
									case 'B':
										registerValue++;
										registerValue++;
									case 'X':
										registerValue++;
								}
								objCode += Integer.toString(registerValue);
								}								
							}
							
							else if(ins*4 == 164 || ins*4 == 168) {
								objCode += Integer.toHexString(Integer.valueOf(xeTable.getValueString(i).substring(xeTable.getValueString(i).lastIndexOf(',')+1))-1);
							}
							else if(ins*4 == 180 || ins*4 == 184) objCode += "0";
							else {
								if(ins*4 == 164 || ins*4 == 168)System.out.println("Error: No number for the instruction(" + xeTable.getInstrString(i) + ") in line " + (i+1));
								else System.out.println("Error: No register for the instruction(" + xeTable.getInstrString(i) + ") in line " + (i+1));
							}
							
						}
						else if((ins*4 >= 192 && ins*4 <= 200) || (ins*4 >= 240 && ins*4 <= 248)) {
							objCode += Integer.toHexString(ins*4);
							shortInstru = 1;
						}
						else if(ins*4 <= 136) {
							objCode += String.format("%2s", Integer.toHexString(ins*4 + instruFeature)).replace(" ", "0").toUpperCase();
						}
						break instructionSet;
					}
					else if(ins == instruList_XE.length) {
						System.out.println("Error: No match instruction(" + xeTable.getInstrString(i) + ") in line" + (i+1));
					}
				} // instructionSet-----END
				int labelLoc = 0;
				findLabel:
				for(int j = 0 ; j < xeTable.getLines() ; j++) {
					if(shortInstru != 0 || xeTable.getValueString(i).equals("") || (this.isNumeric(valueString))) break findLabel;
					if(xeTable.getLabelString(j).equals(valueString)) {
						labelLoc = xeTable.getLocationValue(j);						
						break findLabel;
					}
					else if(j == xeTable.getLines()-1) {
						System.out.println("Error: No match symbol(" + valueString + ") in line " + (i+1));
						System.exit(i);
					}
				}// findLabel-----END
				
				int relativeValue = 0;
				if(!xeTable.getValueString(i).equals(""))relativeValue = labelLoc - xeTable.getLocationValue(i+1);				
				
				if(xbpe % 2 == 1) ;
				else if(xeTable.getValueString(i).equals("")) xbpe = 0;
				else if(relativeValue <= 2047 && relativeValue >= -2048) xbpe += 2;
				else if(!(relativeValue <= 2047 && relativeValue >= -2048) && (labelLoc-registerB <= 4096 && labelLoc-registerB >=0)) {
					relativeValue = labelLoc-registerB;
					xbpe += 4;
				}
				
				if(shortInstru == 2) xeTable.addObjecTable(String.format("%-6s", objCode));
				else if(shortInstru == 1) xeTable.addObjecTable(objCode);
				else if(xbpe%2 == 1) {
					objCode += Integer.toHexString(xbpe);
					objCode += String.format("%5s", Integer.toHexString(labelLoc)).toUpperCase().replace(" ", "0");
					xeTable.addObjecTable(objCode);
				}
				else if(	instruFeature == 1 && 
							!valueString.equals("") && 
							isNumeric(valueString)) {
					String tempString = new String();
					tempString = Integer.valueOf(valueString).toString();
					
					if(valueString.equals(tempString)) {
						if(xbpe > 7) xbpe = 8;
						else xbpe = 0;
						objCode += Integer.toHexString(xbpe).toUpperCase() + String.format("%3s", Integer.toHexString(Integer.valueOf(valueString))).replace(" ", "0").toUpperCase();
						xeTable.addObjecTable(objCode);
					}
				}
				else {
					objCode += Integer.toHexString(xbpe).toUpperCase();
					if(Integer.toHexString(relativeValue).length() > 3) {
						String tempStr = new String();
						tempStr = Integer.toHexString(relativeValue).substring(Integer.toHexString(relativeValue).length()-3);
						objCode += tempStr;
					}
					else objCode += String.format("%3s", Integer.toHexString(relativeValue).toUpperCase()).replace(" ", "0");
					xeTable.addObjecTable(objCode);	
				}				
			} // else
		} // object code table
	} //public ProduceValue_XE(LoadFile xeTable)

	private boolean isNumeric(String valueStr) {
		for(int i = 0 ; i < valueStr.length() ; i++) {
			if(!Character.isDigit(valueStr.charAt(i))) return false;
		}
		return true;
	}
}
