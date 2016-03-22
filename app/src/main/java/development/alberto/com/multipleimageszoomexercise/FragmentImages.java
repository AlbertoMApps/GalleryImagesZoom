package development.alberto.com.multipleimageszoomexercise;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;

/**
 * Created by alber on 22/03/2016.
 */
public class FragmentImages extends Fragment implements View.OnTouchListener, View.OnClickListener{
    private String imagesURL;
    public  static final String TAG ="TAG";
    private ImageView imageView;
    // matrices usadas para mover y hacer zoom sobre la image
    Matrix matrix = new Matrix();//**
    Matrix savedMatrix = new Matrix();//**

    // nos podemos encontrar en uno de estos 3 estados
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // aspectos para el proceso de zoom
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    /**Constructor**/
    public FragmentImages() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slides_images, container, false);
        imageView = (ImageView) view.findViewById(R.id.img);
        imageView.setOnClickListener(this);
        //imageView.setOnTouchListener(this);
        /**Picasso initial settings**/
        Picasso.with(getActivity()).load(this.getImg()).resize(500,200).centerCrop().into(imageView);
        return view;
    }


    /**
     * En cualquier momento que clickes en la imagen, se agrandar la imagen
     * @param v
     */

    @Override
    public void onClick(View v) {
//        ScrollView.LayoutParams params = (ScrollView.LayoutParams)
//                v .getLayoutParams();
//        params.height = 130;
//        v .setLayoutParams(params);
//        v.setOnTouchListener(this);

        /**Picasso settings**/
        Picasso.with(getActivity()).load(this.getImg()).resize(1000,1000).centerCrop().into(imageView);
        v.setOnTouchListener(this);
    }

    /**
     * Imagenes touch y zooming, indicamos que hacer por cada movimiento
     * @param v
     * @param event
     * @return
     */

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        imageView = (ImageView) v;//**

        // volcamos el evento tactil en el registro logcat
        dumpEvent(event);

        // Manejo de eventos tactiles...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //Calculamos la distancia entre los dos dedos, ACTION_DOWN indica que el nuevo dedo se ha introducidoo
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event); //indica la escala de proporcion si los dedos se han acercado o alejado, si es mayor que 1, se habran alejado bastante, si no, lo rechazamos
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event); //punto medio entre distancias de los dedos
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y); //scale the image whenever we move it..
                    }
                }
                break;
        }
        imageView.setImageMatrix(matrix);
        return true; // indica que el evento tuvo lugar
    }

    /*******************************Metodos para el touch**********************************************************

    /**
     * Cargar los eventos en el logcat
     * @param event
     */

    private void dumpEvent(MotionEvent event) {

        String[] names = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER DOWN", "POINTER UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION").append(names[actionCode]);

        if( actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid = ").append(action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for(int i =0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("{pid ").append(event.getPointerId(i));
            sb.append(" )= ").append(event.getX());
            sb.append(" , ").append(event.getY());
            if(i+1< event.getPointerCount()){
                sb.append(";");
            }
            sb.append("]");
            Log.d(TAG, sb.toString());

        }
    }

    /**
     * Calcular los puntos medios de desplazamiento
     * @param point
     * @param event
     */

    private void midPoint(PointF point, MotionEvent event) { //podemos arrastrar y pellizcar la pantalla y calculamos sus puntos medios
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calcular los espacios de desplace
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public String getImg(){
        return imagesURL;
    }
    public void setImg(String imagesURL){
        this.imagesURL = imagesURL;
    }


}
