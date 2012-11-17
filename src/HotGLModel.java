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
public class HotGLModel extends RungeKutta{

  private RungeKutta sister;

  public HotGLModel( FluxManager _fm, RungeKutta _gl ){
    super();
    fm = _fm;
    initial = fm.iHotGl;
    sister  = _gl;
  }

  /** defines how and at what rate concentrations change */
  protected double derivative(){
    return fm.absGl * fm.SAabsGl + fm.infGl*fm.SAinfGl + fm.GNG*fm.SAGNG - fm.UGl*fm.SAGl;
  }

  /** after a single calculation, the concentrations needs to be reset */
  protected void resetConcentration(){
    //       = iHotGl/Gl
    fm.SAGl     = newValue/sister.getNewValue();
  }

}
