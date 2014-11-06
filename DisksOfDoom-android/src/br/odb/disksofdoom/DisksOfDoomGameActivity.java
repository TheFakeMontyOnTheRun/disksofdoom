package br.odb.disksofdoom;

import br.odb.disksofdoom.DisksOfDoomMainApp.Disk;
import br.odb.gameapp.ApplicationClient;
import br.odb.utils.FileServerDelegate;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class DisksOfDoomGameActivity extends Activity implements ApplicationClient, OnClickListener {

	EditText edt1;
	EditText edt2;
	EditText edt3;
	DisksOfDoomMainApp doom;
	private int from;
	private int to;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disks_of_doom_game);

		edt1 = (EditText) findViewById( R.id.editText1 );
		edt2 = (EditText) findViewById( R.id.editText2 );
		edt3 = (EditText) findViewById( R.id.editText3 );
		
		findViewById( R.id.btnFrom1).setOnClickListener( this );
		findViewById( R.id.btnFrom2).setOnClickListener( this );
		findViewById( R.id.btnFrom3).setOnClickListener( this );
		findViewById( R.id.btnTo1).setOnClickListener( this );
		findViewById( R.id.btnTo2).setOnClickListener( this );
		findViewById( R.id.btnTo3).setOnClickListener( this );
		findViewById( R.id.btnDo).setOnClickListener( this );
		
		doom = (DisksOfDoomMainApp) new DisksOfDoomMainApp()
				.setAppName("Disks Of Doom")
				.setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
				.setLicenseName("3-Clause BSD").setReleaseYear(2014);
		doom.setApplicationClient(this);
		doom.start();
		
		updateState();
	}

	private void updateState() {
		String buffer = "";
		
		for( Disk d : doom.pole[ 0 ] ) {
			buffer += d.size + "\n";
		}
		
		edt1.setText( buffer );
		buffer = "";
		
		for( Disk d : doom.pole[ 1 ] ) {
			buffer += d.size + "\n";
		}
		
		edt2.setText( buffer );
		buffer = "";
		
		for( Disk d : doom.pole[ 2 ] ) {
			buffer += d.size + "\n";
		}
		
		edt3.setText( buffer );
		
	}

	@Override
	public void alert(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int chooseOption(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FileServerDelegate getFileServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInput(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String openHTTP(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playMedia(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printError(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printNormal(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printVerbose(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printWarning(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String requestFilenameForOpen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestFilenameForSave() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendQuit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shortPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch( v.getId() ) {
		case R.id.btnFrom1:
			from = 0;
			break;
		case R.id.btnFrom2:
			from = 1;
			break;
		case R.id.btnFrom3:
			from = 2;
			break;
		case R.id.btnTo1:
			to = 0;
			break;
		case R.id.btnTo2:
			to = 1;
			break;
		case R.id.btnTo3:
			to = 2;
			break;
		case R.id.btnDo:
			doom.sendData( "move " + from + " " + to );
			updateState();
			break;
			
		}
		
	}

}
