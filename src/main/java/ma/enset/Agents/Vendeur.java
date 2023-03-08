package ma.enset.Agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class Vendeur extends Agent {
    @Override
    protected void setup() {
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
