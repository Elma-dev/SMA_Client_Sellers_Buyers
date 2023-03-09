package ma.enset.Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

public class Vendeur extends Agent {
    HashMap<String,String> dict;
    @Override
    protected void setup() {
        dict=new HashMap<>();
        dict.put("Lenovo T460","i5 6gen 3000DH");
        dict.put("Lenovo T470","i7 10gen 6000DH");
        dict.put("Lenovo T480s","i9 11gen 9000DH");

        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(
                new OneShotBehaviour() {
                    @Override
                    public void action() {
                        DFAgentDescription dfAgentDescription=new DFAgentDescription();
                        dfAgentDescription.setName(getAID());

                        ServiceDescription serviceDescription1=new ServiceDescription();
                        serviceDescription1.setType("Computers");
                        serviceDescription1.setName("Lenovo T460");

                        ServiceDescription serviceDescription2=new ServiceDescription();
                        serviceDescription2.setType("Computers");
                        serviceDescription2.setName("Lenovo T470");

                        ServiceDescription serviceDescription3=new ServiceDescription();
                        serviceDescription3.setType("Computers");
                        serviceDescription3.setName("Lenovo T480s");

                        dfAgentDescription.addServices(serviceDescription1);
                        dfAgentDescription.addServices(serviceDescription2);
                        dfAgentDescription.addServices(serviceDescription3);

                        try {
                            DFService.register(getAgent(),dfAgentDescription);
                        } catch (FIPAException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
        );
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receive = receive();
                if(receive!=null){
                    String computer=receive.getContent();
                    if(dict.containsKey(computer)){
                        ACLMessage response=new ACLMessage();
                        response.addReceiver(receive.getSender());
                        response.setContent(dict.get(computer));
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
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

}
