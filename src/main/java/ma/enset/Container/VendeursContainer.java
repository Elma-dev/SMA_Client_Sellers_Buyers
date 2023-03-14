package ma.enset.Container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ma.enset.Agents.Vendeur;

import javax.security.auth.callback.Callback;
import java.util.HashMap;
import java.util.Map;


public class VendeursContainer extends Application {
    public Vendeur vendeur;

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
        ObservableList<Map<String,Object>> obList = FXCollections.observableArrayList();


        TableView tableView=new TableView<>(obList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Map,String> nameColumn=new TableColumn("Name");
        nameColumn.setCellValueFactory(new MapValueFactory<>("Name"));
        TableColumn<Map,String> descColumn=new TableColumn("Description");
        descColumn.setCellValueFactory(new MapValueFactory<>("Description"));
        TableColumn<Map,String> priceColumn=new TableColumn("Price");
        priceColumn.setCellValueFactory(new MapValueFactory<>("Price"));

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(descColumn);
        tableView.getColumns().add(priceColumn);



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
            //global Map
            if(!(name.getText().isEmpty() || disc.getText().isEmpty() || price.getText().isEmpty())){
                Map<String,Object> map=new HashMap();
                map.put("Name",name.getText());
                map.put("Description",disc.getText());
                map.put("Price",price.getText());
                obList.add(map);
                GuiEvent guiEvent = new GuiEvent(add, 1);
                guiEvent.addParameter(name.getText()+":"+disc.getText()+":"+price.getText());
                vendeur.onGuiEvent(guiEvent);

            }
        });
    }

    void createContainer() throws Exception{
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        AgentController vend1 = agentContainer.createNewAgent("vend1", "ma.enset.Agents.Vendeur", new Object[]{this});
        vend1.start();
    }
}