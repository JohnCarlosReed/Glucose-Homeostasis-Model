import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.TreeMap;
//import java.util.Iterator;
// --------------------------------------------------------------------------
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
// --------------------------------------------------------------------------

/**
 *
 *  This servlet gives administrative access to Animal's set-able constants.
 *  When the webserver is started, Animals persist in memory.  
 *  AdminServlet accesses them through references and is able to modify 
 *  their variables.  It is basically a Get/Set servlet.  
 *  @author John Reed
 */
public class AdminServlet extends HttpServlet {

    static final long serialVersionUID = 42L;

    public Animal animal;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {


    String mode = request.getParameter( "mode" );
    if( mode != null ){
      animal = AnimalFactory.getAnimalByRef( request.getParameter( "name" ) );
    }
   
    response.setContentType("text/html");

    PrintWriter out = response.getWriter();

    printHeader( out );

    if ( mode == null ){
    
      printPopupMenu( out );
    }
    else if ( mode.equals( "update" ) ){
    
      printPopupMenu( out );

      updateAnimal( request );  
    }
    else if ( mode.equals( "edit" ) ){

      printEditableAdminParams( out );  

      printUpdateButton( out );
    }

    printFooter( out );

  }

  /**
   * This takes the Administrators inputs and sends them
   * to Animal for setting.
   */
  public void updateAnimal( HttpServletRequest request ){

    String _name = request.getParameter( "name" );
    System.out.println("updateAnimal() name is " + _name );

    animal.setAdminVariables( request );
  }

  public void printUpdateButton( PrintWriter out ){
      //    out.println("<CENTER>");
    out.println("<TABLE>");
    out.println("<TR>");
    out.println("<TD>");
    out.println("<INPUT TYPE=\"submit\" VALUE=\"Update\">");
    out.println("</TD>");
    out.println("<TD>");
    out.println("<A HREF=\"/model/AdminServlet\">Cancel</A>");
    out.println("</TD>");
    out.println("</TR>");
    out.println("</TABLE>");
    out.println("<INPUT TYPE=\"hidden\" NAME=\"mode\" VALUE=\"update\">");
    //out.println("</CENTER>");
  }

  public void printPopupMenu( PrintWriter out ){
      //    out.println("<CENTER>");
    out.println("<SELECT NAME=\"name\" SIZE=\"1\">");
    out.println("<OPTION VALUE=\"animal1\">Animal 1");
    out.println("<OPTION VALUE=\"animal2\">Animal 2");
    out.println("</SELECT>");
    out.println("<INPUT TYPE=\"submit\" VALUE=\"Edit\">");
    out.println("<INPUT TYPE=\"hidden\" NAME=\"mode\" VALUE=\"edit\">");
    //out.println("</CENTER>");
    out.println("<BR>");
  }

  public void printHeader( PrintWriter out ){
    out.println("<HTML>");

    out.println("<HEAD>");
    out.println("<TITLE>Servlet Administration Page</TITLE>");
    out.println("</HEAD>");

    out.println("<BODY>");

    out.println("<FORM ACTION=\"/model/AdminServlet\">");
    //out.println("<CENTER>");
    //    out.println("<H3><STRONG>Administration Page</STRONG></H2><BR><BR>");
    out.println("<H3>Glucose Homeostasis Simulation v1.0 Parameter Setup</H3>");
    //out.println("<A HREF=\"/model/AdminServlet\">Admin Page</A>");
    //out.println("&nbsp;|&nbsp;");
    //out.println("<A HREF=\"/model/StudentServlet\">Student Page</A>");
    //out.println("&nbsp;|&nbsp;");
    //out.println("<A HREF=\"/model/javadocs/\">JavaDocs</A>");
    //out.println("<BR>");
    out.println("<HR>");
    //out.println("</CENTER>");
    //out.println("<BR>");
    //out.println("<BR>");


  }

  public void printFooter( PrintWriter out ){
    out.println("</FORM>");
    //out.println("<CENTER>");
    //out.println("<BR>");
    //out.println("<BR>");
    out.println("<HR>");
    //out.println("<A HREF=\"/model/AdminServlet\">Admin Page</A>");
    //out.println("&nbsp;|&nbsp;");
    //out.println("<A HREF=\"/model/StudentServlet\">Student Page</A>");
    //out.println("&nbsp;|&nbsp;");
    //out.println("<A HREF=\"/model/javadocs/\">JavaDocs</A>");
    out.println("<BR>");
    //out.println("</CENTER>");

    out.println("</BODY>");
    out.println("</HTML>");
  }


