package it.unipv.ingsw.k20.view.gui.manager.controller;

import java.io.IOException;

import it.unipv.ingsw.k20.view.gui.manager.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {	
	
	@FXML
	private TextField txtFldUsername;
	@FXML
	private PasswordField pwdFldPassword;
	@FXML
	private Label lblSignUp;
	
	public LoginController() {
		this.txtFldUsername=new TextField();
		this.pwdFldPassword=new PasswordField();
		this.lblSignUp= new Label();
	}
	
	public void login(ActionEvent event) {
		/*
		 * if(txtFldUsername.getText().equals("")&&this.pwdFldPassword.getText().equals("")) {
			
		}
		*/
	}
	
	public void signUp(MouseEvent event) throws IOException
	{	        
	    FXMLLoader loader= new FXMLLoader(getClass().getResource(Constants.PATH_PREFIX+"/resources/Registration.fxml"));
		loader.setController(new RegistrationController());
		Scene scene= new Scene(loader.load());
		scene.getStylesheets().add(getClass().getResource(Constants.STYLE_PATH).toExternalForm());
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.setTitle("Manager");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	
	
}