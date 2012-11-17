//import java.io.*;
import javax.servlet.http.*;
//import javax.servlet.*;
import java.util.*;
import java.text.DecimalFormat;
// ------------------------------------------------------------
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
// ------------------------------------------------------------

/**
 * This class encapsulates an entire experiment.  From here
 * other classes can run experiments via the run() mehtod.
 * @author John Reed 
 */
public class Animal{

  private String name;
  private String description;
  private double time;
  private double stopTime;

  private FluxManager fm;

  private HashMap outputData;

  private GLModel       gl;
  private GGModel       gg;
  private HotGLModel    hotGl;
  private INModel       in;
  private InsigModel    insig;
  private GGsigModel    ggsig;

  private DecimalFormat df = new DecimalFormat( "0.000" );

  /**
  * Animal Constructor
  * @param name the name of the animal
  */
  public Animal( String _name ){

    name = _name;
    description = new String( "Default Experiment" );
    time = 0;

    fm    = new FluxManager();
    gl    = new GLModel( fm );
    gg    = new GGModel( fm );
    hotGl = new HotGLModel( fm,  gl );
    in    = new INModel( fm );
    insig = new InsigModel( fm );
    ggsig = new GGsigModel( fm );

    outputData = new HashMap(); 
    outputData.put( "time",  new ArrayList() );
    outputData.put( "gl",    new HashMap() );
    outputData.put( "gg",    new HashMap() );
    outputData.put( "hotgl", new HashMap() );
    outputData.put( "in",    new HashMap() );
  }

  /**
  * Animal Copy Constructor
  * Copies the data from one animal to create
  * a new one by value, not by ref.
  * @param animal
  */
  public Animal( Animal _other ){
    name           = new String( _other.name );
    description    = new String( _other.description );
    time           = _other.time; 

    fm    = new FluxManager( _other.fm );
    gl    = new GLModel( fm );
    gg    = new GGModel( fm );
    hotGl = new HotGLModel( fm,  gl );
    in    = new INModel( fm );
    insig = new InsigModel( fm );
    ggsig = new GGsigModel( fm );


    outputData = new HashMap(); 
    outputData.put( "time",  new ArrayList() );
    outputData.put( "gl",    new HashMap() );
    outputData.put( "gg",    new HashMap() );
    outputData.put( "hotgl", new HashMap() );
    outputData.put( "in",    new HashMap() );
  }

  /**
  * This function is used by the admininistrator.  This
  * is where the animal's constants are set.
  * Note: I really should change this to take a Java Collection
  * rather than an HttpServletRequest!
  * @param request an servlet request
  */
  public void setAdminVariables(  HttpServletRequest request ){
    description = request.getParameter( "description" ); 
    fm.setState( request );
  }

  /**
  * This returns the data that the student has chosen to see
  * from the analysis checkboxes.
  */
  public HashMap getOutputData(){

    return new HashMap( outputData );
  }

  /**
  * This method updates the data results structure each round of calculation.
  */
  private void updateOutputData( HashMap state ){
    ArrayList analysis = (ArrayList)state.get( "analysis" );
    
    ArrayList alTime;
    HashMap gg;
    HashMap gl;
    HashMap hotgl;
    HashMap in;

    //    HashMap row = new HashMap();

    alTime = (ArrayList)outputData.get( "time" );
    alTime.add( new Double( time ) ); 

    gl    = (HashMap)outputData.get( "gl" );
    gg    = (HashMap)outputData.get( "gg" );
    in    = (HashMap)outputData.get( "in" );
    hotgl = (HashMap)outputData.get( "hotgl" );

    if( analysis.contains( "gl" ) ){
      gl.put( new Double( time ),  new Double( fm.cGl  ) );
    }

    if( analysis.contains( "gg" ) ){
      gg.put( new Double( time ),  new Double( fm.cGg  ) ); 
    }

    if( analysis.contains( "in" ) ){
      in.put( new Double( time ), new Double( fm.cIn  ) );
    }

    if( analysis.contains( "hotgl" ) ){
      hotgl.put( new Double( time ), new Double( fm.SAGl ) );
     }

    outputData.put( "gl",    gl );
    outputData.put( "gg",    gg );
    outputData.put( "in",    in );
    outputData.put( "hotgl", hotgl );
    outputData.put( "time",  alTime );
   

    // outputData is an ArrayList,
    // index ordered, just like Arrays
    // outputData.add( row );

  }

