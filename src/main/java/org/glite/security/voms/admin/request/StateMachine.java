/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/

package org.glite.security.voms.admin.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

public class StateMachine implements Flow {

	private static final Log log = LogFactory.getLog(StateMachine.class);

	TransitionMap map = new TransitionMap();

	TransitionLog tLog = new TransitionLog();

	State currentState;

	State initialState;

	ActionSequence preTransitionActions = new ActionSequence();

	ActionSequence postTransitionActions = new ActionSequence();

	public StateMachine(State s, TransitionMap m) {

		currentState = initialState = s;
		map = m;
	}

	public State getCurrentState() {

		return currentState;
	}

	public State process(Event e) {

		log.debug("Processing event: " + e);

		State targetState = map.getTargetState(currentState, e);
		if (targetState == null)
			logAndThrowException(new NoSuchTransitionException("("
					+ currentState + "," + e + ")-->?"));

		if (!map.containsKey(targetState))
			logAndThrowException(new IllegalTargetStateException(targetState
					.toString()));

		logTransition(targetState, e);

		// Execute pre-transition actions
		// TODO what to do with the return value? log it?
		preTransitionActions.execute();

		currentState = targetState;

		// TODO what to do with the return value?
		currentState.executeActions();

		// Execute post-transition actions
		// TODO what to do with the return value?
		postTransitionActions.execute();

		if (inFinalState())
			log.debug("Reached final state: " + currentState);

		return currentState;
	}

	public boolean inFinalState() {

		return map.isFinalState(currentState);
	}

	private void logAndThrowException(StateMachineException e) {

		tLog.logException(currentState, e);

		throw e;
	}

	private void logTransition(State targetState, Event e) {

		log.debug("Transition:(" + currentState + "," + e + ")-->"
				+ targetState);
		tLog.logTransition(currentState, targetState, e);
	}

	public static void main(String[] args) {

		PropertyConfigurator
				.configure("./src/webapp/WEB-INF/classes/log4j.properties");

		BaseState initialState = new BaseState("WAITING");
		BaseState finalState = new BaseState("DONE");

		BaseEvent signal = new BaseEvent("signal");

		TransitionMap map = new TransitionMap();

		map.addStates(new State[] { initialState, finalState });
		map.addTransition(initialState, new BaseTransition(signal, finalState));

		StateMachine sm = new StateMachine(initialState, map);
		sm.process(signal);

		log.info(sm.map);
		log.info(sm.tLog);
	}

	public void addStates(State[] states) {

		map.addStates(states);
	}

	public void addStates(String[] stateNames) {

		map.addStates(stateNames);
	}

	public void addTransition(String initialStateName, Event e,
			String finalStateName) {

		map.addTransition(initialStateName, e, finalStateName);
	}

	public State getState(String name) {

		return map.getState(name);
	}

	public void addPreTransitionAction(Action a) {

		preTransitionActions.addAction(a);
	}

	public void addPostTransitionAction(Action a) {

		postTransitionActions.addAction(a);
	}

}
