package linkersoft.blackpanther.bubbles;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;



public class BubblingImageView extends ImageView {


    private Bitmap bubbleMAp;
    private int imgv_H;
    private int imgv_W;
    private Path path;
    private Matrix scaleMatrix;
    boolean src_in,centralise,bubbled, bubbleScaling;
    private bubbleClickListener bubbleListener;
    private Canvas bubbleVerse;
    private int bubbState,Ox,Oy,length;
    private View.OnClickListener pathClick;


    public BubblingImageView(Context context, AttributeSet attrs){
        super(context, attrs);
        in(context,attrs);
    }
    public BubblingImageView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        in(context,attrs);
    }


    void in(final Context context,AttributeSet attrs){
        int attrsRay[]={android.R.attr.layout_width};
        TypedArray _attrs =context.obtainStyledAttributes(attrs, attrsRay);
        TypedArray kqo =context.getTheme().obtainStyledAttributes(attrs, R.styleable.BubblingImageView, 0, 0);

        try{
            String tmp=kqo.getString(R.styleable.BubblingImageView_Diameter);
            if(tmp!=null)length= dp2px(Integer.parseInt(tmp.split("dp")[0]),context);
            else length=_attrs.getDimensionPixelSize(0,0);
            Ox=kqo.getInteger(R.styleable.BubblingImageView_Ox,0);
            Oy=kqo.getInteger(R.styleable.BubblingImageView_Oy,0);
            src_in=kqo.getBoolean(R.styleable.BubblingImageView_SRC_IN,true);
            centralise=kqo.getBoolean(R.styleable.BubblingImageView_Centralise,false);
            bubbleScaling=kqo.getBoolean(R.styleable.BubblingImageView_BubbleScale,false);
        }finally{_attrs.recycle();kqo.recycle();}

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        if(centralise){
            Ox=(getWidth()/2)-(length/2);
            Oy=(getHeight()/2)-(length/2);
            centralise=false;
        }if(!bubbled) bubble();
    }

    private void bubble(){

       /*
        150,0#300,149#150,299#0,149
        234,0#299,64#299, 236#235,299#64,299#0,236#0,64#64,0

        150,-0#280,150#150,300#20,150
        224,0#280,64#280,236#210,300#76,300#20,236#20,64#76,0

        150,10#300,150#150,286#0,150
        236,12#300,70#300,230#236,288#66,288#0,228#0,70#64,12

        164,12#284,134#156,288#15,152
        260,14#288,64#276,206#252,280#78,290#16,238#14,66#80,8

        146,12#286,144#133,288#16,164
        220,10#284,66#286,236#220,290#42,286#12,236#26,90#50,18
        */
        float Factor=length/300f;
        int[][]
                qN=new int[][]{new int[]{150,0},new int[]{300,149},new int[]{150,299},new int[]{0,149}},
                qC=new int[][]{new int[]{234,0},new int[]{299,64},new int[]{299, 236},new int[]{235,299},
                        new int[]{64,299},new int[]{0,236},new int[]{0,64},new int[]{64,0}},

                qN0=new int[][]{new int[]{150,0},new int[]{280,150},new int[]{150,300},new int[]{20,150}},
                qC0=new int[][]{new int[]{224,0},new int[]{280,64},new int[]{280,236},new int[]{210,300},
                        new int[]{76,300},new int[]{20,236},new int[]{20,64},new int[]{76,0}},

                qN1=new int[][]{new int[]{150,10},new int[]{300,150},new int[]{150,286},new int[]{0,150}},
                qC1=new int[][]{new int[]{236,12},new int[]{300,70},new int[]{300,230},new int[]{236,288},
                        new int[]{66,288},new int[]{0,228},new int[]{0,70},new int[]{64,12}},

                qN2=new int[][]{new int[]{164,12},new int[]{284,134},new int[]{156,288},new int[]{15,152}},
                qC2=new int[][]{new int[]{260,14},new int[]{288,64},new int[]{276,206},new int[]{252,280},
                        new int[]{78,290},new int[]{16,238},new int[]{14,66},new int[]{80,8}},

                qN3=new int[][]{new int[]{146,12},new int[]{286,144},new int[]{133,288},new int[]{16,164}},
                qC3=new int[][]{new int[]{220,10},new int[]{284,66},new int[]{286,236},new int[]{220,290},
                        new int[]{42,286},new int[]{12,236},new int[]{26,90},new int[]{50,18}};


        for (int i = 0; i < qN.length; i++) {

            qN[i][0]=Ox+(int)(Factor*qN[i][0]);
            qN0[i][0]=Ox+(int)(Factor*qN0[i][0]);
            qN1[i][0]=Ox+(int)(Factor*qN1[i][0]);
            qN2[i][0]=Ox+(int)(Factor*qN2[i][0]);
            qN3[i][0]=Ox+(int)(Factor*qN3[i][0]);

            qN[i][1]=Oy+ (int)(Factor*qN[i][1]);
            qN0[i][1]=Oy+(int)(Factor*qN0[i][1]);
            qN1[i][1]=Oy+(int)(Factor*qN1[i][1]);
            qN2[i][1]=Oy+(int)(Factor*qN2[i][1]);
            qN3[i][1]=Oy+(int)(Factor*qN3[i][1]);

        }for (int i = 0; i < qC.length; i++) {

            qC[i][0]=Ox+(int)(Factor*qC[i][0]);
            qC0[i][0]=Ox+(int)(Factor*qC0[i][0]);
            qC1[i][0]=Ox+(int)(Factor*qC1[i][0]);
            qC2[i][0]=Ox+(int)(Factor*qC2[i][0]);
            qC3[i][0]=Ox+(int)(Factor*qC3[i][0]);

            qC[i][1]=Oy+(int)(Factor*qC[i][1]);
            qC0[i][1]=Oy+(int)(Factor*qC0[i][1]);
            qC1[i][1]=Oy+(int)(Factor*qC1[i][1]);
            qC2[i][1]=Oy+(int)(Factor*qC2[i][1]);
            qC3[i][1]=Oy+(int)(Factor*qC3[i][1]);
        }

        path=new Path();
        final BubblePanther
                q=new BubblePanther(qN,qC),
                q0=new BubblePanther(qN0,qC0),
                q1=new BubblePanther(qN1,qC1),
                q2=new BubblePanther(qN2,qC2),
                q3=new BubblePanther(qN3,qC3);

        path=q.path;
        final ObjectAnimator bubble = (centralise)? ObjectAnimator.ofObject(this,"",new PantherEvaluator(false),q,q2,q3,q,q0,q1,q2,q3,q): ObjectAnimator.ofObject(this,"",new PantherEvaluator(false),q,q0,q1,q2,q3,q2,q1,q0,q);
        bubble.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                Panth((BubblePanther)valueAnimator.getAnimatedValue());
            }
        });
        bubble.setInterpolator(new FastOutSlowInInterpolator());


        if(bubbleScaling){
            Matrix m0=new Matrix(),m1=new Matrix();
            m1.postScale(2f,2f,getWidth()/2,getHeight()/2);
            final ObjectAnimator mrx = ObjectAnimator.ofObject(this,"",new MatrixEvaluator(),m0,m1,m0);
            mrx.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator){
                    scaleMatrix =(Matrix)valueAnimator.getAnimatedValue();
                }
            });
            mrx.setInterpolator(new FastOutSlowInInterpolator());
            final AnimatorSet bubbleScale=new AnimatorSet();
            bubbleScale.setDuration(2000);
            bubbleScale.playTogether(bubble,mrx);
            bubbleScale.addListener(new Animator.AnimatorListener(){
                @Override
                public void onAnimationStart(Animator animator){
                    bubbState =1;
                }

                @Override
                public void onAnimationEnd(Animator animator){
                    bubbState =0;
                }

                @Override
                public void onAnimationCancel(Animator animator){

                }

                @Override
                public void onAnimationRepeat(Animator animator){

                }
            });
            pathClick=new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    if(bubbState ==0){
                        bubbleScale.start();
                        if(bubbleListener !=null) bubbleListener.onClick(view);
                    }
                }
            };
        }else{
            bubble.setDuration(2000);
            bubble.addListener(new Animator.AnimatorListener(){
                @Override
                public void onAnimationStart(Animator animator){
                    bubbState =1;
                }

                @Override
                public void onAnimationEnd(Animator animator){
                    bubbState =0;
                }

                @Override
                public void onAnimationCancel(Animator animator){

                }

                @Override
                public void onAnimationRepeat(Animator animator){

                }
            });
            pathClick=new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    if(bubbState ==0)bubble.start();
                    if(bubbleListener !=null) bubbleListener.onClick(view);
                }
            };
        }setOnClickListener(pathClick);
        bubbled=true;
    }
    private Bitmap bubblify(Bitmap Bmap){
       if(src_in){

            int bW = imgv_W;
            int bH = imgv_H;
            Bitmap output = Bitmap.createBitmap(bW, bH, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);

            Rect rek = new Rect(0, 0, bW, bH);
            canvas.drawARGB(0, 0, 255, 0);

            canvas.drawPath(path, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(Bmap, rek, rek, paint);
            return output;

       }else{

            Canvas canvas=new Canvas(Bmap);
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPath(path, paint);
            return Bmap;

       }
    }
    private void Panth(BubblePanther panther){
        path=panther.path;
        invalidate();
    }
    private static int dp2px(float Xdp, Context context){
        return Math.round(Xdp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDraw(Canvas canvas){
        if((imgv_W= getWidth())==0||(imgv_H=getHeight())==0)return;
        if(bubbleMAp ==null){
           bubbleMAp = Bitmap.createBitmap(imgv_W,imgv_H, Bitmap.Config.ARGB_8888);
           bubbleVerse = new Canvas(bubbleMAp);
        }super.onDraw(bubbleVerse);
        if(bubbleScaling) bubbleVerse.setMatrix(scaleMatrix);
        canvas.drawBitmap(bubblify(bubbleMAp), 0, 0, null);

    }

    public interface bubbleClickListener {
        void onClick(View v);
    }
    public void setOnclickListener(bubbleClickListener bubbleListener){
        this.bubbleListener =bubbleListener;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if(pathClick!=null)super.setOnClickListener(l);
    }

    private class PantherEvaluator implements TypeEvaluator<BubblePanther> {

        private boolean onlyNodes;
        public PantherEvaluator(boolean onlyNodes){
            this.onlyNodes=onlyNodes;
        }
        public BubblePanther evaluate(float fraction, BubblePanther In, BubblePanther Fin){
            return BubblePanther.getFractionPanther(fraction,In,Fin,onlyNodes);
        }

    }
    private class MatrixEvaluator implements TypeEvaluator<Matrix> {


        float[] startEntries = new float[9];
        float[] endEntries = new float[9];
        float[] currentEntries = new float[9];
        Matrix matrix=new Matrix();
        public Matrix evaluate(float fraction,Matrix startValue,Matrix endValue) {
            startValue.getValues(startEntries);
            endValue.getValues(endEntries);
            for (int i=0; i<9; i++)currentEntries[i] = (1-fraction)*startEntries[i]+ fraction*endEntries[i];
            matrix.setValues(currentEntries);
            return matrix;
        }
    }
}



