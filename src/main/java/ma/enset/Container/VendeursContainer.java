package ma.enset.Container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.security.auth.callback.Callback;


public class VendeursContainer extends Application {
    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        createContainer();
        //JFX Comp
        AnchorPane root=new AnchorPane();
        VBox vBox=new VBox();
        HBox[] hBox=new HBox[2];
        TextField name=new TextField();
        TextField disc=new TextField();
        TextField price=new TextField();

        Button add = new Button("ADD");
        ListView<String> listView = new ListView<>();

        TableView<String> tableView=new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String,String> nameColumn=new TableColumn("Name");
        TableColumn<String,String> DescColumn=new TableColumn("Description");
        TableColumn<String,String> PriceColumn=new TableColumn("Price");

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(DescColumn);
        tableView.getColumns().add(PriceColumn);



        hBox[0]=new HBox();
        hBox[0].getChildren().add(name);
        name.setPromptText("Name");
        hBox[0].getChildren().add(disc);
        disc.setPromptText("Description");
        hBox[0].getChildren().add(price);
        price.setPromptText("Price");
        hBox[0].getChildren().add(add);
        hBox[0].setSpacing(10);
        hBox[0].setPadding(new Insets(10));
        hBox[0].setAlignment(Pos.CENTER);




        vBox.getChildren().add(hBox[0]);
        vBox.getChildren().add(tableView);
        vBox.setPadding(new Insets(10));
        vBox.setStyle("-fx-background-color: Green");
        vBox.setPrefHeight(400);
        vBox.setPrefWidth(600);

        root.getChildren().add(vBox);
        Scene scene=new Scene(root,600,400);
        stage.setScene(scene);
        stage.show();

        //events
        add.setOnAction((actionEvent)->{
            if(!(name.getText().isEmpty() || disc.getText().isEmpty() || price.getText().isEmpty())){
                GuiEvent guiEvent = new GuiEvent(add, 1);
                guiEvent.addParameter(name+"|"+disc+"|"+price);
            }
        });
    }

    void createContainer() throws Exception{
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        AgentController vend1 = agentContainer.createNewAgent("vend1", "ma.enset.Agents.Vendeur", new Object[]{});
        vend1.start();
    }
}