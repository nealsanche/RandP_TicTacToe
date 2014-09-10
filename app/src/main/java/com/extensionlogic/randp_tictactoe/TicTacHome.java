package com.extensionlogic.randp_tictactoe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.extensionlogic.randp_tictactoe.Shapes.WinIndicatorLine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class TicTacHome extends Activity{
    public static final String TURN_PREFERENCES = "TurnPrefs" ;
    public static final String PlayerKey = "playerKey";
    public static final String ChoicesKey = "choicesKey";
    SharedPreferences sharedpreferences;
    char[] positionArray;
    String lastPlayerCharacter="X";
    LinearLayout parent_ll;
    String[] gridChoices;
    FrameLayout fl_grid;
    WinIndicatorLine winLine;
    int choiceCount=0;
    Button reset_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_home);
        fl_grid = (FrameLayout)findViewById(R.id.fl_grid);
        parent_ll = (LinearLayout)findViewById(R.id.parent_ll);
        reset_button = (Button)findViewById(R.id.reset_button);


        positionArray = new char[9];
        if(gridChoices==null){
            gridChoices = new String[9];
        }
        getLastPlayed();//retrieve last input choices from sharedpreferences
        if(savedInstanceState!=null) {//populate grid with current answers if resuming activity
            gridChoices = savedInstanceState.getStringArray("grid_choices");
            lastPlayerCharacter = savedInstanceState.getString("last_player");
        }
        addGridBoxes();//add game grid
        setResetButtonListener();

    }


    @Override
    protected void onResume() {
        super.onResume();
        winChecker();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void addWinIndicator(int winScenarioNum){
        winLine = getWinLine(fl_grid,winScenarioNum);
        fl_grid.addView(winLine);

    }



    private void addGridBoxes(){
        int boxNo=0;
        if(gridChoices==null){
            gridChoices = new String[9];
        }

        for(int rowNum=0;rowNum<3;rowNum++){  //add 3 layout rows to contain game boxes
            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,0,1f));
            ll.setOrientation(LinearLayout.HORIZONTAL);
            parent_ll.addView(ll);
            for(int colNum=0;colNum<3;colNum++){  //add boxes to each row
                ImageButton gameBox = new ImageButton(this);
                gameBox.setBackgroundResource(R.drawable.box_border);
                gameBox.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                LinearLayout.LayoutParams layoutParams;
                ll.addView(gameBox);
                gameBox.setTag(boxNo);
                setBoxListener(gameBox);
                if(gridChoices!=null && gridChoices[boxNo]!=null) {//populate previous choice if activity resumed
                    setGameBox( gameBox, gridChoices[boxNo]);
                }
                boxNo++;

            }
        }
    }

    private void setGameBox(ImageButton gameBox, String choice){//set appropriate image and tags according to user choice
        if(choice.equals("X")){
            gameBox.setImageResource(R.drawable.x);

        }else if(choice.equals("O")){
            gameBox.setImageResource(R.drawable.o);
        }else{
            gameBox.setImageResource(R.drawable.box_border);
        }
        gameBox.setScaleType(ImageView.ScaleType.FIT_XY);
        gameBox.setTag(R.string.tag_player_indicator,choice);
        //gridChoices[boxNo]=choice;

    }

    private void setBoxListener( ImageButton boxButton){
        boxButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                choiceCount++; //increment number of choices made in game
                ImageButton ib = (ImageButton)view;
                if(ib.getDrawable()==null) {  // place appropriate image in each box
                    if (lastPlayerCharacter.equals("X")) {
                        setGameBox( ib, "O");
                        lastPlayerCharacter = "O";
                    } else if (lastPlayerCharacter.equals("O")) {
                        setGameBox( ib, "X");
                        lastPlayerCharacter = "X";
                    }
                    gridChoices[(Integer)view.getTag()]=lastPlayerCharacter;//set the array variable according to the box chosen. Integer value of box is stored in tag upon creation
                    winChecker();
                }
            }
        });

    }

    private void winChecker(){
        boolean won = checkAllWinOptions(lastPlayerCharacter);
        if(won){
            showDoneDialog(true);
        }else if(won==false && choiceCount>8){
            showDoneDialog(false);
        }
    }
    private WinIndicatorLine getWinLine(FrameLayout gridBox, int winLineScenario){

        WinIndicatorLine winLine = new WinIndicatorLine(this,gridBox,winLineScenario);

        winLine.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return winLine;
    }


    private boolean checkAllWinOptions(String xo) {
        boolean won=false;
        int[] winningRow;
        int[][] rows = getWinnableRows();
        for(int i=0; i<rows.length;i++){
            int[] checkRow =  rows[i];
            if(won =  checkWinOptionRow( xo,checkRow)){
                winningRow = checkRow;
                addWinIndicator(i);
                //gridChoices = new String[9];
                break;
            }
        }
        return won;
    }



    private int[][] getWinnableRows(){
        int[][] winRows = new int[][]{
                {0,1,2},
                {3,4,5},
                {6,7,8},
                {0,3,6},
                {1,4,7},
                {2,5,8},
                {0,4,8},
                {2,4,6}};
        return winRows;
    }
    private boolean checkWinOptionRow(String xo,int[] checkLine){
        boolean won = true;

        for(int i=0;i<checkLine.length;i++){
            String choice = gridChoices[checkLine[i]];
            if(choice==null || !(choice.equals(xo)))
                won=false;
        }
        return won;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("grid_choices", gridChoices);
        outState.putString("last_player", lastPlayerCharacter);
    }


    private void showDoneDialog(boolean won){


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if(won){
            dialog.setTitle(lastPlayerCharacter+" WINS!");


        }else{
            dialog.setTitle("NO WINNER THIS GAME.");
        }
        dialog.show();
        Button closeBtn = ((Button) dialog.findViewById(R.id.closeButton));
        closeBtn.setText("Done.");
        Button newGameBtn=((Button) dialog.findViewById(R.id.ad_view_image));
        newGameBtn.setText("Click to play again!");

        closeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                resetGame();
                deletePrefs();
                TicTacHome.this.finish();
            }
        });
        newGameBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dialog.cancel();
                resetGame();
                deletePrefs();
            }
        });
    }

    private void resetGame(){
        choiceCount = 0; //reset number of choices entered by players
        if (winLine != null) {
            fl_grid.removeView(winLine);
        }
        winLine = null;
        parent_ll.removeAllViews();
        gridChoices=null;
        addGridBoxes();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        resetGame();
        setLastCharPlaced();
    }

    private void deletePrefs(){
        sharedpreferences = getSharedPreferences(TURN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.remove(PlayerKey);
        editor.commit();

    }
    private void setLastCharPlaced(){
        sharedpreferences = getSharedPreferences(TURN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Set<String> choiceSet = new HashSet<String>(Arrays.asList(gridChoices));
        editor.putStringSet(ChoicesKey, choiceSet);
        for(int i=0;i<gridChoices.length;i++){
            editor.putString(ChoicesKey+i, gridChoices[i]);
        }
        editor.putString(PlayerKey, lastPlayerCharacter);

        editor.commit();

    }
    private void getLastPlayed(){
        sharedpreferences = getSharedPreferences(TURN_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(ChoicesKey))
        {
            for(int i=0;i<gridChoices.length;i++) {
                if(sharedpreferences.getString(ChoicesKey + i, "")!=""){
                    gridChoices[i] = sharedpreferences.getString(ChoicesKey + i, "");
                }

            }

        }
        if(sharedpreferences.contains(PlayerKey)){
            lastPlayerCharacter = sharedpreferences.getString(PlayerKey,"X");
        }
    }

    private void setResetButtonListener(){
        reset_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                deletePrefs();
                resetGame();
            }
        });
    }
}
