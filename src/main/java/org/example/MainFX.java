package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Loan/LoanList.fxml")
        );
        if (loader.getLocation() == null) {
            System.out.println("FXML NOT FOUND!");
        }

        Scene scene = new Scene(loader.load(), 1000, 600);

        stage.setTitle("Loan Management System");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
