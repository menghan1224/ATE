package cn.edu.buaa.sei.rucm.spec.widgets;


public interface TextCellDelegate {

	public void cellTabPressed(TextCell cell, boolean shift);
	public void cellUpPressed(TextCell cell, int offset);
	public void cellDownPressed(TextCell cell, int offset);
	public void cellEnterPressed(TextCell cell);
	public void cellBackspacePressed(TextCell cell);
	
}
