package com.ex.rohit.sketchpad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawingView drawView;
    private ImageButton currPaint,newBtn,saveBtn,drawBtn,eraseBtn;
    private float smallBrush,medBrush,largeBrush;
    RelativeLayout relativeLayout;
    private PopupWindow popupWindowbrsh,popupWindowersr;
    private LayoutInflater layoutInflaterbrsh,layoutInflaterersr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView= (DrawingView)findViewById(R.id.drwng);
        GridLayout paintLayout= (GridLayout)findViewById(R.id.paint_colors);
        currPaint=(ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        newBtn= (ImageButton)findViewById(R.id.newfle);
        newBtn.setOnClickListener(this);
        saveBtn = (ImageButton)findViewById(R.id.sve);
        saveBtn.setOnClickListener(this);

        //Brush Size
        smallBrush= getResources().getInteger(R.integer.small_size);
        medBrush=getResources().getInteger(R.integer.medium_size);
        largeBrush=getResources().getInteger(R.integer.large_size);

        drawBtn=(ImageButton)findViewById(R.id.brshsze);
        drawBtn.setOnClickListener(this);

        relativeLayout= (RelativeLayout)findViewById(R.id.relative);
        drawView.setBrushSize(smallBrush);

        //Erase Button
        eraseBtn= (ImageButton)findViewById(R.id.ersr);
        eraseBtn.setOnClickListener(this);
    }

        public void paintClicked(View view)
        {
            //Checking if the clicked color is not the chosen one
            if(view!=currPaint)
            {
                drawView.setErase(false);

                drawView.setBrushSize(drawView.getLastBrushSize());
                //updating color
                ImageButton imgView= (ImageButton)view;
                String color=view.getTag().toString();
                drawView.setColor(color);
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                currPaint=(ImageButton)view;
            }
        }

    @Override
    public void onClick(View v) {

        //FOR NEW FILE
        if(v.getId()==R.id.newfle)
        {
            AlertDialog.Builder newDialog= new AlertDialog.Builder(this);
            newDialog.setTitle("New Drawing");
            newDialog.setMessage("Start new drawing??\n(You will lose the current  drawing!!!)");
            newDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which)
                {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });

            newDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which)
                {
                    dialog.cancel();
                }
            });
            newDialog.show();
        }

        //FOR SAVE BTN
        else if(v.getId()==R.id.sve)
        {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing

                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved= MediaStore.Images.Media.insertImage(getContentResolver(),drawView.getDrawingCache(),UUID.randomUUID().toString()+".png","drawing");
                    if(imgSaved!=null)
                    {
                        Toast savedToast=Toast.makeText(getApplicationContext(),"Drawing saved to Gallery!!",Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else
                    {
                        Toast unsavedToast= Toast.makeText(getApplicationContext(),"Oops! Image could not be saved.",Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }

        //FOR BRUSH SIZE
        else if(v.getId()==R.id.brshsze)
        {
            layoutInflaterbrsh=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container= (ViewGroup) layoutInflaterbrsh.inflate(R.layout.brush_chooser,null);
            popupWindowbrsh= new PopupWindow(container,500,500,true);
            popupWindowbrsh.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 315,470);

            ImageButton smallBtn = (ImageButton)container.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);

                    popupWindowbrsh.dismiss();
                }
            });

            ImageButton medBtn= (ImageButton)container.findViewById(R.id.medium_brush);
            medBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    drawView.setBrushSize(medBrush);
                    drawView.setLastBrushSize(medBrush);
                    drawView.setErase(false);
                    popupWindowbrsh.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)container.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    popupWindowbrsh.dismiss();
                }
            });

            container.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    popupWindowbrsh.dismiss();
                    return true;
                }
            });

        }

        //FOR ERASE BUTTON
        else if(v.getId()==R.id.ersr)
        {
            layoutInflaterersr=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container = (ViewGroup) layoutInflaterersr.inflate(R.layout.brush_chooser,null);
            popupWindowersr= new PopupWindow(container,500,500,true);
            popupWindowersr.showAtLocation(relativeLayout,Gravity.NO_GRAVITY,365,470);

            ImageButton smallBtn = (ImageButton)container.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    popupWindowersr.dismiss();
                }
            });

            ImageButton medBtn= (ImageButton)container.findViewById(R.id.medium_brush);
            medBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    drawView.setErase(true);
                    drawView.setBrushSize(medBrush);
                    popupWindowersr.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)container.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    popupWindowersr.dismiss();
                }
            });

            container.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    popupWindowersr.dismiss();
                    return true;
                }
            });


        }

    }
}

