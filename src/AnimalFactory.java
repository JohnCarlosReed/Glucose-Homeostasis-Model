// ------------------------------------------------------------
//  NAME:  BOVINE METABOLISM SIMULATION
// 
//   VERSION:  1.0
// 
//   DESCRIPTION:
//     This program simulates bovine metabolism of glucose,
//   insulin, glucagon, and radio-active glucose.
// 
//   DATE:
//   08.16.01
// 
// -----------------------------------------------------------
/**
 * This class maintains the animals residing in memory
 * by the webserver.  It returns Animals by reference for
 * the administrator to configure them.  It returns the
 * animals by copy so the students can run experiments on them.
 * @author John Reed
 */
public class AnimalFactory {

    private static Animal animal1 = new Animal( "animal1" );
    private static Animal animal2 = new Animal( "animal2" );

    /*
     * This calls Animals copy constructory and returns a new
     * animal.
     */
    public static Animal getAnimalByCopy( String input ){
      // should probably fix this so that animal2 isn't default 
      if( input.equals( "animal1" ) ){
        return ( new Animal( animal1 ) );
      }
      else{
        return ( new Animal( animal2 ) );
      }
    }

    /*
     * This class returns an animal by reference so Administrators
     * can set their constants.
     */
    public static Animal getAnimalByRef( String input ){
      // should probably fix this so that animal2 isn't default 
      if( input.equals( "animal1" ) ){
        return ( animal1 );
      }
      else{
        return ( animal2 );
      }
        
    }

}







    


    


