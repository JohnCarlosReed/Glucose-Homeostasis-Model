//import java.io.*; 
//import javax.servlet.*;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.TreeMap;
// ---------------------------------------------------------------------
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
// ---------------------------------------------------------------------

/**
 *  This is the core class of an experiment.  It maintains all of the
 *  variables used in calculations.  Unfortunately, since compounds
 *  may need to know the conentration of other compounds, I've had to
 *  make them publically available.
 *  @author John Reed
 */
public class FluxManager{


  //  Typically, public variables are considered weak programing.  I
  //  left them as public because there are just so many variables.
  //  Otherwise I would have to have gets/sets for each one!
  // 
  public double dt, vol, iGgsig, iInsig, iGl, iIn, iGg, iHotGl, time;

  public double Ggsig, Insig, cGl, cIn, cGg, SAGl;

  public double absGl;

  public double GNG, UGl, PIn, UIn, PInsig, UInsig, PGgsig, UGgsig;  
  
  public double infGl, infGg, infSaline;  
  
  public double PGg, UGg;

  public double expGg, expIn;
  
  public double fedGl, cabs, blabs, b0abs, tabson;

  public double VGNG, KGgGNG, JInGNG, VGl, KGlUGl, KInUGl, JGgUGl, 
                          VGg, JGlPGg, kGg, VIn, KGlPIn, kIn, kPInsig, kUInsig,
			  kPGgsig, kUGgsig, SAabsGl, SAinfGl, SAGNG,
			  infIn, basalIn;



  public FluxManager(){

    initializeModel();

  }


  /*
   * COPY CONSTRUCTOR
   */
  public FluxManager( FluxManager _other ){
    
    copyState( _other );

  }

  /**
   * This is the main method.  This method is called every time
   * a K is calculated in the RungeKutta algorithm.
   */
  public final void evaluateFluxes(){

    // Fed glucose will not be introduced in version 1
    if( false ){
      double Aabs = fedGl/( 1/cabs - 1/(blabs+cabs) );  
      absGl = ( Aabs * (b0abs - Math.exp( -blabs * (time-tabson) ) ) ) * Math.exp( -cabs * (time-tabson) );
    }     
    else{
      absGl  = 0;  
    }

    GNG    = VGNG / ( 1 + KGgGNG/Ggsig + Insig/JInGNG ); 
    UGl    = VGl  / ( 1 + KGlUGl/cGl   + KInUGl/Insig + Ggsig/JGgUGl );
    PIn    = VIn  / ( 1 + Math.pow( KGlPIn/cGl, expIn )) + basalIn ;  
    UIn    = kIn     * cIn;  
    PInsig = kPInsig * cIn; 
    UInsig = kUInsig * Insig;
    PGg    = VGg  / ( 1 + Math.pow( cGl/JGlPGg, expGg ) );
    UGg    = kGg * cGg;   
    PGgsig = kPGgsig * cGg; 
    UGgsig = kUGgsig * Ggsig;
  }


