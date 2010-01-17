/**
 * Copyright 2010 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of a BSD license. See
 * attached license.txt.
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com/
 */

package com.threecrickets.jygments.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.threecrickets.jygments.Def;
import com.threecrickets.jygments.NestedDef;
import com.threecrickets.jygments.ResolutionException;

/**
 * @author Tal Liron
 */
public class Grammar extends NestedDef<Grammar>
{
	//
	// Attributes
	//

	public State getState( String stateName )
	{
		State state = statesByName.get( stateName );
		if( state == null )
		{
			state = new State();
			statesByName.put( stateName, state );
			addDef( state );
		}
		return state;
	}

	public Iterable<State> getStates( Iterable<String> stateNames )
	{
		ArrayList<State> states = new ArrayList<State>();
		for( String stateName : stateNames )
			states.add( getState( stateName ) );
		return states;
	}

	//
	// Operations
	//

	public void resolve() throws ResolutionException
	{
		resolve( this );

		// Are we resolved?
		for( Map.Entry<String, State> entry : statesByName.entrySet() )
		{
			if( !entry.getValue().isResolved() )
			{
				String message = "Unresolved state: " + entry.getKey();
				Def<Grammar> cause = entry.getValue().getCause( this );
				while( cause != null )
				{
					message += ", cause: " + cause;
					cause = cause.getCause( this );
				}
				throw new ResolutionException( message );
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final Map<String, State> statesByName = new HashMap<String, State>();
}
