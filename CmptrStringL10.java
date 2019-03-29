/* Matt Packwood, Orchard Ridge Campus, Monday Evening Class, Fall Semester 2003
 *
 * PA10:  Model figure MVC w/inheritance/array/String concepts
 * screen size 576x432
 *
 * The Applet from PA09 is modified to incorporate name Strings.
 * The user inputs a comma-delimited name set into a String array associated
 * to the figs array by index; names set to figs using index correspondence.
 *
 * The user requests a large display via text entry of name string; the fig
 * is selected by name search.  The user can also change the name set and
 * control variable feature display via scrollbar
 * copyright2002 by Henry Austin and Oakland CC
 */
import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.util.*; // for Random class

public class CmptrStringL10 extends Applet
    implements AdjustmentListener, ActionListener, MouseListener { // 3 events
	TextField inTextTF; // text input ref
	Scrollbar ballB; // screen ball control
	Label ballL; // ball count feedback
	boolean runF= false, nmF= false; // control flags
	int f, smlSlot; // fig index, sml fig slot size
	int scrnW, scrnH; // screen bounds
	final int FIGS= 8, WDGTh= 60, BALLmax= 6;  // constants
	int smlX, smlY, smlSz, bigX, bigY, bigSz, ballCt; // state vars
	Color bgClr= Color.white, fgClr= Color.black;
	Graphics g; // global screen ref
	CompSys [ ] figs; // ref to array of model obj refs
	Color FIGclr [ ]= {null, Color.yellow, Color.green, Color.cyan, // corr clrs
		 Color.gray, Color.pink, Color.orange, Color.magenta, Color.white};
	String [ ] figNms; // ref to corr String array of StickFig names
	int [ ] unitSales; // ref to corr count array
 	String nmInstrS= "Key set of "+ FIGS+ " names w/comma delimiters;"+
 		" press enter"; // text box instructions: no name set
 	String dsplyInstrS= "Key fig name for big display OR key new name set"+
 		" of "+ FIGS+ "names; press enter"; // text box instructions: name set
	Random r; // random obj ref
	MatchString findIt; // ref to String search obj
public void init ( ) {
	setBackground (bgClr); // BG color
	inTextTF= new TextField (45); // for text input, instructions
	add (inTextTF);
	inTextTF.addActionListener (this);
	ballL= new Label ("Balls= "+ ballCt); // ball ct feedback
	add (ballL);
	ballB= new Scrollbar (Scrollbar.HORIZONTAL, 0, 1, 0, BALLmax+ 1); // same as PEx9
	add (ballB);
	ballB.addAdjustmentListener (this);
    this.addMouseListener (this); // activate mouse events
	r= new Random (-32767); // construct Random obj; seed for sameness
	findIt= new MatchString ( ); // cnstrct search obj
	Dimension scrnSz= getSize ( ); // get size of applet screen from HTML tag
	scrnW= scrnSz.width;  scrnH= scrnSz.height; // set screen dim vars
		// calc base vals
	smlSlot= scrnH /8 ;// slot height for sml fig
	smlSz= smlSlot /6; // 20% of 7/8 slot size
	smlX= 18; // qtr inch from left edge
	smlY= 40; // init to top slot
	bigSz= scrnH / 7; // max ht
	bigX= 150; // rt edge
	bigY= scrnH -100; // screen bot- mrgn
		// construct corr arrays
	figs= new CompSys [FIGS+ 1]; // init fig array to N+ 1
	figNms= new String [FIGS+ 1]; // init corr name array
	unitSales= new int [FIGS+ 1]; // init assoc sales array
		// set fig colors & associated sale vals
	for (int i= 1; i <= FIGS; i++) { // ref by traverse index
 		figs [i]= new CompSys (smlX, smlY, smlSz); // cnstrct w/tiny base vals
		figs [i].setClr (FIGclr [i] ); // set to corr fig color
		figs [i].setID (i* i+ i); // hash ID from index (crude....)
			// gen unit sales from bell-curve distribution w/avg=0, std dev=1;
		double c= r.nextGaussian ( ); // 95% of vals are in -2 thru +2 range
		if (c < -4) // compress outliers
			c= -4;
		else if (c > 4)
			c= 4;
		unitSales [i]= (int) ((c+4)* 250); // scale to avg 1000, std dev 250
		smlY+= smlSlot; // incrs y for nxt fig
		} // end traverse
	}
public void paint (Graphics gg) {
	g= gg; // global screen
	g.setFont (new Font ("SansSerif", Font.BOLD, 14)); // dressUp applet text
	inTextTF.setText (runF ? dsplyInstrS : nmInstrS);
	if (runF)  // bigFig dsply request
		dsplyBigFig ( );
	else
		upDateBallCt (0);
		// upDateBallCt with zero
	for (int i= 1; i <= FIGS; i++) { // ref by traverse index
		figs [i].dsplyFig (g); // dsply small fig on screen left side
		g.setColor(Color.black);
		g.drawString (""+figs [i].getName ( ), smlSz*7, figs [i].getY( ) ); // ID nxt to fig
			// x/y here...
		} // end traverse
	// SEE LOGIC OUTLINE
	}
private void dsplyBigFig ( ) { // ref by index set in action event method
	int y= figs [f].getY( ); // save model obj y for tiny, side dsply
	// set figs[f] x/y/sz vals for big dsply
	figs [f].setX (bigX);
	figs [f].setY (bigY);
	figs [f].setSz (bigSz);
	figs [f].dsplyFig (g); // dsply big fig
	g.setColor (Color.black);
	g.drawString ("ID="+figs [f].getID ( )+ ", Unit Sales="+unitSales [f], bigX+bigSz, bigY-(4*bigSz)-9 ); // dsply id & unit sales above big dsply
	// restore figs[f] state to tiny, side dsply
	figs [f].setX (smlX);
	figs [f].setY (y);
	figs [f].setSz (smlSz);
	// SEE LOGIC OUTLINE
	}
public void actionPerformed (ActionEvent e) { // only one widget...
	int i; // method scope...
	String inText= inTextTF.getText ( ); // name set string
	inText= inText.toLowerCase ( ); // force case

	if (-1 == inText.indexOf(",",0) ) {
		f = findIt.findMatch (inText,figNms);
		if (f >= 1)
			{
			showStatus (inText+"** registered **");
			runF = true;
			upDateBallCt (figs [f].getBallCt( ) );
			}
		else
			{
			showStatus ("** NO MATCH for ["+inText+"]");
			}
		}
	else {
	StringTokenizer picker= new StringTokenizer (inText,",");
	for (i= 1; picker.hasMoreTokens () && i <= FIGS; i++) { // trav inText
		figNms [i]= picker.nextToken ( );  // parse name set
		figNms [i]= figNms [i].trim ( ); // remove white space
		}
	if (i == FIGS+ 1) { // set the right size ?
		nmF= true; // OK; register name set
//		msg= "valid name set"; // for statusLn
		showStatus ("** name set registered **");
		for (f=1; f <= FIGS; f++) // set nms in fig objects
			figs [f].setName (figNms [f]); // index corr
		}
	else { // invalid set; too small, bad delimiters, etc
		nmF= false; // set may be corrupted
//		msg= "INVALID name set: ["+ inText+ "]"; // for statusLn
		showStatus ("invalid name set");
		}
	}
	repaint();
	// SEE LOGIC OUTLINE
	}
private void upDateBallCt (int bCt) { // update applet ball count
		ballCt = bCt ; // set global applet ballCt
		ballB.setValue (ballCt);// updte scroll ctr
		ballL.setText("Balls= "+ ballCt);  // updte ball ctr feedback label
	}
public void adjustmentValueChanged (AdjustmentEvent e) { // SB event
	if (f > 0) { // large figure displayed?
		// updte applet ballCt with scroll bar value
		figs [f].setBallCt ( ballB.getValue( ) ); // updte fig ballCt
		showStatus (figs [f].getName ( )+ "'s screen balls updated"); // name and screen balls updated msg
		repaint ( ); // feedback updated ballCt on screen
		}
	else // no large figure selected
		showStatus ("display large figure before using scrollbar"); // msg to dsply big fig before using scrollbar
	}
public void mouseClicked (MouseEvent mE) {
    inTextTF.setText (""); // clear text field instructions
    }
public void mouseReleased (MouseEvent e) { }
public void mousePressed (MouseEvent e) { }
public void mouseEntered (MouseEvent e) { }
public void mouseExited (MouseEvent e) { }
} // ** END APPLET CLASS **

