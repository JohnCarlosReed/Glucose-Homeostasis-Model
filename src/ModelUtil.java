import java.io.*; 
import java.util.HashMap;
//import javax.servlet.http.*;  
// ------------------------------------------------------------------
//
//  NAME:  BOVINE METABOLISM SIMULATION
//
//  VERSION:  1.0
//
//  DESCRIPTION:
//    This program simulates bovine metabolism of glucose,
//  insulin, glucagon, and radio-active glucose.
//
//  DATE:
//  08.16.01
//
// ------------------------------------------------------------------
 
/**
 *  This is a utility class for checking user inputs.
 *  So far it is incomplete, it only checks for time.  This
 *  will be the place though for checking that the user
 *  does not enter negative numbers or odd characters(it
 *  proved to be more difficult than I could solve within
 *  the time provided).  This needs to be fixed for the
 *  next version.
 *
 * @author John Reed
 */
public class ModelUtil {

  private static boolean validity = true;
  private static PrintWriter out;
  private static HashMap state;
    //  private static String message = new String();

  /** Checks to see if the user entered in a time.  If they did
   *  not, enter will print an error message in red.
   */
  public static boolean timeIsValid( PrintWriter _out, HashMap _state) {
    validity = true; 
    state = _state;
    out   = _out;

    out.println( "<FONT COLOR=\"red\">" ); 
    out.println( "<STRONG>" ); 

    checkLastSampleTime();

    out.println( "</STRONG>" ); 
    out.println( "</FONT>" ); 

    return validity;
  }

  /** Checks to make sure that their last sample point is greater than
   *  the current time.
   */
  private static void checkLastSampleTime() {
    double lastSampleTime = ((Double)state.get( "lastSampleTime" )).doubleValue();
    Double cTime = (Double)state.get( "currentTime" );
    double currentTime = cTime.doubleValue(); // just in case this is a ref

    if( lastSampleTime <= currentTime ){
      validity = false;
      out.println( "<BR>Error: Last Sample Time is less than current time<BR>" ); 
    }
  }

}
