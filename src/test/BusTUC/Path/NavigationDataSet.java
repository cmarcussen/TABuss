/**
 * Copyright (C) 2010-2012 Magnus Raaum, Lars Moland Eliassen, Christoffer Jun Marcussen, Rune Sætre
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * README:
 * 
 */

package test.BusTUC.Path;

import java.util.ArrayList;
import java.util.Iterator;


public class NavigationDataSet { 

private ArrayList<Placemark> placemarks = new ArrayList<Placemark>();
private Placemark currentPlacemark;
private Placemark routePlacemark;

@Override
public String toString() {
    String s= "";
    for (Iterator<Placemark> iter=placemarks.iterator();iter.hasNext();) {
        Placemark p = iter.next();
        s += p.getTitle() + "\n" + p.getDescription() + "\n\n";
    }
    return s;
}

public void addCurrentPlacemark() {
    placemarks.add(currentPlacemark);
}

public ArrayList<Placemark> getPlacemarks() {
    return placemarks;
}

public void setPlacemarks(ArrayList<Placemark> placemarks) {
    this.placemarks = placemarks;
}

public Placemark getCurrentPlacemark() {
    return currentPlacemark;
}

public void setCurrentPlacemark(Placemark currentPlacemark) {
    this.currentPlacemark = currentPlacemark;
}

public Placemark getRoutePlacemark() {
    return routePlacemark;
}

public void setRoutePlacemark(Placemark routePlacemark) {
    this.routePlacemark = routePlacemark;
}

}