class MatchString  {  // from search demo....
	private boolean ordered= false; // assume unordered argu set
	private String arguKey [ ];
	private String srchKey;
	private int fnalIdx= -1; // assume unmatched
public MatchString ( ) { } // default constructor; assumes unordered table
public MatchString (boolean seq) { // true=ordered table, false=unordered
	ordered = seq;
	}
public int findMatch (String srch, String tbl [ ] ) {
	srchKey= srch.trim ( ); // trim and set global
	arguKey= tbl; // set global
	fnalIdx= -1; // assume unmatched
	if (ordered)
		srchOrd ( );
	else
		srchBrute ( );
	return fnalIdx;
	}
void srchOrd ( ) { // assumes ordered tbl strings
	for (int i = 0; i < arguKey.length; i++) {
		int rel = srchKey.compareTo (arguKey [i]);
		if (rel < 0) {  // srch < tbl; no match in rmndr of tbl
			break; // exit srch loop; unmatched assumed
			}
		else if (rel == 0) { // match
			fnalIdx = i; break; // set index and exit srch loop
			}
		} // end for
	} // end srchOrd  loop fallOut assumes unmatched
void srchBrute ( ) {	// assumes unordered tbl strings
	for (int i = 0; i < arguKey.length; i++)
		if (srchKey.equals (arguKey [i])){
			fnalIdx = i; break; // match; set index & exit srch loop
			} // endif
	} // end srchBrute  loop fallout assumes unmatched
} // end MatchString

