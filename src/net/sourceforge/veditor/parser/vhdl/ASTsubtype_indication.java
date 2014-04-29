/**
 * 
 * This file is based on the VHDL parser originally developed by
 * (c) 1997 Christoph Grimm,
 * J.W. Goethe-University Frankfurt
 * Department for computer engineering
 *
 **/
package net.sourceforge.veditor.parser.vhdl;



/* Generated By:JJTree: Do not edit this line. ASTsubtype_indication.java */
/* JJT: 0.3pre1 */

public class ASTsubtype_indication extends SimpleNode {
  ASTsubtype_indication(int id) {
    super(id);
  }
  
  public String getIdentifier(){
	  //ASTname astName=(ASTname)getChild(0);
	  //return ((ASTidentifier)(astName.getChild(0))).name;
	  
	  String completetype = "";
	  Token curtoken = getFirstToken();
	  if(curtoken!=null) {
		  completetype = curtoken.toString();
		  while(curtoken!=getLastToken() && curtoken.next!=null) {
			  curtoken = curtoken.next;
			  completetype = completetype + " " +curtoken.toString();
		  }
	  }
	  return completetype;
  }
}
