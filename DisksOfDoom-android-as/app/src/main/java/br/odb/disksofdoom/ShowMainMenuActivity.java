package br.odb.disksofdoom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class ShowMainMenuActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    SeekBar sbDisks;
    TextView tvDisks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_main_menu);

        findViewById( R.id.btnPlay ).setOnClickListener(this);
        sbDisks = (SeekBar) findViewById(R.id.sbDisks);
        sbDisks.setOnSeekBarChangeListener( this );
        tvDisks = (TextView) findViewById( R.id.tvDisks );

        updateText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent( this, DisksOfDoomGameActivity.class );
        intent.putExtra("disks", ( sbDisks.getProgress() + 2) );
        startActivity( intent );
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        updateText();
    }

    void updateText() {
        tvDisks.setText( "Playing with " + ( sbDisks.getProgress() + 2 ) + " disks." );
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
