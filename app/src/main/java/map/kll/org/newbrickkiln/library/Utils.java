package map.kll.org.newbrickkiln.library;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;

import map.kll.org.newbrickkiln.MarkerActivity;
import map.kll.org.newbrickkiln.R;


public final class Utils {
    public static Marker createTappableMarker(final Context c, int resourceIdentifier,
                                              LatLong latLong, final String nameKiln,final  String cityKiln,String ownershipKiln,
                                              String marketKiln,String operatingSeasonsKiln,String daysOpenKiln,
                                               String raw_materialKiln,String fuelKiln,String fuel_quantityKiln,String brick_kindKiln,String chimney_catKiln,String chimney_heightKiln,String chimney_numberKiln,
                                               String moulding_processKiln,String firingKiln,String capacityKiln,String brick_per_batchKiln,String qualityKiln,
                                               String labor_childrenKiln,String labor_maleKiln,String labor_femaleKiln,String labor_totalKiln,String labor_youngKiln,String labor_oldKiln,String labor_currently_studyingKiln,String labor_slcKiln,String labor_informal_eduKiln,String labor_illiterateKiln,String food_allowanceKiln) {
        final String name = nameKiln;
        final String city = cityKiln;
        final String ownership = ownershipKiln;
        final String market = marketKiln;
        final String operating_seasons = operatingSeasonsKiln;
        final String days_open = daysOpenKiln;


        final String raw_material = raw_materialKiln;
        final String fuel = fuelKiln;
        final String fuel_quantity = fuel_quantityKiln;
        final String brick_kind = brick_kindKiln;
        final  String chimney_cat = chimney_catKiln;
        final String chimney_height = chimney_heightKiln;
        final  String chimney_number = chimney_numberKiln;
        final String moulding_process = moulding_processKiln;
        final String firing = firingKiln;
        final String capacity = capacityKiln;
        final String brick_per_batch = brick_per_batchKiln;
        final String quality = qualityKiln;

        final String labor_children = labor_childrenKiln;
        final String labor_male = labor_maleKiln;
        final String labor_female = labor_femaleKiln;
        final String labor_total = labor_totalKiln;
        final  String labor_young = labor_youngKiln;
        final String labor_old = labor_oldKiln;
        final  String labor_currently_studying= labor_currently_studyingKiln;
        final String labor_slc= labor_slcKiln;
        final String labor_informal_edu = labor_informal_eduKiln;
        final String labor_illiterate = labor_illiterateKiln;
        final String food_allowance = food_allowanceKiln;

        Drawable drawable = c.getResources().getDrawable(resourceIdentifier);
        final Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();

        return new Marker(latLong, bitmap, 0, -bitmap.getHeight() / 100) {
            @Override
            public boolean onTap(LatLong geoPoint, Point viewPosition,
                                 Point tapPoint) {
                if (contains(viewPosition, tapPoint)) {
                    Intent intent = new Intent(c,MarkerActivity.class);
                    Bundle bundle1 = new Bundle();

                    bundle1.putString("name",name);
                    bundle1.putString("city",city);
                    bundle1.putString("ownership",ownership);
                    bundle1.putString("market",market);
                    bundle1.putString("operating_seasons",operating_seasons);
                    bundle1.putString("days_open",days_open);

                    bundle1.putString("raw_material",raw_material);
                    bundle1.putString("fuel",fuel);
                    bundle1.putString("fuel_quantity",fuel_quantity);
                    bundle1.putString("brick_kind",brick_kind);
                    bundle1.putString("chimney_cat",chimney_cat);
                    bundle1.putString("chimney_height",chimney_height);
                    bundle1.putString("chimney_number",chimney_number);
                    bundle1.putString("moulding_process",moulding_process);
                    bundle1.putString("firing",firing);
                    bundle1.putString("capacity",capacity);
                    bundle1.putString("brick_per_batch",brick_per_batch);
                    bundle1.putString("quality",quality);


                    bundle1.putString("labor_children",labor_children);
                    bundle1.putString("labor_male",labor_male);
                    bundle1.putString("labor_female",labor_female);
                    bundle1.putString("labor_total",labor_total);
                    bundle1.putString("labor_young",labor_young);
                    bundle1.putString("labor_old",labor_old);
                    bundle1.putString("labor_currently_studying",labor_currently_studying);
                    bundle1.putString("labor_slc",labor_slc);
                    bundle1.putString("labor_informal_edu",labor_informal_edu);
                    bundle1.putString("labor_illiterate",labor_illiterate);
                    bundle1.putString("food_allowance",food_allowance);

                    intent.putExtras(bundle1);
                    c.startActivity(intent);
                    return true;
                }
                return false;

            }
        };
    }

}

