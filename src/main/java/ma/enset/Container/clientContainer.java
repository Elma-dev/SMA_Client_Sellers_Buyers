package ma.enset.Container;

import jade.core.Node;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ma.enset.Agents.Client;


public class clientContainer extends Application {
    public Client client;
    public ObservableList<Text> observableList= FXCollections.observableArrayList();
    public ListView<Text> listView=new ListView<>(observableList);
    public TextArea textArea=new TextArea();
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        createContainer();
        //GUI
        AnchorPane root=new AnchorPane();
        HBox[] hBox=new HBox[2];
        VBox vBox=new VBox();
        TextField textField=new TextField();
        textField.setPromptText("Mac,Hp,Dell,...");
        Button search=new Button("Search");
        Button info = new Button("Info");
        Button sell = new Button("Sell");



        textArea.setDisable(true);
        textArea.setStyle("-fx-opacity: 1");

        //add to hbox:
        hBox[0]=new HBox();
        hBox[0].getChildren().add(textField);
        hBox[0].getChildren().add(search);
        hBox[0].setSpacing(10);
        hBox[0].setAlignment(Pos.CENTER);
        hBox[0].setPadding(new Insets(10));

        //hbox2
        hBox[1]=new HBox();
        hBox[1].getChildren().add(info);
        hBox[1].getChildren().add(sell);
        hBox[1].setAlignment(Pos.CENTER);
        hBox[1].setSpacing(10);
        hBox[1].setPadding(new Insets(10));

        //add to Vbox
        vBox.getChildren().add(hBox[0]);
        vBox.getChildren().add(listView);
        vBox.getChildren().add(hBox[1]);
        vBox.getChildren().add(textArea);
        vBox.setPrefHeight(400);
        vBox.setPrefWidth(400);
        vBox.setPadding(new Insets(10));
        vBox.setStyle("-fx-background-color: red");



        //getEvents
        search.setOnAction(actionEvent -> {
            if(!textField.getText().isEmpty()) {
                GuiEvent guiEvent = new GuiEvent(search, 1);
                guiEvent.addParameter(textField.getText());
                client.onGuiEvent(guiEvent);
            }
        });

        info.setOnAction(actionEvent -> {
            if(listView.getSelectionModel().getSelectedItems().size()!=0){
                GuiEvent guiEvent = new GuiEvent(info, 1);
                guiEvent.addParameter(listView.getSelectionModel().getSelectedItems().get(0).getText());
                client.onGuiEvent(guiEvent);
            }
        });

        //add to root
        root.getChildren().add(vBox);
        Scene scene=new Scene(root,400,400);
        stage.setScene(scene);
        stage.show();
    }

    void createContainer() throws Exception{
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        AgentContainer agentContainer=runtime.createAgentContainer(profile);
        AgentController client1 = agentContainer.createNewAgent("client1", "ma.enset.Agents.Client", new Object[]{this});
        client1.start();
    }
}
