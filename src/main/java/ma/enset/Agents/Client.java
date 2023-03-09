package ma.enset.Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Node;
import jade.core.behaviours.*;
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
    ArrayList<AID> NameSellerHasIt=new ArrayList<>();
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

        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receive = receive();
                if(receive!=null){
                    if(receive.getPerformative()==ACLMessage.ACCEPT_PROPOSAL){
                        Platform.runLater(()->{
                            clientContainer.textArea.setText(receive.getContent());
                        });
                    }else if(receive.getPerformative()==ACLMessage.AGREE){
                        Platform.runLater(()->{
                            clientContainer.textArea.setText("Your Command Has Been Accepted ✨✨✨");
                        });
                    }
                }else {
                    block();
                }
            }
        });

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
            NameSellerHasIt.clear();
            try {
                DFAgentDescription[] search = DFService.search(this, dfAgentDescription);
                for(int i=0;i<search.length;i++){
                    Iterator allServices = search[i].getAllServices();
                    while (allServices.hasNext()){
                        String name = ((ServiceDescription) allServices.next()).getName();
                        if(name.indexOf((String) guiEvent.getParameter(0))!=-1){
                            System.out.println(NameSellerHasIt.indexOf(search[i].getName()));
                            if(NameSellerHasIt.indexOf(search[i].getName())==-1){
                                NameSellerHasIt.add(search[i].getName());
                            }
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
        else if(((Button)(guiEvent.getSource())).getText()=="Info"){
            System.out.println(NameSellerHasIt);
            sendMsgToSellers((String)guiEvent.getParameter(0),NameSellerHasIt);
        }
        else {
            ACLMessage sellMessage=new ACLMessage(ACLMessage.CONFIRM);
            System.out.println(NameSellerHasIt);
            sellMessage.addReceiver(NameSellerHasIt.get(0));
            send(sellMessage);
        }

    }

    void sendMsgToSellers(String msgTxt,ArrayList<AID> to){
        ACLMessage message=new ACLMessage(ACLMessage.PROPOSE);
        message.setContent(msgTxt);
        System.out.println(to);
        for(AID aid : to)
            message.addReceiver(aid);
        System.out.println(message);
        send(message);
    }
}
