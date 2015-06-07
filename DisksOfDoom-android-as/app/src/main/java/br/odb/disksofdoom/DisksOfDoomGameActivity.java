package br.odb.disksofdoom;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.odb.disksofdoom.DisksOfDoomMainApp.Disk;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SolidSquareRenderingNode;
import br.odb.utils.Color;
import br.odb.utils.Rect;

public class DisksOfDoomGameActivity extends BaseActivityAppClient implements OnClickListener {

    public static final int BASE_MEASURE = 20;

    private class Pin extends SolidSquareRenderingNode {

        public Pin(Rect rect, Color color) {
            super(rect, color);
        }
    }

    private class GraphicalDisk extends SolidSquareRenderingNode {

        public GraphicalDisk(Rect rect, Color color) {
            super(rect, color);
        }
    }

    private class BackgroundForPin extends SolidSquareRenderingNode {

        public BackgroundForPin(Rect rect, Color color) {
            super(rect, color);
        }
    }



    public static final Color SELECTED_DISK_COLOUR = new Color(255, 0, 0);
    DisksOfDoomMainApp doom;
    private int from = -1;
    private int to = -1;
    private TextView txtMoves;
    private TextView txtTime;
    long startingTime;
    int numberOfDisks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disks_of_doom_game);

        txtMoves = (TextView) this.findViewById( R.id.txtMoves );
        txtTime = (TextView) this.findViewById( R.id.txtTime );

        findViewById(R.id.pin1).setOnClickListener( this );
        findViewById(R.id.pin2).setOnClickListener( this );
        findViewById(R.id.pin3).setOnClickListener( this );

        doom = (DisksOfDoomMainApp) new DisksOfDoomMainApp()
                .setAppName("Disks Of Doom")
                .setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
                .setLicenseName("3-Clause BSD").setReleaseYear(2014);
        doom.setApplicationClient(this);
        doom.start();
        doom.sendData("new-game 5");

        startingTime = System.currentTimeMillis();

        numberOfDisks = doom.pole[ 0 ].size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while( doom.isAlive()) {
                    try {
                        updateTime();
                        Thread.sleep( 1000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        updateState();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    void updateTime() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtTime.setText("" + (System.currentTimeMillis() - startingTime) / 1000);
            }
        });
    }

    boolean validPoleIndex( int index ) {
        if ( index < 0 || index >= doom.pole.length ) {
            return false;
        }
        return true;
    }

    int sizeOnTopOfPile( int index ) {

        if (validPoleIndex( index ) ) {
            return -1;
        }

        if ( index == - 1 ) {
            return -1;
        }

        if ( doom.pole[ index ].isEmpty() ) {
            return -1;
        }

        return doom.pole[ from ].get( 0 ).size;
    }

    boolean shouldHighlight( int current, int index ) {

        if ( !validPoleIndex( index ) ) {
            return false;
        }

        int topOfPile = sizeOnTopOfPile( index );

        if ( topOfPile == current) {
            return false;
        }

        if ( topOfPile == -1 ) {
            return true;
        }

        return ( current < topOfPile );
    }

    private void updateState() {

        int current = sizeOnTopOfPile( from );

        getPoleRenderingElements((GameView) this.findViewById(R.id.pin1), doom.pole[0], 0, from == 0, shouldHighlight(current, 0));
        getPoleRenderingElements((GameView) this.findViewById(R.id.pin2), doom.pole[1], 1, from == 1, shouldHighlight(current, 1));
        getPoleRenderingElements((GameView) this.findViewById(R.id.pin3), doom.pole[2], 2, from == 2, shouldHighlight(current, 2));

        txtMoves.setText("" + doom.move);
    }

    private void getPoleRenderingElements(GameView polePane, List<Disk> disks, int index, boolean selected, boolean hightlighted) {

        GraphicalDisk disc;
        Pin pole;
        BackgroundForPin background;
        List<RenderingNode> nodes = new ArrayList<>();

        background = new BackgroundForPin(new Rect(0, 0,  polePane.getWidth(), polePane.getHeight()), getBackgroundColour(index));
        nodes.add(background);

        int size = numberOfDisks * BASE_MEASURE;
        int width = polePane.getWidth();
        int height = polePane.getHeight();

        pole = new Pin(new Rect( ( polePane.getWidth() / 2 ), polePane.getHeight() - size, BASE_MEASURE, size), getPinColour(hightlighted));
        nodes.add(pole);

        Color c;
        int pos = 0;

        for (Disk d : disks) {
            ++pos;
            c = getColorForDisk(selected, pos);
            disc = new GraphicalDisk(new Rect( ( width / 2 ) - d.size * ( BASE_MEASURE / 2.0f ), height - ( disks.size() * BASE_MEASURE ) + BASE_MEASURE * ( pos  - 1 ), BASE_MEASURE + (d.size * BASE_MEASURE), BASE_MEASURE), c );
            nodes.add(disc);
        }
        polePane.setRenderingContent(nodes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            polePane.setElevation(hightlighted ? 1.0f : 0.0f);
        }

    }

    private Color getPinColour(boolean hightlighted) {
        return hightlighted ?  new Color(255, 255, 0) : new Color(255, 255, 255);
    }

    private Color getBackgroundColour(int index) {
        return new Color( index * 85, index * 85, index * 85 );
    }

    private Color getColorForDisk(boolean selectedPin, int positionInPin) {
        return selectedPin && (positionInPin == 1) ? SELECTED_DISK_COLOUR : new Color(0, 255 - ( positionInPin * ( 255 / ( numberOfDisks + 1) ) ), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.pin1:
                if ( from == 0 ) {
                    from = -1;
                } else {
                    if ( from == -1 ) {
                        from = 0;
                    } else {
                        to = 0;
                        performMove();
                    }
                }

                break;
            case R.id.pin2:
                if ( from == 1 ) {
                    from = -1;
                } else {
                    if ( from == -1 ) {
                        from = 1;
                    } else {
                        to = 1;
                        performMove();
                    }
                }
                break;
            case R.id.pin3:
                if ( from == 2 ) {
                    from = -1;
                } else {
                    if ( from == -1 ) {
                        from = 2;
                    } else {
                        to = 2;
                        performMove();
                    }
                }
                break;
        }
        updateState();
    }

    public void performMove() {
        doom.sendData("move " + from + " " + to);
        from = -1;
        to = -1;
    }
}
