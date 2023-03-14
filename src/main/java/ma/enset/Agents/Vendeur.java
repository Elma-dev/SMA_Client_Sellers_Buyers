package ma.enset.Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ma.enset.Container.VendeursContainer;

import java.lang.reflect.Array;
import java.util.HashMap;

import static jade.core.MicroRuntime.getAgent;

public class Vendeur extends GuiAgent {
    HashMap<String,String> dict;
    VendeursContainer vendeursContainer;
    DFAgentDescription dfAgentDescription;
    @Override
    protected void setup() {

        if(this.getArguments()!=null){
            vendeursContainer =(VendeursContainer) this.getArguments()[0];
            vendeursContainer.vendeur=this;
        }
        dict=new HashMap<>();
        dict.put("Lenovo T460","i5 6gen 3000DH");
        dict.put("Lenovo T470","i7 10gen 6000DH");
        dict.put("Lenovo T480s","i9 11gen 9000DH");

        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(
                new OneShotBehaviour() {
                    @Override
                    public void action() {
                        dfAgentDescription=new DFAgentDescription();
                        try {
                            DFService.register(myAgent,dfAgentDescription);
                        } catch (FIPAException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receive = receive();
                if(receive!=null){
                    if(receive.getPerformative()==ACLMessage.PROPOSE) {
                        String computer = receive.getContent();
                        if (dict.containsKey(computer)) {
                            ACLMessage response = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                            response.addReceiver(receive.getSender());
                            response.setContent(computer + " " + dict.get(computer));
                            send(response);
                        }
                    }else {
                        ACLMessage response = new ACLMessage(ACLMessage.AGREE);
                        response.addReceiver(receive.getSender());
                        send(response);
                    }
                }else
                    block();

            }
        });
       addBehaviour(parallelBehaviour);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            System.out.println("deregister of agent");
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {

        String parameter = (String) (guiEvent.getParameter(0));
        System.out.println(parameter);
        System.out.println(parameter.split(":"));
        String[] computer=parameter.split(":");
        System.out.println(computer[0] + " "+computer[1]);
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("Computers");
        serviceDescription.setName(computer[0]);
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.modify(this,dfAgentDescription);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }

        dict.put(computer[0],computer[1]+" "+computer[2]);
    }
}
