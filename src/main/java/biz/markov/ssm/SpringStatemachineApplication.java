package biz.markov.ssm;

import biz.markov.ssm.model.Events;
import biz.markov.ssm.model.States;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
@Configuration
@AllArgsConstructor
@Slf4j
public class SpringStatemachineApplication implements CommandLineRunner {

    private StateMachine<States, Events> stateMachine;

    public static void main(String[] args) {
        SpringApplication.run(SpringStatemachineApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Starting state machine");
        stateMachine.start();
        stateMachine.sendEvent(Events.AB);
        stateMachine.sendEvent(Events.BC);
        stateMachine.sendEvent(Events.CD);
        stateMachine.stop();

        stateMachine.start();
        stateMachine.sendEvent(Events.AX);
        stateMachine.stop();
    }
}
