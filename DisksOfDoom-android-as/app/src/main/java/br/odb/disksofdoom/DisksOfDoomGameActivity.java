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
import br.odb.gamerendering.rendering.RenderingContext;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SolidSquareRenderingNode;
import br.odb.utils.Color;
import br.odb.utils.Rect;

public class DisksOfDoomGameActivity extends BaseActivityAppClient implements OnClickListener {

    public static final int BASE_MEASURE = 40;

    private class Pin extends SolidSquareRenderingNode {

        public Pin(Rect rect, Color color) {
            super(rect, color);
        }
    }

    private class GraphicalDisk extends RenderingNode {

        private final Rect area;
        private final Color color;

        public GraphicalDisk( Rect area, Color color) {
            super(  "disk_" + color );

            this.area = area;
            this.color = color;
        }

        @Override
        public void render(RenderingContext renderingContext) {

            Rect rect1 = new Rect( area );
            Rect rect2 = new Rect( area );
            Rect rect3 = new Rect( area );

            rect1.p0.y += 0 * area.getDY() / 3.0f;
            rect2.p0.y += 1 * area.getDY() / 3.0f - ( area.getDY() / 6.0f  );
            rect3.p0.y += 2 * area.getDY() / 3.0f;

            rect1.p1.y = rect1.p0.y + area.getDY() / 3.0f;
            rect2.p1.y = rect2.p0.y + 2 * (area.getDY() / 3.0f ) ;
            rect3.p1.y = rect3.p0.y + area.getDY() / 3.0f;

            renderingContext.drawOval( rect3, color );
            renderingContext.fillRect( color, rect2 );

            Color c2 = new Color( color );
            c2.multiply( 1.25f );
            renderingContext.drawOval( rect1, c2 );
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
        new Thread( doom ).start();
        doom.sendData("new-game " + getIntent().getIntExtra("disks", 3) );

        startingTime = System.currentTimeMillis();

        numberOfDisks = doom.pole[ 0 ].size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while( true ) {
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

        getPoleRenderingElements((GameView) this.findViewById(R.id.pin1), 0, from == 0, shouldHighlight(current, 0));
        getPoleRenderingElements((GameView) this.findViewById(R.id.pin2), 1, from == 1, shouldHighlight(current, 1));
        getPoleRenderingElements((GameView) this.findViewById(R.id.pin3), 2, from == 2, shouldHighlight(current, 2));

        txtMoves.setText("" + doom.move);
    }

    private void getPoleRenderingElements(GameView polePane, int poleIndex, boolean selected, boolean hightlighted) {

        List<Disk> disks = doom.pole[ poleIndex ];
        GraphicalDisk disc;
        Pin pole;
        BackgroundForPin background;
        List<RenderingNode> nodes = new ArrayList<>();

        background = new BackgroundForPin(new Rect(0, 0,  polePane.getWidth(), polePane.getHeight()), getBackgroundColour(poleIndex));
        nodes.add(background);

        int size = numberOfDisks * BASE_MEASURE;
        int width = polePane.getWidth();
        int height = polePane.getHeight();

        pole = new Pin(new Rect( ( polePane.getWidth() / 2 ), polePane.getHeight() - size, BASE_MEASURE, size), getPinColour(hightlighted));
        nodes.add(pole);

        Color c;
        Disk d;

        for ( int pos = disks.size() - 1; pos >= 0; --pos ) {
            d = disks.get( pos );
            c = getColorForDisk(selected, poleIndex, pos);
            disc = new GraphicalDisk(new Rect( ( width / 2 ) - d.size * ( BASE_MEASURE / 2.0f ), height - ( ( disks.size() - 1 ) * ( BASE_MEASURE ) ) + ( BASE_MEASURE  ) * ( pos  - 1 ), BASE_MEASURE + (d.size * BASE_MEASURE), BASE_MEASURE), c );
            nodes.add(disc);
        }

        polePane.setRenderingContent(nodes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            polePane.setElevation(hightlighted ? 1.0f : 0.0f);
        }

    }

    private Color getPinColour(boolean hightlighted) {
        return hightlighted ?  new Color(255, 255, 0) : new Color( 128, 128, 128);
    }

    private Color getBackgroundColour(int index) {
        ++index;
        return new Color( index * 85, index * 85, index * 85 );
    }

    private Color getColorForDisk(boolean selectedPin, int pin, int positionInPin) {

        int  value = doom.pole[ pin ].get( positionInPin ).size;

        return selectedPin && (positionInPin == 0) ? SELECTED_DISK_COLOUR : new Color(0, 255 - ( value * ( 255 / ( numberOfDisks + 1) ) ), 0);
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
