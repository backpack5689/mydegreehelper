<?php
 
class DbOperation
{
    //Database connection link
    private $con;
    public $idvalue = 0;
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/dbconnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();

        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }
 
 /*
 * The create operations
 */
    
    function getvar()
    {
        return $this->idvalue;
    }
    //The following function creates a degree
    function createdegree($object, $degreename, $totalhours, $location)
    {
        $stmt = $this->con->prepare("INSERT INTO degree (degree_name, degree_location, degree_coursehours, degree_object) VALUES (?, ?, ?, ?);");
        $stmt->bind_param("ssis", $degreename, $location, $totalhours, $object);
        
        if($stmt->execute()){
            $stmt2 = $this->con->prepare("SELECT max(degree_id) from degree;");
            $stmt2->execute();
            $stmt2->bind_result($throwaway);
            $this->idvalue = $throwaway;
            return true;
        }
        return false;
    }

    //The following function creates a user
    
 /*
 * The read operations
 */
    
    //The following function goes and gets a specific degree 
    //You will find the degree on the selector
    function getdegree($selector)
    {
        $stmt = $this->con->prepare("SELECT * FROM degree WHERE degree_id = ?");
        $stmt->bind_param("i", $selector);                                           // <----- Possible Bug Point, not quite sure if this is right //
        $stmt->execute();
        $degree = $stmt->bind_result($selector, $degreename, $degreelocation, $degreecoursehours, $degreeobject);

        $stmt->fetch();

        $degree = array(); 

        $degree['id'] = $selector; 
        $degree['name'] = $degreename; 
        $degree['location'] = $degreelocation; 
        $degree['course hours'] = $degreecoursehours; 
        $degree['object'] = $degreeobject; 
        return $degree; 
    }

    //The following function goes and gets all the degrees in order
    //to list them to choose from; this will only return id's, names,
    //locations, and course horus. To get the java object, you will need to 
    //send the id once clicked on into the function getdegree
    function showalldegree()
    {
        $stmt = $this->con->prepare("SELECT degree_id, degree_name, degree_location, degree_coursehours FROM degree");
        $stmt->execute();
        $stmt->bind_result($id, $name, $location, $coursehours);
        
        $degrees = array(); 
        
        while($stmt->fetch())
        {
            $degree  = array();
            $degree['id'] = $id; 
            $degree['name'] = $name; 
            $degree['location'] = $location; 
            $degree['coursehours'] = $coursehours; 
            
            array_push($degrees, $degree); 
        }
        
        return $degrees; 
    }
 
 /*
 * The update operation
 * When this method is called the record with the given id is updated with the new given values
 */

 //This function updates the degree course list object in the database
 function updatedegree($degreeid, $degreeobject)
 {
    $stmt = $this->con->prepare("UPDATE degree SET degree_object = ? WHERE degree_id = ?");
    $stmt->bind_param("si", $degreeobject, $degree_id);
    if($stmt->execute())
        return true; 
    return false; 
 }
 
 
 /*
 * The delete operation
 * When this method is called record is deleted for the given id 
 */ 
 function deletedegree($id){
    $stmt = $this->con->prepare("DELETE FROM degree WHERE degree_id = ? ");
    $stmt->bind_param("i", $id);
    if($stmt->execute())
        return true; 
    
    return false; 
 }
}
?>