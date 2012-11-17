// --------------------------------------------------------------
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
//  NOTES:
// @author John Reed
// --------------------------------------------------------------

/**
 * An abstract class that will be extended by the compounds(Model).
 * Runge Kutta is a calculus algorithm used to calculate polynomials
 * with higher precision.  Each compound will have to define derivative(), 
 * which will define how and with what rate their concentration will change.
 */
public abstract class RungeKutta{

  protected FluxManager fm;   // Defined in the child class!
  protected double initial;   // Defined in the child class!   
  protected double newValue;
  private double dydt1, dydt2, dydt3, dydt4; 

  public RungeKutta(){
  }

  protected void calculateK1(){
    dydt1 = derivative();
    newValue = initial + fm.dt * ( dydt1 / 2 );
  }

  protected void calculateK2(){

    dydt2 = derivative();
    newValue = initial + fm.dt * ( dydt2 / 2 );

  }

  protected void calculateK3(){

    dydt3 = derivative();
    newValue = initial + (fm.dt * dydt3);

  }

  protected void calculateK4(){

    dydt4 = derivative();

    newValue = (initial + (fm.dt/6) * ( dydt1 + 2*dydt2 + 2*dydt3 +dydt4) );

    setNewState();

  }
  /** needs to be defined by the compounds(Models) */
  protected abstract double derivative();

  /** needs to be defined by the compounds(Models) */
  protected abstract void resetConcentration();

  /** after a new concentration is calculated, it is set here */
  protected void setNewState(){ initial = newValue; } 

  /* returns the new conentration */
  protected double getNewValue(){  return newValue; }


}

