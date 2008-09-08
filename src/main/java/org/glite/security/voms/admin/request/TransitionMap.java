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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TransitionMap {

    Map map = new HashMap();

    public TransitionMap() {

        super();
    }

    public void addTransition( String initialStateName, Event e,
            String finalStateName ) {

        State initialState = getState( initialStateName );
        State finalState = getState( finalStateName );

        addTransition( initialState, new BaseTransition( e, finalState ) );

    }

    public void addTransition( State initialState, Transition t ) {

        Set transitions = (Set) map.get( initialState );

        if ( transitions == null ) {

            transitions = new HashSet();
            transitions.add( t );
            map.put( initialState, transitions );

        } else {

            if ( transitions.contains( t ) )
                return;

            transitions.add( t );
        }
    }

    public State getTargetState( State initialState, Event receivedEvent ) {

        Set transitions = (Set) map.get( initialState );

        if ( transitions == null )
            return null;

        Iterator i = transitions.iterator();

        while ( i.hasNext() ) {

            Transition t = (Transition) i.next();
            if ( t.getEvent().equals( receivedEvent ) )
                return t.getTargetState();
        }

        return null;
    }

    public void addStates( String[] stateNames ) {

        for ( int i = 0; i < stateNames.length; i++ )
            map.put( new BaseState( stateNames[i] ), null );
    }

    public void addStates( State[] states ) {

        for ( int i = 0; i < states.length; i++ )
            map.put( states[i], null );
    }

    public State getState( String name ) {

        State s = new BaseState( name );
        if ( map.containsKey( s ) )
            return s;

        return null;

    }

    public boolean containsKey( Object key ) {

        return map.containsKey( key );
    }

    public boolean isFinalState( State s ) {

        Set transitions = (Set) map.get( s );
        return transitions != null ? transitions.isEmpty() : true;
    }

    public String toString() {

        if ( map.isEmpty() )
            return "[]";

        StringBuffer buf = new StringBuffer();

        Iterator i = map.keySet().iterator();

        while ( i.hasNext() ) {
            Object key = i.next();
            buf.append( "\n" + key + ":" + map.get( key ) );
        }

        return buf.toString();
    }

}
