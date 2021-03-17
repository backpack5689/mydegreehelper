<?php
 
class DbOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }
 
 /*
 * The create operations
 */
    
    //The following function creates a degree
    function createdegree($object, $degreename, $totalhours, $location)
    {
        $stmt = $this->con->prepare("INSERT INTO degree (degreename, location, totalhours, object")
        $stmt->bind_param("ssis", $degreename, $location, $totalhours, $object);
        if($stmt->execute())
            return true;
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
        $stmt = $this->con->prepare("SELECT * FROM degree WHERE id = ?");
        $stmt->bind_param("i", $selector)                                           // <----- Possible Bug Point, not quite sure if this is right //
        $stmt->execute();
        $stmt->bind_result($degreename, $degreelocation, $degreecoursehours, $degreeobject);
        
        $degree = array(); 

        $degree['id'] = $selector; 
        $degree['name'] = $degreename; 
        $degree['location'] = $degreelocation; 
        $degree['course hours'] = $degreecoursehours; 
        $degree['object'] = $degreeobject; 
        
        return $degree; 
    }

    //
 
 /*
 * The update operation
 * When this method is called the record with the given id is updated with the new given values
 */
 function updateHero($id, $name, $realname, $rating, $teamaffiliation){
 $stmt = $this->con->prepare("UPDATE heroes SET name = ?, realname = ?, rating = ?, teamaffiliation = ? WHERE id = ?");
 $stmt->bind_param("ssisi", $name, $realname, $rating, $teamaffiliation, $id);
 if($stmt->execute())
 return true; 
 return false; 
 }
 
 
 /*
 * The delete operation
 * When this method is called record is deleted for the given id 
 */
 function deleteHero($id){
 $stmt = $this->con->prepare("DELETE FROM heroes WHERE id = ? ");
 $stmt->bind_param("i", $id);
 if($stmt->execute())
 return true; 
 
 return false; 
 }
}
?>