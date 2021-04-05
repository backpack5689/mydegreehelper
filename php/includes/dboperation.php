<?php
 
class DbOperation
{
    //Database connection link
    private $con;
    public $idvalue;
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/dbconnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
        $idvalue = 0;
 
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
        $stmt = $this->con->prepare(
            "INSERT INTO degree (degree_name, degree_location, degree_coursehours, degree_object) VALUES (?, ?, ?, ?);
             SELECT max(degree_id) FROM degree;
            ");
        $stmt->bind_param("ssis", $degreename, $location, $totalhours, $object);
        
        if($stmt->execute())
            $stmt->bind_result($idvalue);
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
        $stmt = $this->con->prepare("SELECT * FROM degree WHERE degree_id = ?");
        $stmt->bind_param("i", $selector);                                           // <----- Possible Bug Point, not quite sure if this is right //
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

/* The sign up operation */
function signup($username, $email, $password) {

    //checking if the user is already exist with this username or email
    $stmt = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
    $stmt->bind_param("ss", $username, $email);
    $stmt->execute();
    $stmt->store_result();
    
    
    //if the user is successfully added to the database 
    if($stmt->num_rows > 0){
    $stmt->close();
    }else{
    //if user is new creating an insert query 
    $stmt = $conn->prepare("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $username, $email, $password);

    //if the user is successfully added to the database 
    if($stmt->execute()){

    //fetching the user back 
    $stmt = $conn->prepare("SELECT id, id, username, email, password FROM users WHERE username = ?"); 
    $stmt->bind_param("s",$username);
    $stmt->execute();
    $stmt->bind_result($userid, $id, $username, $email);
    $stmt->fetch();
    
    $users = array();
        $users['id'] = $id; 
        $users['username'] = $username; 
        $users['email'] = $email; 
        $users['password'] = $password;

    $stmt->close();
    return true;
    }
}
return false;
    
}

/* Login operation */
function login($username, $password) {

    //creating the query 
    $stmt = $conn->prepare("SELECT id, username, email, password FROM users WHERE username = ? AND password = ?");
    $stmt->bind_param("ss",$username, $password);
    $stmt->execute();
    $stmt->store_result();

    if($stmt->num_rows > 0) {
    $stmt->bind_result($id, $username, $email, $password);
    $stmt->fetch();

    $users = array();
        $users['id'] = $id; 
        $users['username'] = $username; 
        $users['email'] = $email; 
        $users['password'] = $password; 

    return true;
    }
    return false;
}
}
?>