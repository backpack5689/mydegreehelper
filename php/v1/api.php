<?php
/*
* Following tutorial here: https://www.simplifiedcoding.net/android-mysql-tutorial-to-perform-basic-crud-operation/#What-is-CRUD
* For register/login here: https://www.simplifiedcoding.net/android-login-and-registration-tutorial/
*/
 //getting the dboperation class
 require_once '../includes/dboperation.php';

 //function validating all the paramters are available
 //we will pass the required parameters to this function

 function isTheseParametersAvailable($params){
    //assuming all parameters are available
    $available = true;
    $missingparams = "";

    foreach($params as $param){
        if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
            $available = false;
            $missingparams = $missingparams . ", " . $param;
        }
    }

    //if parameters are missing
    if(!$available){
        $response = array();
        $response['error'] = true;
        $response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';

        //displaying error
        echo json_encode($response);

        //stopping further execution
        die();
    }
 }

 //an array to display response
 $response = array();

 //if it is an api call
 //that means a get parameter named api call is set in the URL
 //and with this parameter we are concluding that it is an api call
 if(isset($_GET['apicall'])){

    switch($_GET['apicall']){

        /*
        * the CREATE operations
        */
        //if the api call value is 'createdegree'
        //we will create a record in the database
        case 'createdegree':
            //first check the parameters required for this request are available or not
            isTheseParametersAvailable(array('object','degreename','totalhours','location'));

            //creating a new dboperation object
            $db = new DbOperation();

            //creating a new record in the database
            $result = $db->createdegree(
                $_POST['object'],
                $_POST['degreename'],
                $_POST['totalhours'],
                $_POST['location']
            );


            //if the record is created adding success to response
            if($result){
                //record is created means there is no error
                $response['error'] = false;

                //in message we have a success message
                $response['message'] = 'Degree addedd successfully';

                //and we are getting all the heroes from the database in the response
                $response['id'] = $db->getvar();

            }else{

                //if record is not added that means there is an error
                $response['error'] = true;

                //and we have the error message
                $response['message'] = 'Some error occurred please try again';
            }

        break;

        /*
        * These are the read operations
        */
        // Get Degree function. Get's a degree given an id.
        case 'getdegree':
            $db = new DbOperation();
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['degree'] = $db->getdegree($_GET['selector']);
        break;


        // Show All Degrees. Show's all the degree's in the database.
        case 'showalldegree':
            $db = new DbOperation();
            $response['error'] = false;
            $response['message'] = 'Request successfully completed';
            $response['degrees'] = $db->showalldegree();
        break;

        /*
        * the UPDATE operation
        */
        // update degree. This will update the degree object based on the id
        /* case 'updatedegree':
            isTheseParametersAvailable(array('degree_id', 'degreeobject'));
            $db = new DbOperation();
            $result = $db->updatedegree(
                $_POST['degree_id'],
                $_POST['degreeobject']
            );

            if($result){
                $response['error'] = false;
                $response['message'] = 'Degree updated successfully';
                $response['degree'] = $db->getdegree($_POST['degree_id']);
            }else{
                $response['error'] = true;
                $response['message'] = 'There was some error. To begin tracing, check case "updatedegree" in api.php';
            }
        break; */

        //the delete operation
        /* case 'deletedegree':

            //for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
            if(isset($_GET['id'])){
                $db = new DbOperation();
                if($db->deletedegree($_GET['id'])){
                    $response['error'] = false;
                    $response['message'] = 'Degree deleted successfully';
                    $response['degrees'] = $db->showalldegree();
                }else{
                    $response['error'] = true;
                    $response['message'] = 'Some error occurred please try again';
                }
            }else{
                $response['error'] = true;
                $response['message'] = 'There was some error. To begin tracing, check case "deletedegree" in api.php';
            }
        break; */

        /* Sign up operation */
        case 'signup':

            isTheseParametersAvailable(array('username','email','password'));

            $db = new DbOperation();

            $result = $db->signup(
                $_POST['username'],
                $_POST['email'],
                md5($_POST['password']),
            );

                //if the user already exist in the database
                if($result){
                    $response['error'] = false;
                    $response['message'] = 'User registered successfully';
                }else{
                    $response['error'] = true;
                    $response['message'] = 'User already registered';
                    //$response['user'] = $db->signup($_POST['username']);
                }
            break;

        /* Login operation */
        case 'login':

            //for login we need the username and password
            isTheseParametersAvailable(array('username', 'password'));

            $db = new DbOperation();

            $result = $db->login(
                $_POST['username'],
                md5($_POST['password']),
            );

                if($result['success']){
                    $response['error'] = false;
                    $response['message'] = 'Login was successfull';
                    $response['user'] = $_POST['username'];
                    $response['id'] = $result['id'];
                }else{
                    $response['error'] = true;
                    $response['message'] = 'Invalid username or password';
                }

	    break;
	case 'saveuserprogress':
		isTheseParametersAvailable(array('userid','jsonstring'));
		$db = new DbOperation();

		$result = $db->updateuserprogress(
			$_POST['userid'],
			$_POST['jsonstring']
		);
		if($result)
		{
			$response['error'] = false;
			$response['message'] = 'Update was successful';
			$response['user'] = $_POST['userid'];
		}
		else
		{
			$response['error'] = true;
			$response['message'] = "An error was encountered trying to update the user progress";
		}
		break;

	// Get user information
	case 'getuser':
		isTheseParametersAvailable(array('userid'));
		$db = new DbOperation();

		$result = $db->getuserinfo($_POST['userid']);

        if($result['success']){
          $response['error'] = false;
          $response['user_id'] = $result['user_id'];
          $response['user_fname'] = $result['user_fname'];
          $response['user_lname'] = $result['user_lname'];
          $response['user_email'] = $result['user_email'];
          $response['user_password'] = $result['user_password'];
          $response['user_progress'] = $result['user_progress'];
          $response['user_type'] = $result['user_type'];
          $response['degree_id'] = $result['degree_id'];
          $response['user_username'] = $result['user_username'];
        } else {
          $response['error'] = true;
          $response['message'] = 'Apologies, I messed up';
        }
        break;
    
    // This updates the user progress string 
    // Funciton called updateuser that takes in params (userID, bpID, and progress) and uses them to update users bpID and progress string in DB
    case 'updateUser':
        isTheseParametersAvailable(array('userid', 'bpid', 'progress'));
        $db = new DbOperation();

        $result = $db->updateuser(
            $_POST['userid'],
            $_POST['bpid'],
            $_POST['progress']
        );

        if($result)
        {
            $response['error'] = false;
            $response['message'] = "The user was updated successfully!";
        } else {
            $response['error'] = true;
            $response['message'] = "The user was not updated properly! Please check the API and try again. :(";
        }
        break;
  }
 }else{
 
 //if it is not api call
 //pushing appropriate values to response array
 $response['error'] = true;
 $response['message'] = 'This API call doesnt exist. Please check api.php for the list of API calls';
 }

 //displaying the response in json structure
 echo json_encode($response);
?>