  /**
   * This set's the constants for an experiment.  It is called by
   * the Animal through an Administrator.
   */
  public final void setState(  HttpServletRequest request ){
  
    Double _vol     = new Double( request.getParameter( "vol" ) );
    Double _iGgsig  = new Double( request.getParameter( "iGgsig" ) );
    Double _iInsig  = new Double( request.getParameter( "iInsig" ) );
    Double _iGl     = new Double( request.getParameter( "iGl" ) );
    Double _iIn     = new Double( request.getParameter( "iIn" ) );
    Double _iGg     = new Double( request.getParameter( "iGg" ) );
    Double _iHotGl  = new Double( request.getParameter( "iHotGl" ) );
    Double _VGNG    = new Double( request.getParameter( "VGNG" ) );
    Double _KGgGNG  = new Double( request.getParameter( "KGgGNG" ) );
    Double _JInGNG  = new Double( request.getParameter( "JInGNG" ) );
    Double _VGl     = new Double( request.getParameter( "VGl" ) );
    Double _KGlUGl  = new Double( request.getParameter( "KGlUGl" ) );
    Double _KInUGl  = new Double( request.getParameter( "KInUGl" ) );
    Double _JGgUGl  = new Double( request.getParameter( "JGgUGl" ) );
    Double _VGg     = new Double( request.getParameter( "VGg" ) );
    Double _JGlPGg  = new Double( request.getParameter( "JGlPGg" ) );
    Double _kGg     = new Double( request.getParameter( "kGg" ) );
    Double _VIn     = new Double( request.getParameter( "VIn" ) );
    Double _KGlPIn  = new Double( request.getParameter( "KGlPIn" ) );
    Double _basalIn = new Double( request.getParameter( "basalIn" ) );
    Double _kIn     = new Double( request.getParameter( "kIn" ) );
    Double _kPInsig = new Double( request.getParameter( "kPInsig" ) );
    Double _kUInsig = new Double( request.getParameter( "kUInsig" ) );
    Double _kPGgsig = new Double( request.getParameter( "kPGgsig" ) );
    Double _kUGgsig = new Double( request.getParameter( "kUGgsig" ) );
    Double _SAabsGl = new Double( request.getParameter( "SAabsGl" ) );  
    Double _SAGNG   = new Double( request.getParameter( "SAGNG" ) );
    Double _expGg   = new Double( request.getParameter( "expGg" ) );
    Double _expIn   = new Double( request.getParameter( "expIn" ) );
    Double _dt      = new Double( request.getParameter( "dt" ) );
    Double _cabs    = new Double( request.getParameter( "cabs" ) );
    Double _blabs   = new Double( request.getParameter( "blabs" ) );
    Double _b0abs   = new Double( request.getParameter( "b0abs" ) );
    Double _tabson  = new Double( request.getParameter( "tabson" ) );
    vol     = _vol.doubleValue();
    iGgsig  = _iGgsig.doubleValue();
    iInsig  = _iInsig.doubleValue();
    iGl     = _iGl.doubleValue();
    iIn     = _iIn.doubleValue();
    iGg     = _iGg.doubleValue();
    iHotGl  = _iHotGl.doubleValue();
    VGNG    = _VGNG.doubleValue();
    KGgGNG  = _KGgGNG.doubleValue();
    JInGNG  = _JInGNG.doubleValue();
    VGl     = _VGl.doubleValue();
    KGlUGl  = _KGlUGl.doubleValue();
    KInUGl  = _KInUGl.doubleValue();
    JGgUGl  = _JGgUGl.doubleValue();
    VGg     = _VGg.doubleValue();
    JGlPGg  = _JGlPGg.doubleValue();
    kGg     = _kGg.doubleValue();
    VIn     = _VIn.doubleValue();
    KGlPIn  = _KGlPIn.doubleValue();
    basalIn = _basalIn.doubleValue();
    kIn     = _kIn.doubleValue();
    kPInsig = _kPInsig.doubleValue();
    kUInsig = _kUInsig.doubleValue();
    kPGgsig = _kPGgsig.doubleValue();
    kUGgsig = _kUGgsig.doubleValue();
    SAabsGl = _SAabsGl.doubleValue();  
    SAGNG   = _SAGNG.doubleValue();
    expGg   = _expGg.doubleValue();
    expIn   = _expIn.doubleValue();
    dt      = _dt.doubleValue();
    cabs    = _cabs.doubleValue();
    blabs   = _blabs.doubleValue();
    b0abs   = _b0abs.doubleValue();
    tabson  = _tabson.doubleValue();

    //fedGl   = _fedGl.doubleValue(); Not used for v1.0

  }

  /*
   * This returns the state of FluxManager for the purpose
   * of creating a copies instead of references to the data.
   */
  public final void copyState( FluxManager other ){
    
    Ggsig = other.Ggsig;
    Insig = other.Insig;
    cGl = other.cGl;
    cIn = other.cIn;
    cGg = other.cGg;
    SAGl = other.SAGl;

    vol       = other.vol;
    iGgsig    = other.iGgsig;
    iInsig    = other.iInsig;
    iGl       = other.iGl;
    iIn       = other.iIn;
    iGg       = other.iGg;
    iHotGl    = other.iHotGl;
    infGl     = other.infGl;
    infGg     = other.infGg;
    infIn     = other.infIn;
    SAinfGl   = other.SAinfGl;
    infSaline = other.infSaline;
    VGNG      = other.VGNG;
    KGgGNG    = other.KGgGNG;
    JInGNG    = other.JInGNG;
    VGl       = other.VGl;
    KGlUGl    = other.KGlUGl;
    KInUGl    = other.KInUGl;
    JGgUGl    = other.JGgUGl;
    VGg       = other.VGg;
    JGlPGg    = other.JGlPGg;
    kGg       = other.kGg;
    VIn       = other.VIn;
    KGlPIn    = other.KGlPIn;
    basalIn   = other.basalIn;
    kIn       = other.kIn;
    kPInsig   = other.kPInsig;
    kUInsig   = other.kUInsig;
    kPGgsig   = other.kPGgsig;
    kUGgsig   = other.kUGgsig;
    SAabsGl   = other.SAabsGl;  
    SAGNG     = other.SAGNG;
    expGg     = other.expGg;
    expIn     = other.expIn;
    dt        = other.dt;
    time      = other.time;
    fedGl     = other.fedGl;
    cabs      = other.cabs;
    blabs     = other.blabs;
    b0abs     = other.b0abs;
    tabson    = other.tabson;
  }


