// ---------------------------------------------------------
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
// ---------------------------------------------------------

/**
  * This is a chemical component whose concentration is
  * being simulated by bovine metabolic rates.
  * @author John Reed
  * @see GLModel
  * @see INModel
  * @see GGModel
  * @see GGsigModel
  * @see HotGLModel
  */
public class InsigModel extends RungeKutta{

  public InsigModel( FluxManager _fm ){
    super( );
    fm = _fm;
    initial = fm.iInsig;
  }

  /** defines how and at what rate concentrations change */
  protected double derivative(){
    return fm.PInsig - fm.UInsig;
  }

  /** after a single calculation, the concentrations needs to be reset */
  protected void resetConcentration(){
    fm.Insig = newValue;
    // concentration is not used 
  }

}