  public void printEditableAdminParams( PrintWriter out ){

    TreeMap adminParams = animal.getAdminParams();

    //out.println("<CENTER>");
    out.println("<A HREF=\"/model/AdminServlet\">Cancel</A>");
    out.println( "<TABLE>" );
    out.println( "<TR>" );
    out.println( "<TD>Animal:</TD>" );
    out.println( "<TD><B>" + animal.getName() + "</B></TD>" );
    out.println( "</TR>" );
    out.println( "<TR>" );
    out.println( "<TD>Experiment:</TD>" );
    out.println( "<TD><INPUT TYPE=\"text\" NAME=\"description\" VALUE=\"" + adminParams.get( "description") + "\" SIZE=\"20\"></TD>" );
    out.println( "</TR>" );
    out.println( "<TR>" );
    out.println( "<TD>dt:</TD>" );
    out.println( "<TD><INPUT TYPE=\"text\" NAME=\"dt\" VALUE=\"" + adminParams.get( "dt") + "\" SIZE=\"20\"></TD>" );
    out.println( "</TR>" );
    out.println( "<TR>" );
    out.println( "<TD>vol:</TD>" );
    out.println( "<TD><INPUT TYPE=\"text\" NAME=\"vol\" VALUE=\"" + adminParams.get( "vol") + "\" SIZE=\"20\"></TD>" );
    out.println( "</TR>" );
    out.println( "<TR>" );
    out.println("<TD>iGgsig:</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"iGgsig\" VALUE=\"" + adminParams.get( "iGgsig") + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );
    out.println( "<TR>" );
    out.println("<TD>iInsig</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"iInsig\" VALUE=\"" + adminParams.get( "iInsig") + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );
    out.println( "<TR>" );
    out.println("<TD>iGl</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"iGl\" VALUE=\"" + adminParams.get( "iGl") + "\" SIZE=\"20\"><TD/>");
    out.println( "</TR>" );
    out.println( "<TR>" );
    out.println("<TD>iIn</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"iIn\" VALUE=\"" + adminParams.get( "iIn") + "\" SIZE=\"20\"><TD/>");
    out.println( "</TR>" );
    out.println( "<TR>" );
    out.println("<TD>iGg</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"iGg\" VALUE=\"" + adminParams.get( "iGg") + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>iHotGl</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"iHotGl\" VALUE=\"" + adminParams.get( "iHotGl") + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>VGNG</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"VGNG\" VALUE=\"" + adminParams.get( "VGNG") + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>KGgGNG</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"KGgGNG\" VALUE=\"" + adminParams.get( "KGgGNG") + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>JInGNG</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"JInGNG\" VALUE=\"" + adminParams.get( "JInGNG") + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>VGl</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"VGl\" VALUE=\"" + adminParams.get( "VGl") + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>KGlUGl</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"KGlUGl\" VALUE=\"" + adminParams.get( "KGlUGl" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>KInUGl</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"KInUGl\" VALUE=\"" + adminParams.get( "KInUGl" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>JGgUGl</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"JGgUGl\" VALUE=\"" + adminParams.get( "JGgUGl" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>VGg</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"VGg\" VALUE=\"" + adminParams.get( "VGg" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>JGlPGg</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"JGlPGg\" VALUE=\"" + adminParams.get( "JGlPGg" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>kGg</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"kGg\" VALUE=\"" + adminParams.get( "kGg" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>VIn</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"VIn\" VALUE=\"" + adminParams.get( "VIn" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>KGlPIn</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"KGlPIn\" VALUE=\"" + adminParams.get( "KGlPIn" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>basalIn</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"basalIn\" VALUE=\"" + adminParams.get( "basalIn" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>kIn</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"kIn\" VALUE=\"" + adminParams.get( "kIn" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>kPInsig</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"kPInsig\" VALUE=\"" + adminParams.get( "kPInsig" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>kUInsig</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"kUInsig\" VALUE=\"" + adminParams.get( "kUInsig" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>kPGgsig</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"kPGgsig\" VALUE=\"" + adminParams.get( "kPGgsig" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>kUGgsig</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"kUGgsig\" VALUE=\"" + adminParams.get( "kUGgsig" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>SAabsGl</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"SAabsGl\" VALUE=\"" + adminParams.get( "SAabsGl" ) + "\" SIZE=\"20\"></TD>");

    out.println( "<TR>" );
    out.println("<TD>SAGNG</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"SAGNG\" VALUE=\"" + adminParams.get( "SAGNG" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>expGg</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"expGg\" VALUE=\"" + adminParams.get( "expGg" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>expIn</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"expIn\" VALUE=\"" + adminParams.get( "expIn" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>cabs</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"cabs\" VALUE=\"" + adminParams.get( "cabs" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>blabs</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"blabs\" VALUE=\"" + adminParams.get( "blabs" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>b0abs</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"b0abs\" VALUE=\"" + adminParams.get( "b0abs" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );

    out.println( "<TR>" );
    out.println("<TD>tabson</TD>");
    out.println("<TD><INPUT TYPE=\"text\" NAME=\"tabson\" VALUE=\"" + adminParams.get( "tabson" ) + "\" SIZE=\"20\"></TD>");
    out.println( "</TR>" );


    out.println( "</TABLE>" );
    //out.println("</CENTER>");
    out.println("<BR>");

    //Hidden var!
    out.println( "<INPUT TYPE=\"hidden\" NAME=\"name\" VALUE=\"" + animal.getName() + "\" >" );

  }

}




