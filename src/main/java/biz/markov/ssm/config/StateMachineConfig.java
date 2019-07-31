package biz.markov.ssm.config;

import biz.markov.ssm.model.Events;
import biz.markov.ssm.model.States;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Objects;

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
                .states(EnumSet.allOf(States.class))
                .choice(States.D)
                .choice(States.D0);
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
                    .target(States.D)
                    .event(Events.CD)
                    .and()
                .withChoice()
                    .source(States.D)
                    .first(States.D1, guard(), whenTrueAction())
                    .last(States.D0, whenFalseAction())
                    .and()
                .withChoice()
                    .source(States.D0)
                    .first(States.E, guard(), whenTrueAction())
                    .last(States.D0, whenFalseAction());
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                log.info(
                        "Listener: state changed from {} to {}",
                        Objects.nonNull(from) ? from.getId() : "NONE",
                        to.getId()
                );
            }
        };
    }

    @Bean
    public Guard<States, Events> guard() {
        return ctx -> {
            boolean result = Math.random() < 0.5;
            log.info("Guard: {}", result);
            return result;
        };
    }

    @Bean
    public Action<States, Events> whenTrueAction() {
        return context -> log.info("Action: true");
    }

    @Bean
    public Action<States, Events> whenFalseAction() {
        return context -> log.info("Action: false");
    }
}