  /**
  * This is Animals main method.
  * @param state the state is a HashMap containing
  * all the data necessary to complete calculations.  Such
  * as the current time, and all the user inputs.
  */
  public void run( HashMap state ){
    
    Vector models = new Vector();

    double sampleFrequency = ((Double)state.get( "sampleFrequency" )).doubleValue();
    String runMode         = (String)state.get( "runMode" );

    double timeInterval; 


    stopTime = ((Double)state.get( "lastSampleTime" )).doubleValue();


    if ( runMode.equals( "batch" ) ){
      timeInterval = stopTime;
    }
    else{
      timeInterval = sampleFrequency;
    }

    models.add( gl    );
    models.add( hotGl );
    models.add( in    );
    models.add( insig );
    models.add( gg    );
    models.add( ggsig );

    RungeKutta rk;

    if (time < stopTime ) {
      do{
       
         fm.setInfusionData( this.getInfusionData( state ) );

         Enumeration enumx = models.elements();

         fm.evaluateFluxes(); 

         while( enumx.hasMoreElements() ){
            rk = (RungeKutta) enumx.nextElement();
            rk.calculateK1();
            rk.resetConcentration();
         }


         enumx = models.elements();
         while( enumx.hasMoreElements() ){
            rk = (RungeKutta) enumx.nextElement();
            rk.calculateK2();
            rk.resetConcentration();
         }

         fm.evaluateFluxes(); 
         enumx = models.elements();
         while( enumx.hasMoreElements() ){
            rk = (RungeKutta) enumx.nextElement();
            rk.calculateK3();
            rk.resetConcentration();
         }

         fm.evaluateFluxes(); 
         enumx = models.elements();
         while( enumx.hasMoreElements() ){
            rk = (RungeKutta) enumx.nextElement();
            rk.calculateK4();
            rk.resetConcentration();
         }
    
          
          time += fm.dt;
          time = Double.valueOf( df.format( time ) ).doubleValue();
 
          if( ( time % sampleFrequency ) == 0 ){
  	  this.updateOutputData( state );
  	}

      } 
      while( (time % timeInterval) != 0 ); 

    } // end if( time < stopTime )

  }

  /*
  * This method checks to see if the current time is
  * in between the start and stop time of a particular
  * infusion.  If it is, it sets the infusion value
  * to the user input, if not, it sets the infusion
  * This is a "chain of responsibility" type method
  * The actual user of the data retrieved is FluxManager.
  * value to zero.
  */
  private HashMap getInfusionData( HashMap state ){

    HashMap infusions = new HashMap();
  
    double infGlStart      = ((Double)state.get( "infGlStart"      )).doubleValue();
    double infGgStart      = ((Double)state.get( "infGgStart"      )).doubleValue();
    double infInStart      = ((Double)state.get( "infInStart"      )).doubleValue();
    double SAinfGlStart    = ((Double)state.get( "SAinfGlStart"    )).doubleValue();
    double salineStart     = ((Double)state.get( "salineStart"     )).doubleValue();
    double infGlStop       = ((Double)state.get( "infGlStop"       )).doubleValue();
    double infGgStop       = ((Double)state.get( "infGgStop"       )).doubleValue();
    double infInStop       = ((Double)state.get( "infInStop"       )).doubleValue();
    double SAinfGlStop     = ((Double)state.get( "SAinfGlStop"     )).doubleValue();
    double salineStop      = ((Double)state.get( "salineStop"      )).doubleValue();
    double infGl           = ((Double)state.get( "infGl"           )).doubleValue();
    double infGg           = ((Double)state.get( "infGg"           )).doubleValue();
    double infIn           = ((Double)state.get( "infIn"           )).doubleValue();
    double SAinfGl         = ((Double)state.get( "SAinfGl"         )).doubleValue();
    double infSaline       = ((Double)state.get( "infSaline"       )).doubleValue();

    double _infGl, _infGg, _infIn, _SAinfGl, _infSaline;

    if( (infGlStart < time) && ( infGlStop > time ) ){
      _infGl = infGl;
    }
    else {
      _infGl = 0.0;
    }

    if( ( infGgStart < time) && ( infGgStop > time ) ){
      _infGg = infGg;
    }
    else {
      _infGg = 0.0;
    }

    if( ( infInStart < time) && (  infInStop > time ) ){
      _infIn = infIn;
    }
    else {
      _infIn = 0.0;
    }

    if( ( SAinfGlStart < time) && ( SAinfGlStop > time ) ){
      _SAinfGl = SAinfGl;
    }
    else {
      _SAinfGl = 0.0;
    }

    if( ( salineStart < time) && ( salineStop > time ) ){
      _infSaline = infSaline;
    }
    else {
      _infSaline = 0.0;
    }

    infusions.put( "infGl",     new Double( _infGl     ) );
    infusions.put( "infGg",     new Double( _infGg     ) );
    infusions.put( "infIn",     new Double( _infIn     ) );
    infusions.put( "SAinfGl",   new Double( _SAinfGl   ) );
    infusions.put( "infSaline", new Double( _infSaline ) );

    return infusions;

  }

  /**
  * Chain of responsibility method.
  * This method retrieves the setable constants from
  * FluxManager.  It also add's Animal's description and 
  * returns them as a TreeMap.
  */
  public TreeMap getAdminParams(){

    TreeMap adminParams = fm.getAdminParams(); 

    adminParams.put( "description", description );

    return adminParams;
  }


  /** returns the Animal's name */
  public String getName(){ return name; }

  /** returns the Animal's description */
  public String getDescription(){ return description; }

  /** returns the Animal's current time */
  public double getTime(){ return time; }

}


