import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.DecimalFormat;
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
// ------------------------------------------------------------------

/**
  *  This servlet is the main servlet for the student's view of the program.
  *  This servlet retrieves Animals by copy and calls their run() method, 
  *  sending them the user's input.
  *  @author John Reed
  */
public class StudentServlet extends HttpServlet {
    static final long serialVersionUID = 62L;

    DecimalFormat df = new DecimalFormat( "0.00" );


  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    HttpSession session;  
    Animal animal;
    //HashMap state = new HashMap(); // why doesn't this work as an instance variable??
    String runMode; 
    String mode; 
    ArrayList analysis;
    HashMap outputData;

    response.setContentType("text/html");

    PrintWriter out = response.getWriter();

    String name = request.getParameter( "name" ); 

    mode = request.getParameter( "mode" ); 
    if( mode == null ){
      mode = new String( "new" );
    }

    // runMode refers to the how the experiment is being run,
    // either "batch" or "interactive"
    //
    runMode            = request.getParameter( "runMode" ); 
    
    analysis           = new ArrayList();

    session = request.getSession( true );  

    printHeader( out );

    // User is here for the first time, print a blank form 
    if( mode.equals( "new" ) ){
      out.println("<INPUT TYPE=\"hidden\" NAME=\"mode\"  VALUE=\"start\">");
      printPullDown( out );        
      printFooter( out );
      printEnd( out );
    }
   
    // user is running for the first time
    if( mode.equals( "start" ) ){
      session.setAttribute( "ANIMAL", AnimalFactory.getAnimalByCopy( name ) );
      animal = (Animal)session.getAttribute( "ANIMAL" );
      out.println("<INPUT TYPE=\"hidden\" NAME=\"mode\"  VALUE=\"running\">");
      printDescription( out, animal );
      printSetupTable( out );
      printFooter( out );
    }
    
