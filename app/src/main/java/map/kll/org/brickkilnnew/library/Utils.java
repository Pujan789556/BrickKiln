package map.kll.org.brickkilnnew.library;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;

public class Utils {
    public static Marker createTappableMarker(final Context c, int resourceIdentifier,
                                              LatLong latLong, final String nameKiln,final  String cityKiln, final String ownershipKiln,
                                              final String marketKiln, final String operating_seasonsKiln, final String days_openKiln,
                                              final String raw_materialKiln, final String fuelKiln, final String fuel_quantityKiln, final String brick_kindKiln, final String chimney_catKiln, final String chimney_heightKiln, final String chimney_numberKiln,
                                              final String moulding_processKiln, final String firingKiln, final String capacityKiln, final String brick_per_batchKiln, final String qualityKiln,
                                              final String labor_childrenKiln, final String labor_maleKiln, final String labor_femaleKiln, final String labor_totalKiln, final String labor_youngKiln, final String labor_oldKiln, final String labor_currently_studyingKiln, final String labor_slcKiln, final String labor_informal_eduKiln, final String labor_illiterateKiln, final String food_allowanceKiln) {


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

                    bundle1.putString("name",nameKiln);
                    bundle1.putString("city",cityKiln);
                    bundle1.putString("ownership",ownershipKiln);
                    bundle1.putString("market",marketKiln);
                    bundle1.putString("operating_seasons",operating_seasonsKiln);
                    bundle1.putString("days_open",days_openKiln);

                    bundle1.putString("raw_material",raw_materialKiln);
                    bundle1.putString("fuel",fuelKiln);
                    bundle1.putString("fuel_quantity",fuel_quantityKiln);
                    bundle1.putString("brick_kind",brick_kindKiln);
                    bundle1.putString("chimney_cat",chimney_catKiln);
                    bundle1.putString("chimney_height",chimney_heightKiln);
                    bundle1.putString("chimney_number",chimney_numberKiln);
                    bundle1.putString("moulding_process",moulding_processKiln);
                    bundle1.putString("firing",firingKiln);
                    bundle1.putString("capacity",capacityKiln);
                    bundle1.putString("brick_per_batch",brick_per_batchKiln);
                    bundle1.putString("quality",qualityKiln);


                    bundle1.putString("labor_children",labor_childrenKiln);
                    bundle1.putString("labor_male",labor_maleKiln);
                    bundle1.putString("labor_female",labor_femaleKiln);
                    bundle1.putString("labor_total",labor_totalKiln);
                    bundle1.putString("labor_young",labor_youngKiln);
                    bundle1.putString("labor_old",labor_oldKiln);
                    bundle1.putString("labor_currently_studying",labor_currently_studyingKiln);
                    bundle1.putString("labor_slc",labor_slcKiln);
                    bundle1.putString("labor_informal_edu",labor_informal_eduKiln);
                    bundle1.putString("labor_illiterate",labor_illiterateKiln);
                    bundle1.putString("food_allowance",food_allowanceKiln);

                    intent.putExtras(bundle1);
                    c.startActivity(intent);
                    return true;
                }
                return false;

            }
        };
    }
}
