package biz.markov.ssm.config;

import biz.markov.ssm.enums.Events;
import biz.markov.ssm.enums.States;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
@Slf4j
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.A)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                    .source(States.A)
                    .target(States.B)
                    .event(Events.AB)
                    .and()
                .withExternal()
                    .source(States.B)
                    .target(States.C)
                    .event(Events.BC)
                    .and()
                .withExternal()
                    .source(States.C)
                    .target(States.A)
                    .event(Events.CA);
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                log.info("State changed from {} to {}", from.getId(), to.getId());
            }
        };
    }
}