class CompSys { // SAME AS PEx10 plus var, set and get for String name
// PA08 base model class here; update with var for int id,
// setter/getter methods for x, y, sz, id and ballCt.
//
	int bX, bY, bS; // base vars
	int qS, hS, dS; // work ratio vars; yours may vary...
	int ballCt, figId; // ball ctr and ID
	boolean zipF; // ZIP option on/off
	Graphics g; // class-scope screen ref
	Color sysClr = (Color.yellow); // change Color.yellow refs to sysClr
	String name ;
// ** PUBLIC, INTERACTION METHODS **
public CompSys (int x, int y, int s) { // ** CONSTRUCTOR **
	// init global base, ballCt and zipF vals here
	bX = x;
	bY = y;
	bS = s;
	name = "";
	}
public void setX (int x) { // ** SETTER METHODS **
	// update bX here
	bX=x;
	}
public void setY (int y) {
	bY=y;
	}
public void setBallCt (int ct) {
	// update ballCt here
	ballCt = ct;
	}
public void setID (int id) {
	// update figID here
	figId = id;
	}
public void setClr (Color c) {
	// update sysClr here
	sysClr = c;
	}
public void setSz (int s) {
	// update Sz here
	bS = s;
	}
public void setName (String n) {
	// update name here
	name = n;
	}
public void toglOptStat ( ) { // change toglZIP refs to toglOpt
	zipF= ! zipF;
	}
public boolean getOptStat ( ) {
	return zipF;
	}
public int getBallCt ( ) {
	return ballCt;
	}
public int getX ( ) {
	return bX;
	}
public int getY ( ) {
	return bY;
	}
public int getID ( ) {
	return figId;
	}
public int getSz ( ) {
	return bS;
	}
public String getName ( ) {
	return name;
	}
public void dsplyFig (Graphics gg) {
	g= gg;
	calcRatios ( );
	dsplyKeyBord ( );
	dsplyPtrDev ( );
	dsplyCPU ( );
	dsplyDsply ( );
	}
void calcRatios ( ) {
	// same old same old......
	qS= Math.round (bS/4.0f); // calc ratio vals
	hS= Math.round (bS/2.0f); // calc ratio vals
	dS= Math.round (bS*2.0f); // calc ratio vals
	}
void dsplyCPU ( ) {
	g.setColor (sysClr); //draw stuff
	// CPU, CD, opt ZIP
	g.fillRect (bX, bY-bS, dS+dS+bS, bS); // CPU
	g.setColor (Color.black);
	g.drawRect (bX, bY-bS, dS+dS+bS, bS); // CPU outline
	g.fillRect (bX+dS+hS, bY-hS-qS, dS, qS); // DVD slot
	// conditional ZIP slot display; see hat dsply technique in SF demo
	if (zipF) {
		g.fillRect (bX+hS, bY-bS+qS, bS, qS);
		}
	}
void dsplyKeyBord ( ) {
	// KB & keys
	g.setColor (sysClr);
	g.fillRect (bX, bY, dS+dS, bS); // Keyboard
	g.setColor (Color.black);
	g.drawRect (bX, bY, dS+dS, bS); // Keyboard Outline
	g.fillRect (bX+qS, bY+qS, dS+bS, hS); // Keys
	}
void dsplyPtrDev ( ) {
	// mouse, buttons & cord
	g.setColor (sysClr);
	g.fillOval (bX+dS+dS+qS, bY+qS, hS, hS); // Mouse
	g.setColor (Color.black);
	g.drawOval (bX+dS+dS+qS, bY+qS, hS, hS); // Mouse outline
	g.drawLine (bX+dS+dS, bY+hS, bX+dS+dS+qS, bY+hS); // Mouse cord
	g.fillArc (bX+dS+dS+qS, bY+qS, hS, hS, 45, 90); // Mouse 1
	g.fillArc (bX+dS+dS+qS, bY+qS, hS, hS, 225, 90); // Mouse 2
	}
void dsplyDsply ( ) {
	// monitor, screen & opt balls
	g.setColor (sysClr);
	g.fillRect (bX+hS, bY-dS-dS, dS+dS, dS+bS); // Monitor
	g.setColor (Color.black);
	g.drawRect (bX+hS, bY-dS-dS, dS+dS, dS+bS); // Monitor Outline
	g.setColor (Color.lightGray);
	g.fillRect (bX+hS+qS, bY-dS-bS-hS-qS, dS+bS+hS, dS+hS); // Screen
	// conditional screen ball display; see hair display technique in SF demo
	switch (ballCt) { // no breaks == fall-thru
		case 6: 	g.setColor (Color.black);
				g.fillOval (bX+bS+dS-(((ballCt-3)%3)*bS), bY-dS-hS, bS, bS);
		case 5: 	g.setColor (Color.red);
				g.fillOval (bX+bS+dS-(((ballCt-2)%3)*bS), bY-dS-hS, bS, bS);
		case 4: 	g.setColor (Color.yellow);
				g.fillOval (bX+bS+dS-(((ballCt-1)%3)*bS), bY-dS-hS, bS, bS);
		case 3: 	g.setColor (Color.green);
				g.fillOval (bX+bS+dS-(((ballCt-3)%3)*bS), bY-dS-hS-((ballCt <=5 ? 0 : 1)*bS), bS, bS);
		case 2: 	g.setColor (Color.blue);
				g.fillOval (bX+bS+dS-(((ballCt-2)%3)*bS), bY-dS-hS-((ballCt <=4 ? 0 : 1)*bS), bS, bS);
		case 1: 	g.setColor (Color.white);
				g.fillOval (bX+bS+dS-(((ballCt-1)%3)*bS), bY-dS-hS-((ballCt <=3 ? 0 : 1)*bS), bS, bS);
		} // end switch; minus values ignored
	}
}
