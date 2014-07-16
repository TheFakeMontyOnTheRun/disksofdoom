package br.odb.disksofdoom;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameapp.UserMetaCommandLineAction;

public class SolveCommand extends UserMetaCommandLineAction {

	public SolveCommand( ConsoleApplication app ) {
		super( app );

	}
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void run(ConsoleApplication arg0, String arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "solve";
	}

}
