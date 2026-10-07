package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.example.models.Customer;

import java.util.Optional;

public class CustomerController {

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> provinceCol;

    private ObservableList<Customer> customerList;

    @FXML
    public void initialize() {
        customerList = FXCollections.observableArrayList();

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        
        customerTable.setItems(customerList);
    }

    @FXML
    private void handleAddCustomer() {
        // 1. Create a custom Dialog
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("Enter the customer's details below:");

        // 2. Set the button types (Save and Cancel)
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // 3. Create the form grid for the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 50, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Customer Name");
        
        ComboBox<String> provinceComboBox = new ComboBox<>();
        provinceComboBox.setItems(FXCollections.observableArrayList(
            "Lusaka", "Copperbelt", "Central", "Southern", "North-Western", 
            "Western", "Eastern", "Northern", "Luapula", "Muchinga"
        ));
        provinceComboBox.setPromptText("Select Province");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Province:"), 0, 1);
        grid.add(provinceComboBox, 1, 1);

        // 4. Input Validation: Disable the Save button if inputs are empty
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty() || provinceComboBox.getValue() == null);
        });
        
        provinceComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(nameField.getText().trim().isEmpty() || newValue == null);
        });

        dialog.getDialogPane().setContent(grid);

        // 5. Request focus on the name field by default when the dialog opens
        Platform.runLater(nameField::requestFocus);

        // 6. Convert the form result into a Customer object when Save is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Customer(nameField.getText().trim(), provinceComboBox.getValue());
            }
            return null;
        });

        // 7. Show the dialog and add the new customer to the table if one was created
        Optional<Customer> result = dialog.showAndWait();
        result.ifPresent(customer -> customerList.add(customer));
    }

    @FXML
    private void handleDeleteCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Customer");
        confirm.setContentText("Are you sure you want to delete " + selectedCustomer.getName() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            customerList.remove(selectedCustomer);
        }
    }
}