  /**
   * This is the initial state that FluxManager is put in when
   * the webserver is started.
   */
  public final void initializeModel(){
    vol       = 8.0;
    iGgsig    = 80.0;
    iInsig    = 8.0;
    iGl       = 40.0;
    iIn       = 64.0;
    iGg       = 640.0;
    iHotGl    = 0.0000000001;
    infGl     = 0.0;
    infGg     = 0.0;
    infIn     = 0.0;
    SAinfGl   = 0.0;
    infSaline = 0.0;
    VGNG      = 2.21;
    KGgGNG    = 125;
    JInGNG    = 40.0;
    VGl       = 7.072;
    KGlUGl    = 11.0;
    KInUGl    = 40.0;
    JGgUGl    = 125;
    VGg       = 172.07;
    JGlPGg    = 4.0;
    kGg       = 0.625;
    VIn       = 91.8;
    KGlPIn    = 8.0;
    basalIn   = 0.0075;
    kIn       = 1.0;
    kPInsig   = 1.0;
    kUInsig   = 1.0;
    kPGgsig   = 0.5;
    kUGgsig   = 0.5;
    SAabsGl   = 0.0;  
    SAGNG     = 0.0;
    expGg     = 4.0;
    expIn     = 5.0;
    dt        = 0.005; 
    time      = 0.0; // FIX THIS!!!!!!!!!!!
    fedGl     = 0.0;
    cabs      = 0.015;
    blabs     = 0.03;
    b0abs     = 1.0;
    tabson    = 120.0;


    //set initial concentrations
    Ggsig = iGgsig;
    Insig = iInsig;
    cGl   = iGl/vol;
    cIn   = iIn/vol;
    cGg   = iGg/vol;
    SAGl  = iHotGl/iGl;

  }

  /**
   * These are the paramters that can be set by the students.  It is
   * set through Animal.
   */
  public void setInfusionData( HashMap infusions ){
    infGl     = ( (Double)infusions.get( "infGl"     ) ).doubleValue();
    infGg     = ( (Double)infusions.get( "infGg"     ) ).doubleValue();
    infIn     = ( (Double)infusions.get( "infIn"     ) ).doubleValue();
    SAinfGl   = ( (Double)infusions.get( "SAinfGl"   ) ).doubleValue();
    infSaline = ( (Double)infusions.get( "infSaline" ) ).doubleValue();
  }

  /**
   * This returns the constants that are setable by the Administrator.
   */
  public TreeMap getAdminParams (){
    TreeMap adminParams = new TreeMap();
    adminParams.put( "dt",      new Double( this.dt ) );
    adminParams.put( "vol",     new Double( this.vol ) );
    adminParams.put( "iGgsig",  new Double( this.iGgsig ) );
    adminParams.put( "iInsig",  new Double( this.iInsig ) );
    adminParams.put( "iGl",     new Double( this.iGl ) );
    adminParams.put( "iIn",     new Double( this.iIn ) );
    adminParams.put( "iGg",     new Double( this.iGg ) );
    adminParams.put( "iHotGl",  new Double( this.iHotGl ) );
    adminParams.put( "VGNG",    new Double( this.VGNG ) );
    adminParams.put( "KGgGNG",  new Double( this.KGgGNG ) );
    adminParams.put( "JInGNG",  new Double( this.JInGNG ) );
    adminParams.put( "VGl",     new Double( this.VGl ) );
    adminParams.put( "KGlUGl",  new Double( this.KGlUGl ) );
    adminParams.put( "KInUGl",  new Double( this.KInUGl ) );
    adminParams.put( "JGgUGl",  new Double( this.JGgUGl ) );
    adminParams.put( "VGg",     new Double( this.VGg ) );
    adminParams.put( "JGlPGg",  new Double( this.JGlPGg ) );
    adminParams.put( "kGg",     new Double( this.kGg ) );
    adminParams.put( "VIn",     new Double( this.VIn ) );
    adminParams.put( "KGlPIn",  new Double( this.KGlPIn ) );
    adminParams.put( "basalIn", new Double( this.basalIn ) );
    adminParams.put( "kIn",     new Double( this.kIn ) );
    adminParams.put( "kPInsig", new Double( this.kPInsig ) );
    adminParams.put( "kUInsig", new Double( this.kUInsig ) );
    adminParams.put( "kPGgsig", new Double( this.kPGgsig ) );
    adminParams.put( "kUGgsig", new Double( this.kUGgsig ) );
    adminParams.put( "SAabsGl", new Double( this.SAabsGl ) );
    adminParams.put( "SAGNG",   new Double( this.SAGNG ) );
    adminParams.put( "expGg",   new Double( this.expGg ) );
    adminParams.put( "expIn",   new Double( this.expIn ) );
    adminParams.put( "cabs",    new Double( this.cabs ) );
    adminParams.put( "blabs",   new Double( this.blabs ) );
    adminParams.put( "b0abs",   new Double( this.b0abs ) );
    adminParams.put( "tabson",  new Double( this.tabson ) );

    return adminParams;
  }

} //ends class FluxManager
