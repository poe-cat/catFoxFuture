package com.example.catfoxfuture;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchController implements Initializable {

    @FXML
    private Label searchLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView <PersonModel> tblDataTableView;

    @FXML
    private TableColumn <PersonModel, String> firstnameColumn;
    @FXML
    private TableColumn <PersonModel, String> lastnameColumn;
    @FXML
    private TableColumn <PersonModel, String> usernameColumn;
    @FXML
    private TableColumn <PersonModel, String> passwordColumn;

    ObservableList<PersonModel> searchPersonObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resource) {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String personViewQuery = "SELECT firstname, lastname, username, password FROM demo_db.useraccount";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(personViewQuery);

            while(queryOutput.next()) {

                String queryFirstname = queryOutput.getString("firstname");
                String queryLastname = queryOutput.getString("lastname");
                String queryUsername = queryOutput.getString("username");
                String queryPassword = queryOutput.getString("password");

                searchPersonObservableList.add(new PersonModel(queryFirstname, queryLastname,
                        queryUsername, queryPassword));

            }

            firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            tblDataTableView.setItems(searchPersonObservableList);

            FilteredList<PersonModel> filteredList =
                    new FilteredList<>(searchPersonObservableList, b -> true);


            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(personModel -> {

                    if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if(personModel.getFirstname().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if(personModel.getLastname().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }else if(personModel.getUsername().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                    } else if(personModel.getPassword().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else
                        return false;

                });
            });

            SortedList<PersonModel> sortedList = new SortedList<>(filteredList);

            sortedList.comparatorProperty().bind(tblDataTableView.comparatorProperty());

            tblDataTableView.setItems(sortedList);


        } catch(SQLException e) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }



}