    // user is continuing an experiment
    if( mode.equals( "running" ) ){

      animal = (Animal)session.getAttribute( "ANIMAL" );

      out.println("<INPUT TYPE=\"hidden\" NAME=\"mode\"  VALUE=\"running\">");

      // getParameterValues returns an array, this converts it
      // to an ArrayList
      String[] temp = request.getParameterValues( "analysis" );
      for( int i = 0; i < temp.length; i++ ){
        analysis.add( temp[i] );
      }
  
      HashMap state = new HashMap();
      state.put( "analysis"        , analysis  );
  
      state.put( "infGlStart"      , new Double( request.getParameter( "infGlStart"      ) ) );
      state.put( "infGgStart"      , new Double( request.getParameter( "infGgStart"      ) ) );
      state.put( "infInStart"      , new Double( request.getParameter( "infInStart"      ) ) );
      state.put( "SAinfGlStart"    , new Double( request.getParameter( "SAinfGlStart"    ) ) );
      state.put( "salineStart"     , new Double( request.getParameter( "salineStart"     ) ) );
      state.put( "infGlStop"       , new Double( request.getParameter( "infGlStop"       ) ) );
      state.put( "infGgStop"       , new Double( request.getParameter( "infGgStop"       ) ) );
      state.put( "infInStop"       , new Double( request.getParameter( "infInStop"       ) ) );
      state.put( "SAinfGlStop"     , new Double( request.getParameter( "SAinfGlStop"     ) ) );
      state.put( "salineStop"      , new Double( request.getParameter( "salineStop"      ) ) );
      state.put( "infGl"           , new Double( request.getParameter( "infGl"           ) ) );
      state.put( "infGg"           , new Double( request.getParameter( "infGg"           ) ) );
      state.put( "infIn"           , new Double( request.getParameter( "infIn"           ) ) );
      state.put( "SAinfGl"         , new Double( request.getParameter( "SAinfGl"         ) ) );
      state.put( "infSaline"       , new Double( request.getParameter( "infSaline"       ) ) );
      state.put( "sampleFrequency" , new Double( request.getParameter( "sampleFrequency" ) ) );
      state.put( "lastSampleTime"  , new Double( request.getParameter( "lastSampleTime" ) ) );
      state.put( "runMode"         , runMode );
      state.put( "currentTime"     , new Double( animal.getTime() ) );


      // ----------------------------------------
      // Ths is the main loop of the experiment
      // If the time is OK, it calls the animals
      // run method sending it the user inputs.
      // Animal does calculations and returns
      // the data that students had selected.
      // Users will then have the opportunity
      // to continue running more iterations.
      if( ModelUtil.timeIsValid( out, state ) ){

        animal.run( state );     

        printDescription( out, animal );

        outputData = animal.getOutputData();

        printCurrentSampleData( out, outputData );

        printSetupTable( out, state, runMode, analysis );

        printFooter( out );

        printOutputData( out, outputData );

      }
      else {

        printDescription( out, animal );

        printSetupTable( out, state, runMode, analysis );

        printFooter( out );

      }

      printEnd( out );

    } //ends mode = running

  }

  public void printPullDown( PrintWriter out ){
      Animal one = AnimalFactory.getAnimalByCopy( "animal1" );
      Animal two = AnimalFactory.getAnimalByCopy( "animal2" );
      out.println("<BR>");
      out.println("<SELECT NAME=\"name\" SIZE=\"1\">");
      out.println("<OPTION VALUE=\"animal1\">Animal 1 -- " + one.getDescription() );
      out.println("<OPTION VALUE=\"animal2\">Animal 2 -- " + two.getDescription() );
      out.println("</SELECT>");
      out.println("&nbsp;&nbsp;&nbsp;");
      //      out.println("<BR>");
  }

  public void printHeader( PrintWriter out ){

      out.println("<HTML>");
      //      out.println("<CENTER>");
      out.println("<STRONG>Glucose Homeostasis Simulation v1.1</STRONG> <A HREF=\"/model/StudentServlet\">Run a new simulation</A>");
      //      out.println("<A HREF=\"/model/help.html\">help</A>");
      //      out.println("<A HREF=\"/model/StudentServlet\">run a new simulation</A>");
      out.println("<BR>");
      out.println("<HR>");
      out.println("<FORM ACTION=\"/model/StudentServlet\">");
  }

  public void printDescription( PrintWriter out, Animal animal ){
      out.println("Animal: "     + animal.getName() );
      out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
      //      out.println("Experiment: " + animal.getDescription() );
      //out.println("<BR>");
      //out.println("<BR>");
      out.println( "Current Time: <B> " + animal.getTime() + "</B>" );
      out.println("<BR>");
      out.println("<BR>");


  }

  public void printFooter( PrintWriter out ) {

      out.println("<INPUT TYPE=\"submit\" VALUE=\"Start/Continue\">");
      out.println("<BR>");
      out.println("<HR>");
      //      out.println("<A HREF=\"/model/StudentServlet\">run a new simulation</A><BR>");      
  }

  public void printEnd( PrintWriter out ) {
      out.println("</FORM>");
      out.println("</BODY>");
      out.println("</HTML>");
  }

  /**
   * This takes the data returned from Animal and
   * displays it to the student.
   */
  public void printOutputData( PrintWriter out, HashMap outputData ){
    ArrayList time;
    HashMap gl;
    HashMap gg;
    HashMap in;
    HashMap hotgl;

    time  = (ArrayList)outputData.get( "time" );
    gl    = (HashMap)outputData.get( "gl" );
    gg    = (HashMap)outputData.get( "gg" );
    hotgl = (HashMap)outputData.get( "hotgl" );
    in    = (HashMap)outputData.get( "in" );

    out.println( "time" );
    out.println( "&nbsp;" );
    out.println( "&nbsp;" );

    if( gl.size() > 0 ){
        out.println( "gl  " );
    }
    out.println( "&nbsp;" );
    out.println( "&nbsp;" );

    if( gg.size() > 0 ){
        out.println( "gg  " );
    }
    out.println( "&nbsp;" );
    out.println( "&nbsp;" );

    if( in.size() > 0 ){
        out.println( "in  " );
    }
    out.println( "&nbsp;" );
    out.println( "&nbsp;" );

    if( hotgl.size() > 0 ){
        out.println( "SAGl " );
    }
    out.println( "&nbsp;" );
    out.println( "&nbsp;" );


    out.println( "<BR>" );
   
   for( int i = 0; i < time.size(); i++ ){

     Double t = (Double)time.get( i );
    
     out.println( t );
     out.println( "&nbsp;" );

     if( gl.containsKey( t ) ) {
       out.println( df.format( gl.get( t ) ) );
     }
     else{
       out.println( "&nbsp;" );
     }
    
     if( gg.containsKey( t ) ) {
       out.println( df.format( gg.get( t ) ) );
     }
     else{
       out.println( "&nbsp;" );
     }
    
     if( in.containsKey( t ) ) {
       out.println( df.format( in.get( t ) ) );
     }
     else{
       out.println( "&nbsp;" );
     }
    
     if( in.containsKey( t ) ) {
       out.println( df.format( hotgl.get( t ) ) );
     }
     else{
       out.println( "&nbsp;" );
     }
    
     out.println( "<BR>" );

   }

    out.println( "<BR>" );

  }

  /**
   * This prints the data returned from Animal that
   * is only relavant to the current time point.  This
   * way a user doesn't have to look at a very large,
   * growing speadsheet of data.
   */
  public void printCurrentSampleData( PrintWriter out, HashMap outputData ){
    ArrayList time;
    HashMap gl;
    HashMap gg; 
    HashMap hotgl;
    HashMap in;

    time  = (ArrayList)outputData.get( "time" );
    gl    = (HashMap)outputData.get( "gl" );
    gg    = (HashMap)outputData.get( "gg" );
    hotgl = (HashMap)outputData.get( "hotgl" );
    in    = (HashMap)outputData.get( "in" );

    Double currentSampleTime = (Double)time.get( time.size()-1 ); 

    out.println( "Current Sample Data:" );
    out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" );
    out.println( "<TABLE BORDER=\"1\">" );
    out.println( "<TR>" );
    
    if( gl.containsKey( currentSampleTime ) ) {
      out.println( "<TD ALIGN=\"center\">" );
      out.println( "[gl]" );
      out.println( "</TD>" );
    }

    if( gg.containsKey( currentSampleTime ) ) {
      out.println( "<TD ALIGN=\"center\">" );
      out.println( "[gg]" );
      out.println( "</TD>" );
    }

    if( in.containsKey( currentSampleTime ) ) {
      out.println( "<TD ALIGN=\"center\">" );
      out.println( "[in]" );
      out.println( "</TD>" );
    }

    if( hotgl.containsKey( currentSampleTime ) ) {
      out.println( "<TD ALIGN=\"center\">" );
      out.println( "[SAGl]" );
      out.println( "</TD>" );
    }

    out.println( "</TR>" );

    out.println( "<TR>" );
    if( gl.containsKey( currentSampleTime ) ) {
      out.println( "<TD>" );
      out.println( "<B>" );
      out.println( df.format( gl.get( currentSampleTime ) ) );
      out.println( "</B>" );
      out.println( "</TD>" );
    }
   
    if( gg.containsKey( currentSampleTime ) ) {
      out.println( "<TD>" );
      out.println( "<B>" );
      out.println( df.format( gg.get( currentSampleTime ) ) );
      out.println( "</B>" );
      out.println( "</TD>" );
    }
   
    if( in.containsKey( currentSampleTime ) ) {
      out.println( "<TD>" );
      out.println( "<B>" );
      out.println( df.format( in.get( currentSampleTime ) ) );
      out.println( "</B>" );
      out.println( "</TD>" );
    }
   
    if( hotgl.containsKey( currentSampleTime ) ) {
      out.println( "<TD>" );
      out.println( "<B>" );
      out.println( df.format( hotgl.get( currentSampleTime ) ) );
      out.println( "</B>" );
      out.println( "</TD>" );
    }
    out.println( "</TR>" );

    out.println( "</TABLE>" );
    
  }

  public void printSetupTable( PrintWriter out ){

      //      printCurrentSampleData( out );
      out.println("<BR>");
      out.println("Infusion Setup Table(specify infusion conditions in related box):");
      out.println("<TABLE BORDER=1>");

      out.println("<TR>");
      out.println("<TD>Infusate</TD>");
      out.println("<TD>Glucose<BR>(mmol/min)</TD>");
      out.println("<TD>SAinfGl<BR>(dpm/mmol)</TD>");
      out.println("<TD>Glucagon<BR>(ng/min)</TD>");
      out.println("<TD>Insulin<BR>(mU/min)</TD>");
      out.println("<TD>Saline<BR>(ml/min)</TD>");
      out.println("</TR>");

      out.println("<TR>");
      out.println("<TD>Start Time(min)</TD> ");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGlStart\"   VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"SAinfGlStart\" VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGgStart\"   VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infInStart\"   VALUE=\"0\" SIZE=\"5\"></TD></TD> ");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"salineStart\"  VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("</TR>");

      out.println("<TR>");
      out.println("<TD>Infusion Rate</TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGl\"     VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"SAinfGl\"   VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGg\"     VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infIn\"     VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infSaline\" VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("</TR>");


      out.println("<TR>");
      out.println("<TD>Stop Time(min)</TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGlStop\"   VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"SAinfGlStop\" VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGgStop\"   VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infInStop\"   VALUE=\"0\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"salineStop\"  VALUE=\"0\" SIZE=\"5\"></TD></TD> ");
      out.println("</TR>");
      out.println("</TABLE>");
      out.println("<BR>");
      out.println("<BR>");

      out.println("<TABLE BORDER=\"1\">");
      out.println("<TR><TD>");

      out.println("Sample Setup Table:");
      out.println("<TABLE BORDER=\"1\">");
      out.println("<TR>");
      out.println("<TD>");
      out.println("Blood Sample Frequency (min)");
      out.println("</TD>");
      out.println("<TD>");
      out.println("<INPUT TYPE=\"text\" NAME=\"sampleFrequency\" VALUE=\"1\" SIZE=\"4\">");
      out.println("</TD>");
      out.println("</TR>");

      out.println("<TR>");
      out.println("<TD>");
      out.println("Last Sample Time(min):");
      out.println("</TD>");
      out.println("<TD>");
      out.println("<INPUT TYPE=\"text\" NAME=\"lastSampleTime\" VALUE=\"1\" SIZE=\"4\">");
      out.println("</TD>");
      out.println("</TR>");
      out.println("</TABLE>");

      out.println("</TD>");

      out.println("<TD>");

      out.println(" Analysis:");
      out.println("<BR>");
      out.println("<INPUT TYPE=\"checkbox\" NAME=\"analysis\" VALUE=\"gl\" CHECKED >");
      out.println("[Gl](mM), blood glucose concentration");
      out.println("<BR>");
      out.println("<INPUT TYPE=\"checkbox\" NAME=\"analysis\" VALUE=\"gg\" >");
      out.println("[Gg](ng/L), blood glucagon concentration");
      out.println("<BR>");
      out.println("<INPUT TYPE=\"checkbox\" NAME=\"analysis\" VALUE=\"in\" >");
      out.println("[In](mU/L), blood insulin concentration");
      out.println("<BR>");
      out.println("<INPUT TYPE=\"checkbox\" NAME=\"analysis\" VALUE=\"hotgl\" >");
      out.println("[SAGl](dpm/mmol), specific activity of blood glucose");
      out.println("</TD>");
      out.println("</TR>");
      out.println("</TABLE>");

      out.println("<BR>");

      out.print("Mode: Batch<INPUT TYPE=\"radio\" NAME=\"runMode\" VALUE=\"batch\" CHECKED >");

      out.println("Interactive<INPUT TYPE=\"radio\" NAME=\"runMode\" VALUE=\"interactive\" >");

      out.println("<BR>");
      out.println("<BR>");
      out.println("");
      out.println("");

  }
  public void printSetupTable( PrintWriter out, HashMap state, String runMode, ArrayList analysis ){
  
      out.println("<BR>");
      out.println("Infusion Setup Table(specify infusion conditions in related box):");
      out.println("<TABLE BORDER=1>");

      out.println("<TR>");
      out.println("<TD>Infusate</TD>");
      out.println("<TD>Glucose<BR>(mmol/min)</TD>");
      out.println("<TD>SAinfGl<BR>(dpm/mmol)</TD>");
      out.println("<TD>Glucagon<BR>(ng/min)</TD>");
      out.println("<TD>Insulin<BR>(mU/min)</TD>");
      out.println("<TD>Saline<BR>(ml/min)</TD>");
      out.println("</TR>");

      out.println("<TR>");
      out.println("<TD>Start Time(min)</TD> ");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGlStart\"   VALUE=\"" + state.get( "infGlStart" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"SAinfGlStart\" VALUE=\"" + state.get( "SAinfGlStart" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGgStart\"   VALUE=\"" + state.get( "infGgStart" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infInStart\"   VALUE=\"" + state.get( "infInStart" ) + "\" SIZE=\"5\"></TD></TD> ");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"salineStart\"  VALUE=\"" + state.get( "salineStart" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("</TR>");
      out.println("<TR>");

      out.println("<TD>Infusion Rate</TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGl\"     VALUE=\"" + state.get( "infGl" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"SAinfGl\"   VALUE=\"" + state.get( "SAinfGl" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGg\"     VALUE=\"" + state.get( "infGg" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infIn\"     VALUE=\"" + state.get( "infIn" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infSaline\" VALUE=\"" + state.get( "infSaline" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("</TR>");

      out.println("<TR>");
      out.println("<TD>Stop Time(min)</TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGlStop\"   VALUE=\"" + state.get( "infGlStop" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"SAinfGlStop\" VALUE=\"" + state.get( "SAinfGlStop" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infGgStop\"   VALUE=\"" + state.get( "infGgStop" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"infInStop\"   VALUE=\"" + state.get( "infInStop" ) + "\" SIZE=\"5\"></TD></TD>");
      out.println("<TD><INPUT TYPE=\"text\" NAME=\"salineStop\"  VALUE=\"" + state.get( "salineStop" ) + "\" SIZE=\"5\"></TD></TD> ");
      out.println("</TR>");
      out.println("</TABLE>");
      out.println("<BR>");
      out.println("<BR>");

      out.println("<TABLE BORDER=\"1\">");
      out.println("<TR><TD>");

      out.println("Sample Setup Table:");
      out.println("<TABLE BORDER=\"1\">");
      out.println("<TR>");
      out.println("<TD>");
      out.println("Blood Sample Frequency(min)");
      out.println("</TD>");
      out.println("<TD>");
      out.println("<INPUT TYPE=\"text\" NAME=\"sampleFrequency\" VALUE=\"" + state.get( "sampleFrequency" ) + "\" SIZE=\"4\">");
      out.println("</TD>");
      out.println("</TR>");

      out.println("<TR>");
      out.println("<TD>");
      out.println("Last Sample Time(min):");
      out.println("</TD>");
      out.println("<TD>");
      out.println("<INPUT TYPE=\"text\" NAME=\"lastSampleTime\" VALUE=\"" + state.get( "lastSampleTime" ) + "\" SIZE=\"4\">");
      out.println("</TD>");
      out.println("</TR>");
      out.println("</TABLE>");

      out.println("</TD>");

      out.println("<TD>");

      out.println(" Analysis:");
      out.println("<BR>");
      out.println("<INPUT TYPE=\"checkbox\" NAME=\"analysis\" VALUE=\"gl\"");
      if( analysis.contains( "gl" ) ){
        out.println( " CHECKED" );
      } 
      out.print(">");
      out.println("[Gl](mM), blood glucose concentration");
      out.println("<BR>");
      out.println("<INPUT TYPE=\"checkbox\" NAME=\"analysis\" VALUE=\"gg\"");
      if( analysis.contains( "gg" ) ){
        out.println( " CHECKED" );
      } 
      out.print(">");
      out.println("[Gg](ng/L), blood glucagon concentration");
      out.println("<BR>");
      out.println("<INPUT TYPE=\"checkbox\" NAME=\"analysis\" VALUE=\"in\"");
      if( analysis.contains( "in" ) ){
        out.println( " CHECKED" );
      } 
      out.print(">");
      out.println("[In](mU/L), blood insulin concentration");
      out.println("<BR>");
      out.println("<INPUT TYPE=\"checkbox\" NAME=\"analysis\" VALUE=\"hotgl\"");
      if( analysis.contains( "hotgl" ) ){
        out.println( " CHECKED" );
      } 
      out.print(">");
      out.println("[SAGl](dpm/mmol), specific activity of blood glucose");
      out.println("</TD>");
      out.println("</TR>");
      out.println("</TABLE>");


      out.println("<BR>");
      out.println("<BR>");
      out.print("Mode: Batch<INPUT TYPE=\"radio\" NAME=\"runMode\" VALUE=\"batch\"");
      if( runMode.equals( "batch" ) ){
        out.println( " CHECKED" );
      } 
      out.print(">");

      out.println("Interactive<INPUT TYPE=\"radio\" NAME=\"runMode\" VALUE=\"interactive\"");
      if( runMode.equals( "interactive" ) ){
        out.println( " CHECKED" );
      } 
      out.print(">");

      out.println("<BR>");
      out.println("<BR>");

  }
}
