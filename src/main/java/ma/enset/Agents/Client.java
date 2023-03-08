package ma.enset.Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Node;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import ma.enset.Container.clientContainer;

import java.util.ArrayList;
import java.util.Arrays;

public class Client extends  GuiAgent {
    clientContainer clientContainer;
    DFAgentDescription dfAgentDescription=new DFAgentDescription();
    @Override
    protected void setup() {
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,5000) {
            @Override
            protected void onTick() {
                /*
                try {
                    DFAgentDescription[] search = DFService.search(myAgent, dfAgentDescription);
                    AID[] cservices=new AID[search.length];
                    for(int i=0;i<search.length;i++){
                        cservices[i]=search[i].getName();
                        System.out.println(cservices[i].getLocalName());
                        System.out.println(search[i].getAllServices());
                    }
                } catch (FIPAException e) {
                    throw new RuntimeException(e);
                }

                 */
            }
        });
        parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ServiceDescription serviceDescription=new ServiceDescription();
                serviceDescription.setType("Computers");
                dfAgentDescription.addServices(serviceDescription);
            }
        });

        if(this.getArguments()!=null){
            clientContainer= (clientContainer) this.getArguments()[0];
            clientContainer.client=this;
        }

        System.out.println("Hellooo Cliennnttt");
        addBehaviour(parallelBehaviour);
    }


    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {

        if(((Button)(guiEvent.getSource())).getText()=="Search"){
            clientContainer.listView.getItems().clear();
            ArrayList<Text> computerSType=new ArrayList<>();
            try {
                DFAgentDescription[] search = DFService.search(this, dfAgentDescription);
                for(int i=0;i<search.length;i++){
                    Iterator allServices = search[i].getAllServices();
                    while (allServices.hasNext()){
                        String name = ((ServiceDescription) allServices.next()).getName();
                        if(name.indexOf((String) guiEvent.getParameter(0))!=-1){
                            Platform.runLater(()->{
                                clientContainer.observableList.add( new Text(name));
                            });
                        }

                    }
                }

            } catch (FIPAException e) {
                throw new RuntimeException(e);
            }

        }
        if(((Button)(guiEvent.getSource())).getText()=="Info"){
            sendMsgToSellers((String)guiEvent.getParameter(0));
        }

    }

    void sendMsgToSellers(String msgTxt){
        ACLMessage message=new ACLMessage(ACLMessage.PROPOSE);
        message.setContent(msgTxt);
        for
        message.addReceiver("");
    }
}
